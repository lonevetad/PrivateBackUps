import nltk
from nltk.wsd import lesk
from nltk.stem import WordNetLemmatizer 
from nltk.corpus import wordnet as wn
from nltk.corpus import stopwords 
from nltk.tokenize import word_tokenize


PUNCTUATION = u",.?!()-_\"\'\\\n\r\t;:+*<>@#§^$%&|/"
STOP_WORDS_ENG = set(stopwords.words('english'))
LEMMATIZER = WordNetLemmatizer() 
TAG_DICT = {"J": wn.ADJ,
			"N": wn.NOUN,
			"V": wn.VERB,
			"R": wn.ADV}


def extract_wnpostag_from_postag(tag):
	#take the first letter of the tag
	#the second parameter is an "optional" in case of missing key in the dictionary 
	return TAG_DICT.get(tag[0].upper(), None)

def lemmatize_tupla_word_postag(tupla):
	"""
	giving a tupla of the form (wordString, posTagString) like ('guitar', 'NN'), return the lemmatized word
	"""
	tag = extract_wnpostag_from_postag(tupla[1])	
	return LEMMATIZER.lemmatize(tupla[0], tag) if tag is not None else tupla[0]

def bag_of_words(sentence, stop_words=None):
	if stop_words is None:
		stop_words = STOP_WORDS_ENG
	original_words = word_tokenize(sentence)
	tagged_words = nltk.pos_tag(original_words) #returns a list of tuples: (word, tagString) like ('And', 'CC')
	original_words = None
	lemmatized_words = [ lemmatize_tupla_word_postag(ow) for ow in tagged_words ]
	tagged_words = None
	cleaned_words = [ w for w in lemmatized_words if (w not in PUNCTUATION) and (w not in stop_words) ]
	lemmatized_words = None
	return cleaned_words

def bag_of_words_TooConfusing(selfe, word, sentence):
		# prima di tutto, si ottengono le parole che compaiono frequentemente in vari contesti:
		# ossia, si crea un set di coppie <parola;repetCount> che conta le ripetizioni
		# per ogni contesto, estrarre le parole non-stopped, lemmatizzarla, se esiste incrementare il count, altrimenti inserirla
		# filtrare poi le coppie con conteggio > 1
		return None

def context_from_sentence( word, sentence):
	#simply, use a bag of words, using lemmatizing and removing stop words
	ret = bag_of_words(sentence)
	#2) fai  la lemmatizzazione ( "fiammella bruciò legnetto cenere" -> "fiamma bruciare legno cenere" ) (== portare in forma base le nomi, verbi, aggettivi)
	return ret

def synset_to_signature(sense):
	#create a list bag of words derived from all definitions and examples
	bag = set()
	sources = [sense.definition(), sense.examples()]
	for source in sources:
		for element in source:
			b = bag_of_words(element)
			for w in b:
				bag.add(w)
	return bag

def computeOverlap(signature, context):
	"""
	given a signature (a set of lemmatized words from all symsets' examples and definitions	of
	a word to be disambiguated) and a context (a list of lemmatized words from a sentence holding the word
	to be disambiguated), compute the overlapping: just count the intersection of lemmatized words
	"""
	context_set = set(context)
	intersection = signature & context_set
	return len(intersection)

def simplifiedLesk( word, sentence):
	best_sense = None
	max_overlap = 0
	context = context_from_sentence(word, sentence)
	for sense in wn.synsets(word):
		signature = synset_to_signature(sense)
		overlap = computeOverlap(signature, context)
		if overlap > max_overlap:
			max_overlap = overlap
			best_sense = sense
	return best_sense

def print_synset(synset):
	if synset is not None:
		print(synset.name())
		print(synset.definition())
		print(synset.examples())

def loadSentences(fileName=None):
	if fileName is None:
		fileName = './../sentences.txt';
	with open(tabFileName, 'r') as fp:
		read_lines = [line.rstrip('\n')[2:] for line in fp.readlines() if line.startswith('- ', beg=0, end=4)]
		return read_lines
	return None



#


print('start')

sentence = "Two electric guitar rocks players, and also a better bass player, are standing off to two sides reading corpora while walking"
word = "bass"
sense = simplifiedLesk(word, sentence)

print(sentence, "\n", word, "\n\nsense: ", sense)
print_synset(sense)

print("END")