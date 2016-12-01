import pickle
import pandas as pd
import subprocess
import itertools


def getuniqueareas(trajseries):
    aggregated_traj = list(itertools.chain.from_iterable(trajseries))
    uniqueareas = sorted(list(set(aggregated_traj)))
    return uniqueareas


def toSPMFconverter():
	df = pd.read_pickle('../data/786/786_trajs_combined_balanced.p')

	arealist = getuniqueareas(df.traj)
	areadict = {}

	i = 1
	for item in arealist:
	    areadict[item] = i
	    i += 1
	   
	f = open('../code/code/spmftestsample.txt', 'w')

	num = 0
	for i, item in enumerate(df.traj.head(1000)):
	    if(i > 0):
	        f.write('\n')
	    num += 1
	    item2 = []
	    for area in item:
	        item2.append(areadict[area])
	        item2.append(-1)
	    item2.append(-2)
	    eachline = ' '.join(map(str, item2))
	    f.write(eachline)
	    
	f.close()

def spmftoDF():
	



if __name__ == '__main__':
	# toSPMFconverter()
	subprocess.check_output(['java', '-jar', 'spmf.jar', 'run', 'BIDE+', 'spmftestsample.txt', 'spmftestsampleoutput.txt', '2%'])
	spmfToDF()
