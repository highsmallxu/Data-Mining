"""
Hyperloglog: allows one to count the number of distinct elements in a stream
"""
import hashlib
import numpy as np
from math import log

"""
global variables
"""
# global bits_for_bucket_index
# global bucket_count
# global buckets
# global counter
stream = [1,2,2,3,4,6,7,8,1,8]
bits_for_bucket_index = 10
bucket_count = 2 ** bits_for_bucket_index
buckets = [0] * bucket_count

def hash(x):
    x = str(x)
    h = hashlib.sha1(x)
    hex_ = h.hexdigest()
    return int(hex_, base=16)

def rightmost_binary_1_position(num):
  i = 0
  while (num >> i) & 1 == 0:
      i += 1
  return i + 1

def size(buckets,bucket_count):
    a_m = .7213 / (1 + 1.079 / bucket_count)
    E = a_m * bucket_count ** 2 * sum(2 ** (-Mj) for Mj in buckets) ** (-1)
    if E < (5 / 2.0 * bucket_count):
        V = len([b for b in buckets if b == 0])
        if V:
            E = bucket_count * log(bucket_count / float(V))
    elif E > (1 / 30.0) * 2 ** 32:
        E = -(2 ** 32) * log(1 - (E / 2 ** 32))
    return E

def add(buckets,item):
    x = hash(item)
    i = x & (bucket_count - 1)
    w = x >> bits_for_bucket_index
    buckets[i] = max(buckets[i], rightmost_binary_1_position(w))

def hyperloglog(stream):
    for item in stream:
        add(buckets,item)
    E = size(buckets,bucket_count)
    return E

def union(M,N):
    out = np.maximum(M,N)
    return out

def hyperball(stream):

    # the number of nodes in the stream
    E = hyperloglog(stream)
    num = round(E)

    # create counter
    counter = num * [buckets]

    # initial counter
    for :
        add(counter[id],item)

    # update counter
    t=0
    for





    # for id,item in enumerate(stream):
    #     a = counter[id]
    #     for id,item in enumerate(stream):
    #         a = union(counter[id],a)




hyperball(stream)
# hyperloglog(stream)

x1=[1,2,3]
x2=[3,2,1]
a = np.maximum(x1,x2)
print a