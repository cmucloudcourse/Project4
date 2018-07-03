import Header from './Header'

export default props => (
  <div>
    <Header {...props} />
    <div className="main-container">{props.children}</div>
  </div>
)
