import struct, copy
from hashlib import sha1
import numpy as np
import hashlib

_bit_length = lambda bits : bits.bit_length()
if not hasattr(int, 'bit_length'):
    _bit_length = lambda bits : len(bin(bits)) - 2 if bits > 0 else 0


class HyperLogLog(object):

    __slots__ = ('p', 'm', 'reg', 'alpha', 'max_rank', 'hashobj')

    _hash_range_bit = 32
    _hash_range_byte = 4
    _struct_fmt_str = '<I'

    def __init__(self, p=8, reg=None, hashobj=sha1):
        if reg is None:
            self.p = p
            self.m = 1 << p
            self.reg = np.zeros((self.m,), dtype=np.int8)
        else:
            if not isinstance(reg, np.ndarray):
                raise ValueError("The imported register must be a numpy.ndarray.")
            self.m = reg.size
            self.p = _bit_length(self.m) - 1
            if 1 << self.p != self.m:
                raise ValueError("The imported register has \
                    incorrect size. Expect a power of 2.")
            self.reg = reg
        self.hashobj = hashobj
        self.max_rank = self._hash_range_bit - self.p

    def right(self, bits):
        rank = self.max_rank - _bit_length(bits) + 1
        if rank <= 0:
            raise ValueError("Hash value overflow, maximum size is %d\
                    bits" % self.max_rank)
        return rank

    def right2(self,num):
      i = 0
      while (num >> i) & 1 == 0:
          i += 1
      return i + 1

    def update(self, b):
        hv = struct.unpack(self._struct_fmt_str,
                self.hashobj(b).digest()[:self._hash_range_byte])[0]
        reg_index = hv & (self.m - 1)
        bits = hv >> self.p
        self.reg[reg_index] = max(self.reg[reg_index], self.right(bits))
        return self.reg

    def hash(x):
        x = str(x)
        h = hashlib.sha1(x)
        hex_ = h.hexdigest()
        return int(hex_, base=16)

    def update2(self,b):
         hash_integer = hash(b)
         i = hash_integer & (self.m - 1)
         w = hash_integer >> self.p
         self.reg[i] = max(self.reg[i], self.right2(w))
         return self.reg


