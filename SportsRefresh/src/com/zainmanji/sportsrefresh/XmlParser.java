package com.zainmanji.sportsrefresh;

import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import com.zainmanji.sportsrefresh.HeadlineItemHandler;
import com.zainmanji.sportsrefresh.HeadlineItem;

//XML Parser
public class XmlParser {
    
    private XMLReader initializeReader() throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLReader xmlreader = parser.getXMLReader();
        return xmlreader;
    }
    
    
    public ArrayList<HeadlineItem> parseHeadlineResponse(String xml) {
        
        try {
            
            XMLReader xmlreader = initializeReader();
            
            HeadlineItemHandler headlineHandler = new HeadlineItemHandler();

            //Assign the handler
            xmlreader.setContentHandler(headlineHandler);
            
            //Perform synchronous parse
            xmlreader.parse(new InputSource(new StringReader(xml)));
            
            return headlineHandler.retrieveHeadlineList();
            
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

}