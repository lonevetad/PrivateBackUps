import nltk
import math
from nltk.stem import WordNetLemmatizer 
from nltk.corpus import stopwords 
from nltk.corpus import wordnet as wn
from nltk.tokenize import word_tokenize

	#
	# NOTE: START code took from the Lesk exercise 
	#

PUNCTUATION = u",.?!()[]-_\"\'\\\n\r\t;:+*<>@#§^$%&|/’”“…"
STOP_WORDS_ENG = set(stopwords.words('english'))
LEMMATIZER = WordNetLemmatizer() 
TAG_DICT = {"J": wn.ADJ,
			"N": wn.NOUN,
			"V": wn.VERB,
			"R": wn.ADV}

	#
	# NOTE: END code took from the Lesk exercise 
	#


def indexOf(stringToScan, charToLookFor):
	for i in range(len(stringToScan)):
		s = stringToScan[i]
		if s == charToLookFor or s is charToLookFor:
			return i
	return -1


class ArticleExtracter(object):
	"""docstring for ArticleExtracter"""
	def __init__(self):
		pass

	def extract_content(self, article, compression_rate_percentage = 10):
		return None



#
# EXTRACTOR from WordNet
#
class WordNetExtractor(ArticleExtracter):

	def extract_wnpostag_from_postag(self, tag):
		for key in TAG_DICT:
			if key in tag.upper(): # UPPER CASE
				return TAG_DICT[key]
		return None
		#return TAG_DICT.get(tag[0].upper(), None)

	def lemmatize_tupla_word_postag(self,tupla):
		"""
		giving a tupla of the form (wordString, posTagString) like ('guitar', 'NN'), return the lemmatized word
		"""
		t = tupla[1].strip()
		tag = self.extract_wnpostag_from_postag(t)
		return (LEMMATIZER.lemmatize(tupla[0], tag), t) if tag is not None else (tupla[0], None)

	def bag_of_tagged_words(self, sentence, stop_words=None):
		if stop_words is None:
			stop_words = STOP_WORDS_ENG
		lemmatized_words = [ self.lemmatize_tupla_word_postag(ow) for ow in nltk.pos_tag(word_tokenize(sentence)) ]
		cleaned_words = [ w for w in lemmatized_words if (w[0] not in PUNCTUATION) and (w[0] not in stop_words) ]
		lemmatized_words = None
		return cleaned_words
	#
	# NOTE: END code took from the Lesk exercise 
	#

	def extract_content(self, article, compression_rate_percentage = 10):
		'''
		print("\n\ntitle:: ", article[0])
		for paragraph in article[1]:
			print(paragraph)
		'''
		synsets_repetited = {}
		title_extracted = self.bag_of_tagged_words(article[0])
		for title_word in title_extracted:
			synsets_repetited[title_word] = 1
		#for each article
		for paragraph in article[1]:
			par_words = self.bag_of_tagged_words(paragraph)
			for p_w in par_words:
				if p_w in synsets_repetited:
					synsets_repetited[p_w] = 1 + synsets_repetited[p_w]
				else:
					synsets_repetited[p_w] = 1
		#print( [ (synsets_repetited[w], w) for w in synsets_repetited if synsets_repetited[w] > 1 ])
		return [ w[0] for w in synsets_repetited if synsets_repetited[w] > 1 ] #w[0] because only words are needed, not tags
	#



#
# EXTRACTOR from Nasari
#
class NasariExtractor(ArticleExtracter):

	def __init__(self, articleManager, wordNetExtractor):
		ArticleExtracter.__init__(self)
		self.articleManager = articleManager
		self.wordNetExtractor = wordNetExtractor

	def extract_content(self, article):
		print("implement and print it, later")
		return None

	def weighted_overlap(self, v1, v2):
		correlated_words_1 = v1[2]
		correlated_words_2 = v2[2]
		overlapping_words = [] #a.k.a.: overlapping dimensions
		if(len(correlated_words_1) < len(correlated_words_2)):
			for c_w in correlated_words_1:
				if c_w in correlated_words_2:
					overlapping_words.append(c_w)
		else:
			for c_w in correlated_words_2:
				if c_w in correlated_words_1:
					overlapping_words.append(c_w)
		if len(overlapping_words) == 0:
			return 0.0
		'''
		if len(overlapping_words) == 1:
			denominator = 0.5
		else:
			denominator = 0.5 * math.log(len(overlapping_words))
		'''
		i = 0
		denominator = 0
		numerator = 0
		for o_w in overlapping_words:
			numerator += 1/ (correlated_words_1[o_w] + correlated_words_2[o_w])
			i += 2
			denominator += 1/i
		return numerator / denominator

	def vector_similarity(self, v1, v2):
		score = self.weighted_overlap(v1, v2)
		return 0.0 if score == 0.0 else math.sqrt(score)

	def word_similarity(self, w1, w2):
		return self.vector_similarity(self.get_vector(w1), self.get_vector(w2))


	def get_vector(self, word):
		ddn = self.articleManager.get_nasari_table()
		return ddn[word] if word in ddn else self.articleManager.get_default_nasari_vector(word)



class NasariExtractor_ParagraphByTitle(NasariExtractor):
	def __init__(self, articleManager, wordNetExtractor):
		NasariExtractor.__init__(self, articleManager, wordNetExtractor)
	
	def extract_content(self, article, compression_rate_percentage = 10):
		#determinare le parole importanti del titolo, cercando poi i vettori di Nasari associati
		# poi cercare il paragrafo con il più alto score
		bag_words_title_temp = self.wordNetExtractor.bag_of_tagged_words(article[0])
		bag_words_title = [ w[0].lower() for w in bag_words_title_temp ]
		#print("\n\n--- TITLE UNSTOPPING: ", bag_words_title)
		bag_words_title_temp = None
		title_vectors = [ self.get_vector(tw) for tw in bag_words_title ]
		#print("--- TITLE VECTORED: ", title_vectors)
		#print("PARAGRAPH\n",article[1],"\n")
		#scored_paragraphs = []
		amount_of_paragraph_to_retrieve = math.ceil( len(article[1]) * compression_rate_percentage / 100.0)
		paragraph_and_score = []
		for paragraph in article[1]:
			botw_t  = self.wordNetExtractor.bag_of_tagged_words(paragraph)
			botw_paragraph = [ w[0] for w in botw_t ]
			botw_t = None
			score = self.score_paragraph(title_vectors, botw_paragraph)
#			scored_paragraphs.append( (score, botw_paragraph) )
			paragraph_and_score.append( (score, botw_paragraph, paragraph) )
		paragraph_and_score = sorted(paragraph_and_score, key= lambda x:x[0], reverse=True)
		compressed = []
		i = 0
		while i < amount_of_paragraph_to_retrieve:
			#compressed.append(" ".join(paragraph_and_score[i][]))
			compressed.append(paragraph_and_score[i][2])
			i += 1
		paragraph_score = None
		return compressed

	def score_paragraph(self, title_vectors, paragraph_unstopping_words):
		score = 0
		for puw in paragraph_unstopping_words:
			puw_vector = self.get_vector(puw)
			for t_v in title_vectors:
				score += self.vector_similarity(t_v, puw_vector)
		return score

'''
	def get_best_paragraph(self, article):
		#determinare le parole importanti del titolo, cercando poi i vettori di Nasari associati
		# poi cercare il paragrafo con il più alto score
		bag_words_title_temp = self.wordNetExtractor.bag_of_tagged_words(article[0])
		bag_words_title = [ w[0].lower() for w in bag_words_title_temp ]
		bag_words_title_temp = None
		title_vectors = [ self.get_vector(tw) for tw in bag_words_title ]
		#scored_paragraphs = []
		best_paragraph = (0, None)
		for paragraph in article[1]:
			botw_t  = self.wordNetExtractor.bag_of_tagged_words(article[0])
			botw_paragraph = [ w[0] for w in botw_t ]
			botw_t = None
			score = self.score_paragraph(title_vectors, botw_paragraph)
#			scored_paragraphs.append( (score, botw_paragraph) )
			if score > best_paragraph[0]:
				best_paragraph = (score, botw_paragraph)
		return best_paragraph
	
	def max_score_in_paragraph(self, title_vectors, paragraph_unstopping_words):
		max_score = 0
		for puw in paragraph_unstopping_words:
			puw_vector = self.get_vector(puw)
			for t_v in title_vectors:
				score = self.vector_similarity(t_v, puw_vector)
				if score > max_score:
					max_score = score
		return max_score
'''


#
#
#
#
#
#
#
#
#


class ArticleManager(object):
	"""docstring for ArticleManager"""
	def __init__(self):
		super(ArticleManager, self).__init__()
		self.dd_nasari = None
		self.lines_to_read = -1 # negative number == all files
		self.articles = None
		self.article_extractors = [ WordNetExtractor() ]
		self.article_extractors.append( NasariExtractor_ParagraphByTitle(self, self.article_extractors[0]) )

	def load_dd_nasari(self, pathFileName = None):
		if pathFileName is None:
		 	pathFileName = "./dd-small-nasari-15.txt"
		if self.dd_nasari is not None:
		 	return
		self.dd_nasari = {}
		with open(pathFileName, 'r') as fp:
			#	read_lines = [line.rstrip('\n') for line in fp.readlines()]
			#for line in read_lines:
			counter = 1
			line = fp.readline()
			while line and (self.lines_to_read < 0 or counter < self.lines_to_read):
				parts = line.split(';')
				identifier = parts[0]
				word = parts[1].lower()
				del parts[0]
				del parts[0]
				correlated_words = {} #the "vector"
				for p in parts:
					#try :
					#	index_underscore = p.index("_")
					index_underscore  = indexOf(p, "_")
					if index_underscore >= 0:
						subword = p[0:index_underscore].lower()
						#correlated_words.append( (p[0:index_underscore], float(p[index_underscore+1:])) ) #<word, id_word>
						correlated_words[subword] = float(p[index_underscore+1:]) #<word, id_word>
#					except ValueError:
					#else:
					#	print("ERROR not divisible word: --", p, "--")
				#add the "vector"
				#self.dd_nasari.append( (word, identifier, correlated_words) )
				self.dd_nasari[word] = (word, identifier, correlated_words) 
				correlated_words = None
				parts = None
				line = fp.readline()
				counter += 1

	def get_dd_nasari(self):
		if self.dd_nasari is None:
			self.load_dd_nasari()
		return self.dd_nasari


	def get_nasari_table(self):
		return self.get_dd_nasari()


	def get_default_nasari_vector(self, word):
		return (word, -1, {word: 10} )
	
	def print_dd_nasari(self):
		d = self.get_dd_nasari()
		for word in d:
			print(word, " -> ", d[word])

	def load_articles(self, array_of_text_files = None, original_files=True):
		if array_of_text_files is None:
			prefix = "./"
			if original_files:
				prefix = prefix + "text_data_original/"
			else:
				prefix = prefix + "text_data_modified/"
			array_of_text_files = [
				prefix + "Donald-Trump-vs-Barack-Obama-on-Nuclear-Weapons-in-East-Asia.txt",
				prefix + "People-Arent-Upgrading-Smartphones-as-Quickly-and-That-Is-Bad-for-Apple.txt",
				prefix + "The-Last-Man-on-the-Moon--Eugene-Cernan-gives-a-compelling-account.txt"
			]
		self.articles = []
		for f_t in array_of_text_files:
			with open(f_t, 'r') as fp:
				read_lines = [line.strip('\n') for line in fp.readlines()]
				title = read_lines[0]
				del read_lines[0]
				all_paragraphs = []
				for line in read_lines:
					line = line.strip()
					if not(line == ""):
						all_paragraphs.append(line)
				self.articles.append( (title, all_paragraphs) )


	#now start the exercise


	def perform_exercise(self, index_article_extractor=0):
		'''
			index_article_extractor is the iindex of the article's extractor's implementation,
			ranging from 0 (zero) to N excluded (right now, N=1)
		'''
		self.load_dd_nasari()
		self.load_articles()
		#for article in self.articles:
		#	self.extract_content(article)
		extractor = self.article_extractors[index_article_extractor]
		return [ (article, extractor.extract_content(article, 30)) for article in self.articles ]

	def print_exercise(self, index_article_extractor=0):
		print("ciaone")
		extracted_articles = self.perform_exercise(index_article_extractor)
		print("\n\n\n")
		for e_a in extracted_articles:
			print("article's title: -- ", e_a[0][0])
			print("extracted words:\n\t", e_a[1])
			print("\n\n---------------\n")

# the "main"

nm = ArticleManager()
print("start:")

'''
print("start lines read:")
nm.print_dd_nasari()
print("end read lines")
'''

nm.print_exercise(0)
print("\t###################\n\t#\tSEPARATOR\t#\n\t###################")
nm.print_exercise(1)

'''
nasari_extractor = nm.article_extractors[1]
wo = nasari_extractor.word_similarity("silicon", "helium" )
print("wo: ", wo)
wo = nasari_extractor.word_similarity("angstrom", "helium" )
print("wo: ", wo)
wo = nasari_extractor.word_similarity("angstrom", "silicon" )
print("wo: ", wo)
'''

print("\n\n\nend")
