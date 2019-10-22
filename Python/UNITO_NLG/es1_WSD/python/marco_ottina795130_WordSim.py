from nltk.corpus import wordnet as wn
import math
import numpy as np
from  scipy.stats import spearmanr

class ExerciseManager:

	def __init__(self):
		self.maxDepth = -1
		self.doubleMaxDepth = -2
		self.wordsPairsRead = None
		self.readSimilar = None
		self.similarities = None
		self.mineSimilar = None

	def initAll(self):
		self.maxDepth = self.getMaxDepth()
		self.loadTabFile()

	def getMaxDepth(self, node = None):
		if(self.maxDepth >= 0):
			return self.maxDepth
		if node is None:
			root = wn.synsets('entity')[0]
			if root is None:
				self.maxDepth = 0 #print('root entity is None')
				return 0
			self.maxDepth = self.getMaxDepth(root)
			return self.maxDepth
		maxDep = 0
		hyps = node.hyponyms()
		for h in hyps:
			mr = self.getMaxDepth(h)
			if mr > maxDep:
				maxDep = mr
		return 1 + maxDep


	def getDoubledMaxDepth(self):
		if(self.doubleMaxDepth >= 0):
			return self.doubleMaxDepth
		d = 2*self.getMaxDepth()
		self.doubleMaxDepth = d
		return d

	def loadTabFile(self, tabFileName='./../WordSim353.tab'):
		if tabFileName is None:
			tabFileName = './../WordSim353.tab';
		with open(tabFileName, 'r') as fp:
			read_lines = [line.rstrip('\n') for line in fp.readlines()]
			del read_lines[0] # perche' c'e' una frase di spiegazione
			self.wordsPairsRead = []
			self.readSimilar = []
			for line in read_lines:
				parts = line.split('\t')
				t = (parts[0], parts[1], float(parts[2]))
				self.wordsPairsRead.append(t)
				self.readSimilar.append(t[2])


	def getSimilaritiesFromLoadedPairs(self):
		if self.similarities is not None:
			return self.similarities
		self.similarities = []
		self.mineSimilar = []
		for wp in self.wordsPairsRead:
			synsets1 = wn.synsets(wp[0])
			synsets2 = wn.synsets(wp[1])
			similaritiesToFirstSynset = []
			maxSim = 0
			for ss1 in synsets1:
				similaritiesToSecondSynset = []
				for ss2 in synsets2:
					mineSim = self.similarityWuPalmer(ss1,ss2)
					if mineSim > maxSim :
						maxSim = mineSim
					apiSim = ss1.wup_similarity(ss2)
					simWUP = ("Wu&Palmer", mineSim, ", from API: ", apiSim)
					#
					mineSim = self.similarityShortestPath(ss1,ss2)
					if mineSim > maxSim :
						maxSim = mineSim
					apiSim = ss1.path_similarity(ss2)
					if apiSim is None:
						apiSim = 0
					else:
						apiSim = apiSim * self.getDoubledMaxDepth()
					simSP = ("Path", mineSim, ", from API: ", apiSim)
					#
					mineSim = self.similarityLC(ss1,ss2)
					if mineSim > maxSim :
						maxSim = mineSim
					simSP = ("Path", self.similarityShortestPath(ss1,ss2), ", from API: ", ss1.path_similarity(ss2))
					try:
						apiSim = ss1.lch_similarity(ss2)
					except:
						apiSim = "ERROR: different PoS"
					simLC = ("L&C", mineSim, ", from API: ", apiSim)
					#
				#	similaritiesToSecondSynset.append((ss2.name(), simWUP, simSP, simLC))
				#similaritiesToFirstSynset.append((ss1.name(), similaritiesToSecondSynset))

				#ho maxSim, calcolare le correlazioni	
			self.similarities.append((wp[0], wp[1], maxSim, wp[2])) #, similaritiesToFirstSynset))
			self.mineSimilar.append(maxSim)
		return self.similarities

	def depth(self, node, visited=set()):
		#print(node)
		hys = node.hypernyms();
		#print("node: ", node, "visited: ", visited)
		if len(hys) is 0 :
			return 1
		min_dep = 0
		#print("..hys: ", hys)
		for h in hys:
			#if not (h in visited):
			#	visited.add(h)
				d = self.depth(h, visited)
				if (min_dep is 0) or (d < min_dep):
					min_dep = d
		return 1 + min_dep

	def similarityWuPalmer(self, synsets1, synsets2):
		return 2* self.depth(synsets1.lowest_common_hypernyms(synsets1, synsets2)[0]) / (self.depth(synsets1)+self.depth(synsets2))  #lowest_common_hypernyms

	def similarityShortestPath(self, synsets1, synsets2):
		if synsets1 is synsets2:
			return self.getDoubledMaxDepth()
		spd = synsets1.shortest_path_distance(synsets2)
		if spd is None:
			return 0
		return self.getDoubledMaxDepth() - spd

	def similarityLC(self, synsets1, synsets2):
		spd = synsets1.shortest_path_distance(synsets2)
		if spd is None:
			spd = 0
		return - math.log2( (1 + spd) / (1 + self.getDoubledMaxDepth()))


	def spearmanRankCorrelationCoefficient(self, a1, a2):
		return spearmanr(a1, a2)

	def pearsonCorrelationCoefficient(self, a1, a2):
		return np.cov(a1, a2)/(np.std(a1)*np.std(a2))


	def performExercise(self):
		siml = self.getSimilaritiesFromLoadedPairs()
		#now perform similarities
		a1 = np.array(self.mineSimilar)
		a2 = np.array(self.readSimilar)
		pearson = self.pearsonCorrelationCoefficient(a1, a2)
		spearman = self.spearmanRankCorrelationCoefficient(a1, a2)
		return (siml, pearson, spearman, a1, a2)

	def printAllLoadedSimilarities(self, als = None):
		if als is None:
			als = self.getSimilaritiesFromLoadedPairs()
		print("all similarities:")
		for wordPair in als:
			print("\n\n\tWith words pair: <1:", wordPair[0], " ; 2: ", wordPair[1], "> have similarity:", wordPair[2], ", from file: ", wordPair[3])
			'''
			print(", got from:\n\t\tfor each first's word's synsets:")
			for ss1 in wordPair[3]:
				print("-\t\tsynset 1: ", ss1[0], " paired with:")
				for ss2 in ss1[1]:
					print("--\t\t\tsynset 2: ", ss2[0], " there's similarity:")
					sim = ss2[1]
					print("\t\t\t\t", sim[0], " -> ", sim[1], sim[2], sim[3])
					sim = ss2[2]
					print("\t\t\t\t", sim[0], " -> ", sim[1], sim[2], sim[3])
					sim = ss2[3]
					print("\t\t\t\t", sim[0], " -> ", sim[1], sim[2], sim[3])
			'''
	def printExercise(self):
		res = self.performExercise()
		print("computed similarities: pearson:", res[1], ", spearman-rank: ", res[2], ", got from:")
		self.printAllLoadedSimilarities(res[0])

		
#

print('start')
manager = ExerciseManager()
manager.initAll()
print(manager.getMaxDepth())
manager.printExercise()
print("END")