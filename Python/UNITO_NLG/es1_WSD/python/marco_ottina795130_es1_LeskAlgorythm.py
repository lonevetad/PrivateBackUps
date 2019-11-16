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
WORDS_FROM_SEM_CORPUS = 50 # dovrebbero essere 50

def name_pure_from_synset(synset):
	if synset is None:
		return None
	name = synset.name()
	index = name.find('.')
	return name[:index] if index >= 0 else name


def extract_wnpostag_from_postag(tag):
	#version 2:
	for key in TAG_DICT:
		if key in tag.upper():
			return TAG_DICT[key]
	return None
	#version 1:
	#take the first letter of the tag
	#return TAG_DICT.get(tag[0].upper(), None)

def lemmatize_tupla_word_postag(tupla):
	"""
	giving a tupla of the form (wordString, posTagString) like ('guitar', 'NN'), return the lemmatized word
	"""
	tag = extract_wnpostag_from_postag(tupla[1])	
	#tag = tupla[1]	
	#return (LEMMATIZER.lemmatize(tupla[0], tag), tag) if tag is not None else (tupla[0], None)
	return (LEMMATIZER.lemmatize(tupla[0], tag), tupla[1]) if tag is not None else (tupla[0], None)

def sentence_to_tagged_words(sentence):
	# check iterable for "stringness" of all items.
	'''
	if all(isinstance(item, str) for item in sentence):
		sentence = " ".join(sentence)
	'''
	if not isinstance(sentence, str):
	    raise TypeError
	original_words = word_tokenize(sentence)
	tagged_words = nltk.pos_tag(original_words) #returns a list of tuples: (word, tagString) like ('And', 'CC')
	original_words = None
	return tagged_words

def sentence_to_lemmatized_words(sentence):
	tagged_words = sentence_to_tagged_words(sentence)
	#lemmatized_words = [ lemmatize_tupla_word_postag(ow)[0] for ow in tagged_words ]
	lemmatized_words = [ lemmatize_tupla_word_postag(ow) for ow in tagged_words ]
	tagged_words = None
	return lemmatized_words

def bag_of_tagged_words(sentence, stop_words=None):
	if stop_words is None:
		stop_words = STOP_WORDS_ENG
	lemmatized_words = sentence_to_lemmatized_words(sentence)
	#print(".-.-.-.-.\n\tsentence: ", sentence, "\n\t\tlemmatized_words: ", lemmatized_words)
	cleaned_words = [ w for w in lemmatized_words if (w[0] not in PUNCTUATION) and (w[0] not in stop_words) ]
	lemmatized_words = None
	return cleaned_words

'''
def sentence_to_lemma_tag_tuples(sentence): #useful for exercise 2
	tagged_words = sentence_to_tagged_words(sentence)
	tuples = [ lemmatize_tupla_word_postag(ow) for ow in tagged_words ]
	tagged_words = None
	return tuples

def bag_of_words(sentence, stop_words=None):
	if stop_words is None:
		stop_words = STOP_WORDS_ENG
	lemmatized_words = sentence_to_lemma_tag_tuples(sentence)
	cleaned_words = [ w for w in lemmatized_words if (w[0] not in PUNCTUATION) and (w[0] not in stop_words) ]
	lemmatized_words = None
	return cleaned_words

def bag_of_words_TooConfusing(selfe, word, sentence):
		# prima di tutto, si ottengono le parole che compaiono frequentemente in vari contesti:
		# ossia, si crea un set di coppie <parola;repetCount> che conta le ripetizioni
		# per ogni contesto, estrarre le parole non-stopped, lemmatizzarla, se esiste incrementare il count, altrimenti inserirla
		# filtrare poi le coppie con conteggio > 1
		return None
'''
def context_from_sentence(word, sentence):
	#simply, use a bag of words, using lemmatizing and removing stop words
	ret = bag_of_tagged_words(sentence)
	#2) fai  la lemmatizzazione ( "fiammella bruciò legnetto cenere" -> "fiamma bruciare legno cenere" ) (== portare in forma base le nomi, verbi, aggettivi)
	return ret

def synset_to_signature(sense):
	#create a list bag of words derived from all definitions and examples
	bag = set()
	#populate with it's own name through all of its lemma(s), then its hyponyms and hypernyms
	# since both hyponyms and hypernyms produces ana list of synsets, let's generalize it
	for some_synsets in [ [sense], sense.hyponyms(), sense.hypernyms() ]:
		for inner_synset in some_synsets:
			for lemma in inner_synset.lemmas():
				bag.add(lemma.name())
	#then add 
	b = bag_of_tagged_words(sense.definition())
	for w in b:
		bag.add(w[0])
	
	for example in sense.examples():
		#for element in source:
		b = bag_of_tagged_words(example)
		for w in b:
			bag.add(w[0])
	##print("\t", bag,"\n\n\n")
	return bag

def computeOverlap(signature, context):
	"""
	given a signature (a set of lemmatized words from all symsets' examples and definitions	of
	a word to be disambiguated) and a context (a list of lemmatized words from a sentence holding the word
	to be disambiguated), compute the overlapping: just count the intersection of lemmatized words
	"""
	sign = signature # set([ w[0] for w in signature])
	contx = set([ w[0] for w in context])
	return len(sign & contx) + 1

def simplifiedLesk( word, sentence, context = None):
	best_sense = None
	max_overlap = 0
	if context is None:
		context = context_from_sentence(word, sentence)
	for sense in wn.synsets(word):
		signature = synset_to_signature(sense)
		overlap = computeOverlap(signature, context)
		#if overlap > max_overlap:
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
	return semcor.tagged_sents()[:WORDS_FROM_SEM_CORPUS]


def extract_nouns_from_sentence(sentence):
	return [ w for w in extract_taggedSentence_from_sentenceTree(sentence) if w[1] is not None and w[1][0] is 'N' ]

def extract_nouns_from_taggedSentence(taggedSentence):
	#return [ w for w in taggedSentence if w[1] is not None and w[1][:2] == 'NN' ]
	return [ w for w in taggedSentence if w[1] is not None and w[1] == 'NN' ]

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

	lesk_fn = nltk.wsd.lesk
	nameextractor = name_pure_from_synset
	for s_t in sentences_tree:
		tagged_sentence = extract_taggedSentence_from_sentenceTree(s_t)
		nouns = extract_nouns_from_taggedSentence(tagged_sentence)
		sent = [ x[0] for x in tagged_sentence ] #only the word, remove the tags
		sentence_as_string = " ".join(sent)
		for n in nouns:
		#n = nouns[0]
		#if n is not None:
			#disambiguate
			disambiguated_word_by_me = simplifiedLesk(n[0], sentence_as_string	, tagged_sentence) # tagged_sentence == context
			#disambiguated_word_by_API = lesk_fn(tagged_sentence, n[0]) #returns a synset, 'n'
			disambiguated_word_by_API = lesk_fn(sent, n[0]) #returns a synset, 'n'
			
			none_count = 0
			if disambiguated_word_by_me is not None:
				total_matches += 1
			else:
				none_count = 1
				#if disambiguated_word_by_me is None:
				#	none_count += 1
			if disambiguated_word_by_API is None:
				none_count += 1

			mineDisambiguationToTest = nameextractor(disambiguated_word_by_me)
			apiDisambiguationToTest = nameextractor(disambiguated_word_by_API)
			if (none_count == 2) or ((none_count == 0) and ((mineDisambiguationToTest == apiDisambiguationToTest) or (mineDisambiguationToTest is apiDisambiguationToTest))) :
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