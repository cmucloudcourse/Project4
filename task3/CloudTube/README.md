# CloudTube
## Demo App for 15619 P2.3 (Functions as a Service) 

### Background
Now that you have done the hard work of creating a video processing pipeline on the cloud, we can see how it all comes together in our very own video streaming website. 

Our video processing pipeline supports 2 main features:
1. Showing a preview of the videos when the user mouseovers (you may have seen this on YouTube).
1. Searching for videos by the actual content of the video based on labels assigned by Amazon Rekognition. 

### Deploying CloudTube on Amazon EC2
1. Edit the configuration file `config.js` in this directory, to provide your S3 bucket names and your Amazon CloudSearch endpoint.
1. Install Node.js v6.x.x
    ```bash
    curl -sL https://deb.nodesource.com/setup_6.x | sudo -E bash
    sudo apt-get update 
    sudo apt-get install nodejs
    ```
1. Export your AWS credentials as environment variables
    ```
    export AWS_ACCESS_KEY_ID=<your_access_key_id>
    export AWS_SECRET_ACCESS_KEY=<your_secret_access_key>
    ```
1. In this directory, run the following commands to start the app
    ```bash
    npm install
    npm run build
    npm run start
    ```