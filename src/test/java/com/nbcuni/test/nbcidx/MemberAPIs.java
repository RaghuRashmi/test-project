package com.nbcuni.test.nbcidx;

import static org.testng.AssertJUnit.fail;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.Reporter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.webdriver.API;

/**************************************************************************
 * NBCIDX Member API Functions Library. Copyright.
 *
 * @author Rashmi Sale
 * @version 1.0 Date: May 01, 2014
 **************************************************************************/

public class MemberAPIs {
	
	/** 
	 * Method for IDX member.get API Response.
	 * @return Response from member.get.
	 * @throws Exception 
	 */
	public JsonObject memberGetResponse(API myAPI, AppLib myAL, String params, String myBrandId) throws Exception {
			
		String apicall=null;
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.GET ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the API call for member.get
		apicall = myAL.getApiURL()+"/member/get?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId+"&"+params;
	
		//Send member.get GET Request.
		JsonObject response = myAPI.getRootObjectsFromResponseBody(myAPI.getRestRequest(apicall, myAPI.REQUEST_CONTENTTYPE_JSON));
	
		return response;
	}


	/** 
	 * Method for IDX member.get API Response Code.
	 * @return Response Code from member.get.
	 * @throws Exception
	 */
	public int memberGetResponseCode(API myAPI, AppLib myAL, String params, String myBrandId) throws Exception {
			
		String apicall=null;
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.GET ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the API call for member.get
		apicall = myAL.getApiURL()+"/member/get?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId+"&"+params;
	
		//Send member.get GET Request.
		int myResponseCode = myAL.getHTTPResponseCode(apicall);
			
		return myResponseCode;
	}
	
	/** 
	 * Method for IDX member.put API Response.
	 * @return Response from member.put.
	 * @throws Exception
	 */
	public JsonObject memberPUTResponse(API myAPI, AppLib myAL, String myJsonBody, String myBrandId) throws Exception {
			
		String apicall=null;
		JsonObject myResponse=null;
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.PUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/json";
		
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/member/put?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
						
		//Send member.put POST Request. 
		myResponse = myAPI.getRootObjectsFromResponseBody(myAPI.postRestRequest(apicall, myJsonBodyAfterFormat, myContentType));
		
		return myResponse;	
	}
	
	/** 
	 * Method for IDX member.put API ResponseCode.
	 * @return Response Code from member.put.
	 * @throws Exception
	 */
	public int memberPUTResponseCode(API myAPI, AppLib myAL, String myJsonBody, String myBrandId) throws Exception {
		
		String apicall=null;
		int myResponseCode=0;
				
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.PUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/json";
		
		Reporter.log("JSON Body for POST = " +myJsonBody);
		
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/member/put?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
						
		//Send member.put POST Request. 
		myResponseCode = myAL.postHTTPReponseCode(apicall, myJsonBodyAfterFormat, myContentType);
		
		return myResponseCode;	
	}

	/** 
	 * Method for IDX member.login API Response.
	 * @return Response from member.login.
	 * @throws Exception
	 */
	public JsonObject memberLOGINResponse(API myAPI, AppLib myAL, String myJsonBody, String myBrandId, String memberId, DB myDB) throws Exception {
		
		String apicall=null;
		JsonObject myResponse=null;
				
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.login.
		apicall = myAL.getApiURL()+"/member/login?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
				
		//Send member.login POST Request. 
		myResponse = myAPI.getRootObjectsFromResponseBody(myAPI.postRestRequest(apicall, myJsonBodyAfterFormat, myContentType));
		JsonElement apiLoginStatus = myResponse.get("_logged_in");
		
		Reporter.log("");
		Reporter.log("_logged_in status of Member "+ memberId + " in member.get API Response = " +apiLoginStatus.toString());
		Reporter.log("");
		//Check for _logged_in status = true in API Response
		boolean bloginStatus=apiLoginStatus.toString().equals("true");
		Assert.assertEquals(bloginStatus, true);
		
		// Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating logged_in status for Member " +memberId+ " in MongoDB");
		Reporter.log("");
		
		//Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", memberId);
		searchQuery.put("metadata._logged_in.brand_id", myBrandId);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);	
		fields.put("metadata.email.value.address", 1);
		fields.put("metadata._logged_in.value", 1);
		fields.put("metadata._last_login.brand_id", 1);
					
		DBObject DbObj = myAL.getMongoDbResponse(myDB, searchQuery, fields, myAL.IDX_MEMBERS_COLLECTION, myAL.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		JsonObject DBResponse =  myAL.JsonObjectFromMongoDBObject(DbObj);
		
		//Check for _logged_in status = true in MongoDB
		ArrayList<HashMap<String, String>> dbLoginStatus = myAPI.convertJsonArrayToHashMap(myAPI.getJsonArray(myAPI.getAsJsonObject(DBResponse, "metadata"),"_logged_in"));
		bloginStatus=dbLoginStatus.get(0).toString().equals("{value=true}");
		Assert.assertEquals(bloginStatus, true);
		
		//Validating collection 'idx_members' for 'metadata/_logged_in/' object
		Reporter.log("");
		Reporter.log("Validating collection 'idx_members' for 'metadata/_logged_in/' object"); 
		int db_logged_in_Count = myAL.countElementsJsonArray(myAPI.getJsonArray(myAPI.getAsJsonObject(DBResponse, "metadata"),"_logged_in"));
		if(db_logged_in_Count==1)
			Reporter.log("Number of object in MongoDB collection "+myAL.IDX_MEMBERS_COLLECTION+" in 'metadata/_logged_in/' after member.login = "+ db_logged_in_Count +" and Latest '_logged_in' status of Member "+ memberId + " in MongoDB = " +dbLoginStatus.get(0).toString());
		else
			fail("More than 1 object is present in 'metadata/_logged_in/' after member.login and count of number of object is = " +db_logged_in_Count );
		
		//Validating collection 'idx_members' for 'metadata/_last_login/' object
		Reporter.log("");
		Reporter.log("Validating collection 'idx_members' for 'metadata/_last_login/' object"); 
		int db_last_login_Count = myAL.countElementsJsonArray(myAPI.getJsonArray(myAPI.getAsJsonObject(DBResponse, "metadata"),"_last_login"));
		if(db_last_login_Count==1)
			Reporter.log("Number of object in MongoDB collection "+myAL.IDX_MEMBERS_COLLECTION+" in 'metadata/_last_login/' after member.login = "+ db_last_login_Count );
		else
			fail("More than 1 object is present in 'metadata/_last_login/' after member.login and count of number of object is = " +db_last_login_Count );
		Reporter.log("");
				
		return myResponse;
	}
	
	/** 
	 * Method for IDX member.login API ResponseCode.
	 * @return Response Code from member.login.
	 * @throws Exception 
	 */
	public int memberLOGINResponseCode(API myAPI, AppLib myAL, String myJsonBody, String myBrandId) throws Exception {
		
		String apicall=null;
		int myResponseCode=0;
			
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.login.
		apicall = myAL.getApiURL()+"/member/login?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;
				
		//Send member.login POST Request. 
		myResponseCode = myAL.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		
		return myResponseCode;
	}
	
	/** 
	 * Method for IDX member.logout API Response.
	 * @return Response from member.logout.
	 * @throws Exception 
	 */
	public JsonObject memberLOGOUTResponse(API myAPI, AppLib myAL, String myJsonBody, String myBrandId, String memberId, DB myDB) throws Exception {
		
		String apicall=null;
		JsonObject myResponse=null;
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGOUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.logout
		apicall = myAL.getApiURL()+"/member/logout?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
					
		//Send member.logout POST Request. 
		myResponse = myAPI.getRootObjectsFromResponseBody(myAPI.postRestRequest(apicall, myJsonBodyAfterFormat, myContentType));
		JsonElement apiLoginStatus = myResponse.get("_logged_in");
		
		Reporter.log("");
		Reporter.log("_logged_in status of Member "+ memberId + " in member.get API Response = " +apiLoginStatus.toString());
		Reporter.log("");
		//Check for _logged_in status = false in API Response
		boolean bloginStatus=apiLoginStatus.toString().equals("false");
		Assert.assertEquals(bloginStatus, true);
					
		// Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating logged_in status for Member " +memberId+ " in MongoDB");
		Reporter.log("");
		
		// Find and display 
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", memberId);
		searchQuery.put("metadata._logged_in.brand_id", myBrandId);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);	
		fields.put("metadata.email.value.address", 1);
		fields.put("metadata._logged_in.value", 1);
		fields.put("metadata._last_login.brand_id", 1);
					
		DBObject DbObj = myAL.getMongoDbResponse(myDB, searchQuery, fields, myAL.IDX_MEMBERS_COLLECTION, myAL.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		JsonObject DBResponse =  myAL.JsonObjectFromMongoDBObject(DbObj);
								
		//Check for _logged_in status = false in MongoDB
		ArrayList<HashMap<String, String>> dbLoginStatus = myAPI.convertJsonArrayToHashMap(myAPI.getJsonArray(myAPI.getAsJsonObject(DBResponse, "metadata"),"_logged_in"));
		bloginStatus=dbLoginStatus.get(0).toString().equals("{value=false}");
		Assert.assertEquals(bloginStatus, true);
		
		//Validating collection 'idx_members' for 'metadata/_logged_in/' object
		Reporter.log("");
		Reporter.log("Validating collection 'idx_members' for 'metadata/_logged_in/' object"); 		
		int db_logged_in_Count = myAL.countElementsJsonArray(myAPI.getJsonArray(myAPI.getAsJsonObject(DBResponse, "metadata"),"_logged_in"));
		if(db_logged_in_Count==1)
			Reporter.log("Number of object in MongoDB collection "+myAL.IDX_MEMBERS_COLLECTION+" in 'metadata/_logged_in/' after member.logout = "+ db_logged_in_Count +" and Latest '_logged_in' status of Member "+ memberId + " in MongoDB = " +dbLoginStatus.get(0).toString());
		else
			fail("More than 1 object is present in 'metadata/_logged_in/' after member.logout and count of number of object is = " +db_logged_in_Count );
		
		//Validating collection 'idx_members' for 'metadata/_last_login/' object
		Reporter.log("");
		Reporter.log("Validating collection 'idx_members' for 'metadata/_last_login/' object"); 
		int db_last_login_Count = myAL.countElementsJsonArray(myAPI.getJsonArray(myAPI.getAsJsonObject(DBResponse, "metadata"),"_last_login"));
		if(db_last_login_Count==1)
			Reporter.log("Number of object in MongoDB collection "+myAL.IDX_MEMBERS_COLLECTION+" in 'metadata/_last_login/' after member.logout = "+ db_last_login_Count );
		else
			fail("More than 1 object is present in 'metadata/_last_login/' after member.logout and count of number of object is = " +db_last_login_Count );
		Reporter.log("");
				
		return myResponse;
	}
	
	/** 
	 * Method for IDX member.logout API ResponseCode.
	 * @return Response Code from member.login.
	 * @throws Exception
	 */
	public int memberLOGOUTResponseCode(API myAPI, AppLib myAL, String myJsonBody, String myBrandId) throws Exception {
		
		String apicall=null;
		int myResponseCode=0;
			
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGOUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.logout
		apicall = myAL.getApiURL()+"/member/logout?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;
				
		//Send member.logout POST Request. 
		myResponseCode = myAL.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		
		return myResponseCode;
	}
	
	/** 
	 * Method for IDX member.link API Response.
	 * @return Response from member.link.
	 * @throws Exception 
	 */
	public JsonObject  memberLINKResponse(API myAPI, AppLib myAL, String myJsonBody, String myBrandId, String memberId, DB myDB) throws Exception {
		
		String apicall=null;
		JsonObject myResponse=null;
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LINK ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.link.
		apicall = myAL.getApiURL()+"/member/link?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
				
		//Send member.link POST Request.
		myResponse = myAPI.getRootObjectsFromResponseBody(myAPI.postRestRequest(apicall, myJsonBodyAfterFormat, myContentType));
		
		return myResponse;
	}
	
	
	/** 
	 * Method for IDX member.link API ResponseCode.
	 * @return Response Code from member.link.
	 * @throws Exception
	 */
	public int memberLINKResponseCode(API myAPI, AppLib myAL, String myJsonBody, String myBrandId) throws Exception {
		
		String apicall=null;
		int myResponseCode=0;
			
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LINK ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.link.
		apicall = myAL.getApiURL()+"/member/link?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;
				
		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
				
		//Send member.link POST Request. 
		myResponseCode = myAL.postHTTPReponseCode(apicall, myJsonBodyAfterFormat, myContentType);
		
		return myResponseCode;
	}
	
	
	/** 
	 * Method for IDX member.link_noverify API Response.
	 * @return Response from member.link_noverify.
	 * @throws Exception 
	 */
	public int memberLINKNoVerifyResponse(API myAPI, AppLib myAL, String myJsonBody, String myBrandId) throws Exception {
		
		String apicall=null;
						
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LINK_NOVERIFY ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.link_noverify.
		apicall = myAL.getApiURL()+"/member/link_noverify?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
				
		//Send member.link_noverify POST Request. 
		int myResponseCode = myAL.postHTTPReponseCode(apicall, myJsonBodyAfterFormat, myContentType);
		
		return myResponseCode;
	}
	
	/** 
	 * Method for IDX member.unlink API Response.
	 * @return Response from member.unlink.
	 * @throws Exception 
	 */
	public JsonObject  memberUNLINKResponse(API myAPI, AppLib myAL, String myJsonBody, String myBrandId, String memberId, DB myDB) throws Exception {
		
		String apicall=null;
		JsonObject myResponse=null;
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.UNLINK ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.unlink.
		apicall = myAL.getApiURL()+"/member/unlink?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
					
		//Send member.unlink POST Request. 
		myResponse = myAPI.getRootObjectsFromResponseBody(myAPI.postRestRequest(apicall, myJsonBodyAfterFormat, myContentType));
		
		return myResponse;
	}
	
	
	/** 
	 * Method for IDX member.unlink API ResponseCode.
	 * @return Response Code from member.unlink.
	 * @throws Exception 
	 */
	public int memberUNLINKResponseCode(API myAPI, AppLib myAL, String myJsonBody, String myBrandId) throws Exception {
		
		String apicall=null;
		int myResponseCode=0;
			
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.UNLINK ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.unlink.
		apicall = myAL.getApiURL()+"/member/unlink?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;
				
		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
		
		//Send member.unlink POST Request. 
		myResponseCode = myAL.postHTTPReponseCode(apicall, myJsonBodyAfterFormat, myContentType);
		
		return myResponseCode;
	}

	
	/** 
	 * Method for IDX member.remove API Response Code.
	 * @return Response Code from member.remove.
	 * @throws Exception 
	 */
	public int memberREMOVEResponseCode(API myAPI, AppLib myAL, String myJsonBody, String myBrandId, String memberName, String uuid, DB myDB) throws Exception {
		
		String apicall=null;
			
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.REMOVE ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.remove.
		apicall = myAL.getApiURL()+"/member/remove?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;
	
		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
				
		//Send member.remove POST Request.
		int responseCode = myAL.postHTTPReponseCode(apicall, myJsonBodyAfterFormat, myContentType);
		if(responseCode == 204)
		{
			Reporter.log("member.remove has successfully deleted the Member : "+memberName);
				
			// Get the DB Response
			Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			Reporter.log("Validating Member deleted from MongoDB");
			Reporter.log("");
		
			// Build search query for idx_members collection.
			BasicDBObject idx_members_searchQuery = new BasicDBObject();
			idx_members_searchQuery.put("metadata.username.value", memberName);
			idx_members_searchQuery.put("metadata.username.brand_id",myBrandId);
			
			// Build search query for idx_metadata_history collection.
			BasicDBObject idx_metadata_history_searchQuery = new BasicDBObject();
			idx_metadata_history_searchQuery.put("category","Member");
			idx_metadata_history_searchQuery.put("category_id",uuid);
			idx_metadata_history_searchQuery.put("value.brand_id",myBrandId);
				
			BasicDBObject fields = null;
		
			// Check idx_members collection.
			DBObject DbObj = null;
			DbObj = myAL.getMongoDbResponse(myDB, idx_members_searchQuery, fields, myAL.IDX_MEMBERS_COLLECTION, myAL.FIND);	
			if(DbObj==null)
				Reporter.log("Passed : Member "+memberName+ " successfully deleted from MongoDB Collection ="+myAL.IDX_MEMBERS_COLLECTION);
			else
				fail("Member "+memberName+" still exist in MongoDB Collection ="+myAL.IDX_MEMBERS_COLLECTION);
			Reporter.log(" ");
		
			// Check idx_metadata_history collection.
			DbObj=null;
			DbObj = myAL.getMongoDbResponse(myDB, idx_metadata_history_searchQuery, fields, myAL.IDX_METADATA_HISTORY_COLLECTION, myAL.FIND);	
			if(DbObj==null)
				Reporter.log("Passed : Member "+memberName+ " successfully deleted from MongoDB Collection ="+myAL.IDX_METADATA_HISTORY_COLLECTION);
			else
				fail("Member "+memberName+" still exist in MongoDB Collection ="+myAL.IDX_METADATA_HISTORY_COLLECTION);
			Reporter.log(" ");
		}		
		return responseCode;
	}
	
	/** 
	 * Method for IDX member.delete API Response Code.
	 * @return Response Code from member.delete.
	 * @throws Exception 
	 */
	public int memberDELETEResponseCode(API myAPI, AppLib myAL, String uuid, String myBrandId, DB myDB) throws Exception {
		
		String apicall=null;
			
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.DELETE ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.delete.
		apicall = myAL.getApiURL()+"/member/delete?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId;
	
		//Generate the JSON Body in proper format.
		String postBody="id="+uuid;
				
		//Send member.remove POST Request.
		int responseCode = myAL.postHTTPReponseCode(apicall, postBody, myContentType);
		if(responseCode == 204)
		{
			Reporter.log("member.delete has successfully deleted the Member : "+uuid);
			Reporter.log("");
			
			// Get the DB Response
			Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			Reporter.log("Validating Member deleted from MongoDB");
			Reporter.log("");
		
			// Build search query for idx_members collection.
			BasicDBObject idx_members_searchQuery = new BasicDBObject();
			idx_members_searchQuery.put("_id", uuid);
			idx_members_searchQuery.put("metadata.username.brand_id",myBrandId);
			
			BasicDBObject fields = new BasicDBObject();
			fields.put("_id", 1);
			fields.put("_status", 1);
		
			// Check idx_members collection.
			DBObject DbObj = null;
			DbObj = myAL.getMongoDbResponse(myDB, idx_members_searchQuery, fields, myAL.IDX_MEMBERS_COLLECTION, myAL.FIND);	
			if(DbObj!=null)
			{
				//Check for status = deleted in MongoDB
				String dbStatus =  DbObj.get("_status").toString(); 
				boolean bStatus=dbStatus.toString().equals("deleted");
				Assert.assertEquals(bStatus, true);
				Reporter.log("Passed : Member "+uuid+ " still present in MongoDB Collection = "+myAL.IDX_MEMBERS_COLLECTION +" with 'status' value = "+dbStatus.toString());
			}
			else
				fail("Member "+uuid+" got removed from MongoDB Collection = "+myAL.IDX_MEMBERS_COLLECTION);
			Reporter.log(" ");		
		}		
		return responseCode;
	}
	
	/** 
	 * Method for IDX brand.get API Response.
	 * @return Response from brand.get.
	 * @throws Exception
	 */
	public JsonObject brandGETResponse(API myAPI, AppLib myAL, String myBrandId) throws Exception {
			
		String apicall=null;
		JsonObject myResponse=null;
			
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ BRAND.GET ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
				
		//Generate the Content Type for POST. 
		String myContentType = "application/json";
				
		//Generate the API call for brand.get.
		apicall = myAL.getApiURL()+"/brand/get?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myBrandId+"&id="+myBrandId;
				
		//Send brand.get GET Request. 
		myResponse = myAPI.getRootObjectsFromResponseBody(myAPI.getRestRequest(apicall, myContentType));
		
		return myResponse;
	}
}
