'''
Name: preprocessing.py
Date: 2016-10-13
Description: Trajectory preprocessing module(by Minseok kim), Several preprocessing module

Input: 
TO DO: 
'''
__author__ = 'Sundong Kim: sundong.kim@kaist.ac.kr'

import timing
import pandas as pd
import numpy as np



def trajectoryPreprocessing(mpframe):
	mpframe_renew_trajs = mpframe
	return mpframe_renew_trajs


''' 
존재하는 moving pattern의 device id가 현재 방문 포함 총 몇 번 방문하였는지 계산 (parameter: revisit interval)
'''

### date와 device_id로 date_device_id를 다시 분리
def add_visitcount(mpframe2):
	mpframe2['device_id'] = mpframe2['date_device_id'].apply(lambda x: x[6:])
	mpframe2['date'] = mpframe2['date_device_id'].apply(lambda x: int(x[:5]))

	mpframe2['cnt'] = 1
	mpframe2['new_visit_count'] = mpframe2[['device_id', 'cnt']].groupby('device_id').cumsum()
	mpframe2['revisit_intention'] = 0
	revisit_interval_thres = 90

	for ids in mpframe2['device_id'].unique():
	    dff = mpframe2.loc[mpframe2['device_id']==ids]   
	    a = 0
	    date = min(dff.date)
	    prev_idx = ''
	    for index, row in dff.iterrows():
	        if a+1 == row['new_visit_count']:
	            if date+revisit_interval_thres > row['date']:
	#                 print('regular revisit: {0} days interval'.format(row['date']-date))
	#                 print('previous index: ',prev_idx)
	                mpframe2.set_value(prev_idx, 'revisit_intention', 1)
	#             elif row['new_visit_count'] == 1:
	#                 print('regular revisit: {0} days interval'.format(row['date']-date))
	#             else:
	#                 print('Irregular revisit: {0} days interval'.format(row['date']-date))
	            prev_idx = index
	#             print(row,'\n')
	            a = row['new_visit_count']
	            date = row['date']
	            
	# cols = mpframe2.columns.tolist()
	# newcols = cols[:9]+cols[22:23]+cols[21:22]+cols[10:11]+cols[12:13]+cols[11:12]+cols[13:21]+cols[24:]
	# newcols = newcols[:18]+newcols[20:22]+newcols[19:20]+newcols[18:19]+newcols[22:]
	# mpframe2 = mpframe2[newcols]
	del mpframe2['cnt']
	return mpframe2.ix[:-1]





@timing.timing
def label_balancing(trajs_combined, revisit_interval, frequent_limit):
	### 최근 세달 이내 revisit intention이 없는 moving pattern 제거 
	trajs_combined = trajs_combined.set_index('date_device_id')

	limit_date = max(trajs_combined.date) - revisit_interval
	recent3monthvisitors = trajs_combined.loc[(trajs_combined.date > limit_date) & (trajs_combined.revisit_intention == 0)].index
	trajs_excludelast3months = trajs_combined.drop(recent3monthvisitors)

	### 10번 초 온 사람들은 제거.
	visitcounts = trajs_excludelast3months.groupby(['device_id'])['new_visit_count'].max()
	freqvisitors = visitcounts.loc[visitcounts > frequent_limit ].keys()
	trajs_freqremoved = trajs_excludelast3months.loc[-trajs_excludelast3months.device_id.isin(freqvisitors.tolist())]

	### Revisit intention 비율을 50대 50으로 맞춤 (sampling)
	trajs_1 = trajs_freqremoved.loc[trajs_freqremoved['revisit_intention']==1]
	trajs_0 = trajs_freqremoved.loc[trajs_freqremoved['revisit_intention']==0]
	new_trajs_0 = trajs_0.iloc[np.random.permutation(len(trajs_0))][:trajs_1.shape[0]]  ## trajs_1의 크기에 맞게 trajs_0을 랜덤 샘플링.

	### 1:1 비율의 dataframe 만들기
	trajs_combined_balanced = pd.concat([trajs_1, new_trajs_0])
	trajs_combined_balanced = trajs_combined_balanced.sample(frac=1)

	return trajs_combined_balanced

# placeNum = str(786)
# reindexed_picklePath = "../data/"+placeNum+"/"+placeNum+"_mpframe_160923.p"
# label_balancing(trajs_combined, 90, 10)


def finalprocessing(mpframe4):
	cols = mpframe4.columns.tolist()
	newcols = cols[:12] + cols[13:] + cols[12:13]
	df_learning = mpframe4[newcols]
	df_learning = df_learning.fillna(0)
	df_learning = df_learning.reindex(np.random.permutation(df_learning.index))
	return df_learning






