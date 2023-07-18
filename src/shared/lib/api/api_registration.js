import { API_SERVER } from "./apiServer";
import axios from "axios";


export const API_REGISTRATION = ( payload ) => {
    const res = axios.post(`${API_SERVER}auth/register`,
     payload)
  return res;
  
  };

