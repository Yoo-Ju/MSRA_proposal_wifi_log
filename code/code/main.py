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

import pickle
import pandas as pd
import numpy as np
from numpy import inf


placeNum = str(786)


rawdata_picklePath = "../data/"+placeNum+"/"+placeNum+".p"
reindexed_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe.p"
reindexed_picklePath2 = "../data/"+placeNum+"/"+placeNum+"_mpframe2.p"
statistical_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe3.p"
# crawler.crawling(str(781), str(786))


df = pd.read_pickle(rawdata_picklePath) # 크롤링한 결과 dataframe
# mpframe = reindex.reindex_by_moving_pattern(df)


# mpframe.to_pickle(reindexed_picklePath)
# print('Reindexing process by each indoor moving pattern is done')
# print('Reindexed dataframe is saved in %s' % reindexed_picklePath)


# mpframe = pd.read_pickle(reindexed_picklePath) 
# mpframe_trajs_preprocessed = preprocessing.trajectoryPreprocessing(mpframe)
# mpframe2 = preprocessing.add_visitcount(mpframe_trajs_preprocessed)

# mpframe2.to_pickle(reindexed_picklePath2)
# print('Revisit intention and visit count has been calculated')
# print('Revised dataframe with revisit intention is saved in %s' % reindexed_picklePath2)


# mpframe2 = pd.read_pickle(reindexed_picklePath2)
# mpframe3 = featuregenerator.add_statistical_features(df, mpframe2)
# mpframe3.to_pickle(statistical_picklePath)
# print('Basic statistical features has been calculated')
# print('Revised dataframe with basic statistical features is saved in %s' % statistical_picklePath)

mpframe3 = pd.read_pickle(statistical_picklePath)

result1 = []
result2 = []

for i in range(10):
	mpframe4 = preprocessing.label_balancing(mpframe3, 90, 10)
	mpframe5 = featuregenerator.add_indoor_temporal_movement_features(mpframe4)

	df_learning1 = preprocessing.finalprocessing(mpframe4)
	df_learning2 = preprocessing.finalprocessing(mpframe5)

	
	data = np.asarray(df_learning1)
	data[data == inf] = 0
	X, y = data[:, 11:-1], data[:, -1].astype(int)
	# print('Number of features:', X.shape[1])
	cvresults = predict.basicDecisionTree(X, y)
	result1.append(np.mean(cvresults))


	data = np.asarray(df_learning2)
	data[data == inf] = 0
	X, y = data[:, 11:-1], data[:, -1].astype(int)
	# print('Number of features:', X.shape[1])
	cvresults = predict.basicDecisionTree(X, y)
	result2.append(np.mean(cvresults))

print(np.mean(result1))
print(np.mean(result2))









