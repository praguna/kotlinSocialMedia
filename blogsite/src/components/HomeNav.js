import { Link } from "react-router-dom"
const HomeNav = (props)=>{

  const c1 = props.param !== "post" && props.enable? "ui large header": ""
  const c2 = props.param === "post" && props.enable? "ui large header": ""
  return (
    <nav className="navbar navbar-expand navbar-dark bg-dark justify-content-center sticky-top">
        <Link to={"/"} className="navbar-brand">
          Blogger
        </Link>
        <div className="navbar-nav ml-auto">
          <li className="nav-item">
            <a href="/feed/mypost" className={"nav-link "+ c1}>
              MyPosts
            </a>
          </li>
          <li className="nav-item">
            <a href="/feed/post" className={"nav-link "+c2}>
              Feed
            </a>
          </li>
        </div>
      </nav>
)
  }

export default HomeNav