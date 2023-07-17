import { API_SERVER } from "./apiServer";
import axios from "axios";


export const API_LOGIN = (payload) => {
    const res = axios.post(`${API_SERVER}auth/login`, {
      username: payload.username,
      password: payload.password,
    })
  return res;
  
  };

