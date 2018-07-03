const next = require('next')
const routes = require('./routes')
const app = next({ dev: process.env.NODE_ENV !== 'production' })
const handler = routes.getRequestHandler(app)
const config = require('./config')
const axios = require('axios')

if (
  process.env.AWS_ACCESS_KEY_ID === undefined ||
  process.env.AWS_SECRET_ACCESS_KEY === undefined
) {
  console.log('Please ensure that you have exported your AWS credentials as environment variables:')
  console.log(' export AWS_ACCESS_KEY_ID=<your_access_key_id>')
  console.log(' export AWS_SECRET_ACCESS_KEY=<your_secret_access_key>\n')
  process.exit(1)
}

// With express
const express = require('express')
app.prepare().then(() => {
  express()
    .use(
      '/s3',
      require('react-s3-uploader/s3router')({
        bucket: config.S3_ENDPOINTS.USER.INPUT_BUCKET,
        region: 'us-east-1', //optional
        headers: { 'Access-Control-Allow-Origin': '*' }, // optional
        ACL: 'public-read', // this is default
        uniquePrefix: false, // (4.0.2 and above) default is true, setting the attribute to false preserves the original filename in S3
      })
    )
    .use('/searchproxy', (req, res, next) => {
      axios
        .get(
          `http://${req.query.searchEndpoint}/2013-01-01/search?q=${
            req.query.q
          }&size=${req.query.size}`
        )
        .then(
          response => {
            res.status(200).send(response.data.hits)
          },
          error => {
            console.error(`Error connecting to ${req.query.searchEndpoint}`)
            res.status(200).send([])
          }
        )
    })
    .use('/s3proxy', (req, res, next) => {
      axios.get(`${config.STORAGE_DOMAIN}/${req.query.bucket}`).then(
        response => {
          res.status(200).send(response.data)
        },
        error => {
          console.error(`Error fetching from bucket ${req.query.bucket}`)
          res.status(200).send([])
        }
      )
    })
    .use(handler)
    .listen(process.env.NODE_ENV !== 'production' ? 3000 : 8000, () => {
      console.log('App is listening on port 8000')
    })
})
