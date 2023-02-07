package com.adf.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

	@FunctionName("ReadFromBlobAndWriteToCosmosDB")
    public HttpResponseMessage  run( @HttpTrigger(name = "HttpTrigger", 
    	      methods = {HttpMethod.GET}, 
    	      authLevel = AuthorizationLevel.ANONYMOUS) 
    	    HttpRequestMessage<Optional<String>> request,
    	    @BlobInput(name = "file", dataType = "String", connection="AzureWebJobsStorage", path = "input/Source.csv") String content,
    	    @CosmosDBOutput(
            name = "databaseOutput",
            databaseName = "ADFCosmosDB",
            collectionName = "ADFDemoCollection",
            connectionStringSetting = "AzureCosmosDBConnection")
            OutputBinding<List<Employee>> document,
            final ExecutionContext context) {

		context.getLogger().info("Function Started"); 
		
		//context.getLogger().info("Query Param: "+ request.getQueryParameters().get("file"));
		
        context.getLogger().info("Blob Received - "+ content);
        
        String fileName = request.getHeaders().get("filename");
		
		context.getLogger().info(fileName); 
        
        List<String> contentList = Arrays.asList(content.split("\n"));
        List<Employee> empList = new ArrayList<>();
        for(String s : contentList) {
			Employee emp = Employee.createEmployee(s);
			empList.add(emp);
			
		}
        
        document.setValue(empList);
		context.getLogger().info("Saved to DB - " + empList);
        
       
		return request.createResponseBuilder(HttpStatus.OK)
			        .body("Object Saved" )
			        .build();
		
    }
	

	
}
