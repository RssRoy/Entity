#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Feb 28 22:35:58 2018

@author: ranjit
"""

import os
import urllib
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from time import sleep
from dateutil.parser import parse
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
import csv
import json

proxy = '10.50.52.221:80'

os.environ['http_proxy'] = proxy 
os.environ['HTTP_PROXY'] = proxy
os.environ['https_proxy'] = proxy
os.environ['HTTPS_PROXY'] = proxy


#tm_client = ticketpy.ApiClient('Nu77RwY7bX8B7PCJyDF30cvIM1SRSSGQ')

dir_path = os.path.dirname(os.path.realpath(__file__))
print(dir_path)
prefs = {"profile.managed_default_content_settings.images":2}
chromeOptions = webdriver.ChromeOptions()
chromeOptions.add_argument('--ignore-certificate-errors')
#chromeOptions.add_argument('--headless') 
#chromeOptions.add_argument('--disable-gpu')
chromeOptions.add_experimental_option("prefs",prefs)
driver = webdriver.Chrome(dir_path+'/chromedriver',chrome_options=chromeOptions)
pageid=1
apikey="Nu77RwY7bX8B7PCJyDF30cvIM1SRSSGQ"
baseurl="https://app.ticketmaster.com"

jsonstring = urllib.request.urlopen("https://app.ticketmaster.com/discovery/v2/events.json?size=5&countryCode=no&locale=no&apikey=Nu77RwY7bX8B7PCJyDF30cvIM1SRSSGQ")
jstr = json.load(jsonstring)
#maxpage=jstr["page"]["totalPages"]
maxpage=1
for pageid in range(1,maxpage+1):
    
    csvfile = dir_path+'/ticketmaster/page-'+str(pageid)+'.csv'
    #social_link = dir_path+'/social_link.csv'
    header = ['ai_id', 'category', 'event_name', 'event_desc', 'start_date', 'start_time', 'end_date', 'end_time', 'host_name', 'location_name', 'location_address', 'location_desc', 'host_contact', 'lat', 'lng', 'city', 'ticket_link', 'facebookhost_id', 'website', 'facebook_id', 'linkedin_id', 'twitter_id', 'instagram_id', 'pinterest_id', 'google_id', 'skype_id', 'youtube_id', 'discord_id', 'snapchat_id', 'ello_id', 'periscope_id', 'vimeo_id', 'meerkat_id', 'vine_id', 'flickr_id', 'tumblr_id', 'medium_id', 'tripadvisor_id', 'dribble_id', 'whatsapp_id']
    with open(csvfile, "w") as output:
        writer = csv.writer(output, lineterminator='\n')
        writer.writerow(header)
        
    #driver.get('https://trdevents.no/events/list/?tribe_event_display=list&tribe-bar-date=2018-03-01')
    
    #print(jstr["_links"]["last"]["href"])
    eventid=0
    for eventid in range(0,5):
        #Event Name
        event_name=jstr["_embedded"]["events"][eventid]["name"]
        print("Event Name:"+event_name)
        
        #Event Description
        print("event desc get link : "+jstr["_embedded"]["events"][eventid]["url"])
        sleep(3)
        driver.get(jstr["_embedded"]["events"][eventid]["url"])
        driver.find_element_by_css_selector('#eventinfo > header > div.eventinfo__main__info > div > div.eventcard__body').click()
                                            
        event_desc = driver.find_element_by_css_selector('body > div.ReactModalPortal > div > div > div > div.special_info_content__container > div.special_info_content__event_info_container > div > div.special_info_content__event_info').text
        event_desc = " ".join(event_desc.split())
        print("Event Desc:"+event_desc)
        
        imgfolder=dir_path+'/ticketmaster/images/'+event_name
        if not os.path.exists(imgfolder):
            os.makedirs(imgfolder)
        i=0
        try:
            size=len(jstr["_embedded"]["events"][eventid]["images"])
            for i in range(0,size):
                #event_img
                print("Downloading image for event : "+event_name)
                #try:
                img_src=jstr["_embedded"]["events"][eventid]["images"][i]["url"]
                img_name=img_src[(img_src.rfind('/')+1):]
                img_extension=img_name[img_name.rfind('.'):]
                img_name=img_name[:img_name.find('_')]+img_extension
                urllib.request.urlretrieve(img_src, imgfolder+'/'+img_name)
                print("Downloaded image for event :"+event_name)
                #except:
                #    print("Can't Download")
        except:
            print("**********************************No Images")
        
        #site_scraped
        site_scraped = baseurl+jstr["_links"]["self"]["href"]
        print("Site scraped : "+site_scraped)
        
        #ticket link
        ticket_link = jstr["_embedded"]["events"][eventid]["url"]
        print("ticket link : "+ticket_link)
        
        #latitude
        i=0
        lat= jstr["_embedded"]["events"][eventid]["_embedded"]["venues"][0]["location"]["latitude"]
        print("latitude : "+lat)
            
        #longitude
        lng=jstr["_embedded"]["events"][eventid]["_embedded"]["venues"][0]["location"]["longitude"]
        print("longitude : "+lng)
        
        #city
        city =jstr["_embedded"]["events"][eventid]["_embedded"]["venues"][0]["city"]["name"]
        print("city : "+city)
        
        #start date
        sdate=jstr["_embedded"]["events"][eventid]["dates"]["start"]["localDate"]
        dt = parse(sdate)
        start_date = dt.strftime('%d/%m/%Y')
        print("Start Date:"+start_date)
        
        #start_time
        start_time=jstr["_embedded"]["events"][eventid]["dates"]["start"]["localTime"]
        print("start_time : "+start_time)
        
        #host_name
        host_name = jstr["_embedded"]["events"][eventid]["promoter"]["name"]
        print("host_name: "+host_name)
        
        #location_name
        location_name = jstr["_embedded"]["events"][eventid]["_embedded"]["venues"][0]["name"]
        print("location_name: "+location_name)
        
        #location_address
        location_address = jstr["_embedded"]["events"][eventid]["_embedded"]["venues"][0]["address"]["line1"]
        print("location_address: "+location_address)
        
        #host_contact
        host_contact=''
        #driver.get(jstr["_embedded"]["events"][eventid]["_embedded"][0]["url"])
        #driver.find_element_by_css_selector(
        
        #location_name
        location_name = jstr["_embedded"]["events"][eventid]["_embedded"]["venues"][0]["name"]
        
        #location_address
        location_address = jstr["_embedded"]["events"][eventid]["_embedded"]["venues"][0]["address"]["line1"]
        
        
        #less used values
        ai_id=''
        category=''
        location_desc=''
        linkedin_id=''
        twitter_id=''
        instagram_id=''
        pinterest_id=''
        google_id=''
        skype_id=''
        youtube_id=''
        discord_id=''
        snapchat_id=''
        ello_id=''
        periscope_id=''
        vimeo_id=''
        meerkat_id=''
        vine_id=''
        flickr_id=''
        tumblr_id=''
        medium_id=''
        tripadvisor_id=''
        dribble_id=''
        whatsapp_id=''
        end_date=''
        end_time=''
        website=''
        
        #fb_id, fb_event, booking link
        facebookhost_id = ''
        facebook_id = ''
        
        final_list = [ai_id, category, event_name, event_desc, start_date, start_time, end_date, end_time, host_name, location_name, location_address, location_desc, host_contact, lat, lng, city, ticket_link, facebookhost_id, website, facebook_id, linkedin_id, twitter_id, instagram_id, pinterest_id, google_id, skype_id, youtube_id, discord_id, snapchat_id, ello_id, periscope_id, vimeo_id, meerkat_id, vine_id, flickr_id, tumblr_id, medium_id, tripadvisor_id, dribble_id, whatsapp_id]
        print("Final List:")
        print(final_list)
        with open(csvfile, "a") as output:
            writer = csv.writer(output, lineterminator='\n')
            writer.writerow(final_list)
           
        print('============================================')
        print('')
            
        
    nexturl=baseurl+jstr["_links"]["next"]["href"]+'&apikey='+apikey
    print("Next URL : "+nexturl)
    jsonstring = urllib.request.urlopen(nexturl)
    jstr = json.load(jsonstring)
    driver.close()



