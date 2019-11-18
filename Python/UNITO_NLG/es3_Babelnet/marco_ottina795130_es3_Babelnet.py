import math
#import urllib2
#import urllib
#from urllib import request
import requests
'''
from urllib.request import urlopen
try:
	from urllib import urlencode
except ImportError: 
	try:
		from urllib.request import urlencode
	except ImportError: 
		from urllib.parse import urlencode
import json
import gzip
try:
	from StringIO import StringIO ## for Python 2
except ImportError:
	from io import StringIO ## for Python 3
'''
			 #"https://babelnet.io/v5/getSynset?id=bn:14792761n&key=c7f1058b-3674-4bc9-b345-d9f8ad54cafd"
SERVICE_URL = 'https://babelnet.io/v5/getSynset'
#SERVICE_URL = 'https://babelnet.io/v5/getSynsetIds'
KEY = 'c7f1058b-3674-4bc9-b345-d9f8ad54cafd'
LANG = "IT"
MAX_RANGE = 4.0
PREFIX_BN_SYNSET_REMOVED_ON_DICTIONARIES = "bn:"

#class CosineSimilarityCalculator(object):

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
		if synset_id is None:
			return None
		return self.synset_id_to_word[synset_id] if synset_id in self.synset_id_to_word else None

	def get_synsets_id_by_word(self, word):
		if word is None:
			return None
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


	def load_babelnet_words(self, file_name=None):
		if self.bn_vectors is not None:
			return self.bn_vectors
		if file_name is None:
			file_name = "./mini_NASARI.tsv"
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
				line = fp.readline()
		return self.bn_vectors

	#

	def cosine_similarity(self, bn1, bn2):
		#cosine_similarity(bn1[2], bn2[2])
		a = bn1[2] 
		b = bn2[2] 
		return sum([i*j for i,j in zip(a, b)])/(math.sqrt(sum([i*i for i in a]))* math.sqrt(sum([i*i for i in b]))) * MAX_RANGE

	def tuple_bnvectors_score(self, w1, w2):
		#first redirect/mapping described on the "mappature" part, close to the EOF
		synsets1 = self.it_words_to_babelnet_id.get_synsets_id_by_word(w1)
		synsets2 = self.it_words_to_babelnet_id.get_synsets_id_by_word(w2)
		if synsets1 is None or synsets2 is None:
			return None
		greater_ss1 = None
		greater_ss2 = None
		greater_ss_similarity = -2
		caching_ss2_to_bnvectors = {}
		#now perform the second mapping: start it through ...

		#..filling the cache:
		# since the retrival operation requires an Internet connection,
		# ti's wise to perform it only once and hold results in RAM
		vector_retriver_by_id = self.bn_vectors.get_by_bn_id #cache the function, also
		for ss2 in synsets2:
			bn = vector_retriver_by_id(ss2)
			if bn is not None:
				caching_ss2_to_bnvectors[ss2] = bn

		cos_sim = self.cosine_similarity #cache the function
		for ss1 in synsets1:
			#second and last part of the second mapping
			bn1 = vector_retriver_by_id(ss1)
			if bn1 is not None:
				for ss2 in caching_ss2_to_bnvectors:
					bn2 = caching_ss2_to_bnvectors[ss2]
					#i have all synset pairs
					score = cos_sim(bn1, bn2)
					if score > greater_ss_similarity:
						greater_ss_similarity = score
						greater_ss1 = ss1
						greater_ss2 = ss2
						#tuple_bnvectors_score = score # a che serve?
			#else:
			#	print(ss1, "not mapped in babelnet")
		if greater_ss1 is None:
			glossa_ss1 = None
		else:	
			glossa_ss1 = self.get_babelnet_gloss_by_babel_id(PREFIX_BN_SYNSET_REMOVED_ON_DICTIONARIES + greater_ss1)
		if greater_ss2 is None:
			glossa_ss2 = None
		else:	
			glossa_ss2 = self.get_babelnet_gloss_by_babel_id(PREFIX_BN_SYNSET_REMOVED_ON_DICTIONARIES + greater_ss2)
		return (greater_ss1, greater_ss2, greater_ss_similarity, glossa_ss1, glossa_ss2)

	#

	def get_babelnet_gloss_by_word(self, word):

		'''
		a word string, not the babel_id
		'''
		return None

	def get_babelnet_gloss_by_babel_id(self, babel_id):
		params = {
			'id' : babel_id,
			'searchLang' : LANG,
			'key'  : "c7f1058b-3674-4bc9-b345-d9f8ad54cafd"
		}
		'''
		url = SERVICE_URL + '?' + urlencode(params)
		req = request(url)
		req.add_header('Accept-encoding', 'gzip')
		response = urlopen(req)
		results = []
		if response.info().get('Content-Encoding') == 'gzip':
				buf = StringIO( response.read())
				f = gzip.GzipFile(fileobj=buf)
				data = json.loads(f.read())
				for result in data:
						results.append(result['id'])
		return results
		'''
		'''
		'''
		r = requests.get(url=SERVICE_URL, params=params)
		data = r.json()

		if data is None or len(data["glosses"]) == 0:
			return ["GLOSS INESISTENTE"]
		else:
			return [ g["gloss"] for g in data["glosses"] ]
	#

	def perform_exercise(self):
		self.load_pairs_scored_by_hands()
		self.load_it_words_to_babelnet_id_map()
		self.load_babelnet_words()

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
				w1 = self.it_words_to_babelnet_id.get_word_by_synset_id(computed_score[0])
				w2 = self.it_words_to_babelnet_id.get_word_by_synset_id(computed_score[1])
				print("\t\t", computed_score[2], ".....from: ", computed_score[0], "->", w1 , "and", computed_score[1], "->", w2)
				print("\t\thaving glossa:\n\t\t - ", w1)
				print("\t\t\t ", computed_score[3])
				print("\t\t - ", w2)
				print("\t\t\t ", computed_score[4])
	#(w1, s2):
		return None
	'''

#mappature:
# dal file "100_coppie_..." (ex it.test.data.txt) prendo le coppie, che sono in italiano
# da lì mappo le italiano -> babelnet-word tramite il file "SemEval17_IT_senses2synsets"
# ottenuto il bable id, faccio babel_id -> vettore per ottenere i vettori da valutare con mini_babelnet...
# quindi
# "100_coppie.." -> "SemVal17_IT.." -> "mini_babelnet..."

'''

'''
em.load_pairs_scored_by_hands()
printer = lambda w1, w2, sim_prior : print("w1:\t", w1, ", w2:\t", w2, ",\tsim_prior: ", sim_prior)
#printer = lambda w1, w2, sim_prior, sim_computed : print("w1:\t", w1, ", w2:\t", w2, ",\tsim_prior: ", sim_prior, ",\tsim_computed: ", sim_computed)
em.pairs_scored_by_hand.for_each(printer)
'''
em = Es3Manager()

print("START\n\n\n")

em.print_exercise()

print("\n\n\nEND")


'''
import pprint
d = requests.get("https://babelnet.io/v5/getSynset?id=bn:14792761n&key=c7f1058b-3674-4bc9-b345-d9f8ad54cafd").json()
print(d)
print("\n\n\n\n\n\n\n............\n\n\n\n ")
pp = pprint.PrettyPrinter(indent=4)
pp.pprint(d)

print("\n\n\n\n\n\n\n.......babel gloss.....\n\n\n\n ")
'''

