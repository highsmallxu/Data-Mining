"""
Hyperloglog: allows one to count the number of distinct elements in a stream
"""
import hashlib

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

def size(M):
    pass

def hyperloglog(stream,bits_for_bucket_index):
    bucket_count = 2 ** bits_for_bucket_index
    buckets = [0] * bucket_count
    for item in stream:
        x = hash(item)
        i = x & (bucket_count - 1)
        w = x >> bits_for_bucket_index
        buckets[i] = max(buckets[i], rightmost_binary_1_position(w))


stream = [1, 2, 3, 4, 5, 5, 5, 3]
hyperloglog(stream,10)
