import React from 'react'
import NoSSR from 'react-no-ssr'
import { Grid, Container } from 'semantic-ui-react'
import {
  getTitleFromSlug,
  getRankFromSlug,
  getViewsFromSlug,
} from '../helpers/utils'
import {
  Player,
  ControlBar,
  CurrentTimeDisplay,
  TimeDivider,
  PlaybackRateMenuButton,
  VolumeMenuButton,
} from 'video-react'
import config from '../config'

const ThumbnailPreview = ({ timecode, thumbnail, handleSeek, source }) => (
  <div className="thumbnail-container" onClick={handleSeek}>
    <img src={`${config.STORAGE_DOMAIN}/${config.S3_ENDPOINTS[source].THUMBNAIL_BUCKET}/${
        thumbnail
      }.png`} />
    <div className="overlay">{timecode}</div>
  </div>
)

class VideoPage extends React.Component {
  handleSeek(seconds) {
    // start a little earlier, since there is some error translating
    // from fps to seconds
    this.refs.player.seek(seconds - 1)
  }

  render() {
    return (
      <div className="video-container">
        <Grid centered>
          <Grid.Column width={14}>
            <Grid.Row>
              <NoSSR>
                <Player autoPlay ref="player">
                  <source
                    src={`${config.STORAGE_DOMAIN}/${config.S3_ENDPOINTS[this.props.source].INPUT_BUCKET}/${
                      this.props.slug
                    }.mp4`}
                  />
                  <ControlBar>
                    <CurrentTimeDisplay order={4.1} />
                    <TimeDivider order={4.2} />
                    <PlaybackRateMenuButton
                      rates={[5, 2, 1, 0.5, 0.1]}
                      order={7.1}
                    />
                    <VolumeMenuButton />
                  </ControlBar>
                </Player>
              </NoSSR>
            </Grid.Row>
            <Grid.Row>
              <br />
              <Container fluid>
                <p className="ranking">#{getRankFromSlug(this.props.slug)} ON TRENDING</p>
                <p className="title">{getTitleFromSlug(this.props.slug)}</p>
                <p className="views">{getViewsFromSlug(this.props.slug)} views</p>
              </Container>

              {
                this.props.timecodes.length > 0 ?
                  <Container fluid className="search-labels">
                    <p className="title">Video Content Search Results: {this.props.query.toUpperCase() }</p>

                    {this.props.timecodes.map(timecode => (
                      <ThumbnailPreview
                        key={this.props.slug + '_' + timecode}
                        timecode={timecode}
                        source={this.props.source}
                        thumbnail={`${this.props.slug}_${timecode}`}
                        handleSeek={this.handleSeek.bind(this, timecode)}
                      />
                    ))}

                  </Container>
                  : false
              }

            </Grid.Row>
          </Grid.Column>
        </Grid>
      </div>
    )
  }
}

export default VideoPage
