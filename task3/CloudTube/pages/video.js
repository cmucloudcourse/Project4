import React from 'react'

import VideoPage from '../components/VideoPage'
import SingleHeaderLayout from '../components/SingleHeaderLayout'
import DefaultPage from '../hoc/DefaultPage'

class VideoView extends React.Component {
  static async getInitialProps({ query }) {
    return {
      slug: query.slug,
      source: query.source,
      query: query.query,
      timecodes: query.timecodes ? query.timecodes.split('_') : []
    }
  }

  render() {
    return (
      <SingleHeaderLayout searchQuery={this.props.query}>
        <VideoPage {...this.props} />
      </SingleHeaderLayout>
    )
  }
}

export default DefaultPage(VideoView)
