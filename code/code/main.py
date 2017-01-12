'''
Name: main.py
Date: 2016-08-23 ~
Description: Combine everything
Writer: Sundong Kim(sundong.kim@kaist.ac.kr)
'''

import crawler
import reindex
import featuregenerator
import preprocessing
import predict
import sequencefeaturegenerator
import sequencefeaturegenerator_taslike
import pickle
import pandas as pd
import numpy as np
from numpy import inf
import timing2
from sklearn.model_selection import KFold, StratifiedKFold
from sklearn.metrics import accuracy_score
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier, AdaBoostClassifier
from sklearn.gaussian_process.kernels import RBF
from sklearn.preprocessing import normalize
import CompanionTrajectory





pd.options.mode.chained_assignment = None  # make ease to use .loc function 

##################################################
##   Data Directories  
##################################################
placeNum = str(786)  
rawdata_picklePath = "../data/"+placeNum+"/"+placeNum+"_0_rawdata.p"
reindexed_picklePath = "../data/"+placeNum+"/"+placeNum+"_1_reindexed.p"
trajpreprocessed_picklePath = "../data/"+placeNum+"/"+placeNum+"_2_trajprocessed.p"
revisitintentionadded_picklePath = "../data/"+placeNum+"/"+placeNum+"_3_revisitintentionadded.p"
frequentvisitorremoved_picklePath = "../data/"+placeNum+"/"+placeNum+"_4_frequentvisitorremoved.p"
statistical_picklePath = "../data/"+placeNum+"/"+placeNum+"_5_statistical.p"
# mpframe6_train_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe6_train.p"



# crawler.crawling(str(781), str(786))    # Crawler method
df_rd = pd.read_pickle(rawdata_picklePath) 


##################################################
##  Several Preprocessing steps to make initial train/test data to play
##  (Can be changed tentatively - 170112)
##  1. Reindex raw data, trajectory containing only 'out' are deleted.
##  2. Trajectory preprocessing (Minseok's module)
##  3. Add visit count (Will be ignored), Add revisit count(our objectives)
##  4. Add basic statistical features
##  Detailed description of each methods are written in each script file.   
##################################################

# df_ri = reindex.reindex_by_moving_pattern(df_rd, 100) # moving patterns having dwell_time less than 100 in area 'in' are deleted
# df_ri.to_pickle(reindexed_picklePath)
# print('Reindexing process by each indoor moving pattern is done')
# print('Reindexed dataframe is saved in %s' % reindexed_picklePath)
# # print(df_ri.tail(5))

# df_ri = pd.read_pickle(reindexed_picklePath) 
# df_tp = preprocessing.trajectoryPreprocessor(df_ri)
# df_tp.to_pickle(trajpreprocessed_picklePath)
# print('Trajectories are preprocessed by using Minseokkim\'s module')
# print('Revised dataframe with preprocessed trajectories is saved in %s' % trajpreprocessed_picklePath)


# df_tp = pd.read_pickle(trajpreprocessed_picklePath) 
# df_riadded = preprocessing.add_revisit_intention(df_tp, 90)   # argv[1] = revisit_interval
# df_riadded.to_pickle(revisitintentionadded_picklePath)
# print('Revisit intention and visit count has been calculated')
# print('Revised dataframe with revisit intention is saved in %s' % revisitintentionadded_picklePath)


# df_riadded = pd.read_pickle(revisitintentionadded_picklePath)
# print('Before removing frequent visitors: ', df_riadded.shape)
# ## remove_frequent_visitors: arg[1]: revisit interval(90days) 이내 방문이 없는 사람들 제거 -- 전 단계와 합쳐야 함, # arg[2]: ignore customers if they visit more than n times(10 times)
# df_fvremoved = preprocessing.remove_frequent_visitors(df_riadded, 90, 10)  
# df_fvremoved.to_pickle(frequentvisitorremoved_picklePath)
# print('After removing frequent visitors: ', df_fvremoved.shape)





##################################################
##  Main part of playing with our dataframe (Add features, test accuracy)
##  (Can be changed tentatively - 170112)
##################################################


def check(mpframe3):

	mpframe4 = featuregenerator.add_statistical_features(df_rd, mpframe3)
	mpframe4.to_pickle(statistical_picklePath)
	print('Basic statistical features has been calculated')
	print('Revised dataframe with basic statistical features is saved in %s' % statistical_picklePath)
	mpframe4 = mpframe4.set_index('date_device_id')

	# mpframe3.loc[:, 'revisit_intention'] = mpframe3['revisit_intention'].astype(int)
	# # mask = mpframe3['traj'].str.len() > 4   # Length threshold of moving patterns to use
	# # mpframe3 = mpframe3.loc[mask]
	# print('initial shape of the data frame: ', mpframe3.shape)

	# ## remove_frequent_visitors: arg[1]: revisit interval(90days) 이내 방문이 없는 사람들 제거 -- 전 단계와 합쳐야 함, # arg[2]: ignore customers if they visit more than n times(10 times)
	# mpframe4 = preprocessing.remove_frequent_visitors(mpframe3, 90, 10)  
	# print('After removing frequent visitors: ', mpframe4.shape)

	result1 = []
	result3 = []

	kf = StratifiedKFold(n_splits=10)
	for train_index, test_index in kf.split(mpframe4, mpframe4['revisit_intention']):
		
		## mpframe4: without sequential pattern features
		mpframe4_train = mpframe4.ix[train_index]
		mpframe4_test = mpframe4.ix[test_index]

		## mpframe6: with sequential pattern features
		mpframe6_train, seqE = sequencefeaturegenerator_taslike.add_frequent_sequence_features(mpframe4_train, 0.005, 0.05, True, False, []) 
		mpframe6_test, seqE_deprecated = sequencefeaturegenerator_taslike.add_frequent_sequence_features(mpframe4_test, 0.005, 0.05, True, True, seqE) 

		print('Frequent sequence features has been added: ', mpframe6_train.shape)	
		print('Frequent sequence features has been added: ', mpframe6_test.shape)

		df_learning1_train = preprocessing.finalprocessing(mpframe4_train)
		df_learning1_test = preprocessing.finalprocessing(mpframe4_test)
		df_learning3_train = preprocessing.finalprocessing(mpframe6_train)
		df_learning3_test = preprocessing.finalprocessing(mpframe6_test)

		print(df_learning1_train.shape)
		print(df_learning1_test.shape)
		print(df_learning3_train.shape)
		print(df_learning3_test.shape)

		train = np.asarray(df_learning1_train)
		train[train == inf] = 0
		X_train, y_train = train[:, 11:-1], train[:, -1].astype(int)
		# X_train = normalize(X_train, norm='l2', axis=1)
		print('Train:', X_train.shape)

		test = np.asarray(df_learning1_test)
		test[test == inf] = 0
		X_test, y_test = test[:, 11:-1], test[:, -1].astype(int)
		# X_test = normalize(X_test, norm='l2', axis=1)
		print('Test:', X_test.shape)


		# clf = DecisionTreeClassifier(max_depth=5)
		clf = AdaBoostClassifier()
		clf = clf.fit(X_train, y_train)
		y_pred = clf.predict(X_test)
		prediction_accuracy = accuracy_score(y_test, y_pred)
		result1.append(prediction_accuracy)
		# cvresults = predict.basicDecisionTree(X, y)
		print("Result 1: ", prediction_accuracy)
		# result1.append(np.mean(cvresults))


		train = np.asarray(df_learning3_train)
		train[train == inf] = 0
		X_train, y_train = train[:, 11:-1], train[:, -1].astype(int)
		# X_train = normalize(X_train, norm='l2', axis=0)
		print('Train(with seq patterns):', X_train.shape)

		test = np.asarray(df_learning3_test)
		test[test == inf] = 0
		X_test, y_test = test[:, 11:-1], test[:, -1].astype(int)
		# X_test = normalize(X_test, norm='l2', axis=0)
		print('Test(with seq patterns):', X_test.shape)

		# clf = DecisionTreeClassifier(max_depth=5)
		clf = AdaBoostClassifier()
		clf = clf.fit(X_train, y_train)
		y_pred = clf.predict(X_test)
		prediction_accuracy = accuracy_score(y_test, y_pred)
		result3.append(prediction_accuracy)
		# cvresults = predict.basicDecisionTree(X, y)
		print("Result 3: ", prediction_accuracy)
		# result1.append(np.mean(cvresults))


	print("Average results for exp 1: ", np.mean(result1))
	print("Average results for exp 3: ", np.mean(result3))
	print("-------------")



df_fvremoved = pd.read_pickle(frequentvisitorremoved_picklePath)
check(df_fvremoved)


# mpframe3 = pd.read_pickle(statistical_picklePath)
# mpframe3 = CompanionTrajectory.companionFinder(mpframe3, 3)
# del mpframe3['companion']
# check(mpframe3)
# mpframe3 = pd.read_pickle(statistical_picklePath_beforetrajpreprocess)
# check(mpframe3)




