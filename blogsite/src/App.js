import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import React from "react";
import Login from "./components/Login";
import Register from "./components/Register";
import Home from "./components/Home";
import AuthService from "./services/auth.service";
import { Routes, Route} from "react-router-dom";
import ViewPost from './components/ViewPost';


const RefreshHome = () => {
  const currentUser = AuthService.getCurrentUser();
  return (
    currentUser ?  (<Home/>) : ( <Login /> ) 
  )
}

const App = () => {  

  return (
    <div>
        <Routes>
          <Route path="/" element= { <RefreshHome /> } />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/home" element={<Home/>} />
          <Route path="/post/:postId" element={<ViewPost/>} />
          <Route path="/post" element={<ViewPost/>} />
          <Route path="/feed/:post" element={<Home/>} />
        </Routes>
    </div>
  );

}

export default App;
