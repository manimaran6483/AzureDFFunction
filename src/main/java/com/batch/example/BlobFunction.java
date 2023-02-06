package com.batch.example;

import java.util.Optional;

import org.springframework.util.SerializationUtils;

import com.batch.example.model.Employee;
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

	@FunctionName("ReadFromBlobAndWriteToCosmosDB")
	@StorageAccount("DefaultEndpointsProtocol=https;AccountName=azblobstgact;AccountKey=BanPPWQqgp/0U19HDkoHMQQvmGzKRs2uClO2+MEydm4iz3n+XeI8GK+HLEPVXPTtIXWLgJIKqsRx+AStqNfolg==;EndpointSuffix=core.windows.net")
    public HttpResponseMessage  run( @HttpTrigger(name = "HttpTrigger", 
    	      methods = {HttpMethod.GET}, 
    	      authLevel = AuthorizationLevel.ANONYMOUS) 
    	    HttpRequestMessage<Optional<String>> request,
    	    @BlobInput(name = "file", dataType = "binary", path = "input/") byte[] content,
    	    @CosmosDBOutput(
            name = "databaseOutput",
            databaseName = "ADFCosmosDB",
            collectionName = "Items",
            connectionStringSetting = "AccountEndpoint=https://adfdemodb.documents.azure.com:443/;AccountKey=jYi62k7HLZ51HXdl3FSwKH3YUblt5PyyKOywC1382kVKjaXsAOX8zVnBF8nFuW01YllrG3BvNOS8ACDbRpQsRA==;")
            OutputBinding<Employee> document,
            final ExecutionContext context) {

		context.getLogger().info("Function Started");
		
        Employee emp = new Employee();
        emp = (Employee)SerializationUtils.deserialize(content);
		
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