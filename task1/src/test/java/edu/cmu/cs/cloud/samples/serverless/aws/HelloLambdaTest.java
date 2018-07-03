package edu.cmu.cs.cloud.samples.serverless.aws;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import edu.cmu.cs.cloud.samples.serverless.aws.pojo.CIDR;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

public class HelloLambdaTest {

    @Test
    public void invokeHelloTest() {
        HelloLambda helloLambda = new HelloLambda();

        Context context = mock(Context.class);

        CIDR result = helloLambda.handleRequest(new CIDR("172.10.242.81/12"), context);

//        assertEquals("172.15.255.255", result);

        System.out.println(result.getAddressCount()+" "+result.getFirstAddress()+" "+result.getLastAddress());
    }
}
