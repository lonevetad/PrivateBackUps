from nltk.corpus import wordnet as wn
import math
import numpy as np
from  scipy.stats import spearmanr

class SynsetsHolderDuckTyping(object):
	'''
	classe che esegue un "wrap" di un nodo fornendo i synset degli iponinmi.
	Utile per calcolare la profondità massima di tutta Wordnet.
	Infatti tale metodo si basa su un nodo (in caso di nullità si cerca il
	nodo più "alla radice", ossia 'entity'), iterando per ogni suo iponimo.
	La funzione 'wordnet.synsets("entity")', però, restituisce non un singolo nodo,
	ma una lista di synsets. Quindi è necessario creare un "nodo finto" che
	fornisca tale lista di iponimi e richiamare la funzione di calcolo della
	profondità, in modo ricorsivo.
	'''
	def __init__(self, synsets):
		self.synsets = synsets

	def hyponyms(self):
		return self.synsets

class ExerciseManager(object):

	def __init__(self):
		self.maxDepth = -1
		self.doubleMaxDepth = -2
		self.wordsPairsRead = None
		self.readSimilar = None
		self.similarities = None
		self.mineSimilarity_wup = None
		self.mineSimilarity_path = None
		self.mineSimilarity_lch = None
		self.sysnetsDepthCache = {}

	def initAll(self):
		self.maxDepth = self.getMaxDepth() # 21 is obtained from previous tests
		self.doubleMaxDepth = 2*self.maxDepth
		self.loadTabFile()

	def getMaxDepth(self, node = None):
		if(self.maxDepth >= 0):
			return self.maxDepth
		if node is None:
			root_synsets = wn.synsets('entity')
			if root_synsets is None or root_synsets[0] is None:
				self.maxDepth = 0 #print('root entity is None')
				return 0
			self.maxDepth = self.getMaxDepth(SynsetsHolderDuckTyping(root_synsets))
			return self.maxDepth
		maxDep = 0
		hyps = node.hyponyms()
		for h in hyps:
			mr = self.getMaxDepth(h)
			if mr > maxDep:
				maxDep = mr
		return 1 + maxDep


	def getDoubledMaxDepth(self):
		return self.doubleMaxDepth

	def loadTabFile(self, tabFileName='./../WordSim353.tab'):
		if tabFileName is None:
			tabFileName = './../WordSim353.tab';
		with open(tabFileName, 'r') as fp:
			read_lines = [line.rstrip('\n') for line in fp.readlines()]
			del read_lines[0] # perche' c'e' una frase di spiegazione
			self.wordsPairsRead = []
			self.readSimilar = []
			self.similarities = None #clear cache
			for line in read_lines:
				parts = line.split('\t')
				t = (parts[0], parts[1], float(parts[2]))
				self.wordsPairsRead.append(t)
				self.readSimilar.append(t[2])


	def computeSimilaritiesOfLoadedPairs(self, synsets_lookupper = wn.synsets): # cache the wn.synsets function 
		if self.similarities is not None:
			return self.similarities
		self.similarities = []
		self.mineSimilarity_wup  = []
		self.mineSimilarity_path = []
		self.mineSimilarity_lch  = []
		for wp in self.wordsPairsRead:
			synset1 = synsets_lookupper(wp[0])
			synset2 = synsets_lookupper(wp[1])
			similaritiesToFirstSynset = []
			maxSim_wup = 0
			maxSim_path = 0
			maxSim_lch = 0
			for ss1 in synset1:
				similaritiesToSecondSynset = []
				for ss2 in synset2:
					mineSim = self.similarityWuPalmer(ss1,ss2)
					if mineSim > maxSim_wup :
						maxSim_wup = mineSim
					apiSim = ss1.wup_similarity(ss2)
					simWUP = ("Wu&Palmer", mineSim, apiSim)
					#
					mineSim = self.similarityShortestPath(ss1,ss2)
					if mineSim is None:
						mineSim = 0
					else:
						mineSim = mineSim / self.doubleMaxDepth
					if mineSim > maxSim_path :
						maxSim_path = mineSim
					apiSim = ss1.path_similarity(ss2)
					if apiSim is None:
						apiSim = 0
					#else:
					#	apiSim = apiSim * self.doubleMaxDepth
					simSP = ("Path", mineSim, apiSim)
					#
					mineSim = self.similarityLC(ss1,ss2)
					if mineSim > maxSim_lch :
						maxSim_lch = mineSim
					try:
						apiSim = ss1.lch_similarity(ss2)
					except:
						apiSim = "ERROR: different PoS"
					simLC = ("L&C", mineSim, apiSim)
					#
					similaritiesToSecondSynset.append((ss2.name(), simWUP, simSP, simLC))
				similaritiesToFirstSynset.append((ss1.name(), similaritiesToSecondSynset))

			self.similarities.append((wp[0], wp[1], wp[2], similaritiesToFirstSynset)) # )) #
			self.mineSimilarity_wup.append(maxSim_wup)
			self.mineSimilarity_path.append(maxSim_path)
			self.mineSimilarity_lch.append(maxSim_lch)
		return self.similarities

	def depth_Manual(self, node, visited=set()): #
		hys = node.hypernyms();
		if len(hys) is 0 :
			return 1
		min_dep = 0
		for h in hys:
			if not (h.name() in visited):
				visited.add(h.name())
				d = self.depth_Manual(h, visited)
				if (min_dep is 0) or (d > min_dep):
					min_dep = d
		return 1 + min_dep

	def depth(self, synset):
		synsName = synset.name()
		cachedDepth = self.sysnetsDepthCache.get(synsName, None)
		if cachedDepth is not None:
			return cachedDepth
		cachedDepth = min(len(hyp_path) for hyp_path in synset.hypernym_paths())
		self.sysnetsDepthCache[synsName] = cachedDepth
		return cachedDepth

	def similarityWuPalmer(self, synset1, synset2):
		# search for the LOWEST common subsuber -> highest depth
		lcs_s = synset1.lowest_common_hypernyms(synset1, synset2)
		lcs = None
		lcs_depth = None
		for l in lcs_s:
			l_d = self.depth(l)
			if (lcs_depth is None) or (l_d > lcs_depth):
				lcs_depth = l_d
				lcs = l
		return (2.0*lcs_depth) / (self.depth(synset1) + self.depth(synset2))  #lowest_common_hypernyms

	def similarityShortestPath(self, synset1, synset2):
		if (synset1 is synset2) or (synset1 == synset2) or (synset1.name() is synset2.name()) or (synset1.name() == synset2.name()) or (id(synset1.name()) == id(synset2.name())):
			return self.doubleMaxDepth
		spd = synset1.shortest_path_distance(synset2)
		if spd is None:
			return 0 #self.doubleMaxDepth
		return self.doubleMaxDepth - spd# "* self.doubleMaxDepth" added as test

	def similarityLC(self, synset1, synset2):
		if (synset1 is synset2) or (synset1 == synset2) or(synset1.name() is synset2.name()) or (synset1.name() == synset2.name()) or (id(synset1.name()) == id(synset2.name())):
			return - math.log(1 / (1 + self.doubleMaxDepth))
		else:
			spd = synset1.shortest_path_distance(synset2)
			if spd is None:
				return 0
			return - math.log( spd / self.doubleMaxDepth)


	def spearmanRankCorrelationCoefficient(self, a1, a2):
		return spearmanr(a1, a2)

	def pearsonCorrelationCoefficient(self, a1, a2):
		return np.cov(a1, a2)/(np.std(a1)*np.std(a2))


	def performExercise(self):
		siml = self.computeSimilaritiesOfLoadedPairs()
		#now perform similarities
		similarities_to_test = {
			"Wu&Palmer"	: self.mineSimilarity_wup,
			"Path"		: self.mineSimilarity_path,
			"L&C"		: self.mineSimilarity_lch
		}
		indexes_computed = {
			"Wu&Palmer"	: None,
			"Path"		: None,
			"L&C"		: None
		}
		a_read_similarities = np.array(self.readSimilar)
		#calcolo gli indici tra i valori dati nel file e quelli calcolati
		for sim in similarities_to_test:
			a_mine = np.array(similarities_to_test[sim])
			pearson = self.pearsonCorrelationCoefficient(a_read_similarities, a_mine)
			spearman = self.spearmanRankCorrelationCoefficient(a_read_similarities, a_mine)
			indexes_computed[sim] = (sim, pearson, spearman)
		return (siml, indexes_computed)

	def printComputedPairsSimilarities(self, als = None):
		if als is None:
			als = self.computeSimilaritiesOfLoadedPairs()
		print("all similarities:")
		for wordPair in als:
			print("\n\n\tWith words pair: <1:", wordPair[0], " ; 2: ", wordPair[1], "> have similarity from file: ", wordPair[2])
			print(", got from:\n\t\tfor each first's word's synsets:")
			for ss1 in wordPair[4]:
				print("-\t\tsynset 1: ", ss1[0], " paired with:")
				for ss2 in ss1[1]:
					print("--\t\t\tsynset 2: ", ss2[0], " there's similarity:")
					sim = ss2[1]
					print("\t\t\t\t", sim[0], " -> ", sim[1], ", from API: ", sim[2])
					sim = ss2[2]
					print("\t\t\t\t", sim[0], " -> ", sim[1], ", from API: ", sim[2])
					sim = ss2[3]
					print("\t\t\t\t", sim[0], " -> ", sim[1], ", from API: ", sim[2])
	
	def printExercise(self):
		res = self.performExercise()
		indexes_computed = res[1]
		print("computed: similarities")
		for index_c in indexes_computed:
			t = indexes_computed[index_c]
			print("\n-", t[0], "\n\t\t___ pearson:", t[1], "\n\t\t___ spearman:", t[2])
		#self.printComputedPairsSimilarities(res[0])

#

print('start')
manager = ExerciseManager()
manager.initAll()
print("max depth:", manager.getMaxDepth())
print("double of max depth:", manager.getDoubledMaxDepth())
manager.printExercise()
print("END")