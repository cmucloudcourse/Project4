# Project4
This projects works on AWS Lamda Functions. In this project we explore various ways of creating AWS Lambda functions and their advantages.

# Task 1
Given a CIDR signature, write a AWS Lamda function to print its first address, last address and the # of addresses in the CIDR.

Example : 
$ aws lambda invoke --function-name cidrStats --payload '{"cidrSignature": "172.10.242.81/12"}' $OUT_FILE
 { "StatusCode": 200 }

 $ jq < $OUT_FILE
 {
   "cidrSignature": "172.10.242.81/12",
   "firstAddress": "172.0.0.0",
   "lastAddress": "172.15.255.255",
   "addressCount": 1048576
 }
 
 
 # Task 2  
 
Upload a video to S3 bucket. This upload generates a SNS notification containing S3 event to a Lambda function. This lamda function then generates the thumbnails from this video every second and uploads it to a target S3 bucket. 

Create a pipeline to with the following : 

Two S3 buckets - One for video files upload and one for storing thumbnails
An SNS topic - Notifications of S3 object creation events are sent to this topic
An S3 notification event - To publish an event to the SNS topic when a file is uploaded in the video-input bucket
A Lambda function - To generate thumbnails of uploaded videos using FFmpeg and upload to the target bucket.
