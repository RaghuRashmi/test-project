/* TEST UTILITY - not part of user story */

package US7669_HistoricBrandDataVerification;

import static org.testng.AssertJUnit.fail;
import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

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

public class Print_Member {
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
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
	@Test(groups = {"util1"})
	public void print_member() throws Exception {
		
		//Find and display from MongoDB
		DBCollection myCollection = mydb.getCollection(al.IDX_MEMBERS_COLLECTION);
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", "01beda39b4b04e9a8c0c8eefca617886");
		
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.brand_data", 1);

		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, null, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
		fail("MongoDB Cursor is empty. No Data found from Database");
				
		try
		{  
		CommandResult result = mydb.doEval("Object.bsonsize("+DbObj+")");
	    double bsonSize = (Double) result.get("retval");
	    System.out.println(bsonSize);
	    double size_mb = ((bsonSize/1024.0)/1024.0);
	    System.out.println(size_mb);
		}catch(MongoInternalException e){
		     System.out.println("Exception thrown  :" + e);
		     Reporter.log("Exception thrown  :" + e);
		     System.out.println("Maximum size of 16 MB is reached");
			 Reporter.log("Maximum size of 16 MB is reached");
		 }
	}
}

