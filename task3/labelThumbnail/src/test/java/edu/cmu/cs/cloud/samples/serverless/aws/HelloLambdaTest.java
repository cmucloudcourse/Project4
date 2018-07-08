package edu.cmu.cs.cloud.samples.serverless.aws;

import com.amazonaws.services.lambda.runtime.Context;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

public class HelloLambdaTest {

    @Test
    public void invokeHelloTest() {
        HelloLambda helloLambda = new HelloLambda();

        Context context = mock(Context.class);
    }

    @Test
    public void testFileNamesplit(){
        String key = "dancing_16.mov";
        System.out.println(key.split("\\.")[0]);
    }

    @Test
    public void testLabels(){
        String bucketName = "project4-thumbnail-bucket";
        String imageName = "testimg.png";

        HelloLambda.getLabels(bucketName,imageName);
    }

    @Test
    public void testCreateJSON(){
        List<String> testlabesl = new ArrayList<>(Arrays.asList("label1", "label2","label3"));
        HelloLambda.createJSON(testlabesl, "testimage.png");
    }

    @Test
    public void testUploadCloudSearchDoc() throws FileNotFoundException {
        HelloLambda.uploadCloudSearchDoc("/tmp/result");
    }
}
