package US6391_member.login.HistoricLoginDataVerification;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.util.Date;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoInternalException;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

public class TC01_Create_Broken_Record_LoginData {
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
    CommandResult result;
    
	public static String uuid;	
	
	/**
	 * Instantiate the TestNG Before Class Method.
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
			api = new API();
			api.setProxy(proxy);
			ma = new MemberAPIs();
			mydb = al.getMongoDbConnection();
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
	/**
     * Instantiate the TestNG After Class Method.
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
     * @throws Exception - error
     */
	@Test(groups = {"util"})
	public void create_broken_record() throws Exception {
		
		Date now = new Date();
		Reporter.log("now="+now.toString());
	
		
		String timeStamp = al.getCurrentTimestamp();
		String randomMemberName = "test_"+timeStamp;
		String randomPassword = timeStamp;
		String randomEmail = "test"+timeStamp+"@gmail.com";
		
		
		//Generate basic member
		String username = al.getCurrentTimestamp();
		String jsonBody= "{'username':'"+randomMemberName+"','password':'"+randomPassword+"','email': { 'address': '"+randomEmail+"', 'verified': true } }";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		Reporter.log(response.toString());
		
		//myJsonBody ="id="+randomEmail+"&password="+randomPassword;
		//JsonObject mylogin = ma.memberLogin(al, myJsonBody, randomMemberName, proxy, mydb);
		
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		uuid = id.toString();
		String sApiUUID = uuid.substring(1, uuid.length()-1);
		System.out.println("UUID = "+sApiUUID);
		
				
		//Find and display from MongoDB
		DBCollection myCollection = mydb.getCollection(al.IDX_MEMBERS_COLLECTION);
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", sApiUUID);
				

		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, null, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		//update brands array[];
		//DBObject updateBrandObj = new BasicDBObject("brands.1", "90a1118e76924841b6e4f5aafcdf4e54");
		//myCollection.update(searchQuery,  new BasicDBObject("$set", updateBrandObj));
		
						
		DBObject _last_login_data = new BasicDBObject();
		_last_login_data.put("attribution" , "user");
		_last_login_data.put("derivation", "declared");
		_last_login_data.put("brand_id", "5876e8579c2f422e99b56d8d0567d346");
		_last_login_data.put("site_id", null);
		_last_login_data.put("subsource", "form_registration");
		_last_login_data.put("source", "online_action");
		_last_login_data.put("custom_tags", null);
		_last_login_data.put("modality" , "descriptive");
		
	    
	    DBObject _logged_in_data = new BasicDBObject();
	    _logged_in_data.put("attribution" , "user");
	    _logged_in_data.put("derivation", "declared");
	    _logged_in_data.put("brand_id", "5876e8579c2f422e99b56d8d0567d346");
	    _logged_in_data.put("site_id", null);
	    _logged_in_data.put("subsource", "form_registration");
	    _logged_in_data.put("source", "online_action");
	    _logged_in_data.put("custom_tags", null);
	    _logged_in_data.put("modality" , "descriptive");
	    
		//Update barnd_data metadata.
	    
	    Date prev = new Date();
	    prev = now;
	    int i,j=0;
		double bsonSize, size_mb=0.0d;
		do {	
	    	j++;
	    	for(i=1; i<=1650; i++)
			{
				prev.setTime(prev.getTime() + 2000);
				_last_login_data.put("value", prev);
				_last_login_data.put("_created", prev);
				
			    	
			    DBObject updateObj = new BasicDBObject("metadata._last_login", _last_login_data);
			    myCollection.update(searchQuery, new BasicDBObject("$push", updateObj));
			    
							   
			    prev.setTime(prev.getTime() + 2000);
			    _logged_in_data.put("value", true);
			    _logged_in_data.put("_created", prev);
			  
			    
			    updateObj = new BasicDBObject("metadata._logged_in", _logged_in_data);
			    myCollection.update(searchQuery, new BasicDBObject("$push", updateObj));
			    
			}
			
			DbObj = getMongoDbResponse(mydb, searchQuery, null, al.IDX_MEMBERS_COLLECTION);	
			if(DbObj==null)
				fail("MongoDB Cursor is empty. No Data found from Database");
						
			try
			{ 
				int noRecords = (((i-1)*2)*j)+1;
				System.out.println("Number of records in _logged_in till now = "+noRecords);
				Reporter.log("Number of records in _logged_in till now = "+noRecords);
				
				result = mydb.doEval("Object.bsonsize("+DbObj+")");
				bsonSize = (Double) result.get("retval");
			    size_mb = ((bsonSize/1024.0)/1024.0);
			    Reporter.log("Size of member in MB = "+size_mb);
			    System.out.println("Size of member in MB = "+size_mb);
			    
			 }catch(MongoInternalException e){
			     System.out.println("Exception thrown  :" + e);
			     Reporter.log("Exception thrown  :" + e);
			     System.out.println("Maximum size of 16 MB is reached");
				 Reporter.log("Maximum size of 16 MB is reached");
				 size_mb = 16.0d;
			}					    
	    }while (size_mb<16.0d);
	    
	    //Send member.login POST Request 
		jsonBody ="";
		jsonBody ="id="+randomEmail+"&password="+randomPassword;
		JsonObject mylogin = ma.memberLOGINResponse(api, al, jsonBody, surfBrandId, randomMemberName, mydb);
		
		//Send member.remove POST Request 
		jsonBody= "id="+uuid;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, surfBrandId, randomMemberName, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);		
	}
	
	public DBObject getMongoDbResponse(DB myDB, BasicDBObject mySearchQuery, BasicDBObject myFields, String myCollectionName) throws Exception {

		DBObject myDbObj = null;
		DBCollection myCollection = myDB.getCollection(myCollectionName);

		// Query MongoDB to get specific fields
		myDbObj = myCollection.findOne(mySearchQuery, myFields);
		if(myDbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
			
		return myDbObj;
	}
}
