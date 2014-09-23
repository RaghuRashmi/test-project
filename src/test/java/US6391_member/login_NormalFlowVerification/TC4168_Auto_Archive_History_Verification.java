package US6391_member.login_NormalFlowVerification;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

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

public class TC4168_Auto_Archive_History_Verification {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	private static String randomMemberName = "";
	private static String randomPassword = "";
	private static String randomEmail= "";
	private static String uuid="";
	
	/**
	 * Instantiate the TestNG Before Class Method.
	 * 
	 * @param sEnv - environment
	 * @throws Exception - error
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
	
	/**
     * Instantiate the TestNG After Class Method.
     * 
     * @throws Exception - error
     */
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
     * 
     * @throws Exception - error
     */

	@Test(groups = {"full"})
	public void verifyHistoryCollection() throws Exception {

		String timeStamp = al.getCurrentTimestamp();
		randomMemberName = "test_"+timeStamp;
		randomPassword = timeStamp;
		randomEmail = "test"+timeStamp+"@gmail.com";
		
		Reporter.log(" ");
		Reporter.log("1) Creating Random Member");
		Reporter.log(" ");
		
		Reporter.log("Random Member Created with below information =>");
		Reporter.log(" ");
		Reporter.log("Member Name : " + randomMemberName);
		Reporter.log("Password : " + randomPassword);
		Reporter.log("Email-Id : " + randomEmail);
		Reporter.log("");
		Reporter.log("--X--");
				
		Reporter.log(" ");
		Reporter.log("2) member.PUT");
		Reporter.log(" ");

		//Generate the JSON Body for POST 
		String myJsonBody ="{'username':'"+randomMemberName+"','password':'"+randomPassword+"','email': { 'address': '"+randomEmail+"', 'verified': true } }";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, myJsonBody, surfBrandId);
				
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		String sApiUUID = sUUID.substring(1, sUUID.length()-1);
		uuid = sApiUUID;
		System.out.println("UUID : "+sApiUUID);
			
		//Get the DB Response	
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Member present in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", randomMemberName);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);	
		
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		String dbUUID =  DbObj.get("_id").toString();
		String dbresponse = DbObj.toString();				

		boolean bMembername = dbresponse.contains(randomMemberName);
		Assert.assertEquals(bMembername, true);
		Assert.assertEquals(sApiUUID, dbUUID);
		Reporter.log("Passed : Member = " +randomMemberName+ " present in Mongo Database with UUID = " +dbUUID);
		Reporter.log("");
		Reporter.log("--X--");
		Reporter.log("");		
		
		int idx_metadata_history_count = 0;
		int count =0 ;
			
		BasicDBObject idx_metadata_history_searchQuery = new BasicDBObject();
		idx_metadata_history_searchQuery.put("category","Member");
		idx_metadata_history_searchQuery.put("category_id",uuid);
		idx_metadata_history_searchQuery.put("field","_logged_in");
	
		for(int pass = 0; pass <3 ; pass++)
		{
			Reporter.log("");
			Reporter.log("==================================================================================================================================================");
			Reporter.log((pass+3)+") Logging-in and Logging-out the member "+randomMemberName+ " Pass "+(pass+1));
			Reporter.log("==================================================================================================================================================");
			Reporter.log("");
			
			myJsonBody ="id="+randomEmail+"&password="+randomPassword;
			JsonObject mylogin = ma.memberLOGINResponse(api, al, myJsonBody, surfBrandId, randomMemberName, mydb);
			if(pass!=0)
				idx_metadata_history_count++;
			
			Reporter.log("Validating collection 'idx_metadata_history' for 'metadata/_logged_in/' object"); 
			count = al.getCountFromMongoDbResponse(mydb, idx_metadata_history_searchQuery, al.IDX_METADATA_HISTORY_COLLECTION);
			Reporter.log("");
			if(count!=idx_metadata_history_count)
				fail("'idx_metadata_history' collection has wrong number of records for 'metadata/_logged_in/', Expected count: "+idx_metadata_history_count +", Actual count :"+count);
			else
				Reporter.log("Passed : 'idx_metadata_history' collection has valid number of records for 'metadata/_logged_in/', Expected count: "+idx_metadata_history_count +", Actual count :"+count);
			Reporter.log("");
							
			// logout the member using same brand id.
			myJsonBody = "";
			myJsonBody ="id="+randomEmail;
			JsonObject mylogout = ma.memberLOGOUTResponse(api, al, myJsonBody, surfBrandId, randomMemberName, mydb);
			idx_metadata_history_count++;
			
			Reporter.log("Validating collection 'idx_metadata_history' for 'metadata/_logged_in/' object"); 
			count = al.getCountFromMongoDbResponse(mydb, idx_metadata_history_searchQuery, al.IDX_METADATA_HISTORY_COLLECTION);
			Reporter.log("");
			if(count!=idx_metadata_history_count)
				fail("'idx_metadata_history' collection has wrong number of records for 'metadata/_logged_in/', Expected count: "+idx_metadata_history_count +", Actual count :"+count);
			else 
				Reporter.log("Passed : 'idx_metadata_history' collection has valid number of records for 'metadata/_logged_in/', Expected count: "+idx_metadata_history_count +", Actual count :"+count);
			Reporter.log("");
			Reporter.log("--X--");
			Reporter.log("");
			
		}		
		//Send member.remove POST Request 
		Reporter.log("6) member.remove to remove member from IDX");
		myJsonBody= "id="+uuid;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, randomMemberName, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
		Reporter.log("--X--");
		Reporter.log("");
	}
}
