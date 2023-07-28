import { API_SERVER } from "./apiServer";
import axios from "axios";


export const API_DELETE_TASK = (payload) => {
    const{
        idTask,
        accessToken,
    }=payload;
    const res = axios.delete(`${API_SERVER}tasks/${idTask}/delete`,
    {
        headers: {
            Authorization: `Bearer ${accessToken}`,
        },
    })
    return res;
  
  };

