package com.zainmanji.sportsrefresh;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.zainmanji.sportsrefresh.Image;
import com.zainmanji.sportsrefresh.HeadlineItem;


//This class parses through a given XML result
public class HeadlineItemHandler extends DefaultHandler {
    
	//Declare and initialize variables
    private StringBuffer buffer = new StringBuffer();
    private ArrayList<HeadlineItem> headlineList;
    private HeadlineItem headlineItem;
    private ArrayList<Image> headlineImagesList;
    private Image headlineImage;
    private Boolean newMobileLink = false;
    private Boolean getImageDetails = false;
    private Boolean getDescription = false;
    
    
    //Check beginning tags in XML response from API and act accordingly
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    	
        buffer.setLength(0);
        
        if (localName.equals("headlines")) {
        	headlineList = new ArrayList<HeadlineItem>();
        }
        else if (localName.equals("headlinesItem")) {
        	newMobileLink = true;
        	getDescription = true;
        	headlineItem = new HeadlineItem();
        }
        else if (localName.equals("images")) {
        	headlineImagesList = new ArrayList<Image>();
        }
        else if (localName.equals("imagesItem")) {
        	headlineImage = new Image();
        	getImageDetails = true;
        }
    }
    
    
    //Check end tags in XML response from API and act accordingly
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
        if (localName.equals("headlinesItem")) {
        	headlineList.add(headlineItem);
        }
        else if (localName.equals("headline")) {
        	headlineItem.headline = buffer.toString();
        }
        else if (localName.equals("lastModified")) {
        	headlineItem.lastModified = buffer.toString();
        }
        else if (localName.equals("mobile")) {
        	if (newMobileLink) {
        		headlineItem.mobileLink = buffer.toString();
        		newMobileLink = false;
        	}
        }
        else if (localName.equals("story")) {
        	headlineItem.story = buffer.toString();
        }
        else if (localName.equals("source")) {
        	headlineItem.source = buffer.toString();
        }
        else if (localName.equals("description")) { 
        	if (getDescription) {
        		headlineItem.description = buffer.toString();
        		getDescription = false;
        	}
        }
        else if (localName.equals("imagesItem")) {
			headlineImagesList.add(headlineImage);
			getImageDetails = false;
		}	
		else if (localName.equals("images")) {
			headlineItem.imagesList = headlineImagesList;
		}
		else if (localName.equals("type") && getImageDetails) {
			headlineImage.type = buffer.toString();
		}
		else if (localName.equals("url") && getImageDetails) {
			headlineImage.url = buffer.toString();
		}
		else if (localName.equals("height") && getImageDetails) {
			headlineImage.height = Integer.parseInt(buffer.toString());
		}
		else if (localName.equals("width") && getImageDetails) {
			headlineImage.width = Integer.parseInt(buffer.toString());
		}
		else if (localName.equals("size") && getImageDetails) {
			headlineImage.size = buffer.toString();
		}
    }
    
    
    @Override
    public void characters(char[] ch, int start, int length) {   	
        buffer.append(ch, start, length);
    }
        
    //Retrieve list of headlines
    public ArrayList<HeadlineItem> retrieveHeadlineList() {
        return headlineList;
    }
    
}