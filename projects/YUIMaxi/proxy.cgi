#!/usr/bin/python
from curses.ascii import isprint
import os
import cgi
import cgitb; cgitb.enable()
import urllib
import json
import re

# user libraries for parsing wiki articles
import Wikipedia
import Wiki2Plain

# time libraries
import datetime

def printable(input):
	return ''.join([char for char in input if isprint(char)])

##### function to get the wiki data ########
def get_wiki_article(article_name):
	lang = 'en'
	wiki = Wikipedia.Wikipedia(lang)
	
	# strip white space and covert them to underscores for wikipedia
	wiki_article = article_name
	wiki_article = re.sub('%20+', '_', wiki_article)

	try:
		raw = wiki.article(wiki_article)
	except:
		raw = None

	if raw:
		wiki2plain = Wiki2Plain.Wiki2Plain(raw)
		content = wiki2plain.text
		
		# strip out the references section
		wikidata = content[ : content.find('==References==') ]
		wikidata = content[ : content.find('==See also==') ]
		
		# strip out the sections which are tables
		pattern = re.compile(r'\|\-.*?\|\}', re.DOTALL) 
		wikidata = re.sub(pattern, '', wikidata)
		
		pattern = re.compile(r'\{\{.*?\}\}', re.DOTALL) 
		wikidata = re.sub(pattern, '', wikidata)
		
		# add HTML mark ups for headings
		wikidata = re.sub(r'(?P<name>====.*====)', '<span class="title3">\g<name></span>', wikidata)
		wikidata = wikidata.replace('====', '')
		wikidata = re.sub(r'(?P<name>===.*===)', '<span class="title2">\g<name></span>', wikidata)
		wikidata = wikidata.replace('===', '')
		wikidata = re.sub(r'(?P<name>==.*==)', '<span class="title1">\g<name></span>', wikidata)
		wikidata = wikidata.replace('==', '')
		# convert '\n' characters to <br> HTML tags
		wikidata = wikidata.replace('\n', '<br/>')
	return wikidata
		

### Function to get the Twitter data #########
def get_twitter_article(article_name):
	twitter_article = re.sub('%20+', '+', article_name)
	
	rpp = 100
	twitterdata = []
	for page in range(1,3):
		twitterurl = 'http://search.twitter.com/search.json?q=' + twitter_article + '&rpp=' + str(rpp) + '&page=' + str(page)
		twitter_data = json.loads( urllib.urlopen(twitterurl).read() )
	
		for result in twitter_data['results']:
			timestamp = result['created_at'][: len(result['created_at'])-6]
			timestamp = toLocalTime( timestamp, 0 )
			
			dict = {'text': result['text'], 'user': result['from_user'], 'timestamp': timestamp}
			twitterdata.append(dict)
	graph_data = []
	if(len(twitterdata)):
		# time graph for the tweets
		graph_data = time_tweet_listing(twitterdata)
	return twitterdata, graph_data


#### calculate the time range of the tweets function #########
def time_tweet_listing(tdata):
	time_diff = toDatetimeObject( tdata[0]['timestamp'] ) - toDatetimeObject( tdata[len(tdata)-1]['timestamp'] )
	interval = (time_diff/10).seconds
	count = 0
	curr_interval = 1
	first_timestamp = tdata[0]['timestamp']
	time_tweet_list = []
	dict = {'category': first_timestamp, 'values': 0}
	time_tweet_list.append(dict)
	for tweet in tdata:
		td = toDatetimeObject( first_timestamp ) - toDatetimeObject( tweet['timestamp'] )
		if (td.days * 3600 * 24  + td.seconds) < (interval * curr_interval):
			count = count+1
		else:
			dict = {'category': toString( toDatetimeObject( first_timestamp ) - datetime.timedelta(seconds = interval * curr_interval) ), 'values': count}
			time_tweet_list.append(dict)
			count = 1
			curr_interval = curr_interval + 1
	return time_tweet_list




#### Function to get Google News API ####
def get_google_article(article_name):
	siteapikey = 'ABQIAAAAoFhCI1ZCL19hKoQRmZA7xRTfVN6g-2FJ7778ptnb8mvqcEH5GBSyOv5B2gM7lrZCmEMSlPLSFmPPPw'
	
	gnewsurl = ('https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=' + article_name + '&key=' + siteapikey + '&rsz=5')
	gdata = json.loads( urllib.urlopen(gnewsurl).read() )
	
	gnewsdata = []
	for news_item in gdata['responseData']['results']:
		timestamp = news_item['publishedDate'][: len(news_item['publishedDate'])-6]
		timestamp = toLocalTime( timestamp, 8 )

		dict = { 'title': news_item['titleNoFormatting'],
				 'content': re.sub(r'<.*?>', '', news_item['content']),
				 'url': news_item['unescapedUrl'], 
				 'publisher': news_item['publisher'],
				 'date': timestamp
		 	   }
		gnewsdata.append(dict)
	
	return gnewsdata


########## time functions ###################
def toLocalTime(dateStr, tz):
	dateObj = toDatetimeObject(dateStr)
	gmttime = dateObj + datetime.timedelta(hours = tz)
	return toString( gmttime + datetime.timedelta(hours = local_tz) ) 	
	
def toString(date):
	return datetime.datetime.strftime(date, "%a, %d %b %Y %H:%M:%S")

def toDatetimeObject(datestr):
	return datetime.datetime.strptime(datestr, "%a, %d %b %Y %H:%M:%S")


########## Main ###################################
if __name__ == '__main__':
	print "Content-Type: application/json\r\n"
	
	form = cgi.FieldStorage()
	article_name = form["title"].value
	local_tz = int(form["timezone"].value)
	#article_name = 'Sachin Tendulkar'
	#local_tz = -6
	
	if "refresh" in form:
		refresh_val = form["refresh"].value
		if refresh_val == 'tweets':
			twitterdata, graph_data = get_twitter_article(article_name)
	
			# create items to send to the fromt-end javascript
			fe_data = { 't_data': twitterdata, 'graph_data': graph_data }
			data = json.dumps( fe_data )
			print data
			
		if refresh_val == 'news':
			gnewsdata = get_google_article(article_name)
		
			# create items to send to the fromt-end javascript
			data = json.dumps( gnewsdata )
			print data
		
	else:
		# get the data
		wikidata = printable(get_wiki_article(article_name))
		twitterdata, graph_data = get_twitter_article(article_name)
		gnewsdata = get_google_article(article_name)
			
		# create items to send to the fromt-end javascript
		fe_data = { 'w_data': wikidata, 't_data': twitterdata, 'g_data': gnewsdata, 'graph_data': graph_data }
		data = json.dumps( fe_data )
		print data