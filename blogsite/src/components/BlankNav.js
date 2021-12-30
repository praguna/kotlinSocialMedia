import {Link} from "react-router-dom"

const BlankNav = () => {
    return (
        <nav className="navbar navbar-expand navbar-dark bg-dark justify-content-center">
            <Link to={"/"} className="navbar-brand">
                Blogger
            </Link>
        </nav>
    )
}

export default BlankNav
