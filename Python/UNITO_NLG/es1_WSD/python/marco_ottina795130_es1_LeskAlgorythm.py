import nltk
from nltk.wsd import lesk
from nltk.stem import WordNetLemmatizer 
from nltk.corpus import semcor
from nltk.corpus import stopwords 
from nltk.corpus import wordnet as wn
from nltk.tokenize import word_tokenize

PUNCTUATION = u",.?!()-_\"\'\\\n\r\t;:+*<>@#§^$%&|/’”"
STOP_WORDS_ENG = set(stopwords.words('english'))
LEMMATIZER = WordNetLemmatizer() 
TAG_DICT = {"J": wn.ADJ,
			"N": wn.NOUN,
			"V": wn.VERB,
			"R": wn.ADV}
WORDS_FROM_SEM_CORPUS = 50



class POSFilteredWordsProvider(object):
	"""
	docstring for POSFilteredWordsProvider
	Iter over a list of "Tree" element filtering it by a given POS tag, one of the given set:
	('NN' or 'JJ' or 'VB' or 'RB').
	If "policy_extraction" is True then name extracted is the synset's "name()", otherwise the original word is kept
	Example of usage:
	sentence_tree = semcor.tagged_sents(tag="both")[ some_index ]
	iterator = POSFilteredWordsProvider(sentence_tree, pos='NN', policy_extraction=True)
	while iterator.has_next():
		print( iterator.next() )
	or
	iterator.contains('dog.n.01')
	"""
	def __init__(self, sentence_tree, pos=('NN' or 'JJ' or 'VB' or 'RB'), policy_extraction = True):
		super(POSFilteredWordsProvider, self).__init__()
		self.sentence_tree = sentence_tree
		self.pos = pos + " " # the pos to look for
		self.policy_extraction = policy_extraction
		self.current_index = 0
		self.len_s_t = 0
		self.filtered_words = None
		self._build()

	def _build(self):
		fw = []
		self.filtered_words = fw
		for word_node in self.sentence_tree:
			s = str(word_node)
			if self.pos in s:
				start_index = s.find('\'') + 1 #+1 added to optimize
				if start_index > 0:
					#search the next quote
					end_index = start_index #+ 1
					len_s = len(s)
					while end_index < len_s and s[end_index] is not '\'':
						end_index += 1
					if end_index < len_s:
						synset_name_and_word = s[start_index : end_index] #+1
						# synset_name_and_word is like "probe.n.01.investigation" or "friday.n.01.Friday" .. replace the first part with the last
						len_s = len(synset_name_and_word) - 1
						end_index = len_s
						while end_index >= 0 and synset_name_and_word[end_index] is not '.':
							end_index -= 1
						if end_index > 0:
							if self.policy_extraction:
								fw.append((word_node, synset_name_and_word[:end_index]))
							else:
								start_index = 0
								while start_index < end_index and synset_name_and_word[start_index] is not '.':
									start_index += 1
								if start_index is not end_index:
									fw.append((word_node, synset_name_and_word[end_index+1:] + synset_name_and_word[start_index: end_index]))#start_index: .. keep the dot
		self.filtered_words = fw
		self.len_s_t = len(fw)
		return fw;


	def has_next(self):
		return self.current_index < self.len_s_t

	def next(self):
		if self.has_next():
			e = self.filtered_words[self.current_index]
			self.current_index += 1
			return e
		return None

	def contains(self, synset_name):
		if synset_name is None or self.len_s_t <= 0:
			return False
		for (_, s_n) in self.filtered_words:
			if synset_name in s_n:
				return True
		return False


def extract_synset_name(synset):
	if synset is None:
		return None
	name = synset.name()
	return name

def extract_wnpostag_from_postag(tag):
	for key in TAG_DICT:
		if key in tag.upper():
			return TAG_DICT[key]
	return None

def lemmatize_tupla_word_postag(tupla):
	"""
	giving a tupla of the form (wordString, posTagString) like ('guitar', 'NN'), return the lemmatized word
	"""
	tag = extract_wnpostag_from_postag(tupla[1])	
	return (LEMMATIZER.lemmatize(tupla[0], tag), tupla[1]) if tag is not None else (tupla[0], None)

def sentence_to_tagged_words(sentence):
	original_words = word_tokenize(sentence)
	tagged_words = nltk.pos_tag(original_words) #returns a list of tuples: (word, tagString) like ('And', 'CC')
	original_words = None
	return tagged_words

def sentence_to_lemmatized_words(sentence, word_to_not_include = None):
	tagged_words = sentence_to_tagged_words(sentence)
	#lemmatized_words = [ lemmatize_tupla_word_postag(ow)[0] for ow in tagged_words ]
	lemmatized_words = None
	if word_to_not_include is None:
		lemmatized_words = [ lemmatize_tupla_word_postag(ow) for ow in tagged_words ]
	else:
		lemmatized_words = [ lemmatize_tupla_word_postag(ow) for ow in tagged_words if not (ow[0] == word_to_not_include or ow[0] is word_to_not_include) ]	
	tagged_words = None
	return lemmatized_words

def bag_of_tagged_words(sentence, stop_words=None, word_to_not_include = None):
	if stop_words is None:
		stop_words = STOP_WORDS_ENG
	lemmatized_words = sentence_to_lemmatized_words(sentence, word_to_not_include=word_to_not_include)
	#print(".-.-.-.-.\n\tsentence: ", sentence, "\n\t\tlemmatized_words: ", lemmatized_words)
	cleaned_words = [ w for w in lemmatized_words if (w[0] not in PUNCTUATION) and (w[0] not in stop_words) ]
	lemmatized_words = None
	return cleaned_words

def context_from_sentence(word, sentence):
	#simply, use a bag of words, using lemmatizing and removing stop words
	ret = bag_of_tagged_words(sentence, word_to_not_include = word)
	return ret

def synset_to_signature(sense):
	#create a list bag of words derived from all definitions and examples
	bag = set()
	#populate with it's own name through all of its lemma(s), then its hyponyms and hypernyms
	# since both hyponyms and hypernyms produces ana list of synsets, let's generalize it
	
	'''
	for some_synsets in [ [sense], sense.hyponyms(), sense.hypernyms() ]:
		for inner_synset in some_synsets:
			for lemma in inner_synset.lemmas():
				bag.add(lemma.name())
	'''
	#then add 
	b = bag_of_tagged_words(sentence = sense.definition())
	for w in b:
		bag.add(w[0])
	
	for example in sense.examples():
		b = bag_of_tagged_words(sentence = example)
		for w in b:
			bag.add(w[0])
	return bag

def computeOverlap(signature, context):
	"""
	given a signature (a set of lemmatized words from all symsets' examples and definitions	of
	a word to be disambiguated) and a context (a list of lemmatized words from a sentence holding the word
	to be disambiguated), compute the overlapping: just count the intersection of lemmatized words
	"""
	'''
	Knowing that the stemming implemente on nltk package is badly implemented as it
	returns the first shortest-in-length word (i.e., it could return a plural word
	if it's as long as the single one, like 'men' and 'man'), the overlapping could
	be computed as the best overlapping among all possible alternatives provided by
	the call "wn._morphy(your_word_as_string, wn.ASSOCIATED-POS_TAG)", but
	this could be a not easy, not trivial and heavy computation. -> Too lazy.
	'''
	sign = signature # set([ w[0] for w in signature])
	contx = set([ w[0] for w in context]) # context is a list of tuple <word ; POS_tag>
	return len(sign & contx)

def simplifiedLesk( word, sentence, context = None):
	best_sense = None
	max_overlap = 0
	if context is None:
		context = context_from_sentence(word, sentence)
	for sense in wn.synsets(word, pos=wn.NOUN):
		if sense is not None:
			signature = synset_to_signature(sense)
			overlap = computeOverlap(signature, context)
			if best_sense is None or overlap > max_overlap:
				max_overlap = overlap
				best_sense = sense
	return best_sense


def prepare_sentence(sentence):
	sentence = sentence[0:1].lower() + sentence[1:]
	startingStars = sentence.index("**")
	if startingStars < 0 or startingStars is None:
		return None
	endingStars = sentence.index("**", startingStars+1)
	if endingStars < 0 or endingStars < (startingStars +2) :
		return None
		# original, ambiguos-word, cleaned sentence, start-index
	word = sentence[startingStars+2: endingStars].lower()
	sentence_before_ambiguity = sentence[0 : startingStars]
	sentence_after_ambiguity  = sentence[endingStars+2 : ]
	cleaned_sentence = sentence_before_ambiguity + word + sentence_after_ambiguity
	return (sentence, word, cleaned_sentence, sentence_before_ambiguity, sentence_after_ambiguity ) #, startingStars, endingStars )

#

def sysnsetLemmasToString(synset):
	return "[" + " | ".join([sy.name() for sy in synset.lemmas()]) + "]"

def print_synset(synset):
	if synset is not None:
		print(synset.name())
		print(synset.definition())
		print(synset.examples())

# ESERCIZIO 1

def load_sentences(fileName=None):
	if fileName is None:
		fileName = './../sentences.txt';
	with open(fileName, 'r') as fp:
		#read_lines = [line.rstrip('\n')[2:] for line in fp.readlines() if line.startswith('- ', beg= 0, end = 4) ]
		read_lines = [line.rstrip('\n')[2:] for line in fp.readlines() if line.startswith('- ', 0, 4) ]
		return read_lines
	return None

def perform_exercise_sentences(sentences = load_sentences()):
	if sentences is None:
		return None
	prepared_sentences = [ prepare_sentence(s) for s in sentences if s is not None ]
	results = [ (ps[2], ps[1], simplifiedLesk(ps[1], ps[2]), ps[3], ps[4] ) for ps in prepared_sentences if ps is not None]
	return results

def print_exercise_sentences(results = None):
	if results is None:
		results = perform_exercise_sentences()
	
	for r in results:
		s = r[2]
		print("\n\n----------------\n\n", r[0], " -> ", r[1], " ===>\n\t- synset found: ", r[2])
		if s is not None:
			sname = s.name()
			print("\t- name: ", sname, "\n\t- definition: ", s.definition(), "\n\t- ID", sname[len(sname)-2:], "\n\t- rebuilding sentence: ", r[3]+sysnsetLemmasToString(s)+r[4] ) # , "\n\t- "


# ESERCIZIO 2

def load_sentences_SemCor():
	return semcor.tagged_sents(tag="both")[:WORDS_FROM_SEM_CORPUS]

def extract_nouns_from_taggedSentence(taggedSentence):
	#return [ w for w in taggedSentence if w[1] is not None and w[1][:2] == 'NN' ]
	return [ w for w in taggedSentence if w[1] is not None and (w[1] == 'NN' or w[1] is 'NN') ]

def extract_taggedSentence_from_sentenceTree(tagged_sent, include_punctuation=True):
	extracted = []
	for word_maybe_composed in tagged_sent:
		#usually, there's a single word, but in some case there are more: "City of Atlanta"
		for word_composing in word_maybe_composed.pos():
			if include_punctuation or word_composing[1] is not None:
				extracted.append(word_composing)
	return extracted



def perform_exercise_SemCor(sentences_tree = None):
	if sentences_tree is None:
		sentences_tree = load_sentences_SemCor()
	if sentences_tree is None:
		return None
	matches = 0
	total_matches = 0
	iterator_nouns  = None
	#caching of functions
	nameextractor = extract_synset_name
	for s_t in sentences_tree:
		iterator_nouns = POSFilteredWordsProvider(s_t, 'NN') # SentenceTreePOSFilter(s_t, 'NN')
		tagged_sentence = extract_taggedSentence_from_sentenceTree(s_t, include_punctuation=False)
		nouns = extract_nouns_from_taggedSentence(tagged_sentence)
		sent = [ x[0] for x in tagged_sentence ] #only the word, remove the tags
		sentence_as_string = " ".join(sent)
		for n in nouns:
			that_noun = n[0]
			disambiguated_word_by_me = simplifiedLesk(that_noun, sentence_as_string, context=tagged_sentence) # tagged_sentence == context

			None_count = 0
			if disambiguated_word_by_me is not None:
				total_matches += 1
				mineDisambiguationToTest = nameextractor(disambiguated_word_by_me)
				if iterator_nouns.contains(mineDisambiguationToTest) :
					matches += 1
	return (matches, total_matches)

def calculate_accuracy(result):
	return ((100*result[0])/result[1])

def print_exercise_SemCor(sentences = None, ex_results = None):
	if ex_results is None:
		ex_results = perform_exercise_SemCor(sentences)
	print("my matches: ", ex_results[0], ", total_matches: ", ex_results[1], ", accuracy: ", calculate_accuracy(ex_results), "%")

#

#

#

print('start')
'''
sentence = "Two electric guitar rocks players, and also a better bass player, are standing off to two sides reading corpora while walking"
word = "bass"
sense = simplifiedLesk(word, sentence)
print(sentence, "\n", word, "\n\nsense: ", sense)
'''
print("\n\nnow perform_exercise_sentences\n\n")
print_exercise_sentences()

print("\n\n\nnow perform_exercise_SemCor\n\n")
print_exercise_SemCor()


print("END")