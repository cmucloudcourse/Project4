import React from 'react'

import GalleryPage from '../components/GalleryPage'
import SingleHeaderLayout from '../components/SingleHeaderLayout'
import DefaultPage from '../hoc/DefaultPage'
import config from '../config'

import CloudSearchClient from '../helpers/CloudSearchClient'

class SearchResultsView extends React.Component {
  static async getInitialProps(ctx) {
    const searchQuery = ctx.query.q

    const sampleClient = new CloudSearchClient(config.CLOUDSEARCH_ENDPOINT.SAMPLE)
    const userClient = new CloudSearchClient(config.CLOUDSEARCH_ENDPOINT.USER)

    const trendingVideos = await sampleClient.search(searchQuery, 16)
    const yourVideos = await userClient.search(searchQuery, 16)

    return { trendingVideos, yourVideos, searchQuery }
  }

  render() {
    return (
      <SingleHeaderLayout searchQuery={this.props.searchQuery}>
        <GalleryPage {...this.props} />
      </SingleHeaderLayout>
    )
  }
}

export default DefaultPage(SearchResultsView)
