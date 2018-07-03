import React from 'react'
import NoSSR from 'react-no-ssr'
import { Grid, Segment, Progress, Icon } from 'semantic-ui-react'
import ReactS3Uploader from 'react-s3-uploader'

class UploadPage extends React.Component {
  constructor(props) {
    super(props)
    this.state = { complete: false, progress: 0, started: false, error: false }
  }

  onUploadFinish(info) {
    console.log('Upload Complete: ', info)
    this.setState({ complete: true })
  }

  onUploadProgress(progress) {
    if (progress % 10 === 0) {
      console.log('Upload Progress: ', progress, '%')
    }
    this.setState({ progress })
  }

  onUploadStart(file, next) {
    console.log('Start Upload: ', file)
    this.setState({ started: true, progress: 0, complete: false, fileName: file.name })
    next(file)
  }

  onUploadError(error) {
    console.error(error)
    this.setState({ error: true })
  }

  render() {
    return (
      <div className="upload-container">
        <Grid centered>
          <Grid.Column width={14}>
              <Grid stackable className="upload-status">
                <Grid.Column width={4}>
                  <NoSSR>
                    <ReactS3Uploader
                      id="file"
                      className="file-input"
                      signingUrl="/s3/sign"
                      signingUrlMethod="GET"
                      s3path=""
                      preprocess={this.onUploadStart.bind(this)}
                      onProgress={this.onUploadProgress.bind(this)}
                      onError={this.onUploadError.bind(this)}
                      onFinish={this.onUploadFinish.bind(this)}
                      uploadRequestHeaders={{ 'x-amz-acl': 'public-read' }} // this is the default
                      contentDisposition="auto"
                      scrubFilename={filename =>
                        filename.replace(/[^\w\d-.]+/gi, '_')
                      }
                      autoUpload={true}
                    />
                    <label for="file" className="clickable upload-btn">
                      Choose Video <Icon name="cloud upload" />
                    </label>
                  </NoSSR>
                </Grid.Column>
                <Grid.Column width={12}>
                  {this.state.started ? (
                    <Progress
                      size="big"
                      percent={this.state.progress}
                      color={this.state.complete ? 'olive' : 'red'}
                      inverted
                      progress
                    >
                      {this.state.fileName + ' - '}
                      {this.state.error
                        ? 'Error, please refresh and try again'
                        : this.state.complete
                          ? 'Upload Complete'
                          : 'Uploading Video...'}
                    </Progress>
                  ) : (
                    false
                  )}
                </Grid.Column>
              </Grid>
          </Grid.Column>
        </Grid>
      </div>
    )
  }
}

export default UploadPage
