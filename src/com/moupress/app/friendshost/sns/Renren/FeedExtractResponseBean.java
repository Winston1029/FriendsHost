package com.moupress.app.friendshost.sns.Renren;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.renren.api.connect.android.common.ResponseBean;



public class FeedExtractResponseBean extends ResponseBean{

	public static final String MESSAGE  = "message";
	public static final String TITLE  = "title";
	public static final String NAME  = "name";


	public static final int DEFAULT_POST_ID = 0;
	
	private String title;
	private String name;
	private String message;
	
	private ArrayList<FeedElement> feedList; 
	
	
	public FeedExtractResponseBean(String response)  {
		super(response);
		
//		try {
//			System.out.println("Response "+response);
//			JSONObject json = new JSONObject(response);
//			title = json.getString(TITLE);
//			name = json.getString(NAME);
//			message = json.getString(MESSAGE);
//			
//			System.out.println("title " + title+" name "+ name + " message " + message);
//		} catch(JSONException je) {
//			Util.logger(je.getMessage());
//			title = "";
//		}
		try
		{
			feedList = new ArrayList<FeedElement>();
			ParseString(response);
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	public ArrayList<FeedElement> getFeedList() {
		return feedList;
	}

	public void setFeedList(ArrayList<FeedElement> feedList) {
		this.feedList = feedList;
	}

	public void ParseString(String response) 
	{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		
		try  {
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        InputSource inStream = new InputSource();
	        inStream.setCharacterStream(new StringReader(response));
	        //Document doc = docBuilder.parse (new File("book.xml"));
			Document doc = docBuilder.parse (inStream);
			
			NodeList listOffeeds = doc.getElementsByTagName("feed_post");
			System.out.println(" Number of feeds "+listOffeeds.getLength());
			
			for(int index=0; index < listOffeeds.getLength(); index++) {
			      Node feed = listOffeeds.item(index);
			      
			      if(feed.getNodeType() == Node.ELEMENT_NODE)
			      {
			    	  Element element = (Element) feed;
			    	  
			    	  FeedElement feedChild = new FeedElement();
			    	  NodeList feedElements = feed.getChildNodes();
			    	  for(int iIndex=0; iIndex < feedElements.getLength(); iIndex ++)
			    	  {
			    		  if(feedElements.item(index).getNodeType() == Node.ELEMENT_NODE)
			    		  {
			    			  Element feedElement = (Element) feedElements.item(iIndex);
			    			  if(feedElement.getNodeName().equals("name"))
			    				  feedChild.setName(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if(feedElement.getNodeName().equals("update_time"))
			    				  feedChild.setUpdate_time(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if(feedElement.getNodeName().equals("headurl"))
			    				  feedChild.setHeadurl(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if (feedElement.getNodeName().equals("message"))
			    				  feedChild.setMessage(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if (feedElement.getNodeName().equals("title"))
			    				  feedChild.setTitle(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if(feedElement.getNodeName().equals("prefix"))
			    				  feedChild.setPrefix(feedElement.getFirstChild().getNodeValue().trim());
			    			  //System.out.println("Node Name "+feedElement.getNodeName()+" Node Text "+feedElement.getFirstChild().getNodeValue().trim());
			    			  
			    		  }
			    	  }
			    	  
			    	  feedList.add(feedChild);
			      }
			}
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMessage() {
		return message;
	}
	
	
}
