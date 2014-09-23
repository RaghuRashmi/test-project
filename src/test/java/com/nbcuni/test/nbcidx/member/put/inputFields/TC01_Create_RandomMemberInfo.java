package com.nbcuni.test.nbcidx.member.put.inputFields;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.text.SimpleDateFormat;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ibm.icu.util.Calendar;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Put API - TC01_Create_RandomMemberInfo. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC01_Create_RandomMemberInfo {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private Proxy proxy=null;
	
	public static String randomMemberUserName = "";
	public static String randomPassword = "";
	public static String randomMemberFirstName = "";
	public static String randomMemberMiddleName = "";
	public static String randomMemberLastName = "";
	public static String randomMemberScreenName = "";
	public static String randomEmail= "";
	public static String gender = "";
	public static int birthdateDay = 0;
	public static int birthdateMonth = 0;
	public static int birthdateYear = 0;
	public static String sfinalUUID="";
	public static String randomPhone= "";


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
			api = new API();
			api.setProxy(proxy);
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
	/**
     * Instantiate the TestNG Test Method.
     */
	@Test(groups = {"full"})
	public void createRandomMember() throws Exception {
		
		Reporter.log(" ");
		Reporter.log("/************************ Gerating Random Member Info ********************************/");
		Reporter.log(" ");
			
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		randomMemberUserName = "test_"+timeStamp;
		randomPassword = timeStamp;
		randomEmail = "test_"+timeStamp+"@gmail.com";
		randomMemberFirstName = "test_"+timeStamp+"_First";
		randomMemberMiddleName = "test_"+timeStamp+"_Middle";
		randomMemberLastName = "test_"+timeStamp+"_Last";
		randomMemberScreenName = "test_"+timeStamp+"_Screen";
		birthdateDay = 4;
		birthdateMonth = 10;
		birthdateYear = 1984;
		gender = "f";
		randomPhone = "2015678947";
			
		Reporter.log("Random Member will be generated with below information =>");
		Reporter.log("Member User Name : " + randomMemberUserName);
		Reporter.log("Password : " + randomPassword);
		Reporter.log("Email-Id : " + randomEmail);
		Reporter.log("Member First Name : " + randomMemberFirstName);
		Reporter.log("Member Middle Name : " + randomMemberMiddleName);
		Reporter.log("Member Last Name : " + randomMemberLastName);
		Reporter.log("BirthDate : " + birthdateDay+"/"+birthdateMonth+"/"+birthdateYear);
		Reporter.log("Gender : " + gender);
		Reporter.log("Member Screen Name : " + randomMemberScreenName);
		Reporter.log("Member Phone Number : " + randomPhone);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
	}
}
