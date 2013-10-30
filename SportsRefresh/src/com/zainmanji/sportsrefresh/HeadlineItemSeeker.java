package com.zainmanji.sportsrefresh;

import java.util.ArrayList;
import com.zainmanji.sportsrefresh.HeadlineItem;

//This class makes call to the ESPN API to gather XML response from API
public class HeadlineItemSeeker {
        
	protected static final String URL = "http://api.espn.com/v1/sports/news/?_accept=text/xml&apikey=c9jdbn9zf3wun7zexabpmxep";
	protected HttpRetriever httpRetriever = new HttpRetriever();
    protected XmlParser xmlParser = new XmlParser();
	
	//Get list of headlines
    public ArrayList<HeadlineItem> find(String query) {
        ArrayList<HeadlineItem> headlineList = retrieveHeadlineList(query);
        return headlineList;
    }
    
    //Make call to ESPN API at specified URL and parse through XML
    private ArrayList<HeadlineItem> retrieveHeadlineList(String query) {
        String response = httpRetriever.retrieve(URL);
        return xmlParser.parseHeadlineResponse(response);
    }

}