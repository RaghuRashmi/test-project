package US7669_HistoricBrandDataVerification;

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

public class TC01_Create_Broken_Record_BrandData {
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	private CommandResult result;
    private static String uuid;	
	private static String username;
	
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
	
		//Generate basic member
		username = al.getCurrentTimestamp();
		String jsonBody= "{'status' : 'active','gender' : 'f','firstname' : 'broken_member','lastname' : 'broken','username' : '"+username+"', 'brand_data' : [{'attribution' : 'user','derivation' : 'declared','brand_id' : '5876e8579c2f422e99b56d8d0567d346','site_id' : 'eb2a272d33f049b3aff749e5d045c236','subsource' : 'form_registration','value' : {'custom_data_1' : null},'source' : 'online_action','custom_tags' : null,'modality' : 'descriptive'}]}";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		Reporter.log(response.toString());
				
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
		DBObject updateBrandObj = new BasicDBObject("brands.1", "90a1118e76924841b6e4f5aafcdf4e54");
		myCollection.update(searchQuery,  new BasicDBObject("$set", updateBrandObj));
								
		DBObject Surf_brand_data = new BasicDBObject();
		Surf_brand_data.put("attribution" , "user");
	    Surf_brand_data.put("derivation", "declared");
	    Surf_brand_data.put("brand_id", "5876e8579c2f422e99b56d8d0567d346");
	    Surf_brand_data.put("site_id", "eb2a272d33f049b3aff749e5d045c236");
	    Surf_brand_data.put("subsource", "form_registration");
	    Surf_brand_data.put("value", new BasicDBObject("Surf_Example_custom_data",null));
	    Surf_brand_data.put("source", "online_action");
	    Surf_brand_data.put("custom_tags", null);
			    
	    DBObject API_Explorer_brand_data = new BasicDBObject();
	    API_Explorer_brand_data.put("attribution" , "user");
	    API_Explorer_brand_data.put("derivation", "declared");
	    API_Explorer_brand_data.put("brand_id", "90a1118e76924841b6e4f5aafcdf4e54");
	    API_Explorer_brand_data.put("site_id", null);
	    API_Explorer_brand_data.put("subsource", "form_registration");
	    API_Explorer_brand_data.put("value", new BasicDBObject("API_Explorer_custom_data",null));
	    API_Explorer_brand_data.put("source", "online_action");
	    API_Explorer_brand_data.put("custom_tags", null);
	    
		//Update barnd_data metadata.
	    Date prev = new Date();
	    prev = now;
	    int i,j=0;
		double bsonSize, size_mb=0.0d;
	    do
	    {	
	    	j++;
	    	for(i=1; i<=1650; i++)
			{
				prev.setTime(prev.getTime() - 4000);
				Surf_brand_data.put("_created", prev);
			    Surf_brand_data.put("modality" , "descriptive");
			    	
			    DBObject updateObj = new BasicDBObject("metadata.brand_data", Surf_brand_data);
			    myCollection.update(searchQuery, new BasicDBObject("$push", updateObj));
			   							   
			    prev.setTime(prev.getTime() - 4000);
				API_Explorer_brand_data.put("_created", prev);
			    API_Explorer_brand_data.put("modality" , "descriptive");
			    
			    updateObj = new BasicDBObject("metadata.brand_data", API_Explorer_brand_data);
			    myCollection.update(searchQuery, new BasicDBObject("$push", updateObj));
			 }
			DbObj = getMongoDbResponse(mydb, searchQuery, null, al.IDX_MEMBERS_COLLECTION);	
			if(DbObj==null)
				fail("MongoDB Cursor is empty. No Data found from Database");
						
			try
			{ 
				int noRecords = (((i-1)*2)*j)+1;
				System.out.println("Number of records in brand_data till now = "+noRecords);
				Reporter.log("Number of records in brand_data till now = "+noRecords);
				
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
	    
		//Send member.remove POST Request 
		String myJsonBody= "id="+uuid;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, username, uuid, mydb);
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
