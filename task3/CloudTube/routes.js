const routes = module.exports = require('next-routes')()

routes
  .add('gallery', '/', 'index')
  .add('video', '/view/:source/:slug/q/:query?/:timecodes?', 'video')
  .add('upload', '/upload', 'upload')
  .add('search', '/search', 'search')
