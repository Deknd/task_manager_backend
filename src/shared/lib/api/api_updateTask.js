import { API_SERVER } from "./apiServer";
import axios from "axios";


export const API_UPDATE = (payload) => {
    const{
        task,
        accessToken,
    }=payload;
    const res = axios.put(`${API_SERVER}tasks/update`, task,
    {
        headers: {
            Authorization: `Bearer ${accessToken}`,
        },
    })
    return res;
  
  };

