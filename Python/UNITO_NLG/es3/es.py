import math

MAX_RANGE = 4.0

#class CosineSimilarityCalculator(object):

def dot_product2(v1, v2):
	return sum(map(lambda x, y: x*y , v1, v2))

#
def cosine_similarity_1(v1, v2):
	prod = dot_product2(v1, v2)
	len1 = math.sqrt(dot_product2(v1, v1))
	len2 = math.sqrt(dot_product2(v2, v2))
	return prod / (len1 * len2)

#
def cosine_similarity_2(a, b):
	return sum([i*j for i,j in zip(a, b)])/(math.sqrt(sum([i*i for i in a]))* math.sqrt(sum([i*i for i in b])))

#
def cosine_similarity_3(text1, text2):
	vec1 = text1
	vec2 = text2
	intersection = set(vec1.keys()) & set(vec2.keys())
	numerator = sum([vec1[x] * vec2[x] for x in intersection])
	sum1 = sum([vec1[x]**2 for x in vec1.keys()])
	sum2 = sum([vec2[x]**2 for x in vec2.keys()])
	denominator = math.sqrt(sum1) * math.sqrt(sum2)
	if not denominator:
		 return 0.0
	else:
		 return round(float(numerator) / denominator, 3)

#
#
#

class WordPairSimilaritiesHolder(object):
	def __init__(self):
		self.pairs_scored_by_hand = {}

	def add_pair(self, w1, w2, similarity):
		if w1 is None or w2 is None:
			return False
		t = (w2, w1)
		if t in self.pairs_scored_by_hand:
			return False
		t = (w1, w2)
		if t in self.pairs_scored_by_hand:
			return False
		# or w1 in pairs or w2 in pairs:
		self.pairs_scored_by_hand[t] = (t, similarity) 
		#self.pairs_scored_by_hand[t] = [t, similarity, 0.0] #list: words' tupla, priory similarity, babelnet's coine's similarity (initially 0.0)

	def get_similarity(self, w1, w2):
		if w1 is None or w2 is None:
			return -1 # negative number == not present
		t = (w2, w1)
		if t in self.pairs_scored_by_hand:
			return self.pairs_scored_by_hand[t]
		t = (w1, w2)
		if t in self.pairs_scored_by_hand:
			return self.pairs_scored_by_hand[t]
		return -1 # negative number == not present

	def for_each(self, words_similarity_consumer = None ):
		if words_similarity_consumer is None:
			words_similarity_consumer = lambda w1, w2, sim_prior, tuple_bnvectors_maximising_score : print("w1: ", w1, ", w2: ", w2, ", sim_prior: ", sim_prior, ", tuple_bnvectors_maximising_score: ", tuple_bnvectors_maximising_score)
			#words_similarity_consumer = lambda w1, w2, sim_prior, sim_computed : print("w1: ", w1, ", w2: ", w2, ", sim_prior: ", sim_prior, ", sim_computed: ", sim_computed)
		for t in self.pairs_scored_by_hand:
			p = self.pairs_scored_by_hand[t]
			words_similarity_consumer(t[0], t[1], p[1]) #duck typing
			#words_similarity_consumer(t[0], t[1], p[1], p[2]) #duck typing



class BabelNetVectorsManager(object):
	'''
	Store BabelNet's vectors as tuples like (id, name, list of scores)
	'''
	def __init__(self) :
		self.by_id = {}
		self.by_word_string = {}

	def add_word(self, bn_id, word_string, vectors):
		t = (bn_id, word_string, vectors)
		self.by_id[bn_id] = t
		self.by_word_string[word_string] = t

	def get_by_bn_id(self, bn_id):
		return self.by_id[bn_id] if bn_id in self.by_id else None

	def get_by_word_string(self, word_string):
		return self.by_word_string[word_string] if word_string in self.by_word_string else None

	def for_each(self, babelnet_vector_consumer=None):
		if babelnet_vector_consumer is None:
			babelnet_vector_consumer = lambda bn_id, w2, sim_prior : print("w1: ", w1, ", w2: ", w2, ", sim_prior: ", sim_prior)
		


class WordsToBabelNetIDMap(object):
	def __init__(self) :
		self.word_to_synsets_id = {}
		self.synset_id_to_word = {}

	def add_match_word_to_synsets_id(self, word, synsets_id):
		self.word_to_synsets_id[word] = synsets_id
		for s in synsets_id:
			self.synset_id_to_word[s] = word

	def get_word_by_synset_id(self, synset_id):
		return self.synset_id_to_word[synset_id] if synset_id in self.synset_id_to_word else None

	def get_synsets_id_by_word(self, word):
		return self.word_to_synsets_id[word] if word in self.word_to_synsets_id else None

#
#
#

class Es3Manager(object):
	def __init__(self):
		self.pairs_scored_by_hand = None
		self.bn_vectors = None
		self.it_words_to_babelnet_id = None
		#self.babelnet_word_vector_to_read

	def check_pairs_non_nullity(self):
		if self.pairs_scored_by_hand is not None:
			return True
		self.pairs_scored_by_hand = WordPairSimilaritiesHolder()
		return False

	def load_pairs_scored_by_hands(self, file_name=None):
		if self.check_pairs_non_nullity():
			return
		if file_name is None:
			file_name = "./100_coppie_annotate_ottina_400-500.txt"
		#
		with open(file_name, 'r') as fp:
			line = fp.readline()
			while line:
				parts = line.split('\t')
				self.pairs_scored_by_hand.add_pair(parts[0].strip(), parts[1].strip(), float(parts[2].strip()))
				#self.pairs_scored_by_hand.add_pair(parts[0].strip(), parts[1].strip(), float(parts[2].strip()))
				line = fp.readline()

	def load_it_words_to_babelnet_id_map(self, file_name=None):
		if self.it_words_to_babelnet_id is not None:
			return self.it_words_to_babelnet_id
		if file_name is None:
			file_name = "./SemEval17_IT_senses2synsets.txt"
		self.it_words_to_babelnet_id = WordsToBabelNetIDMap()
		with open(file_name, 'r') as fp:
			line = fp.readline().strip()
			while line:
				word_name = line[1:].strip() #remove the hashtag
				line = fp.readline()
				babelnet_id_synsets = []
				while line and line[0] is not "#":
					#reove unecessary "bn:" at start
					babelnet_id_synsets.append( line[3:].strip() )
					line = fp.readline()
				self.it_words_to_babelnet_id.add_match_word_to_synsets_id(word_name, babelnet_id_synsets)
				#print("readin babelnet word_name: ", word_name, " -> ", babelnet_id_synsets)


	def load_babelnet_words(self, file_name=None):
		if self.bn_vectors is not None:
			return self.bn_vectors
		if file_name is None:
			file_name = "./mini_NASARI_hardlink.tsv"
		self.bn_vectors = BabelNetVectorsManager()
		with open(file_name, 'r') as fp:
			line = fp.readline()
			while line:
				parts = line.split('\t')
				vector_identifier = parts[0].strip()
				bn_id = vector_identifier[3:12]
				bn_word = vector_identifier[14:]
				del parts[0]
				vectors = [ float(x.strip()) for x in parts ]
				parts = None
				self.bn_vectors.add_word(bn_id, bn_word, vectors)
				#print("\nreadin babelnet vector_identifier: id:", bn_id, ", word:", bn_word, ", vectors:", None) # vectors)
				line = fp.readline()
		return self.bn_vectors

	def cosine_similarity(self, bn1, bn2):
		# TODO
		return cosine_similarity_1(bn1[2], bn2[2]) * MAX_RANGE

	def tuple_bnvectors_score(self, w1, w2):
		#first redirect/mapping described on the "mappature" part, close to the EOF
		synsets1 = self.it_words_to_babelnet_id.get_synsets_id_by_word(w1)
		synsets2 = self.it_words_to_babelnet_id.get_synsets_id_by_word(w2)
		if synsets1 is None or synsets2 is None:
			return None
		greater_ss1 = None
		greater_ss2 = None
		greater_ss_similarity = 0
		caching_ss2_to_bnvectors = {}
		#now perform the second mapping: start it through ...

		#..filling the cache
		for ss2 in synsets2:
			bn = self.bn_vectors.get_by_bn_id(ss2)
			if bn is not None:
				caching_ss2_to_bnvectors[ss2] = bn

		for ss1 in synsets1:
			#second and last part of the second mapping
			bn1 = self.bn_vectors.get_by_bn_id(ss1)
			if bn1 is not None:
				for ss2 in caching_ss2_to_bnvectors:
					bn2 = caching_ss2_to_bnvectors[ss2]
					#i have all synset pairs
					score = self.cosine_similarity(bn1, bn2)
					if score > greater_ss_similarity:
						greater_ss_similarity = score
						greater_ss1 = ss1
						greater_ss2 = ss2
						#tuple_bnvectors_score = score # a che serve?
			#else:
			#	print(ss1, "not mapped in babelnet")



		# TODO perform similarity
		return (greater_ss1, greater_ss2, greater_ss_similarity)

	def perform_exercise(self):
		self.load_pairs_scored_by_hands()
		self.load_it_words_to_babelnet_id_map()
		self.load_babelnet_words()

		print("\n\n\n--------READ\n\n")

		results = []
		'''
		for pair in self.pairs_scored_by_hand:
			computed_score = self.tuple_bnvectors_score(pair[0], pair[1])
			results.append( () )
		'''
		self.pairs_scored_by_hand.for_each( lambda w1, w2, sim_prior: results.append( (w1,w2,sim_prior, self.tuple_bnvectors_score(w1,w2)) ) )
		return results

	def print_exercise(self):
		res = self.perform_exercise()
		for r in res:
			print("\n\n w1: ", r[0], ", w2: ", r[1])
			print("\t\tmine similarity: ")
			print("\t\t", r[2])
			print("\t\tcomputed greater synset pair\'s similarity:")
			computed_score = r[3]
			if computed_score is None:
				print("\t\tNone")
			else:
				print("\t\t", computed_score[2], ".....from: ", computed_score[0], "->", self.it_words_to_babelnet_id.get_word_by_synset_id(computed_score[0]), "and", computed_score[1], "->", self.it_words_to_babelnet_id.get_word_by_synset_id(computed_score[1]))

	#deprecated
	'''
	def word_dissimilarity(w1, s2):
		return None
	'''

#mappature:
# dal file "100_coppie_..." (ex it.test.data.txt) prendo le coppie, che sono in italiano
# da lì mappo le italiano -> babelnet-word tramite il file "SemEval17_IT_senses2synsets"
# ottenuto il bable id, faccio babel_id -> vettore per ottenere i vettori da valutare con mini_babelnet...
# quindi
# "100_coppie.." -> "SemVal17_IT.." -> "mini_babelnet..."


em = Es3Manager()
em.load_pairs_scored_by_hands()
'''
printer = lambda w1, w2, sim_prior : print("w1:\t", w1, ", w2:\t", w2, ",\tsim_prior: ", sim_prior)
#printer = lambda w1, w2, sim_prior, sim_computed : print("w1:\t", w1, ", w2:\t", w2, ",\tsim_prior: ", sim_prior, ",\tsim_computed: ", sim_computed)
em.pairs_scored_by_hand.for_each(printer)
'''
em.print_exercise()
