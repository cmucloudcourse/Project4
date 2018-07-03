import config from '../config'

const getSlugFromKey = key => key.substring(0, key.lastIndexOf('_'))

// These functions generate fake stuff that looks real
// given a video slug

const getTimecodeFromKey = key =>
  key.substring(key.lastIndexOf('_') + 1, key.length)

const getTitleFromSlug = slug => slug.replace(/_/g, ' ')

const getChannelFromSlug = slug => 'CloudComputing'

const getCaptionFromSlug = slug => '1M Views · 14 hours ago'

const getRankFromSlug = slug => '27'

const getViewsFromSlug = slug => '6,103,127'

const getThumbnailUrlFromKey = (key, source) => {
  return `${config.STORAGE_DOMAIN}/${config.S3_ENDPOINTS[source].THUMBNAIL_BUCKET}/${
    key
  }_1.png`
}

const getPreviewUrlFromKey = (key, source) => {
  return `${config.STORAGE_DOMAIN}/${config.S3_ENDPOINTS[source].PREVIEW_BUCKET}/${
    key
  }.gif`
}

export {
  getSlugFromKey,
  getTimecodeFromKey,
  getTitleFromSlug,
  getChannelFromSlug,
  getCaptionFromSlug,
  getRankFromSlug,
  getViewsFromSlug,
  getThumbnailUrlFromKey,
  getPreviewUrlFromKey
}
