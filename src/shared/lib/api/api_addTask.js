import { API_SERVER } from "./apiServer";
import axios from "axios";


export const API_ADD_TASK = (payload) => {
    const{
        id,
        task,
        accessToken,
    }=payload;
    const res = axios.post(`${API_SERVER}users/${id}/tasks`, task,
    {
        headers: {
            Authorization: `Bearer ${accessToken}`,
        },
    })
    return res;
  
  };

