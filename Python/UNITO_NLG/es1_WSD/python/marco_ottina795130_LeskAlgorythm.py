import nltk
from nltk.wsd import lesk
from nltk.stem import WordNetLemmatizer 
from nltk.corpus import semcor
from nltk.corpus import stopwords 
from nltk.corpus import wordnet as wn
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
	return (LEMMATIZER.lemmatize(tupla[0], tag), tag) if tag is not None else (tupla[0], None)

def sentence_to_tagged_words(sentence):
	# check iterable for "stringness" of all items.
	if all(isinstance(item, str) for item in sentence):
		sentence = " ".join(sentence)
	if not isinstance(sentence, str):
	    raise TypeError
	original_words = word_tokenize(sentence)
	tagged_words = nltk.pos_tag(original_words) #returns a list of tuples: (word, tagString) like ('And', 'CC')
	original_words = None
	return tagged_words

def sentence_to_lemmatized_words(sentence):
	tagged_words = sentence_to_tagged_words(sentence)
	lemmatized_words = [ lemmatize_tupla_word_postag(ow)[0] for ow in tagged_words ]
	tagged_words = None
	return lemmatized_words

def bag_of_words(sentence, stop_words=None):
	if stop_words is None:
		stop_words = STOP_WORDS_ENG
	lemmatized_words = sentence_to_lemmatized_words(sentence)
	cleaned_words = [ w for w in lemmatized_words if (w not in PUNCTUATION) and (w not in stop_words) ]
	lemmatized_words = None
	return cleaned_words

def sentence_to_lemma_tag_tuples(sentence): #useful for exercise 2
	tagged_words = sentence_to_tagged_words(sentence)
	tuples = [ lemmatize_tupla_word_postag(ow) for ow in tagged_words ]
	tagged_words = None
	return tuples

def bag_of_tagged_words(sentence, stop_words=None):
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

def context_from_sentence(word, sentence):
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
	return len(signature & set(context))

def simplifiedLesk( word, sentence, context = None):
	best_sense = None
	max_overlap = 0
	if context is None:
		context = context_from_sentence(word, sentence)
	for sense in wn.synsets(word):
		signature = synset_to_signature(sense)
		overlap = computeOverlap(signature, context)
		if overlap > max_overlap:
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

def print_exercise_sentences(ex_results = None):
	if results is None:
		print("result is None")
	else:
		for r in results:
			s = r[2]
			print("\n\n----------------\n\n", r[0], " -> ", r[1], " ===>\n\t- synset found: ", r[2])
			if s is not None:
				sname = s.name()
				print("\t- name: ", sname, "\n\t- definition: ", s.definition(), "\n\t- ID", sname[len(sname)-2:], "\n\t- rebuilding sentence: ", r[3]+sysnsetLemmasToString(s)+r[4] ) # , "\n\t- "


# ESERCIZIO 2

def load_sentences_SemCor():
	return semcor.sents()[:50]

def perform_exercise_SemCor(sentences = None):
	if sentences is None:
		sentences = load_sentences_SemCor()
	if sentences is None:
		return None
	matches = 0
	total_matches = 0
	for s in sentences:
		sentence_lemmaTokenized = bag_of_tagged_words(s)
		context = [w[0] for w in sentence_lemmaTokenized]
		#for each noun
		for w in sentence_lemmaTokenized:
			if w[1] is wn.NOUN:
				#disambiguate
				total_matches += 1
				disambiguated_word_by_me = simplifiedLesk(w[0], s, context)
				disambiguated_word_by_API = nltk.wsd.lesk(context, w[0]) #returns a synset, 'n'
				#if disambiguated_word_by_API is not None:
				#	print(disambiguated_word_by_API.name())
				none_count = 0
				if disambiguated_word_by_me is None:
					none_count += 1
				if disambiguated_word_by_API is None:
					none_count += 1
				if (none_count == 2) or ((none_count == 0) and ((disambiguated_word_by_me.name() == disambiguated_word_by_API.name()) or (disambiguated_word_by_me.name() is disambiguated_word_by_API.name()))) :
					matches += 1
	return (matches, total_matches)

def print_exercise_SemCor(sentences = None, ex_results = None):
	if ex_results is None:
		ex_results = perform_exercise_SemCor(sentences)
	print("\n\n my matches: ", ex_results[0], ", total_matches: ", ex_results[1], ", accuracy: ", ((100*ex_results[0])/ex_results[1]), "%")

#

print('start')

sentence = "Two electric guitar rocks players, and also a better bass player, are standing off to two sides reading corpora while walking"
word = "bass"
sense = simplifiedLesk(word, sentence)
#print(sentence, "\n", word, "\n\nsense: ", sense)
print_synset(sense)


allLines = load_sentences()
for s in allLines:
	print(s)

print("\n\nnow perform_exercise_sentences\n\n")
#results = perform_exercise_sentences(allLines)
#print_exercise_sentences(results)

print("\n\nnow perform_exercise_SemCor\n\n")
print_exercise_SemCor()

print("END")