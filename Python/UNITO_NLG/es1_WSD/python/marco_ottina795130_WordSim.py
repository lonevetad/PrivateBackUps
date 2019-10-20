from nltk.corpus import wordnet as wn
import math


class ExerciseManager:

	def __init__(self):
		self.maxDepth = -1
		self.doubleMaxDepth = -2
		self.wordsPairs = []
		self.similarities = None

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
			self.wordsPairs = []
			for line in read_lines:
				parts = line.split('\t')
				t = (parts[0], parts[1], float(parts[2]))
				self.wordsPairs.append(t)


	def getAllLoadedSimilarities(self):
		if self.similarities is not None:
			return self.similarities
		self.similarities = []
		for wp in self.wordsPairs:
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
					apiSim = ss1.wup_similarity(ss2)
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
					similaritiesToSecondSynset.append((ss2.name(), simWUP, simSP, simLC))
				similaritiesToFirstSynset.append((ss1.name(), similaritiesToSecondSynset))
			self.similarities.append((wp[0], wp[1], maxSim, similaritiesToFirstSynset))
		return self.similarities

	def printAllLoadedSimilarities(self):
		als = self.getAllLoadedSimilarities()
		print("all similarities:")
		for wordPair in als:
			print("\n\tWith words pair: <1:", wordPair[0], " ; 2: ", wordPair[1], "> have similarity:", wordPair[2], ", got from:\n\t\tfor each first's word's synsets:")
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
		
#

print('start')
manager = ExerciseManager()
manager.initAll()
print(manager.getMaxDepth())
manager.printAllLoadedSimilarities()
print("END")