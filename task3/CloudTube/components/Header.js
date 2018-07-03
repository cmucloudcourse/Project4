import React from 'react'
import { Link, Router } from '../routes'
import { Input, Menu, Icon } from 'semantic-ui-react'

class Header extends React.Component {
  constructor(props) {
    super(props)
    this.state = { query: props.searchQuery }
  }

  handleQueryChange(event) {
    this.setState({ query: event.target.value })
  }

  handleSearch() {
    Router.pushRoute('search', { q: this.state.query })
  }

  handleKeypress(event) {
    if (event.key === 'Enter') {
      Router.pushRoute('search', { q: this.state.query })
    }
  }

  render() {
    return (
      <header className="youtube">
        <Menu inverted color="black" fixed="top" borderless>
          <Menu.Item>
            <Icon size="large" name="content" />
          </Menu.Item>

          <Menu.Item className="clickable">
            <Link route="gallery">
              <img src="/static/youtube.png" />
            </Link>
          </Menu.Item>

          <Menu.Item>
            <Input
              value={this.state.query}
              inverted
              onChange={this.handleQueryChange.bind(this)}
              onKeyPress={this.handleKeypress.bind(this)}
              action={{
                icon: 'search',
                onClick: this.handleSearch.bind(this),
              }}
              placeholder="Search..."
            />
          </Menu.Item>

          <Menu.Menu position="right">
            <Link route="upload">
              <Menu.Item className="clickable">
                <Icon size="large" name="cloud upload" />
                Upload
              </Menu.Item>
            </Link>
          </Menu.Menu>
        </Menu>
      </header>
    )
  }
}

export default Header
