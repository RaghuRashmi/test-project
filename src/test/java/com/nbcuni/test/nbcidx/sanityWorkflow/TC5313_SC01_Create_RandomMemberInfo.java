package com.nbcuni.test.nbcidx.sanityWorkflow;

import org.testng.Reporter;

import org.testng.annotations.Test;

import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Sanity Test Cases - SC01_Create_RandomMemberInfo. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Feb 7, 2014
 **************************************************************************/

public class TC5313_SC01_Create_RandomMemberInfo {

	private CustomWebDriver cs;
	private AppLib al;
		
	public static String randomMemberName = "";
	public static  String randomPassword = "";
	public static  String randomEmail= "";
	
	/**
     * Instantiate the TestNG Test Method.
     * 
     * @throws Exception - error
     */
	@Test(groups = {"sanity","API"})
	public void createRandomMember() throws Exception {
	
		cs = null;
		al = new AppLib(cs);
	
		String timeStamp = al.getCurrentTimestamp();
		randomMemberName = "test_"+timeStamp;
		randomPassword = timeStamp;
		randomEmail = "test"+timeStamp+"@gmail.com";
	
		Reporter.log(" ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Creating Random Member for Sanity Test ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log(" ");
	
		Reporter.log("Random Member Created with below information =>");
		Reporter.log(" ");
		Reporter.log("Member Name : " + randomMemberName);
		Reporter.log("Password : " + randomPassword);
		Reporter.log("Email-Id : " + randomEmail);	
		Reporter.log(" ");
	}
}

