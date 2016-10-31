'''
Name: sequencefeaturegenerator.py
Date: 2016-10-25
Description: Frequent Sequence 를 찾은 후 Feature로 이용하는 모듈


Input: 
TO DO: 
'''

import timing
import pandas as pd
from pymining import seqmining
import numpy as np
from scipy.special import entr
from collections import defaultdict



def is_subseq(x, y):
    it = iter(y)
    return all(c in it for c in x)

# assert is_subseq('india', 'indonesia')
# assert is_subseq('oman', 'romania')
# assert is_subseq('mali', 'malawi')
# assert is_subseq((''), ('a', 'b', 'c', 'd', 'e'))
# assert not is_subseq('mali', 'banana')
# assert not is_subseq('ais', 'indonesia')
# assert not is_subseq('ca', 'abc')

def entropy(prob1, prob2):
    return(-prob1*np.log2(prob1)-prob2*np.log2(prob2))

def informationGain(a, b, c, d):
    ''' 
    a = (True, 1.0) - Subsequence, Revisit intention      
    b = (True, 0.0)       
    c = (False, 1.0)    
    d = (False, 0.0) 
    '''
    # Entropy before
    prob1a = (a+c) / (a+b+c+d)
    prob2a = 1-prob1a
    entropy_before = entropy(prob1a, prob2a)
    
    # Entropy after sequence
    prob1b = a / (a+b)
    prob2b = 1 - prob1b
    prob3b = c / (c+d)
    prob4b = 1 - prob3b
    
    entropy1 = entropy(prob1b, prob2b)
    entropy2 = entropy(prob3b, prob4b)
    entropy_after = (a+b)/(a+b+c+d)*entropy1 + (c+d)/(a+b+c+d)*entropy2
    
    IG = entropy_before-entropy_after
    
    return IG



def recursivelyFindLongestSequence(aabaaba, new_list):

    try:
        for item in aabaaba:
            testval = 0
            for longt in new_list:
                testval += is_subseq(item, longt)

            if testval == 0:
                new_list.append(item)

        for item in new_list:
            aabaaba.remove(item)


        recursivelyFindLongestSequence(aabaaba, new_list)
    except:
        pass


### If multiple subsequences have same support, there is a chance to have closed sequence and its subsequence
### Here, we only select independent longest sequences by deleting its subs.
### Technique used: Chance key, value and removing subs by dynamic programming
### Method recursivelyFindLongestSequence is used for the dynamic programming part.
### Input: freq_seqs_sample - Output: freqfreqfreq

def leavelongest_samesupport(freq_seqs_sample):

	freq_seqs_sample2 = {}
	for kv in freq_seqs_sample:
	    freq_seqs_sample2.setdefault(kv[1], []).append(kv[0])

	freqfreqfreq = []

	for k, v in freq_seqs_sample2.items():
	    if len(v) > 1:
	        v = sorted(v, key = len, reverse=True)
	        new_list = []
	        new_list.append(v[0])
	        recursivelyFindLongestSequence(v, new_list) 
	        for item in new_list:
	            freqfreqfreq.append(tuple((item, k)))
	    else:
	        freqfreqfreq.append(tuple((v[0], k)))
	       
	freqfreqfreq = sorted(freqfreqfreq, key=lambda tup: tup[1], reverse=True)
	return freqfreqfreq




@timing.timing
def generate_sortE(df):
	
	### Extract subsequences with support greater than 200 (from 8886 for this example ~ 2.5%)
	seqs = df.traj
	freq_seqs = seqmining.freq_seq_enum(seqs, 200)
	print('pymining package has been done')

	### Sort subsequences by support
	freq_seqs_sorted = sorted(freq_seqs, key=lambda tup: tup[1], reverse=True)


	### Minimum subsequence length == 4  (TODO: parameter)
	freq_seqs_sample = []
	for x in freq_seqs_sorted:
	    if (len(x[0]) >= 4):
	#         if (x[0][0] != 'out') & (x[0][0] != 'in'):
	        freq_seqs_sample.append(x)

	### Leave longest supersequence 
	longest_sequences_support = leavelongest_samesupport(freq_seqs_sample)
    

	### Make another list to save only sequence features without support value
	longest_sequences = []
	for x in longest_sequences_support:
	    if (len(x[0]) >= 4):
	        longest_sequences.append(x[0])
	print('longest subsequence has been calcualated')

	### Calculate information gain(IG), by adding sequence as a feature - descending order by IG   
	### 시간이 엄청 오래 걸림!!!!!!!!!! 여기서 다시 계산하지 말고, 처음에 같이 했어야 하는데...    
	igdict = {}
	for traj in longest_sequences:
	    c = df.apply(lambda x: (is_subseq(traj, x['traj']), x['revisit_intention']), axis=1)
	    cc = c.value_counts().sort_index(ascending=False)
	    IG = informationGain(cc[0], cc[1], cc[2], cc[3])
	    igdict[traj] = IG
	print('ig calculation has been done')

	### SortE: Tuples list of sequences(key) and their information gain
	sortE = sorted(igdict.items(), key=lambda value: value[1], reverse=True)
	return sortE


@timing.timing
def generate_seqE(sortE, numFeatures):
	seqE = []
	for item in sortE[:numFeatures]:
	    seqE.append(item[0])
	return seqE



# Sequence feature를 걍 숫자로 표현 - 해당 feature를 가지면 1, 가지지 않으면 0
def relatedfeatures(traj, seqE):
    sss = 2001
    ddd = []
    for seq in seqE:
        if is_subseq(seq, traj) == True:
            ddd.append(sss)
        sss += 1
    return ddd

def generateIGFeatureColumns(df, seqE):
    sss = 2001
    for seq in seqE:
        df[sss] = 0
        sss += 1
    for row in df.iterrows():
        for seq_ig in row[1]['seq_ig_ft']:
            df.set_value(row[0], seq_ig, 1) 


def add_frequent_sequence_features(df, numFeatures):
	sortE = generate_sortE(df)
	seqE = generate_seqE(sortE, numFeatures)
	newdf = df
	newdf['seq_ig_ft'] = df.apply(lambda x: relatedfeatures(x['traj'], seqE), axis=1)
	generateIGFeatureColumns(newdf, seqE)
	del newdf['seq_ig_ft']
	return newdf

