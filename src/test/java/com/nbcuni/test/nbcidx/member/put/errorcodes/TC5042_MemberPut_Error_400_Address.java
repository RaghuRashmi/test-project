package com.nbcuni.test.nbcidx.member.put.errorcodes;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Put API - TC5042_MemberPut_Error_400_Address. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5042_MemberPut_Error_400_Address {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private Proxy proxy=null;

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
			ma = new MemberAPIs();
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
	/**
     * Instantiate the TestNG Test Method.
     */
	@Test(groups = {"full"})
	public void memberPut_error400_address() throws Exception {
		
		// Create Random Member
		String UUID=TC01_Create_RandomMemberInfo.sfinalUUID;
		
		Reporter.log("1) Validating member.put 400 error code for 'address' field by setting invalid country code");
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Generate the JSON Body for POST 
		String jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address': [{'address1': '150','address2': 'Jackson Avenue','city': 'Parlin','state': 'NJ','postalcode': '08859','country': 'ZZ','primary': true,'type': 'home'}]}";
		
		int responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("2) Validating member.put 400 error code for 'address' field by setting invalid state when country is US.");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address': [{'address1': '150','address2': 'Jackson Avenue','city': 'Parlin','state': 'NJJ','postalcode': '08859','country': 'US','primary': true,'type': 'home'}]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("3) Validating member.put 400 error code for 'address' field by setting invalid state when country is US");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address':[{'state': 'CZ','postalcode': '01234','country': 'US','primary': true,'type': 'home'}]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("4) Validating member.put 400 error code for 'address' field by setting invalid postal code when country is US");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address':[{'country': 'US','postalcode': '900567'}]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("5) Validating member.put 400 error code for 'address' field by setting invalid postal code when country is US");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address':[{'country': 'US','postalcode': '900567-2864'}]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("6) Validating member.put 400 error code for 'address' field by setting invalid postal code with state as NJ and country is US");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address':[{'postalcode': '0708','state': 'NJ','country': 'US'}]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("7) Validating member.put 400 error code for 'address' field by  setting invalid value of primary in the address field");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address':[{'state': 'CZ','postalcode': '01234','country': 'US','primary': truee,'type': 'home'}]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("8) Validating member.put 400 error code for 'address' field by setting invalid value of type in the address field");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address':[{'state': 'CZ','postalcode': '01234','country': 'US','primary': true,'type': home}]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("9) Validating member.put 400 error code for 'address' field by setting empty address object in the address field:");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address':[{}]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("10 Validating member.put 400 error code for 'address' field by setting empty address object in the address field");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address':[]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("11) Validating member.put 400 error code for 'address' field by attempting to set two primary addresses");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','address':[{'address1': '100 Universal City Plaza','address2': 'Bldg 1440','city': 'Universal City','state': 'CA','postalcode': '91608-1111','country': 'US','primary': false,'type': 'business'},{'address1': '200 Universal City Plaza','address2': 'Bldg 1550','city': 'Universal City','state': 'RI','postalcode': '01321-2222','country': 'US','primary': true,'type': 'home'},{'address1': '300 Universal City Plaza','address2': 'Bldg 1660','city': 'Harwood Heights','state': 'IL','postalcode': '60706-3333','country': 'US','primary': true,'type': 'home'}]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("12) Validating member.put 400 error code for 'address' field by attempting to set invalid 'Address' field");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','Address':[{'address1': '100 Universal City Plaza','address2': 'Bldg 1440','city': 'Universal City','state': 'CA','postalcode': '91608-1111','country': 'US','primary': false,'type': 'business'}]}";
							
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		
	}
}
