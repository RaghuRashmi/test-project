package com.nbcuni.test.nbcidx;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
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
	
	private API api=new API();

	/** 
	 * Method for IDX member.put API.
	 * @return Response from member.put.
	 * @throws Exception Code Error
	 */
	public JsonObject memberPUT(AppLib myAL, String myJsonBody, Proxy proxy) throws Exception {
			
		String apicall=null;
		JsonObject myResponse=null;
		
		api.setProxy(proxy);
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.PUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/json";
		
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/member/put?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myAL.getSurfBrandId();

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
						
		//Send member.put POST Request. 
		myResponse = api.getRootObjectsFromResponseBody(api.postRestRequest(apicall, myJsonBodyAfterFormat, myContentType));
		
		return myResponse;
		
	}
	
	/** 
	 * Method for IDX member.put API Error Codes.
	 * @return Response Code from member.put.
	 * @throws Exception Code Error
	 */
	public int memberPUTError(AppLib myAL, String myJsonBody, Proxy proxy) throws Exception {
		
		String apicall=null;
		int myResponseCode=0;
		
		api.setProxy(proxy);
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.PUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/json";
		
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/member/put?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myAL.getSurfBrandId();

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
						
		//Send member.put POST Request. 
		myResponseCode = myAL.postHTTPReponseCode(apicall, myJsonBodyAfterFormat, proxy, myContentType);
		
		return myResponseCode;
		
	}

	/** 
	 * Method for IDX member.login API.
	 * @return Response from member.login.
	 * @throws Exception Code Error
	 */
	public JsonObject memberLogin(AppLib myAL, String myJsonBody, String memberId, Proxy proxy, DB myDB) throws Exception {
		
		String apicall=null;
		JsonObject myResponse=null;
		
		api.setProxy(proxy);
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/member/login?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myAL.getSurfBrandId();

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
				
		//Send member.put POST Request. 
		myResponse = api.getRootObjectsFromResponseBody(api.postRestRequest(apicall, myJsonBodyAfterFormat, myContentType));
		JsonElement apiLoginStatus = myResponse.get("_logged_in");
		
		Reporter.log("");
		Reporter.log("_logged_in status of Member "+ memberId + " in member.get API Response = " +apiLoginStatus.toString());
		Reporter.log("");
		
		boolean bloginStatus=apiLoginStatus.toString().equals("true");
		Assert.assertEquals(bloginStatus, true);
				
			
		// Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating logged_in status for Member " +memberId+ " in MongoDB");
		Reporter.log("");
		
		/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", memberId);
		searchQuery.put("metadata._logged_in.brand_id", myAL.getSurfBrandId());
				
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
								
		ArrayList<HashMap<String, String>> dbLoginStatus = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"_logged_in"));
		bloginStatus=dbLoginStatus.get(0).toString().equals("{value=true}");
		Assert.assertEquals(bloginStatus, true);
		
		Reporter.log("");
		Reporter.log("Validating collection 'idx_members' for 'metadata/_logged_in/' object"); 
		int db_logged_in_Count = myAL.countElementsJsonArray(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"_logged_in"));
		if(db_logged_in_Count==1)
			Reporter.log("Number of object in MongoDB collection "+myAL.IDX_MEMBERS_COLLECTION+" in 'metadata/_logged_in/' after member.login = "+ db_logged_in_Count +" and Latest '_logged_in' status of Member "+ memberId + " in MongoDB = " +dbLoginStatus.get(0).toString());
		else
			fail("More than 1 object is present in 'metadata/_logged_in/' after member.login and count of number of object is = " +db_logged_in_Count );
		
		Reporter.log("");
		Reporter.log("Validating collection 'idx_members' for 'metadata/_last_login/' object"); 
		int db_last_login_Count = myAL.countElementsJsonArray(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"_last_login"));
		if(db_last_login_Count==1)
			Reporter.log("Number of object in MongoDB collection "+myAL.IDX_MEMBERS_COLLECTION+" in 'metadata/_last_login/' after member.login = "+ db_last_login_Count );
		else
			fail("More than 1 object is present in 'metadata/_last_login/' after member.login and count of number of object is = " +db_last_login_Count );
		
		Reporter.log("");
				
		return myResponse;
		
	}
	
	/** 
	 * Method for IDX member.login API Error Codes.
	 * @return Response Code from member.login.
	 * @throws Exception Code Error
	 */
	public int memberLoginError(AppLib myAL, String myJsonBody, Proxy proxy) throws Exception {
		
		String apicall=null;
		int myResponseCode=0;
		
		api.setProxy(proxy);
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.Login ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/member/login?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myAL.getSurfBrandId();
				
		//Send member.put POST Request. 
		myResponseCode = myAL.postHTTPReponseCode(apicall, myJsonBody, proxy, myContentType);
		
		return myResponseCode;
		
	}
	
	/** 
	 * Method for IDX member.logout API.
	 * @return Response from member.logout.
	 * @throws Exception Code Error
	 */
	public JsonObject memberLogout(AppLib myAL, String myJsonBody, String memberId, Proxy proxy, DB myDB) throws Exception {
		
		String apicall=null;
		JsonObject myResponse=null;
		
		api.setProxy(proxy);
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGOUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/member/logout?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myAL.getSurfBrandId();

		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=myAL.convertIntoJsonFormat(myJsonBody);
					
		//Send member.put POST Request. 
		myResponse = api.getRootObjectsFromResponseBody(api.postRestRequest(apicall, myJsonBodyAfterFormat, myContentType));
		JsonElement apiLoginStatus = myResponse.get("_logged_in");
		
		Reporter.log("");
		Reporter.log("_logged_in status of Member "+ memberId + " in member.get API Response = " +apiLoginStatus.toString());
		Reporter.log("");
		
		boolean bloginStatus=apiLoginStatus.toString().equals("false");
		Assert.assertEquals(bloginStatus, true);
				
			
		// Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating logged_in status for Member " +memberId+ " in MongoDB");
		Reporter.log("");
		
		/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", memberId);
		searchQuery.put("metadata._logged_in.brand_id", myAL.getSurfBrandId());
				
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
								
		ArrayList<HashMap<String, String>> dbLoginStatus = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"_logged_in"));
		bloginStatus=dbLoginStatus.get(0).toString().equals("{value=false}");
		Assert.assertEquals(bloginStatus, true);
		
		Reporter.log("");
		Reporter.log("Validating collection 'idx_members' for 'metadata/_logged_in/' object"); 		
		int db_logged_in_Count = myAL.countElementsJsonArray(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"_logged_in"));
		if(db_logged_in_Count==1)
			Reporter.log("Number of object in MongoDB collection "+myAL.IDX_MEMBERS_COLLECTION+" in 'metadata/_logged_in/' after member.logout = "+ db_logged_in_Count +" and Latest '_logged_in' status of Member "+ memberId + " in MongoDB = " +dbLoginStatus.get(0).toString());
		else
			fail("More than 1 object is present in 'metadata/_logged_in/' after member.logout and count of number of object is = " +db_logged_in_Count );
		
		Reporter.log("");
		Reporter.log("Validating collection 'idx_members' for 'metadata/_last_login/' object"); 
		int db_last_login_Count = myAL.countElementsJsonArray(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"_last_login"));
		if(db_last_login_Count==1)
			Reporter.log("Number of object in MongoDB collection "+myAL.IDX_MEMBERS_COLLECTION+" in 'metadata/_last_login/' after member.logout = "+ db_last_login_Count );
		else
			fail("More than 1 object is present in 'metadata/_last_login/' after member.logout and count of number of object is = " +db_last_login_Count );
		
		Reporter.log("");
				
		return myResponse;
		
	}
	
	/** 
	 * Method for IDX member.link API.
	 * @return Response from member.link.
	 * @throws Exception Code Error
	 */
	public int  memberLink(AppLib myAL, String myPostBody, String memberId, Proxy proxy, DB myDB) throws Exception {
		
		String apicall=null;
		
		api.setProxy(proxy);
				
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LINK ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/member/link?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myAL.getSurfBrandId();

		Reporter.log("Post Body for POST = " +myPostBody);
				
		int myResponseCode = myAL.postHTTPReponseCode(apicall, myPostBody, proxy, myContentType);
		
		//return myResponse;
		return myResponseCode;
		
	}
	
	/** 
	 * Method for IDX member.link_noverify API.
	 * @return Response from member.link_noverify.
	 * @throws Exception Code Error
	 */
	public int  memberLinkNoVerify(AppLib myAL, String myPostBody, Proxy proxy) throws Exception {
		
		String apicall=null;
		
		api.setProxy(proxy);
				
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LINK_NOVERIFY ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/member/link_noverify?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myAL.getSurfBrandId();

		Reporter.log("Post Body for POST = " +myPostBody);
				
		int myResponseCode = myAL.postHTTPReponseCode(apicall, myPostBody, proxy, myContentType);
		
		//return myResponse;
		return myResponseCode;
		
	}
	
	/** 
	 * Method for IDX member.remove API.
	 * @return Response from member.remove.
	 * @throws Exception Code Error
	 */
	public Boolean memberREMOVE(AppLib myAL, String memberId, Proxy proxy, DB myDB) throws Exception {
		
		String apicall=null;
		
		api.setProxy(proxy);
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.REMOVE ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/member/remove?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+myAL.getSurfBrandId();
	
		//Generate the JSON Body.
		String myJsonBody ="id="+memberId;
		Reporter.log("JSON Body for POST = " +myJsonBody);
				
		//Send member.remove POST Request.
		int responseCode = myAL.postHTTPReponseCode(apicall, myJsonBody, proxy, myContentType);
		if(responseCode == 204)
			Reporter.log("member.remove has successfully deleted the Member : "+memberId);
		else
			fail("Failed : HTTP error code : "+ responseCode);
		
		// Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Member deleted from MongoDB");
		Reporter.log("");
		
		// Build search query for idx_members collection.
		BasicDBObject idx_members_searchQuery = new BasicDBObject();
		//idx_members_searchQuery.put("metadata.username.value", memberId);
		idx_members_searchQuery.put("_id", memberId);
		
		// Build search query for idx_metadata_history collection.
		BasicDBObject idx_metadata_history_searchQuery = new BasicDBObject();
		idx_metadata_history_searchQuery.put("category","Member");
		idx_metadata_history_searchQuery.put("category_id",memberId);
				
		BasicDBObject fields = null;
			
		// Check idx_members collection.
		DBObject DbObj = myAL.getMongoDbResponse(myDB, idx_members_searchQuery, fields, myAL.IDX_MEMBERS_COLLECTION, myAL.FIND);	
		if(DbObj==null)
		{
			Reporter.log("Passed : Member "+memberId+ " successfully deleted from MongoDB Collection ="+myAL.IDX_MEMBERS_COLLECTION);
			Reporter.log(" ");
		}
		else
			fail("Member "+memberId+" still exist in MongoDB Collection ="+myAL.IDX_MEMBERS_COLLECTION);
		
		
		// Check idx_metadata_history collection.
		DbObj = myAL.getMongoDbResponse(myDB, idx_metadata_history_searchQuery, fields, myAL.IDX_METADATA_HISTORY_COLLECTION, myAL.FIND);	
		if(DbObj==null)
		{
			Reporter.log("Passed : Member "+memberId+ " successfully deleted from MongoDB Collection ="+myAL.IDX_METADATA_HISTORY_COLLECTION);
			Reporter.log(" ");
		}
		else
			fail("Member "+memberId+" still exist in MongoDB Collection ="+myAL.IDX_METADATA_HISTORY_COLLECTION);

		return true;
	}
	
	/** 
	 * Method for IDX brand.get API.
	 * @return Response from brand.get.
	 * @throws Exception Code Error
	 */
	public JsonObject brandGET(AppLib myAL, String brandId, Proxy proxy) throws Exception {
			
		String apicall=null;
		JsonObject myResponse=null;
		
		api.setProxy(proxy);
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ BRAND.GET ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/json";
				
		//Generate the API call for member.put.
		apicall = myAL.getApiURL()+"/brand/get?API_KEY="+myAL.getDefaultApiKey()+"&BRAND_ID="+brandId+"&id="+brandId;
				
		//Send member.put POST Request. 
		myResponse = api.getRootObjectsFromResponseBody(api.getRestRequest(apicall, myContentType));
		
		return myResponse;
		
	}

}
