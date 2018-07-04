package edu.cmu.cs.cloud.samples.serverless.aws;

import com.amazonaws.services.lambda.runtime.Context;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class HelloLambdaTest {

    @Test
    public void invokeHelloTest() {
        HelloLambda helloLambda = new HelloLambda();

        Context context = mock(Context.class);
    }

    @Test
    public void testDownload(){
        Context context = mock(Context.class);

        HelloLambda.downloadFile("project4-video-input-bucket","README.md","/tmp/task2/README-PROG");
    }

    @Test
    public void testExecuteCommand(){
        HelloLambda.executeBashCommand("ls -ltr /tmp/task2/");
        String ffmpegcommand = "/Users/Manas/Downloads/ffmpeg -i /tmp/task2/README-PROG"+" -y -vf fps=1 video_%d.png";
        HelloLambda.executeBashCommand(ffmpegcommand);
    }
}
