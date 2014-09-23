package com.nbcuni.test.nbcidx.member.put.brands;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/*****************************************************************************
 * NBCIDX Member.Put API - TC5078_MemberDelete_Brand_INTERNALb7b4. Copyright. 
 * Run these internal brand tests only in QA.
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 *****************************************************************************/

public class TC4574_MemberPut_Brand_INTERNALb7b4 {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	JsonObject response=null;
	int responseCode;
	
	/**
	 * Instantiate the TestNG Before Class Method.
	 */
	@BeforeClass(alwaysRun = true)
	@Parameters("Environment")
	public void startEnvironment(String sEnv) {
		try {
			cs = null;
			al = new AppLib(cs);
			al.setEnvironmentInfo(sEnv);
			proxy = al.getHttpProxy();
			mydb = al.getMongoDbConnection();
			api = new API();
			api.setProxy(proxy);
			ma = new MemberAPIs();
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
    @AfterClass(alwaysRun = true)
    public void stopEnvironment() {
    	try {
        al.closeMongoDbConnection();
    	} catch (Exception e) {
			fail(e.toString());
			}
    }
	
	/**
     * Instantiate the TestNG Test Method.
     */
	@Test(groups = {"QA_Internal"})
	public void memberPut_INTERNALb7b4() throws Exception {
				
		Reporter.log("1) Validating member.put with brand = INTERNAL b7b4");
		
		String username = al.getCurrentTimestamp();
				
		//Generate the JSON Body for POST 
		String jsonBody ="{'username': '"+username+"','suffix': 'Phd','phone': [{'mobile': true,'number': '3053496333','primary': false},{'number': '4096789304'}],'prefix': 'Sir','brand_data': {'Mydata': 'Any-Brand'},'avatar': 'http://www.nbc.com/','address': [{'address1': '150','address2': 'Jackson Avenue','city': 'Parlin','state': 'NJ','postalcode': '08859','country': 'US','primary': true,'type': 'home'}],'screenname': 'Captain'}";
		
		// Get b7b4 Brand Id
		String internalBrandId = "6a783166489e4735a425dddd0a38b7b4";
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, internalBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		String sApiUUID = sUUID.substring(1, sUUID.length()-1);
		
		//Get the DB Response 		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating brand_data in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", username);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.status.value", 1);
		fields.put("metadata.username.value", 1);	
		fields.put("metadata.password.value", 1);
		fields.put("metadata.suffix.value", 1);
		fields.put("metadata.prefix.value", 1);
		fields.put("metadata.screenname.value", 1);
		fields.put("metadata.brand_data.value", 1);
				
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		String dbUUID =  DbObj.get("_id").toString();
		String dbresponse = DbObj.toString();				
	
		boolean bMembername = dbresponse.contains(username);
		Assert.assertEquals(bMembername, true);
		Assert.assertEquals(sApiUUID, dbUUID);
		Reporter.log("Passed : Member = " +username+ " present in Mongo Database with UUID = " +dbUUID);
			
		JsonObject DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		
		// Checking brand_data
		ArrayList<HashMap<String, String>> brandDataArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"brand_data"));
		boolean bMemberBrandData = brandDataArrlst.get(0).toString().contains("Mydata:Any-Brand");
		Reporter.log(brandDataArrlst.get(0).toString());
		Assert.assertEquals(bMemberBrandData, true);
		Reporter.log("Passed : Latest 'brand_data' of Member = " +username+ " in MongoDB is = " +brandDataArrlst.get(0).toString());
		Reporter.log("");

		Reporter.log("************************************************************************************************************************************************************");
		Reporter.log("");
		Reporter.log("2) Updating fields for member "+username+ " using member.put API");
		//Updating fields for member
		jsonBody = "";
		jsonBody ="{'_id': '"+sApiUUID+"','suffix': 'MS','phone': [{'mobile': true,'number': '8884444333'}],'prefix': 'Dr.','brand_data': {'MyChoice': 'INTERNAL b7b4'},'address': [{'address1': '1070 Morris Ave', 'address2': 'Apt # 1254','city': 'Union','state': 'NJ','postalcode': '07083','country': 'US','primary': true,'type': 'home'}],'avatar': 'http://www.mbc.com/','screenname': 'Master'}";
				
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, internalBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating fields updated in MongoDB");
		Reporter.log("");
				
		DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		
		// Checking brand_data
		brandDataArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"brand_data"));
		bMemberBrandData = brandDataArrlst.get(0).toString().contains("MyChoice:INTERNAL b7b4");
		Reporter.log(brandDataArrlst.get(0).toString());
		Assert.assertEquals(bMemberBrandData, true);
		Reporter.log("3) Passed : Latest 'brand_data' of Member = " +username+ " in MongoDB is = " +brandDataArrlst.get(0).toString());
		Reporter.log("");
		
		// Checking Suffix
		ArrayList<HashMap<String, String>> suffixArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"suffix"));
		boolean bMemberSuffix = suffixArrlst.get(0).toString().contains("MS");
		Assert.assertEquals(bMemberSuffix, true);
		Reporter.log("4) Passed : Latest 'suffix' of Member = " +username+ " in MongoDB is "+suffixArrlst.get(0).toString());
		Reporter.log("");
				
		// Checking Prefix
		ArrayList<HashMap<String, String>> prefixArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"prefix"));
		boolean bMemberPrefix = prefixArrlst.get(0).toString().contains("Dr.");
		Assert.assertEquals(bMemberPrefix, true);
		Reporter.log("5) Passed : Latest 'prefix' of Member = " +username+ " in MongoDB is "+prefixArrlst.get(0).toString());
		Reporter.log("");
		
		// Checking Screenname
		ArrayList<HashMap<String, String>> screennameArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"screenname"));
		boolean bMemberScreenname = screennameArrlst.get(0).toString().contains("Master");
		Assert.assertEquals(bMemberScreenname, true);
		Reporter.log("6) Passed : Latest 'screenname' of Member = " +username+ " in MongoDB is "+screennameArrlst.get(0).toString());
		Reporter.log("");
		
		//Removing the member from IDX
		Reporter.log("7) Removing the member from IDX INTERNALb7b4 brand");
		jsonBody= "id="+sApiUUID;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, internalBrandId, username, sApiUUID, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
	}
}
