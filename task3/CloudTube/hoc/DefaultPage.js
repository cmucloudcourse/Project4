import React from 'react'
import Router from 'next/router'
import Head from '../components/Head'

export default BarePage =>
  class DefaultPage extends React.Component {
    static async getInitialProps(ctx) {
      const pageProps =
        BarePage.getInitialProps && (await BarePage.getInitialProps(ctx))
      return {
        ...pageProps
      }
    }

    render() {
      return (
        <div>
          <Head />
          <BarePage {...this.props} />
        </div>
      )
    }
  }
