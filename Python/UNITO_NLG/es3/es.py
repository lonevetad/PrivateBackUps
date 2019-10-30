
class WordPairSimilaritiesHolder(object):
	def __init__(self):
		self.pairs = {}

	def add_pair(self, w1, w2, similarity):
		if w1 is None or w2 is None:
			return False
		t = (w2, w1)
		if t in self.pairs:
			return False
		t = (w1, w2)
		if t in self.pairs:
			return False
		# or w1 in pairs or w2 in pairs:
		self.pairs[t] = (t, similarity)

	def get_similarity(self, w1, w2):
		if w1 is None or w2 is None:
			return -1 # negative number == not present
		t = (w2, w1)
		if t in self.pairs:
			return self.pairs[t]
		t = (w1, w2)
		if t in self.pairs:
			return self.pairs[t]
		return -1 # negative number == not present

	def for_each(self, words_similarity_consumer = None ):
		if words_similarity_consumer is None:
			words_similarity_consumer = lambda w1, w2, sim : print("w1: ", w1, ", w2: ", w2, ", sim: ", sim)
		for t in self.pairs:
			words_similarity_consumer(t[0], t[1], self.pairs[t][1]) #duck typing



class Es3Manager(object):
	def __init__(self):
		self.pairs = WordPairSimilaritiesHolder()

	def load_pairs(self, file_name=None):
		if file_name is None:
			file_name = "./100_coppie_annotate_ottina_400-500.txt"
		#
		with open(file_name, 'r') as fp:
			line = fp.readline()
			while line:
				parts = line.split('\t')
				self.pairs.add_pair(parts[0].strip(), parts[1].strip(), float(parts[2].strip()))
				line = fp.readline()

em = Es3Manager()
em.load_pairs()
em.pairs.for_each(None)
