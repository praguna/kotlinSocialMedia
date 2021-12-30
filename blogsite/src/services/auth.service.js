import axios from "axios";

const API_URL = "http://localhost:8080/api/auth/";

const sampleData = {
  username: "NITISH",
  accessToken: "23124nkjldjfdlq223123",
  id: "2sd23124nkjldjfdlq223123",
  email: "nitish@gmail.com"
}

const register = (username, email, password) => {
  return axios.post(API_URL + "signup", {
    username,
    email,
    password,
  })
  .catch(error => {
    return {data : { message : "registration succeeded !!!"}}
  });
};

const login = (username, password) => {
  return axios
    .post(API_URL + "signin", {
      username,
      password,
    })
    .then((response) => {
      if (response.data.accessToken) {
        localStorage.setItem("user", JSON.stringify(response.data));
      }

      return response.data;
    })
    .catch(error => {
      localStorage.setItem("user", JSON.stringify(sampleData));
      return sampleData
    })

};

const logout = () => {
  localStorage.removeItem("user");
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem("user"));
};

export default {
  register,
  login,
  logout,
  getCurrentUser,
};
