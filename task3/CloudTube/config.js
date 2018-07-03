const config = {

  // Domain of the Cloud Service Provider's storage service
  STORAGE_DOMAIN: 'https://s3.amazonaws.com',

  S3_ENDPOINTS: {
    SAMPLE: {
      /** We have provided some sample buckets hosting some sample videos so you can see how the app should work. You do not have to change these fields. **/
      INPUT_BUCKET: 'cloudtube-video-input-bucket',
      PREVIEW_BUCKET: 'cloudtube-preview-gif-bucket',
      THUMBNAIL_BUCKET: 'cloudtube-thumbnail-bucket',
    },

    USER: {
      /** TODO: Change these fields to point to your own S3 buckets, so that the app can download/upload to them **/
      INPUT_BUCKET: 'amanda-video-input-bucket',
      PREVIEW_BUCKET: 'amanda-preview-gif-bucket',
      THUMBNAIL_BUCKET: 'amanda-thumbnail-bucket',
    }
  },

  CLOUDSEARCH_ENDPOINT: {
    SAMPLE: 'search-cloudtube-rwaa3wrvhjculftmhacrtoqfrm.us-east-1.cloudsearch.amazonaws.com',

    /** TODO: Change this to your cloudsearch domain, you can find this on your cloudsearch domain dashboard **/
    USER: 'search-stuff-dk5y2ah4vcms4yck2fudtoobke.us-east-1.cloudsearch.amazonaws.com'
  }

}

module.exports = config