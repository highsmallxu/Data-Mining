import math
import numpy as np
import csv
from math import log
from hyperloglog import HyperLogLog
import operator
import random

"""
read csv
"""
stream = []
with open("small.csv","rb") as f:
    data = csv.reader(f,delimiter=' ')
    for row in data:
        y = []
        for x in row:
            y.append(int(x))
        stream.append(y)

def findnode():
    node_list = []
    for pair in stream:
        for node in pair:
            if node not in node_list:
                node_list.append(node)
    return node_list

def findneighbour(nodes):
    neighbour = {}
    for node in nodes:
        n = []
        for pair in stream:
            if node in pair:
                tmp = pair[1]
                if tmp!=node:
                    n.append(tmp)
        neighbour[node] = n
    return neighbour


def createcounter(nodes):
    counter = {}
    for node in nodes:
        h = HyperLogLog()
        h.update(str(node))
        counter[node] = h.reg
    return counter

"""
main function
"""
def count(h):
    alpha = .7213 / (1 + 1.079 / h.m)
    e = alpha * float(h.m ** 2) / np.sum(2.0**(-h.reg))
    if e < (5 / 2.0 * h.m):
        V = len([b for b in h.reg if b == 0])
        if V:
            e = h.m * log(h.m / float(V))
    elif e > (1 / 30.0) * 2 ** 32:
        e = -(2 ** 32) * log(1 - (e / 2 ** 32))
    return e


def hyperloglog():
    h = HyperLogLog()
    data = []
    for pair in stream:
        for node in pair:
            data.append(node)
    for item in data:
        h.update(str(item).encode('utf8'))
    print "the number of node"
    num = count(h)
    print num






def hyperball():
    node_list = findnode()
    neighbour = findneighbour(node_list)
    counter = createcounter(node_list)

    t = 0
    d = {}
    for node in node_list:
        d[node]=0

    while(True):
        t+=1
        flag = 0
        new = {}
        for node in node_list:
            a = counter[node]
            nb = neighbour[node]
            for n in nb:
                w = counter[n]
                a = np.maximum(w, a)
            new[node] = a
            dif = new[node] - counter[node]
            difmap = map(lambda x:x**2,dif)
            difsum = math.sqrt(sum(difmap))

            if(t>0):
                current_d = (1/t)*difsum
                d[node] = d[node] + current_d

        # check if counter changes its value
        for node in node_list:
            if list(new[node])!=list(counter[node]):
                flag+=1

        counter = new
        # t+=1

        if flag == 0:
            break

    sorted_x = sorted(d.items(), key=operator.itemgetter(1),reverse=True)

    print "nodes are orderd by closeness centrality:"
    print sorted_x


if __name__ == "__main__":
    hyperloglog()
    hyperball()
