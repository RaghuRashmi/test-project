package com.nbcuni.test.nbcidx.rally.connector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.CreateRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.CreateResponse;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.codec.binary.Base64;

public class uploadReportRally {

    public void uploadFileAttachment(String pathToReport, String reportName) throws Exception {

//    	Config config = new Config();
    	
        //Connection parameters
        String rallyURL = "https://rally1.rallydev.com/";
        String wsapiVersion = "1.40";
        String applicationName = "Attach Automated Test Reports";

        // Credentials
        String userName = "raghu.rashmi@nbcuni.com";
        String userPassword = "Rashmi31Sale";

        RallyRestApi restApi = new RallyRestApi(
                        new URI(rallyURL),
                        userName,
                        userPassword);
        restApi.setWsapiVersion(wsapiVersion);
        restApi.setApplicationName(applicationName);

        //User settings
        String testerUserName = userName;

        //Task ID - update in config with each iteration change
        String existTaskID = "TA50614";       

        //Read User
        QueryRequest userRequest = new QueryRequest("User");
        userRequest.setFetch(new Fetch("UserName", "Subscription", "DisplayName"));
        userRequest.setQueryFilter(new QueryFilter("UserName", "=", testerUserName));
        QueryResponse userQueryResponse = restApi.query(userRequest);
        JsonArray userQueryResults = userQueryResponse.getResults();
        JsonElement userQueryElement = userQueryResults.get(0);
        JsonObject userQueryObject = userQueryElement.getAsJsonObject();
        String userRef = userQueryObject.get("_ref").getAsString();

        //Query for existing User Story
        QueryRequest  existTaskRequest = new QueryRequest("Tasks");
        existTaskRequest.setFetch(new Fetch("FormattedID","Name"));
        existTaskRequest.setQueryFilter(new QueryFilter("FormattedID", "=", existTaskID));
        QueryResponse userStoryQueryResponse = restApi.query(existTaskRequest);
        userStoryQueryResponse.getResults().get(0).getAsJsonObject();
        String existUserStoryRef = userStoryQueryResponse.getResults().get(0).getAsJsonObject().get("_ref").getAsString();

        //Read In File Content
        String ReportFile = pathToReport;
        String fileBase64String;
        long attachmentSize;

        //Open file
        RandomAccessFile myFileHandle = new RandomAccessFile(ReportFile, "r");

        try {
            
        	//Get and check length
            long longlength = myFileHandle.length();
            //Max upload size for Rally attachments is 5MB
            long maxAttachmentLength = 5120000;
            if (longlength > maxAttachmentLength) throw new IOException("File size too large for Rally attachment, > 5 MB");

            //Read file and return data
            byte[] fileBytes = new byte[(int) longlength];
            myFileHandle.readFully(fileBytes);
            fileBase64String = Base64.encodeBase64String(fileBytes);
            attachmentSize = longlength;

            //First create AttachmentContent from image string
            JsonObject myAttachmentContent = new JsonObject();
            myAttachmentContent.addProperty("Content", fileBase64String);
            CreateRequest attachmentContentCreateRequest = new CreateRequest("AttachmentContent", myAttachmentContent);
            CreateResponse attachmentContentResponse = restApi.create(attachmentContentCreateRequest);
            String myAttachmentContentRef = attachmentContentResponse.getObject().get("_ref").getAsString();
            System.out.println("Attachment Content created: " + myAttachmentContentRef);            

            //Now create the Attachment itself
            JsonObject myAttachment = new JsonObject();
            myAttachment.addProperty("Artifact", existUserStoryRef);
            myAttachment.addProperty("Content", myAttachmentContentRef);
            myAttachment.addProperty("Name", reportName);
            myAttachment.addProperty("Description", "Attachment From Automated Test Suite");
            myAttachment.addProperty("ContentType","text/html");
            myAttachment.addProperty("Size", attachmentSize);
            myAttachment.addProperty("User", userRef);          

            CreateRequest attachmentCreateRequest = new CreateRequest("Attachment", myAttachment);
            CreateResponse attachmentResponse = restApi.create(attachmentCreateRequest);
            attachmentResponse.getObject().get("_ref").getAsString();
            
            	if (attachmentResponse.wasSuccessful()) {
            		System.out.println("Successfully created Attachment");
            	} else {
            		String[] attachmentContentErrors;
            		attachmentContentErrors = attachmentResponse.getErrors();
                        	System.out.println("Error occurred creating Attachment: ");
                        	for (int i=0; i<attachmentContentErrors.length;i++) {
                        		System.out.println(attachmentContentErrors[i]);
                        	}                   
            	}
            
        } catch (Exception e) {
                System.out.println("Exception occurred while attempting to create Content and/or Attachment: ");
                e.printStackTrace();            
        }

        finally {
            //Release all resources
        	myFileHandle.close();
            restApi.close();
        }                
    }
}