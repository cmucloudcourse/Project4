import React from 'react'

import UploadPage from '../components/UploadPage'
import SingleHeaderLayout from '../components/SingleHeaderLayout'
import DefaultPage from '../hoc/DefaultPage'

class UploadView extends React.Component {
  static async getInitialProps({ query }) {
    return {
    }
  }

  render() {
    return (
      <SingleHeaderLayout>
        <UploadPage {...this.props} />
      </SingleHeaderLayout>
    )
  }
}

export default DefaultPage(UploadView)
