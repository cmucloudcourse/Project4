import axios from 'axios'
import config from '../config'
import xml from 'xml-parse'

class S3Client {
  constructor(bucketName) {
    this.bucketName = bucketName
  }

  async listKeys() {
    let xmlResponse
    try {
      if (process.browser) {
        // proxy the request through the server to bypass CORS restriction on S3
        xmlResponse = (await axios.get(`/s3proxy?bucket=${this.bucketName}`))
          .data
      } else {
        xmlResponse = (await axios.get(
          `${config.STORAGE_DOMAIN}/${this.bucketName}`
        )).data
      }

      const xmlObject = xml.parse(xmlResponse)
      const keys = []
      for (let element of xmlObject) {
        if (element.tagName === 'ListBucketResult') {
          for (let child of element.childNodes) {
            if (child.tagName === 'Contents') {
              for (let gchild of child.childNodes) {
                if (gchild.tagName === 'Key') {
                  const fullKey = gchild.innerXML
                  keys.push(fullKey.substring(0, fullKey.indexOf('.')))
                  break
                }
              }
            }
          }
          break
        }
      }
      return keys
    } catch (err) {
      console.error(
        `Error fetching objects from ${config.STORAGE_DOMAIN}/${
          this.bucketName
        }`
      )
      return []
    }
  }
}

export default S3Client
