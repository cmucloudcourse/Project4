import React from 'react'

import GalleryPage from '../components/GalleryPage'
import SingleHeaderLayout from '../components/SingleHeaderLayout'
import DefaultPage from '../hoc/DefaultPage'
import S3Client from '../helpers/S3Client'
import config from '../config'

class GalleryView extends React.Component {

  static async getInitialProps(ctx) {

    const sampleClient = new S3Client(config.S3_ENDPOINTS.SAMPLE.INPUT_BUCKET)
    const userClient = new S3Client(config.S3_ENDPOINTS.USER.INPUT_BUCKET)

    const sampleKeys = await sampleClient.listKeys()
    const userKeys = await userClient.listKeys()

    const trendingVideos = sampleKeys.map(key => ({
      slug: key
    }))

    const yourVideos = userKeys.map(key => ({
      slug: key
    }))

    return {trendingVideos, yourVideos}
  }

  render() {
    return (
      <SingleHeaderLayout>
        <GalleryPage {...this.props}/>
      </SingleHeaderLayout>
    )
  }
}

export default DefaultPage(GalleryView)
