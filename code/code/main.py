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
import sequencefeaturegenerator2
import pickle
import pandas as pd
import numpy as np
from numpy import inf
import timing2


placeNum = str(786)


rawdata_picklePath = "../data/"+placeNum+"/"+placeNum+".p"
reindexed_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe.p"
trajpreprocessed_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe_trajprocessed.p"
reindexed_picklePath2 = "../data/"+placeNum+"/"+placeNum+"_mpframe2.p"
statistical_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe3.p"
statistical_picklePath_beforetrajpreprocess = "../data/"+placeNum+"/"+placeNum+"_mpframe3_beforetrajpreprocess.p"
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

def check(mpframe3):


	mpframe3['revisit_intention'] = mpframe3['revisit_intention'].astype(int)
	mask = mpframe3['traj'].str.len() > 0   # Trajectory length threshold
	mpframe3 = mpframe3.loc[mask]

	result1 = []
	result2 = []
	result3 = []

	for i in range(10):

		print('initial shape of the data frame: ', mpframe3.shape)
		mpframe4 = preprocessing.label_balancing(mpframe3, 90, 10)  # arg[1]: revisit interval(90days), # arg[2]: ignore customers if they visit more than n times(10 times)
		print('Label balancing has been done: ', mpframe4.shape)
		mpframe5 = featuregenerator.add_indoor_temporal_movement_features(mpframe4)
		print('Indoor temporal movement features has been added: ', mpframe5.shape)
		mpframe6 = sequencefeaturegenerator2.add_frequent_sequence_features(mpframe4, 0.01, 0.02, True) 
		### arguments(dataframe, nfeatures, Temporal, out/in Cond, areaCond, 0secondsCond )
		print('Frequent sequence features has been added: ', mpframe6.shape)	

		df_learning1 = preprocessing.finalprocessing(mpframe4)
		df_learning2 = preprocessing.finalprocessing(mpframe5)
		df_learning3 = preprocessing.finalprocessing(mpframe6)

		
		data = np.asarray(df_learning1)
		data[data == inf] = 0
		X, y = data[:, 11:-1], data[:, -1].astype(int)
		# print('Number of features:', X.shape[1])
		cvresults = predict.basicDecisionTree(X, y)
		# print("Result 1: ", np.mean(cvresults))
		result1.append(np.mean(cvresults))


		data = np.asarray(df_learning2)
		data[data == inf] = 0
		X, y = data[:, 11:-1], data[:, -1].astype(int)
		# print('Number of features:', X.shape[1])
		cvresults = predict.basicDecisionTree(X, y)
		# print("Result 2: ", np.mean(cvresults))
		result2.append(np.mean(cvresults))


		data = np.asarray(df_learning3)
		data[data == inf] = 0
		X, y = data[:, 11:-1], data[:, -1].astype(int)
		# print('Number of features:', X.shape[1])
		cvresults = predict.basicDecisionTree(X, y)
		# print("Result 3: ", np.mean(cvresults))
		result3.append(np.mean(cvresults))


	print("Average results for exp 1: ", np.mean(result1))
	print("Average results for exp 2: ", np.mean(result2))
	print("Average results for exp 3: ", np.mean(result3))
	print("-------------")





mpframe3 = pd.read_pickle(statistical_picklePath)
check(mpframe3)
mpframe3 = pd.read_pickle(statistical_picklePath_beforetrajpreprocess)
check(mpframe3)




