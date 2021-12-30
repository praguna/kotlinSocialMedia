import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import React, { useState, useEffect } from "react";
import Login from "./components/Login";
import Register from "./components/Register";
import Home from "./components/Home";
import AuthService from "./services/auth.service";
import { Routes, Route} from "react-router-dom";


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
        </Routes>
    </div>
  );

}

export default App;
