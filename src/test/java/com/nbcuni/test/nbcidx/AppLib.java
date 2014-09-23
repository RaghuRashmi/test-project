package com.nbcuni.test.nbcidx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;

//import com.entrib.mongo2gson.Mongo2gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.ibm.icu.util.Calendar;

import static org.testng.AssertJUnit.fail;

import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;

import com.nbcuni.test.lib.Util;
import com.nbcuni.test.webdriver.CustomWebDriver;
import com.nbcuni.test.webdriver.CustomWebDriverException;
import com.nbcuni.test.webdriver.Utilities;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**************************************************************************
 * NBCIDX Application Library. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: March 04, 2014
 **************************************************************************/

public class AppLib {
	private CustomWebDriver custWebDr = null;
	private final Util ul;
	private String environment = "";
	private String apiUrl = "";
	private String apiLegacyUrl = "";
	private String defaultApiKey = "";
	private String surfBrandId = "";
	private String apiExplBrandId = "";
	private String proxyUrl = "";
	private Integer proxyPort = 0;
	private String dbUserid = "";
	private String dbPassword = "";
	private String dbServer = "";
	private Integer dbPort = 0;
	private String dbDBName = "";
	private MongoClient mongo = null;  // Since 2.10.0, uses MongoClient
	
	public String IDX_MEMBERS_COLLECTION = "idx_members";
	public String IDX_BRANDS_COLLECTION = "idx_brands";
	public String IDX_CLIENTS_COLLECTION= "idx_clients";
	public String IDX_USERS_COLLECTION= "idx_users";
	public String IDX_METADATA_HISTORY_COLLECTION = "idx_metadata_history";	
	
	public String FIND = "find";
	public String FINDONE = "findone";

	private static String configFileName = "src" + File.separator + "test" + File.separator + "resources"
			+ File.separator + "config.properties";
	private static Properties configProperties = null;
	private static boolean isConfigLoaded = false;
	
	public int HTTP_OK = 200;
	public int HTTP_NOTFOUND = 404;
	
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	
	private String socialUsername = "";
	private String socialPassword = "";
	private String socialValidEmail = "";
	
	private String facebookUID = "";
	private String facebookProviderUID = "";
	
	private String linkedInUID = "";	
	private String linkedInProviderUID = "";
	
	private String googlePlusUID = "";
	private String googlePlusProviderUID = "";
	
	private String twitterUID = "";
	private String twitterProviderUID = "";
	
	private String yahooUID = "";
	private String yahooProviderUID = "";
	
	/**
	 * @param cs1 Custom Webdriver
	 */
	public AppLib(CustomWebDriver cs1) {
		custWebDr = cs1;
		ul = new Util(custWebDr);
		//wait = new WebDriverWait(custWebDr, 15);
		try {
			loadConfig();
		} catch (Exception e) {
			new CustomWebDriverException(e, custWebDr);
		}
	}

	/**
	 * loads config file
	 */
	private void loadConfig() {
		try {
			if (!isConfigLoaded) {
				File configFile = new File(configFileName);
				configProperties = new Properties();
				InputStream input = null;
				if (configFile.exists()) {
					try {
						input = new FileInputStream(configFile);
						configProperties.load(input);
					} catch (FileNotFoundException e) {
						Utilities.logSevereMessage("The config.properties file was not found in this location: " + configFileName);
					} finally {
						if (input != null) {
							input.close();
						}
					}
					isConfigLoaded = true;
				} else {
					Utilities.logSevereMessage("The config.properties file " + configFileName + " does not exist in this location: " + System.getProperty("user.dir"));
				}
			}
		} catch (Exception e) {
			new CustomWebDriverException(e, custWebDr);
		}
	}

	/**
	 * gets env
	 */
	private void setEnvironmentFromConfig() {
		try {
			apiUrl = configProperties.getProperty(environment + ".API.Url");
			apiLegacyUrl = configProperties.getProperty(environment + ".API.Legacy.Url");
			defaultApiKey  = configProperties.getProperty(environment + ".Default.API_KEY");
			surfBrandId = configProperties.getProperty(environment + ".SURF.BrandId");
			apiExplBrandId = configProperties.getProperty(environment + ".APIEXPL.BrandId");
			proxyUrl = configProperties.getProperty(environment + ".ProxyURL");
			proxyPort = Integer.valueOf(configProperties.getProperty(environment + ".ProxyPort"));
			dbUserid  = configProperties.getProperty(environment + ".DB.Userid");
			dbPassword  = configProperties.getProperty(environment + ".DB.Password");
			dbServer  = configProperties.getProperty(environment + ".DB.Server");
			dbPort  = Integer.valueOf(configProperties.getProperty(environment + ".DB.Port"));
			dbDBName  = configProperties.getProperty(environment + ".DB.DBName");
			
			socialUsername  = configProperties.getProperty("Social.Username");
			socialPassword  = configProperties.getProperty("Social.Password");
			socialValidEmail  = configProperties.getProperty("Social.Email-Id");
			facebookUID  = configProperties.getProperty("Facebook_UID");
			facebookProviderUID  = configProperties.getProperty("Facebook_ProviderUID");
			linkedInUID = configProperties.getProperty("LinkedIn_UID");
			linkedInProviderUID = configProperties.getProperty("LinkedIn_ProviderUID");
			googlePlusUID = configProperties.getProperty("GooglePlus_UID");
			googlePlusProviderUID = configProperties.getProperty("GooglePlus_ProviderUID");
			twitterUID = configProperties.getProperty("Twitter_UID");
			twitterProviderUID = configProperties.getProperty("Twitter_ProviderUID");
			yahooUID = configProperties.getProperty("Yahoo_UID");
			yahooProviderUID = configProperties.getProperty("Yahoo_ProviderUID");
			} catch (Exception e) {
			new CustomWebDriverException(e, custWebDr);
		}
	}
	
	/**
	 * Get all Social Login User Info
	 */	
	public final String getSocialUsername() throws Exception {
		return this.socialUsername;
	}
	public final String getSocialPassword() throws Exception {
		return this.socialPassword;
	}
	public final String getSocialValidEmail() throws Exception {
		return this.socialValidEmail;
	}
	
	/**
	 * Get all Social Login User Gigya IDs
	 */	
	public final String getFacebookUID() throws Exception {
		return this.facebookUID;
	}
	public final String getFacebookProviderUID() throws Exception {
		return this.facebookProviderUID;
	}
	public final String getLinkedInUID() throws Exception {
		return this.linkedInUID;
	}
	public final String getLinkedInProviderUID() throws Exception {
		return this.linkedInProviderUID;
	}
	public final String getGooglePlusUID() throws Exception {
		return this.googlePlusUID;
	}
	public final String getGooglePlusProviderUID() throws Exception {
		return this.googlePlusProviderUID;
	}
	public final String getTwitterUID() throws Exception {
		return this.twitterUID;
	}
	public final String getTwitterProviderUID() throws Exception {
		return this.twitterProviderUID;
	}
	public final String getYahooUID() throws Exception {
		return this.yahooUID;
	}
	public final String getYahooProviderUID() throws Exception {
		return this.yahooProviderUID;
	}


	/* Returns the API URL e.g. http://sys04-media.msnbc.msn.com. */
	public final String getApiURL() throws Exception {
		return this.apiUrl;
	}

	/* Returns the Legacy API URL e.g. http://sys04-media.msnbc.msn.com. */
	public final String getApiLegacyURL() throws Exception {
		return this.apiLegacyUrl;
	}

	/* Returns the Default API Key. */
	public final String getDefaultApiKey() throws Exception {
		return this.defaultApiKey;
	}

	/* Returns the SURF Brand Id. */
	public final String getSurfBrandId() throws Exception {
		return this.surfBrandId;
	}
	
	/* Returns the API Explorer Brand Id. */
	public final String getAPIExplBrandId() throws Exception {
		return this.apiExplBrandId;
	}
	
	/* Returns the Proxy URL. */
	public final String getHttpProxyURL() throws Exception {
		return this.proxyUrl;
	}
	
	/* Returns the Proxy Port Number. */
    public final String getHttpProxyPort() throws Exception {
			return this.proxyPort.toString();
    }
    
    /* Returns the Database User ID */
	public final String getDbUserid() throws Exception {
		return this.dbUserid;
	}

	/* Returns the Database User Password */
	public final String getDbPassword() throws Exception {
		return this.dbPassword;
	}

	/* Returns the Database Server Address */
	public final String getDbServer() throws Exception {
		return this.dbServer;
	}
	
	/* Returns the Database Port Number */
	public final Integer getDbPort() throws Exception {
		return this.dbPort;
	}
	
	/* Returns the Database Name. */
	public final String getDbDBName() throws Exception {
		return this.dbDBName;
	}
	
	/* Returns the Proxy Address. */
	public Proxy getHttpProxy() throws Exception {
		SocketAddress addr;
		Proxy httpProxy=null;
		
		if((this.proxyUrl!=null) && (this.proxyPort!=0))
		{
			//Reporter.log("Proxy is enabled, hence all API requests will go via configured Proxy");
			addr = new InetSocketAddress(this.proxyUrl, this.proxyPort);
			httpProxy = new Proxy(Proxy.Type.HTTP, addr);
		}
		else
			Reporter.log("Proxy is disabled, hence all API requests will go without Proxy");

		return httpProxy;
	}
	
	/* Returns the environment being tested. */
	public final String getEnvironment() throws Exception {
		return this.environment;
	}

	/* Sets the environment. */
	public final void setEnvironmentInfo(String sEnv) throws Exception {
		Reporter.log("Setting Environment to: " + sEnv);
		this.environment = sEnv;
		this.setEnvironmentFromConfig();
		this.disableSSLValidation();
	}
	
	/* Returns the MongoDB Database Connection. */
	public DB getMongoDbConnection() throws Exception {

		// Connect to MongoDB 
		try {
			mongo = new MongoClient(this.dbServer, this.dbPort);
			} catch (UnknownHostException e) {
			e.printStackTrace();
			}
	 
		// Get the database 
		DB db = mongo.getDB(this.dbDBName);  // if database doesn't exists, MongoDB will create it for you
		
		// Authenticate the database 
		boolean auth = db.authenticate(this.dbUserid, this.dbPassword.toCharArray());
		if(!auth)
			fail("DB Authentication failed");

		return db;

	}
	
	/* Closes the MongoDB Database connection. */
	public void closeMongoDbConnection() throws Exception {
		try {
            mongo.close();
			} catch (Exception e) {
           fail(e.toString());
			}
	}
		
	/* Returns the MongoDB Response */
	public DBObject getMongoDbResponse(DB myDB, BasicDBObject mySearchQuery, BasicDBObject myFields, String myCollectionName, String myOperation) throws Exception {

		DBObject myDbObj = null;
		DBCollection myCollection = myDB.getCollection(myCollectionName);

		// Query MongoDB to get specific fields
		
		Reporter.log("MongoDB Query :  " + mySearchQuery.toString());
			 	
		if (myOperation == "find")
		{
			DBCursor cursor = myCollection.find(mySearchQuery, myFields);
			if(cursor.hasNext())
			{
				while(cursor.hasNext()) {
					myDbObj = cursor.next();
			        Reporter.log("Got the MongoDB Response : "+myDbObj.toString());
					Reporter.log(" ");
					}  
			}			
			else
				Reporter.log("MongoDB Cursor is empty. No Data found from Database");
		
			cursor.close();	
		}

		if (myOperation == "findone")
		{
			myDbObj = myCollection.findOne(mySearchQuery, myFields);
			if(myDbObj==null)
				Reporter.log("MongoDB Cursor is empty. No Data found from Database");
			else
			{
				Reporter.log("Got the MongoDB Response : "+myDbObj.toString());
				Reporter.log(" ");
			}
		}
		return myDbObj;
	}
	
	/* Returns the count of documents from MongoDB which matched the passed query. */
	public int getCountFromMongoDbResponse(DB myDB, BasicDBObject mySearchQuery, String myCollectionName) throws Exception {

		DBCollection myCollection = myDB.getCollection(myCollectionName);

		// Query MongoDB to get specific fields
		
		Reporter.log("MongoDB Query :  " + mySearchQuery.toString());
			 	
		int count = myCollection.find(mySearchQuery).count();
		
		Reporter.log("Number of records in MongoDB collection "+myCollectionName+", which matched this Query =  " + count);

		return count;
	}

	/* Disables the SSL Validation. */	
	public void disableSSLValidation() throws Exception	{
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
		};

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

	}
		
	public Object getFieldFromCursor(DBObject o, String fieldName) {

	    final String[] fieldParts = StringUtils.split(fieldName, '.');

	    int i = 1;
	    Object val = o.get(fieldParts[0]);

	    while(i < fieldParts.length && val instanceof DBObject) {
	        val = ((DBObject)val).get(fieldParts[i]);
	        i++;
	    }

	    return val;
	}
	
	/* Returns Json Object from MongoDB Object. */
	public JsonObject JsonObjectFromMongoDBObject(DBObject mongodbobj) {
		//Mongo2gson mongo2gson = new Mongo2gson();
		
	    // Now convert the BasicDBObject into JsonObejct
		JsonObject myJsonObject = getAsJsonObject(mongodbobj);
	    
	    return myJsonObject;
    }
		
	/* Returns the Error Code and prints the Error String for specified GET url */
	public int getHTTPResponseCode(String url) throws Exception { 
		URLConnection urlConnection = null;
		JsonReader reader = null;
		int code = 0;
		
		Reporter.log(" ");
		Reporter.log("API Request URL:  " + url);
		Reporter.log(" ");
		
		Proxy proxy = this.getHttpProxy();
		if (proxy!=null)
			urlConnection = new URL(url).openConnection(proxy);
		else
			urlConnection = new URL(url).openConnection();
		
		urlConnection.connect();

		if (urlConnection instanceof HttpURLConnection) {
			HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
			
			code = httpConnection.getResponseCode();
			if (code == 200) 
			{
				Reporter.log("HTTP API call SUCCESS response code of " + code + " returned");
				reader = new JsonReader(new InputStreamReader(httpConnection.getInputStream()));
				JsonParser parser = new JsonParser();
				JsonElement rootElement = parser.parse(reader);
				Reporter.log("RESPONSE BODY:  " + rootElement.toString());
			}
			else
			{
				Reporter.log("HTTP ERROR CODE: " + code);
				reader = new JsonReader(new InputStreamReader(httpConnection.getErrorStream()));
				JsonParser parser = new JsonParser();
				JsonElement rootElement = parser.parse(reader);	
				Reporter.log("RESPONSE BODY:  " + rootElement.toString());
			}			
		} else 
			fail("HTTP API call error - not a valid http request!");
		
		return code;
	}

	/* Returns the Error Code and prints the Error String for specified POST url */
	public int postHTTPReponseCode(String url, String query, String contentType) throws Exception {
		URLConnection urlConnection = null;
		JsonReader reader = null;
		int code = 0;
		
		Reporter.log("API - postRestRequest :: Data will be submitted as <POST>\n\n: " + query);
		
		Reporter.log(" ");
		Reporter.log("API Request URL:  " + url);
		Reporter.log(" ");
		
		Proxy proxy = this.getHttpProxy();
		if (proxy!=null)
			urlConnection = new URL(url).openConnection(proxy);
		else 
			urlConnection = new URL(url).openConnection();
				
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput (true);
		((HttpURLConnection) urlConnection).setRequestMethod("POST");
		urlConnection.setRequestProperty("Content-Length", Integer.toString(query.length()));
		urlConnection.setRequestProperty("Content-Type", contentType);
				
		OutputStream os = urlConnection.getOutputStream();
		try {
		os.write(query.getBytes());
		} finally {
			try { os.flush(); os.close(); } 
				catch (IOException logOrIgnore) {}
		}

		if (urlConnection instanceof HttpURLConnection) {
			HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
			
			code = httpConnection.getResponseCode();
			Reporter.log("HTTP RESPONSE: " + code);
					
			if(code==204 || code ==200)                                          ;
			else
			{
				reader = new JsonReader(new InputStreamReader(httpConnection.getErrorStream()));
				JsonParser parser = new JsonParser();
				JsonElement rootElement = parser.parse(reader);
				Reporter.log("RESPONSE BODY:  " + rootElement.toString());
			}
		} else 
			fail("HTTP API call error - not a valid http request!");
		
		return code;
	}
	
	/* Convert the given mongo BasicDBList object to JsonArray. */
    public static JsonArray getAsJsonArray(DBObject object) {
        if (!(object instanceof BasicDBList)) {
            throw new IllegalArgumentException("Expected BasicDBList as argument type!");
        }
        BasicDBList list = (BasicDBList)object;
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < list.size(); i++) {
            Object dbObject = list.get(i);
            if (dbObject instanceof BasicDBList) {
                jsonArray.add(getAsJsonArray((BasicDBList) dbObject));
            } else if (dbObject instanceof BasicDBObject) { // it's an object
                jsonArray.add(getAsJsonObject((BasicDBObject) dbObject));
            } else {   // it's a primitive type number or string 
                jsonArray.add(getAsJsonPrimitive(dbObject));
            }
        }
        return jsonArray;
    }

    /* Convert the given mongo BasicDBObject to JsonObject. */
    public static JsonObject getAsJsonObject(DBObject object) {
        if (!(object instanceof BasicDBObject)) {
            throw new IllegalArgumentException("Expected BasicDBObject as argument type!");
        }
        BasicDBObject dbObject = (BasicDBObject)object;        
        Set<String> keys = dbObject.keySet();
        Iterator<String> iterator = keys.iterator();
        JsonObject jsonObject = new JsonObject();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object innerObject = dbObject.get(key);
            if (innerObject instanceof BasicDBList) {
                jsonObject.add(key, getAsJsonArray((BasicDBList)innerObject));
            } else if (innerObject instanceof BasicDBObject) {
                jsonObject.add(key, getAsJsonObject((BasicDBObject)innerObject));
            } else {
                jsonObject.add(key, getAsJsonPrimitive(innerObject));
            }
        }
        return jsonObject;
    }

    /* Convert the given object to Json primitive JsonElement based on the type. */
    public static JsonElement getAsJsonPrimitive(Object value) {
        if (value instanceof String) {
            return new JsonPrimitive((String) value);
        } else if (value instanceof Character) {
            return new JsonPrimitive((Character) value);
        } else if (value instanceof Integer) {
            return new JsonPrimitive((Integer) value);
        } else if (value instanceof Long) {
            return new JsonPrimitive((Long) value);
        } else if (value instanceof Double) {
            return new JsonPrimitive((Double) value);
        } else if (value instanceof Boolean) {
            return new JsonPrimitive((Boolean) value);
        }
        throw new IllegalArgumentException("Unsupported value type for: " + value);
    }

	/* Returns the current timestamp. */
    public String getCurrentTimestamp() throws Exception {    
    	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    	return timeStamp;
    }
    
    /* Returns the current timestamp in UTC. */
    public String getUTCtimeStamp() throws Exception { 
	    long utcTimeStamp = System.currentTimeMillis()/1000;
	    return Long.toString(utcTimeStamp);
    }
    
    /* Converts the string into required Json format. */
    public String convertIntoJsonFormat(String myJsonBody) throws Exception {
		myJsonBody=myJsonBody.replace("'","\"");
		return myJsonBody;
    }
    
    /* Creates and returns the random UUID. */
    public String createRandomUUID() throws Exception {
    	UUID uid = UUID.randomUUID();
		String strUUID = uid.toString().trim().replace("-", "").replace("null", "");
		return strUUID;
    }
    
    /* Counts number of elements in JsonArray. */
	public int countElementsJsonArray(JsonArray arrJson) throws Exception {
		ArrayList<HashMap<String, String>> arrayData = new ArrayList<HashMap<String, String>>();
		Integer count = 0;
		String value;
		for (JsonElement element : arrJson) {
			JsonObject object = element.getAsJsonObject();
			if (object.isJsonNull()) {
				fail("Null entry in ArrayList position " + count + ". Array Data: " + arrJson.toString());
			}

			HashMap<String, String> items = new HashMap<String, String>();
			for (Map.Entry<String, JsonElement> member : object.entrySet()) {
				if (member != null) {
					if (member.getValue().isJsonNull()) {
						value = null;
					} else {
						value = member.getValue().toString().replaceAll("\"", "");
					}

					items.put(member.getKey(), value);
				} else {
					fail("Json array member had a null entry at Array index " + count + ". Array item: "
							+ object.toString() + "Array Data: " + arrJson.toString());
				}
			}
			count = count + 1;
			arrayData.add(new HashMap<String, String>(items));
			items.clear();
		}

		return arrayData.size();
	}	
	
    /* Counts number of elements in Arraylist. */
	public HashMap<String,String> findFromArrayListHashMapResults(ArrayList<HashMap<String, String>> al, String key) throws Exception{
		int count = 1;
		for (Iterator<HashMap<String, String>> iterator = al.iterator(); iterator.hasNext();) {
			//Reporter.log("*****Displaying ArrayList["+count+"] Data*****");
			HashMap<String, String> hashMap = (HashMap<String, String>) iterator.next();
			HashMap<String,String> site = findHashMapResults(hashMap,key);
			if(site != null)
				return site;
			count = count + 1;
		}
		return null;
	}
	
    /* Counts number of elements in JsonArray. */
	public HashMap<String,String> findHashMapResults(HashMap<String,String> map, String key) throws Exception{
		String value="";
		//Reporter.log("*****Displaying Key: Value Pairs From HashMap*****");
		for (Map.Entry<String, String> mapHeadersEntry  : map.entrySet()) {
			//Reporter.log(mapHeadersEntry.getKey() + ": " + mapHeadersEntry.getValue());
			if(mapHeadersEntry.getKey().equals(key))
			{
				value= mapHeadersEntry.getValue();
				if(!value.isEmpty())
					return map;
			}
		}
		return null;
	}
	
    /* Converts to Hex String. */
	private String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
 
		return formatter.toString();
	}
 
    /* Calculates the HMAC code on given Data using provided Secret Key. */
	public String calculateHMAC(String data, String key)
		throws SignatureException, NoSuchAlgorithmException, InvalidKeyException{
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}

	public String convertUUID(UUID uuid) throws Exception {
		String strUUID = uuid.toString().trim().replace("-", "").replace("null", "");
		return strUUID;
	}
}
	
	
	
	
