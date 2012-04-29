package com.moupress.app.friendshost.sns.Renren;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.moupress.app.friendshost.sns.FeedItem;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeed;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.common.ResponseBean;



public class FeedExtractResponseBean extends ResponseBean{

	public static final String MESSAGE  = "message";
	public static final String TITLE  = "title";
	public static final String NAME  = "name";


	public static final int DEFAULT_POST_ID = 0;
	
	private String title;
	private String name;
	private String message;
	
	private List<FeedItem> feedList; 
	
	
	public FeedExtractResponseBean(String response, String format)  {
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
		try {
			if (format.equals(Renren.RESPONSE_FORMAT_JSON)) {
				response = "{ \"data\": " + response + "}"; 
				RenrenFeedHome bean = new Gson().fromJson(response, RenrenFeedHome.class);
				feedList = bean.getData();
				//System.out.println("Renren with JSON Response");
			} else if (format.equals(Renren.RESPONSE_FORMAT_XML)) {
				feedList = new ArrayList<FeedItem>();
				ParseString(response);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	public List<FeedItem> getFeedList() {
		return feedList;
	}

	public void setFeedList(ArrayList<FeedItem> feedList) {
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
			
			for(int index=0; index < listOffeeds.getLength(); index++) {
			      Node feed = listOffeeds.item(index);

			      if(feed.getNodeType() == Node.ELEMENT_NODE)
			      {
			    	  RenenFeedElement feedChild = new RenenFeedElement();
			    	  NodeList feedElements = feed.getChildNodes();
			    	  for(int i=0; i < feedElements.getLength(); i ++) {
			    		  //if(feedElements.item(i).getNodeType() == Node.ELEMENT_NODE) {
			    		  Element feedElement = (Element) feedElements.item(i);
		    		  	  if (feedElement.getFirstChild() != null) {
			    			  if (feedElement.getNodeName().equals("post_id"))
			    				  feedChild.setId(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if(feedElement.getNodeName().equals("feed_type"))
			    				  feedChild.setFeed_type(feedElement.getFirstChild().getNodeValue().trim()); 
			    			  else if(feedElement.getNodeName().equals("actor_type"))
			    				  feedChild.setActor_type(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if(feedElement.getNodeName().equals("actor_id")) {
			    				  feedChild.getFriend().setId(feedElement.getFirstChild().getNodeValue().trim());
			    				  feedChild.setActor_id(feedElement.getFirstChild().getNodeValue().trim());
			    			  }
			    			  else if(feedElement.getNodeName().equals("name")) {
			    				  feedChild.setName(feedElement.getFirstChild().getNodeValue().trim());
			    				  feedChild.getFriend().setName(feedElement.getFirstChild().getNodeValue().trim());
			    			  }
			    			  else if(feedElement.getNodeName().equals("update_time"))
			    				  feedChild.setUpdate_time(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if(feedElement.getNodeName().equals("headurl"))
			    				  feedChild.getFriend().setHeadurl(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if (feedElement.getNodeName().equals("message"))
			    				  feedChild.setMessage(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if (feedElement.getNodeName().equals("title"))
			    				  feedChild.setTitle(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if(feedElement.getNodeName().equals("href")) 
			    				  feedChild.setLink(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if(feedElement.getNodeName().equals("prefix"))
			    				  feedChild.setPrefix(feedElement.getFirstChild().getNodeValue().trim());
			    			  else if(feedElement.getNodeName().equals("description"))
			    				  feedChild.setDescription(feedElement.getFirstChild().getNodeValue().trim());
			    			  
			    			  // <attachment>
			    			  else if(feedElement.getNodeName().equals("attachment")) {
			    				  NodeList attachmentElements = feedElement.getChildNodes();
		    					  for ( int j = 0; j < attachmentElements.getLength(); j++ ) {
			    				  
			    						  //if(attachmentElements.item(i).getNodeType() == Node.ELEMENT_NODE) {
			    						  Element attachmentElement = (Element) attachmentElements.item(j);
			    						  
			    						  // <feed_media>
			    						  if(attachmentElement.getNodeName().equals("feed_media")) {
			    							  NodeList feedMediaElements = attachmentElement.getChildNodes();
			    							  
			    							  for ( int k = 0; k < feedMediaElements.getLength(); k++ ) {
			    								  //if(feedMediaElements.item(j).getNodeType() == Node.ELEMENT_NODE) {
			    									  Element feedMediaElement = (Element) feedMediaElements.item(k);
			    									  
			    									  if(feedMediaElement.getNodeName().equals("media_id"))
									    				  feedChild.setFeed_media_media_id(feedMediaElement.getFirstChild().getNodeValue().trim());
									    			  else if(feedMediaElement.getNodeName().equals("owner_id"))
									    				  feedChild.setFeed_media_owner_id(feedMediaElement.getFirstChild().getNodeValue().trim());
									    			  else if(feedMediaElement.getNodeName().equals("owner_name"))
									    				  feedChild.setFeed_media_owner_name(feedMediaElement.getFirstChild().getNodeValue().trim());
									    			  else if(feedMediaElement.getNodeName().equals("media_type"))
									    				  feedChild.setFeed_media_media_type(feedMediaElement.getFirstChild().getNodeValue().trim());
									    			  else if(feedMediaElement.getNodeName().equals("src"))
									    				  if ( feedMediaElement.getFirstChild() != null ) { // cater for returning <src /> scenario
									    					  feedChild.setFeed_media_src(feedMediaElement.getFirstChild().getNodeValue().trim());
									    				  }
			    								  //}
			    							  }
			    						  }
			    						  // </feed_media>
			    						  //}
			    				  }
			    			  } 
			    			  // </attachment>
			    			  
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
