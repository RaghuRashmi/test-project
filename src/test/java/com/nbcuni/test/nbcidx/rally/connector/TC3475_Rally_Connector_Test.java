package com.nbcuni.test.nbcidx.rally.connector;

import static org.testng.AssertJUnit.fail;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.webdriver.CustomWebDriver;

public class TC3475_Rally_Connector_Test {


		private CustomWebDriver cs;
		private AppLib al;
		
		public static String randomMemberName = "";
		public static String randomPassword = "";
		public static String randomEmail= "";
		public static String uuid="";
		
		JsonObject response=null;
		
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
		public void create_Random_Member() throws Exception {
		
		
		String timeStamp = al.getCurrentTimestamp();
		randomMemberName = "test_"+timeStamp;
		randomPassword = timeStamp;
		randomEmail = "test"+timeStamp+"@gmail.com";
		
		Reporter.log(" ");
		Reporter.log("/************************ Creating Random Member ********************************/");
		Reporter.log(" ");
		
		Reporter.log("Random Member Created with below information =>");
		Reporter.log(" ");
		Reporter.log("Member Name : " + randomMemberName);
		Reporter.log("Password : " + randomPassword);
		Reporter.log("Email-Id : " + randomEmail);

	}
}

	
