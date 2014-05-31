#!/usr/bin/python

import os
import cgi
import cgitb; cgitb.enable()
import urllib

if __name__ == '__main__':
    print "Content-Type: application/json\r\n"
    url = 'http://en.wikipedia.org/w/api.php?' + os.getenv('QUERY_STRING', '')
#    url = 'http://en.wikipedia.org/w/api.php?action=query&format=json&prop=revisions&titles=diablo&rvlimit=100&rvprop=timestamp|user|comment'
    data = urllib.urlopen(url).read()
    print data


