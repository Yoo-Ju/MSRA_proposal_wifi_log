import random
import pandas as pd
import numpy as np
import sequencefeaturegenerator2
import predict

def createRandomTrajs(length):
	rand = random.randrange(length) + 10
	listt = []  # traj
	listt2 = [] # time

	foo = ['1f', '2f', '3f', '1f-left', '1f-inner', '1f-right', '2f-inner', '2f-left', '2f-right']
	listt.append('out')
	listt2.append(random.randrange(300))
	listt.append('in')
	listt2.append(random.randrange(300))
	for i in range(rand-2):
	    listt.append(random.choice(foo))
	    listt2.append(random.randrange(30))
	return listt, listt2


def generatefakedata(size, length):
	listt = []
	listt2 = []
	listt3 = []
	for i in range(size):
		fakedata1, fakedata2 = createRandomTrajs(length)
		listt.append(fakedata1)
		listt2.append(fakedata2)
		# prob_revisit = 0.5
		prob_revisit = (len(fakedata1)-10)/(length)
		listt3.append(np.random.choice([0, 1], p=[1-prob_revisit, prob_revisit]))    #(0,1 비율이 길이에 비례하게)
	d = {'traj': listt, 'dwell_time': listt2, 'revisit_intention': listt3}
	dfyo = pd.DataFrame(data=d, columns=['traj', 'dwell_time', 'revisit_intention'])
	return dfyo


if __name__ == '__main__':
	# try:
	for i in range(5):
		num = 5000
		length = 10
		df = generatefakedata(num, length)
		print('Generating %d random fake trajectories with average length %d' %(num, length+5))

		# print (df.head(5))
		mpframe6 = sequencefeaturegenerator2.add_frequent_sequence_features(df, int(round(df.shape[0]*0.02)))
		print('Number of features(frequent sequences):', mpframe6.shape[1]-3)
		mpframe6['r_i'] = mpframe6['revisit_intention']
		del mpframe6['revisit_intention']
		data = np.asarray(mpframe6)
		X, y = data[:, 2:-1], data[:, -1].astype(int)
		# print('Number of features:', X.shape[1])
		cvresults = predict.basicDecisionTree(X, y)
		print("Average accuracy of DT with 10-fold CV: ", np.mean(cvresults))
		# result1.append(np.mean(cvresults))
	# except Exception as e:
	#     import sys
	#     print(sys.exc_traceback.tb_lineno)
	#     sys.exit("ERROR: " + str(e))
