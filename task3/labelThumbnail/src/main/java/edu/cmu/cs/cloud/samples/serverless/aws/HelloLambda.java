package edu.cmu.cs.cloud.samples.serverless.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomain;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClient;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClientBuilder;
import com.amazonaws.services.cloudsearchdomain.model.UploadDocumentsRequest;
import com.amazonaws.services.cloudsearchdomain.model.UploadDocumentsResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.transfer.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cmu.cs.cloud.samples.serverless.aws.pojo.CloudSearchPOJO;
import edu.cmu.cs.cloud.samples.serverless.aws.pojo.Fields;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class HelloLambda implements RequestHandler<S3Event, String> {

    private static LambdaLogger lambdaLogger = LambdaRuntime.getLogger();

    @Override
    public String handleRequest(S3Event event, Context context) {

        String filePath = "/tmp/result";
        lambdaLogger.log("Starting S3 Event Parsing" + "\n");
        lambdaLogger.log("Event JSON : "+event.toString());
        lambdaLogger.log("Record JSON : "+event.getRecords().toString());
            for (S3EventNotification.S3EventNotificationRecord record : event.getRecords()) {

                lambdaLogger.log("Source Bucket Name is  " + record.getS3().getBucket().getName() + "\n");
                lambdaLogger.log("Source Bucket ARN is  " + record.getS3().getBucket().getArn() + "\n");
                lambdaLogger.log("Source Bucket Object Key is  " + record.getS3().getObject().getKey() + "\n");
                lambdaLogger.log("Source Bucket URL DECODED Object Key is  " + record.getS3().getObject().getUrlDecodedKey() + "\n");
                String srcBucketName = record.getS3().getBucket().getName();
                String srcKey = record.getS3().getObject().getUrlDecodedKey();
//                AmazonS3 s3Client = new AmazonS3Client();
//                S3Object s3Object = s3Client.getObject(srcBucketName, srcKey);
//
//                downloadFile(srcBucketName, srcKey, "/tmp/task2/" + srcKey);
//
//
//                executeBashCommand("cp /var/task/ffmpeg /tmp/");
//                executeBashCommand("chmod 755 /tmp/ffmpeg");
//                executeBashCommand("mkdir -p /tmp/task2/result");
//                executeBashCommand("ls -ltr /tmp/task2/");
//                String ffmpegcommand = "/tmp/ffmpeg -i /tmp/task2/" + srcKey + " -y -vf fps=1/2 /tmp/task2/result/" + srcKey.split("\\.")[0] + ".gif";
//                executeBashCommand(ffmpegcommand);
                  List<String> labels = getLabels(srcBucketName, srcKey);
                  if(null == labels || labels.isEmpty()){
                      lambdaLogger.log("Labels can not be generated. Check the label logs");
                      return "ERROR CREATING LABELS";
                  }

                  if(createJSON(labels,srcKey)){
                      try {
                          uploadCloudSearchDoc(filePath);
                      } catch (FileNotFoundException e) {
                          lambdaLogger.log("Error while uploading json to Cloud search "+e.getMessage());
                      }
                  }else{
                      lambdaLogger.log("Error creating JSON file");
                      return "ERROR CREATING JSON FILE";
                  }


            }
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


    public static List<String> getLabels(String bucket, String imageName) {
        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withS3Object(new S3Object()
                                .withName(imageName).withBucket(bucket)));
        List<String> stringLabels = new ArrayList<>();
        try {
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.getLabels();
            lambdaLogger.log("Detected labels for " + imageName + "\n");
            labels.parallelStream().forEach(label -> {
                lambdaLogger.log(label.getName() + ": " + label.getConfidence().toString()+"\n");
                stringLabels.add(label.getName());
            });

        } catch (AmazonRekognitionException e) {
            lambdaLogger.log(e.getErrorMessage());
        }
        return stringLabels;
    }

    public static boolean createJSON(List<String> labels, String imageName){
        List<CloudSearchPOJO> pojos = new ArrayList<>();
        CloudSearchPOJO cloudSearchPOJO = new CloudSearchPOJO();
        cloudSearchPOJO.setId(imageName.split("\\.")[0]);
        cloudSearchPOJO.setType("add");
        Fields fields = new Fields();
        fields.setKey(imageName.split("\\.")[0]);
        fields.setLabels(labels);
        cloudSearchPOJO.setFields(fields);
        pojos.add(cloudSearchPOJO);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();
        lambdaLogger.log(gson.toJson(pojos));

        try (FileWriter file = new FileWriter("/tmp/result")) {
            file.write(gson.toJson(pojos));
            System.out.println("Successfully Copied JSON Object to File...");
            return true;
        } catch (IOException e) {
            lambdaLogger.log(e.getMessage());
            return false;
        }


    }

    public static void uploadCloudSearchDoc(String filePath) throws FileNotFoundException {
        String endPoint = "doc-demo-task3-yhjml4fh6ffpmkbeq6rdmrtvcu.us-east-1.cloudsearch.amazonaws.com";
//        AmazonCloudSearchDomain domain = AmazonCloudSearchDomainClient.builder().standard().build().;
        AmazonCloudSearchDomainClient domain = new AmazonCloudSearchDomainClient();
        domain.setEndpoint(endPoint);
        File f = new File(filePath);
        InputStream docAsStream = new FileInputStream(f);
        UploadDocumentsRequest req = new UploadDocumentsRequest().withDocuments(docAsStream).withContentLength(f.length()).withContentType("application/json");
        UploadDocumentsResult result = domain.uploadDocuments(req);
        System.out.println(result.toString());

    }




}

