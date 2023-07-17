import { API_SERVER } from "./apiServer";
import axios from "axios";


export const API_UPDATE_REFRESH_TOKEN = (payload) => {
    const res = axios.post(`${API_SERVER}auth/refresh`, {
        refreshToken: payload.refreshToken,
    })
  return res;
  
  };

  