import axios from 'axios'
import { getSlugFromKey, getTimecodeFromKey } from './utils'

class CloudSearchClient {
  constructor(searchEndpoint) {
    this.searchEndpoint = searchEndpoint
  }

  async search(searchQuery, size) {
    let results

    try {
      if (process.browser) {
        // proxy the request through the server to bypass CORS restriction on Cloudsearch
        results = (await axios.get(
          `/searchproxy?q=${searchQuery}&size=${size}&searchEndpoint=${
            this.searchEndpoint
          }`
        )).data
      } else {
        results = (await axios.get(
          `http://${this.searchEndpoint}/2013-01-01/search?q=${
            searchQuery
          }&size=${size}`
        )).data.hits
      }
    } catch (error) {
      console.error(`Error connecting to ${this.searchEndpoint}`)
      return []
    }

    // filter down the results so we only have unique videos
    const uniqueVideos = {}

    if (results.hit) {
      results.hit.map(hit => {
        const key = hit.id
        const slug = getSlugFromKey(key)
        if (!(slug in uniqueVideos)) {
          uniqueVideos[slug] = {
            slug: slug,
            timecodes: [parseInt(getTimecodeFromKey(key))],
          }
        } else {
          // collect all the timecodes that contain this label
          uniqueVideos[slug].timecodes.push(parseInt(getTimecodeFromKey(key)))
        }
        // if it happens to be a keyword match on the title of the
        // video, do not append timecodes
        if (hit.fields.title && hit.fields.title.toLowerCase().indexOf(searchQuery) >= 0) {
          uniqueVideos[slug].timecodes = []
        }
      })
    }

    const videos = []
    for (let key in uniqueVideos) {
      const video = uniqueVideos[key]
      videos.push({
        slug: video.slug,
        timecodes: video.timecodes.sort((a, b) => a - b),
        query: searchQuery,
      })
    }

    return videos
  }
}

export default CloudSearchClient
