__author__ = 'wuyanzhe'

import pickle
import dns

with open('record.txt', 'rb') as f:
    obj = pickle.load(f)
    print(obj)