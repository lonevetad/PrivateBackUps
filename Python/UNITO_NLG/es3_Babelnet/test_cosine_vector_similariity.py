import math
import time

def dot_product2(v1, v2):
	return sum(map(lambda x, y: x*y , v1, v2))

#
def cosine_similarity_1(v1, v2):
	prod = dot_product2(v1, v2)
	len1 = math.sqrt(dot_product2(v1, v1))
	len2 = math.sqrt(dot_product2(v2, v2))
	return prod / (len1 * len2)

def cosine_similarity_2(a, b):
	return sum([i*j for i,j in zip(a, b)])/(math.sqrt(sum([i*i for i in a]))* math.sqrt(sum([i*i for i in b])))

#
def optimized_cosine_similarity_2(a, b):
	l = lambda x, y: x*y
	return sum([i*j for i,j in zip(a, b)])/(math.sqrt(sum(map(l, a, a)))* math.sqrt(sum(map(l , b, b))))

#
def cosine_similarity_3_between_dictionaries(text1, text2):
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
def cosine_similarity_3(text1, text2):
	vec1 = text1
	vec2 = text2
	intersection = min(len(vec1), len(vec2))
	r = range(intersection)
	numerator = sum([vec1[x] * vec2[x] for x in r])
	sum1 = sum([x**2 for x in vec1])
	sum2 = sum([x**2 for x in vec2])
	denominator = math.sqrt(sum1) * math.sqrt(sum2)
	if not denominator:
		 return 0.0
	else:
		#return round(float(numerator) / denominator, 3)
		return float(numerator) / denominator

#
def mine_cosine_similarity_safe(v1, v2):
	l1 = len(v1)
	l2 = len(v2)
	if l1 is l2 or l1 == l2:
		r = range(l1)
		numerator = 0
		for i in r:
			numerator += v1[i] * v2[i]
		s1 = 0
		for v in v1:
			s1 += v*v
		s2 = 0
		for v in v2:
			s2 += v*v
		denominator = math.sqrt(s1) * math.sqrt(s2)
		if not denominator:
			 return 0.0
		else:
			#return round(float(numerator) / denominator, 3)
			return float(numerator) / denominator
	return -10000000

def mine_cosine_similarity(v1, v2):
	l1 = len(v1)
	r = range(l1)
	numerator = 0
	for i in r:
		numerator += v1[i] * v2[i]
	s1 = 0
	for v in v1:
		s1 += v*v
	s2 = 0
	for v in v2:
		s2 += v*v
	denominator = math.sqrt(s1) * math.sqrt(s2)
	if not denominator:
		 return 0.0
	else:
		#return round(float(numerator) / denominator, 3)
		return float(numerator) / denominator


def get_arrays(k):
	return [ [ x for x in range(k) ],	[ k-x for x in range(k) ] ]

k = 1000
tests = [
	[
		[1,2,3],
		[1,1,4]
	], [
		[2, 0, 1, 1, 0, 2, 1, 1],
		[2, 1, 1, 0, 1, 1, 1, 1]
	], [
		[ 2, 3, -1, 5, 8, 13, 0, 0, 4],
		[ 2, 3, -1, 5, 8, 13, 0, 0, 4]
	], 
		get_arrays(100)
	,	get_arrays(k)
	,	get_arrays(k*10)
	,	get_arrays(k*k)
	#,	get_arrays(k*k*10)
]
coss = { '1': cosine_similarity_1, '2': cosine_similarity_2, '3': cosine_similarity_3, 'mine': mine_cosine_similarity, 'opt': optimized_cosine_similarity_2}
#print(tests)
i = 0
for test in tests:
	a = test[0]
	b = test[1]
	#print("\nhaving:\n\ta: ", a, "\n\tb: ", b)
	print("\n\n-------test number",i,", compute: ")
	for fn in coss:
		f = coss[fn]
		print("--------function name: ", fn)

		print("\t: ", f(a,b))
		i +=1

repetitions = 10000
r = range(repetitions)
print("\n\n\n\n", "benchmarking", repetitions, "times")

for fn in coss:
	f = coss[fn]
	print("\n\n--------\nfunction name: ", fn)
	for test in tests:
		a = test[0]
		b = test[1]
		start_time = time.time()
		for i in r:
			f(a,b)
		print("--- %s seconds ---" % (time.time() - start_time))

