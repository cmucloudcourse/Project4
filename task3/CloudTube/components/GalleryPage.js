import React from 'react'
import { Grid, Container } from 'semantic-ui-react'
import ReactHoverObserver from 'react-hover-observer'
import { Link } from '../routes'
import {
  getTitleFromSlug,
  getChannelFromSlug,
  getCaptionFromSlug,
  getThumbnailUrlFromKey,
  getPreviewUrlFromKey,
} from '../helpers/utils'

const VideoItem = ({ video, source, isHovering }) => (
  <Link
    route="video"
    params={{
      slug: video.slug,
      source: source,
      query: video.query,
      timecodes:
        video.timecodes && video.timecodes.length > 0
          ? video.timecodes.join('_')
          : undefined,
    }}
  >
    <div className="video-item">
      <div
        className="content-container"
        style={{
          backgroundImage: `url(${getThumbnailUrlFromKey(video.slug, source)})`,
          display: `${isHovering ? 'none' : 'block'}`,
        }}
      />
      <div
        className="content-container"
        style={{
          backgroundImage: `url(${getPreviewUrlFromKey(video.slug, source)})`,
          display: `${isHovering ? 'block' : 'none'}`,
        }}
      />
      <p className="title">{getTitleFromSlug(video.slug)}</p>
      <p className="channel">{getChannelFromSlug(video.slug)}</p>
      <p className="caption">{getCaptionFromSlug(video.slug)}</p>
    </div>
  </Link>
)

const GallerySection = ({ videos, header, source }) => (
  <Container className="section-container">
    <p className="section-header">{header}</p>
    <Grid container columns={4} stackable>
      <Grid.Row>
        {videos.length > 0 ? (
          videos.map(video => (
            <Grid.Column key={video.slug}>
              <ReactHoverObserver hoverDelayInMs={300}>
                <VideoItem video={video} source={source} />
              </ReactHoverObserver>
            </Grid.Column>
          ))
        ) : (
          <p className="placeholder">No videos found</p>
        )}
      </Grid.Row>
    </Grid>
  </Container>
)

const GalleryPage = props => (
  <div className="gallery-container">
    <GallerySection
      header="Trending"
      videos={props.trendingVideos}
      source="SAMPLE"
    />
    <GallerySection
      header="Your Videos"
      videos={props.yourVideos}
      source="USER"
    />
  </div>
)

export default GalleryPage
