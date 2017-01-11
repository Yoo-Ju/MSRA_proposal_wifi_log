'''
Name: main.py
Date: 2016-08-23
Description: Combine everything
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
from sklearn.preprocessing import normalize
import CompanionTrajectory

pd.options.mode.chained_assignment = None


placeNum = str(786)


rawdata_picklePath = "../data/"+placeNum+"/"+placeNum+".p"
reindexed_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe.p"
trajpreprocessed_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe_trajprocessed.p"
reindexed_picklePath2 = "../data/"+placeNum+"/"+placeNum+"_mpframe2.p"
statistical_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe3.p"
statistical_picklePath_beforetrajpreprocess = "../data/"+placeNum+"/"+placeNum+"_mpframe3_beforetrajpreprocess.p"
mpframe6_train_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe6_train.p"
# crawler.crawling(str(781), str(786))


df = pd.read_pickle(rawdata_picklePath) # 크롤링한 결과 dataframe



# mpframe = reindex.reindex_by_moving_pattern(df)
# mpframe.to_pickle(reindexed_picklePath)
# print('Reindexing process by each indoor moving pattern is done')
# print('Reindexed dataframe is saved in %s' % reindexed_picklePath)


# mpframe = pd.read_pickle(reindexed_picklePath) 
# mpframe_trajs_preprocessed = preprocessing.trajectoryPreprocessor(mpframe)
# mpframe_trajs_preprocessed.to_pickle(trajpreprocessed_picklePath)
# print('Trajectories are preprocessed by using Minseokkim\'s module')
# print('Revised dataframe with preprocessed trajectories is saved in %s' % trajpreprocessed_picklePath)


# mpframe_trajs_preprocessed = pd.read_pickle(trajpreprocessed_picklePath) 
# mpframe2 = preprocessing.add_visitcount(mpframe_trajs_preprocessed)
# mpframe2.to_pickle(reindexed_picklePath2)
# print('Revisit intention and visit count has been calculated')
# print('Revised dataframe with revisit intention is saved in %s' % reindexed_picklePath2)


# mpframe2 = pd.read_pickle(reindexed_picklePath2)
# mpframe3 = featuregenerator.add_statistical_features(df, mpframe2)
# mpframe3.to_pickle(statistical_picklePath)
# print('Basic statistical features has been calculated')
# print('Revised dataframe with basic statistical features is saved in %s' % statistical_picklePath)

# def crossvalidation():






def check(mpframe3):


	mpframe3.loc[:, 'revisit_intention'] = mpframe3['revisit_intention'].astype(int)
	mask = mpframe3['traj'].str.len() > 0   # Trajectory length threshold
	mpframe3 = mpframe3.loc[mask]

	result1 = []
	result3 = []


	# for i in range(10):


	print('initial shape of the data frame: ', mpframe3.shape)
	mpframe4 = preprocessing.label_balancing(mpframe3, 90, 10)  # arg[1]: revisit interval(90days), # arg[2]: ignore customers if they visit more than n times(10 times)
	print('Label balancing has been done: ', mpframe4.shape)

	kf = StratifiedKFold(n_splits=10)
	for train_index, test_index in kf.split(mpframe4, mpframe4['revisit_intention']):
		mpframe4_train = mpframe4.ix[train_index]
		mpframe4_test = mpframe4.ix[test_index]


		# mpframe5_train = featuregenerator.add_indoor_temporal_movement_features(mpframe4_train)
		# print('Indoor temporal movement features has been added : ', mpframe5.shape)
		mpframe6_train, seqE = sequencefeaturegenerator_taslike.add_frequent_sequence_features(mpframe4_train, 0.01, 0.02, True, False, []) 
		mpframe6_test, seqE_deprecated = sequencefeaturegenerator_taslike.add_frequent_sequence_features(mpframe4_test, 0.01, 0.02, True, True, seqE) 

		# mpframe6_train.to_pickle(mpframe6_train_picklePath)
		### arguments(dataframe, nfeatures, Temporal, out/in Cond, areaCond, 0secondsCond )
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
		print('Number of features:', X_train.shape)

		test = np.asarray(df_learning1_test)
		test[test == inf] = 0
		X_test, y_test = test[:, 11:-1], test[:, -1].astype(int)
		# X_test = normalize(X_test, norm='l2', axis=1)
		print('Number of features:', X_test.shape)

		clf = DecisionTreeClassifier(max_depth=5)
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
		print('Number of features(with seqmining):', X_train.shape)

		test = np.asarray(df_learning3_test)
		test[test == inf] = 0
		X_test, y_test = test[:, 11:-1], test[:, -1].astype(int)
		# X_test = normalize(X_test, norm='l2', axis=0)
		print('Number of features(with seqmining):', X_test.shape)

		clf = DecisionTreeClassifier(max_depth=5)
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





mpframe3 = pd.read_pickle(statistical_picklePath)
check(mpframe3)


# mpframe3 = pd.read_pickle(statistical_picklePath)
# mpframe3 = CompanionTrajectory.companionFinder(mpframe3, 3)
# del mpframe3['companion']
# check(mpframe3)






# mpframe3 = pd.read_pickle(statistical_picklePath_beforetrajpreprocess)
# check(mpframe3)




