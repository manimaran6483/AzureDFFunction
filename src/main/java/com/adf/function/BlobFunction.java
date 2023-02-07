package com.adf.function;

import java.util.List;
import java.util.Optional;

import org.springframework.util.SerializationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BlobInput;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.StorageAccount;


public class BlobFunction {

	@SuppressWarnings("unchecked")
	@FunctionName("ReadFromBlobAndWriteToCosmosDB")
    public HttpResponseMessage  run( @HttpTrigger(name = "HttpTrigger", 
    	      methods = {HttpMethod.GET}, 
    	      authLevel = AuthorizationLevel.ANONYMOUS) 
    	    HttpRequestMessage<Optional<String>> request,
    	    @BlobInput(name = "file", dataType = "binary", path = "input/{Query.file}", connection="AzureWebJobsStorage") byte[] content,
    	    @CosmosDBOutput(
            name = "databaseOutput",
            databaseName = "ADFCosmosDB",
            collectionName = "ADFDemoCollection",
            connectionStringSetting = "AzureCosmosDBConnection")
            OutputBinding<Employee> document,
            final ExecutionContext context) {

		context.getLogger().info("Function Started");
		
		context.getLogger().info("Query Param: "+ request.getQueryParameters().get("file"));
		
        Employee emp = new Employee();
        
        context.getLogger().info("Blob Received - "+ content);
        
        context.getLogger().info("Blob Received - "+ content.toString());
        
        Object obj = SerializationUtils.deserialize(content);
        
        
        emp = (Employee) obj;
        
		document.setValue(emp);
       
		context.getLogger().info("Saved to DB");
        
		try {
			return request.createResponseBuilder(HttpStatus.OK)
			        .body("Object Saved : " + new ObjectMapper().writeValueAsString(emp))
			        .build();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return request.createResponseBuilder(HttpStatus.OK)
			        .body("Object Saved : " )
			        .build();
		
    }

	
}
