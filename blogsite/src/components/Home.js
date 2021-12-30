import { React } from "react";
import AuthService from "../services/auth.service";
import { Link, useNavigate } from "react-router-dom"

const Home = () => {

  const navigate = useNavigate()

  const logOut = () => {
    AuthService.logout();
    navigate("/", { replace: true })
  };

  const currentUser = AuthService.getCurrentUser();

  return (
    <div>
      <nav className="navbar navbar-expand navbar-dark bg-dark justify-content-center">
        <Link to={"/"} className="navbar-brand">
          Blogger
        </Link>

        <div className="navbar-nav ml-auto">
          <li className="nav-item">
            <a href="/login" className="nav-link" onClick={logOut}>
              LogOut
            </a>
          </li>
        </div>
      </nav>

      <div className="container">
        <header className="jumbotron">
          <h3>
            <strong>{currentUser.username}</strong> Profile
          </h3>
        </header>
        <p>
          <strong>Token:</strong> {currentUser.accessToken.substring(0, 20)} ...{" "}
          {currentUser.accessToken.substr(currentUser.accessToken.length - 20)}
        </p>
        <p>
          <strong>Id:</strong> {currentUser.id}
        </p>
        <p>
          <strong>Email:</strong> {currentUser.email}
        </p>
      </div>
    </div>
  )

};

export default Home;
