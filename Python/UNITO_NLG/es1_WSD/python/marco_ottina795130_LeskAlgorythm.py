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
WORDS_FROM_SEM_CORPUS = 50 # dovrebbero essere 50


def extract_wnpostag_from_postag(tag):
	#take the first letter of the tag
	#the second parameter is an "optional" in case of missing key in the dictionary 
	return TAG_DICT.get(tag[0].upper(), None)

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
	#print("\t\t\t\t sense's ", sense, " definition : ;;;; ", sense.definition())
	b = bag_of_tagged_words(sense.definition())
	for w in b:
		bag.add(w)
	#print("\t\t\t\t has ", len(sense.examples()), " examples")
	for example in sense.examples():
		#for element in source:
		b = bag_of_tagged_words(example)
		#print("\t\t\t\t...adding: ", b)
		for w in b:
			bag.add(w)
	return bag

def computeOverlap(signature, context):
	"""
	given a signature (a set of lemmatized words from all symsets' examples and definitions	of
	a word to be disambiguated) and a context (a list of lemmatized words from a sentence holding the word
	to be disambiguated), compute the overlapping: just count the intersection of lemmatized words
	"""
	#print("...........overlap::\n\t\t...signature: ", signature, "\n\t\t...set(context): ", set(context), "\n\t\t... res: ", signature & set(context))
	#return len(signature & set(context))
	sign = set([ w[0] for w in signature])
	contx = set([ w[0] for w in context])
	return len(sign & contx)

def simplifiedLesk( word, sentence, context = None):
	best_sense = None
	max_overlap = 0
	if context is None:
		context = context_from_sentence(word, sentence)
	#print("\t####### context:, ", context)
	for sense in wn.synsets(word):
		#print("\tLesk-ing of word: ", word, " and sense: ", sense)
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
	for s_t in sentences_tree:
		tagged_sentence = extract_taggedSentence_from_sentenceTree(s_t)
		nouns = extract_nouns_from_taggedSentence(tagged_sentence)
		sent = [ x[0] for x in tagged_sentence ] #only the word, remove the tags
		sentence_as_string = " ".join(sent)
		'''
		print("for sentence\n\t---", sent)
		print("\t---sentence_as_string: ", sentence_as_string)
		print("\t---sent tagged_sentence: ", len(tagged_sentence), ", tagged_sentence: ", tagged_sentence)
		print("\t---sent len: ", len(sent), ", sent: ", sent)
		print("\t---nouns len: ", len(nouns), ", nouns: ", nouns)
		'''
		#for each noun
		for n in nouns:
		#n = nouns[0]
		#if n is not None:
			#disambiguate
			#disambiguated_word_by_me = simplifiedLesk(n[0], sent)
			#disambiguated_word_by_API = nltk.wsd.lesk(context_from_sentence(n[0], sent), n[0]) #returns a synset, 'n'
			disambiguated_word_by_me = simplifiedLesk(n[0], sentence_as_string	, tagged_sentence) # tagged_sentence == context
			disambiguated_word_by_API = nltk.wsd.lesk(tagged_sentence, n[0]) #returns a synset, 'n'
			#if disambiguated_word_by_API is not None:
			#print("-disambiguated_word_by_API: ", disambiguated_word_by_API)

			if disambiguated_word_by_me is not None:
				total_matches += 1
				none_count = 0
				#if disambiguated_word_by_me is None:
				#	none_count += 1
				if disambiguated_word_by_API is None:
					none_count += 1
				if (none_count == 2) or ((none_count == 0) and ((disambiguated_word_by_me.name() == disambiguated_word_by_API.name()) or (disambiguated_word_by_me.name() is disambiguated_word_by_API.name()))) :
					matches += 1
				'''
				else:
					print("wrongly disambiguated_word_by_me: ", disambiguated_word_by_me)
					print("wrongly disambiguated_word_by_API: ", disambiguated_word_by_API)
				if (none_count == 2):
					matches += 1
				if (none_count == 0):
					if((disambiguated_word_by_me.name() == disambiguated_word_by_API.name()) or (disambiguated_word_by_me.name() is disambiguated_word_by_API.name())):
						matches += 1
					else:
						print("missclassifying .. mine: ", disambiguated_word_by_me, ", api: ", disambiguated_word_by_API)
				'''
	return (matches, total_matches)

def calculate_accuracy(result):
	return ((100*result[0])/result[1])

def print_exercise_SemCor(sentences = None, ex_results = None):
	if ex_results is None:
		ex_results = perform_exercise_SemCor(sentences)
	print("\n\n my matches: ", ex_results[0], ", total_matches: ", ex_results[1], ", accuracy: ", calculate_accuracy(ex_results), "%")

#

print('start')
'''
entence = "Two electric guitar rocks players, and also a better bass player, are standing off to two sides reading corpora while walking"
word = "bass"
sense = simplifiedLesk(word, sentence)
#print(sentence, "\n", word, "\n\nsense: ", sense)
print_synset(sense)


allLines = load_sentences()
for s in allLines:
	print(s)
results = perform_exercise_sentences(allLines)
print_exercise_sentences(results)
'''
print("\n\nnow perform_exercise_sentences\n\n")
print_exercise_sentences()

print("\n\nnow perform_exercise_SemCor\n\n")
print_exercise_SemCor()

'''
semcor_tagged_sents = semcor.tagged_sents(tag='both')[:10]

for sent in semcor_tagged_sents:
	print("\n\n----", sent)
	for subtree1 in sent:
		print("\t---", subtree1)
		print("\tpos: ", subtree1.pos())
	print("\t_extracted: ",extract_taggedSentence_from_sentenceTree(sent))
	print("\t@@only nouns: ",extract_nouns(sent))
#print( semcor_tagged_sents )
'''


print("END")