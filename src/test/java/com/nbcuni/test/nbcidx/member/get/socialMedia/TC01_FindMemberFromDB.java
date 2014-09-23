package com.nbcuni.test.nbcidx.member.get.socialMedia;

import static org.testng.AssertJUnit.fail;

import java.util.UUID;

import org.testng.Reporter;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Get API - TC01_FindMemberFromDB. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC01_FindMemberFromDB {
	
	private CustomWebDriver cs;
	private static AppLib al;
	
	public DBObject findRandomUUIDWithSocialInfo (DB myDB, String provider) throws Exception {
		
	  	al = new AppLib(cs);
	  	
	  	//Generate random UUID. Random UUID is the reference to fetch any existent member's UUID from MongoDB.
	  	UUID uid = UUID.randomUUID();
						
		String finalUUID = al.convertUUID(uid);
		Reporter.log("1) Creating Random UUID value to use as a reference for MongoDB :"+finalUUID);
		Reporter.log("");
		
		//Fetch the member from MongoDB
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("brands", 1);
		fields.put("metadata.username.value", 1);	
		fields.put("metadata.email.value.address", 1);
		if(provider.equals("facebook"))
			fields.put("metadata._provider.value.facebook", 1);
		else if(provider.equals("twitter"))		
			fields.put("metadata._provider.value.twitter", 1);
		else if(provider.equals("google"))		
			fields.put("metadata._provider.value.google", 1);
		else if(provider.equals("yahoo"))		
			fields.put("metadata._provider.value.yahoo", 1);
		else if(provider.equals("linkedin"))		
			fields.put("metadata._provider.value.linkedin", 1);
		else 
			fail("Wrong value for Provider in fields");
					
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", new BasicDBObject("$gte", finalUUID));
						
		if(provider.equals("facebook"))
			searchQuery.put("metadata._provider.0.value.facebook", new BasicDBObject("$exists", true));
		else if(provider.equals("twitter"))		
			searchQuery.put("metadata._provider.0.value.twitter", new BasicDBObject("$exists", true));
		else if(provider.equals("google"))		
			searchQuery.put("metadata._provider.0.value.google", new BasicDBObject("$exists", true));
		else if(provider.equals("yahoo"))		
			searchQuery.put("metadata._provider.0.value.yahoo", new BasicDBObject("$exists", true));
		else if(provider.equals("linkedin"))		
			searchQuery.put("metadata._provider.0.value.linkedin", new BasicDBObject("$exists", true));
		else 
			fail("Wrong value for Provider in SearchQuery");
			
		DBObject myDbObj = al.getMongoDbResponse(myDB, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FINDONE);	
		if(myDbObj==null)
		{
			searchQuery = new BasicDBObject();
			searchQuery.put("_id", new BasicDBObject("$lte", finalUUID));
			if(provider.equals("facebook"))
				searchQuery.put("metadata._provider.0.value.facebook", new BasicDBObject("$exists", true));
			else if(provider.equals("twitter"))		
				searchQuery.put("metadata._provider.0.value.twitter", new BasicDBObject("$exists", true));
			else if(provider.equals("google"))		
				searchQuery.put("metadata._provider.0.value.google", new BasicDBObject("$exists", true));
			else if(provider.equals("yahoo"))		
				searchQuery.put("metadata._provider.0.value.yahoo", new BasicDBObject("$exists", true));
			else if(provider.equals("linkedin"))		
				searchQuery.put("metadata._provider.0.value.linkedin", new BasicDBObject("$exists", true));
			else 
				fail("Wrong value for Provider in SearchQuery");
					
			myDbObj = al.getMongoDbResponse(myDB, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FINDONE);
			if(myDbObj==null)
				fail("MongoDB Cursor is empty. No Data found from Database");
		}
		
		return myDbObj;
	}
}

