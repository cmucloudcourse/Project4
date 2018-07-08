package edu.cmu.cs.cloud.samples.serverless.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class HelloLambda implements RequestHandler<SNSEvent, String> {

    private static LambdaLogger lambdaLogger = LambdaRuntime.getLogger();

    @Override
    public String handleRequest(SNSEvent event, Context context) {



        String destBucketName = "project4-thumbnail-bucket";
        lambdaLogger.log("Starting SNS Event Parsing" + "\n");
        lambdaLogger.log(event.getRecords().toString());
        for (SNSEvent.SNSRecord snsRecord:   event.getRecords()) {
            String s3json = snsRecord.getSNS().getMessage();
            lambdaLogger.log(s3json);
            S3EventNotification s3EventNotification = S3EventNotification.parseJson(s3json);

            for (S3EventNotification.S3EventNotificationRecord record : s3EventNotification.getRecords()) {

                lambdaLogger.log("Source Bucket Name is  " + record.getS3().getBucket().getName() + "\n");
                lambdaLogger.log("Source Bucket ARN is  " + record.getS3().getBucket().getArn() + "\n");
                lambdaLogger.log("Source Bucket Object Key is  " + record.getS3().getObject().getKey() + "\n");
                lambdaLogger.log("Source Bucket URL DECODED Object Key is  " + record.getS3().getObject().getUrlDecodedKey() + "\n");

                String srcBucketName = record.getS3().getBucket().getName();
                String srcKey = record.getS3().getObject().getUrlDecodedKey();

                AmazonS3 s3Client = new AmazonS3Client();
                S3Object s3Object = s3Client.getObject(srcBucketName, srcKey);

                downloadFile(srcBucketName, srcKey, "/tmp/task2/" + srcKey);


                executeBashCommand("cp /var/task/ffmpeg /tmp/");
                executeBashCommand("chmod 755 /tmp/ffmpeg");
                executeBashCommand("mkdir -p /tmp/task2/result");
                executeBashCommand("ls -ltr /tmp/task2/");
                String ffmpegcommand = "/tmp/ffmpeg -i /tmp/task2/" + srcKey + " -y -vf fps=1 /tmp/task2/result/" + srcKey.split("\\.")[0] + "_%d.png";
                executeBashCommand(ffmpegcommand);
            }
        }

        executeBashCommand("ls -ltr /tmp/task2/result/");
        uploadDir("/tmp/task2/result",destBucketName);
        executeBashCommand("rm -rf /tmp/task2/result/");
        return "ok";
    }

    public static void downloadFile(String src_bucket_name, String src_key_name,
                                    String file_path)
    {

        File f = new File(file_path);
        TransferManager xfer_mgr = TransferManagerBuilder.standard().build();
        try {
            Download xfer = xfer_mgr.download(src_bucket_name, src_key_name, f);
            // or block with Transfer.waitForCompletion()
            waitForCompletion(xfer);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        xfer_mgr.shutdownNow();
    }

    private static void waitForCompletion(Transfer xfer)
    {
        try {
            xfer.waitForCompletion();
//            Thread.sleep(5000);
        } catch (AmazonServiceException e) {
            lambdaLogger.log("Amazon service error: " + e.getMessage());
            System.exit(1);
        } catch (AmazonClientException e) {
            lambdaLogger.log("Amazon client error: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            lambdaLogger.log("Transfer interrupted: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void executeBashCommand(String command, String ... params){
            StringBuffer output = new StringBuffer();
            try{
                Process p = Runtime.getRuntime().exec(command);
                p.waitFor();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";
                while ((line = bufferedReader.readLine())!= null) {
                    output.append(line + "\n");
                }

                lambdaLogger.log("Output of command "+command+" is : "+output);

            } catch (IOException e) {
                lambdaLogger.log("Exception while executing the command "+ e.getStackTrace());
                e.printStackTrace();
            } catch (InterruptedException e) {
                lambdaLogger.log("Exception while executing the command "+ e);
            }finally {
            }

    }


    public static void uploadDir(String dir_path, String bucket_name)
    {

        TransferManager xfer_mgr = TransferManagerBuilder.standard().build();
        try {
            MultipleFileUpload xfer = xfer_mgr.uploadDirectory(bucket_name,
                    null, new File(dir_path), false);
            // loop with Transfer.isDone()
            // or block with Transfer.waitForCompletion()
           waitForCompletion(xfer);
        } catch (AmazonServiceException e) {
            lambdaLogger.log(e.getErrorMessage());
            System.exit(1);
        }
        xfer_mgr.shutdownNow();
    }

}
