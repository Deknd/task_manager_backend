import React from "react";
import { useDispatch } from "react-redux";
import { actionTaskSlice } from "../tasksSlice/tasksSlice";



export const ChangeStatusTask = ( props ) => {

    const{
        id,
        activeStatus,
        forStatus,
        setStatus,
        children,
    } = props;
// DONE FAILED TODO

    const dispatch = useDispatch();
    const changeStatus = () => {
        let status = null;
        if(activeStatus === 'TODO'){
            status = forStatus;
        } else { 
            status = 'TODO'
             }
        dispatch(actionTaskSlice.updateStatusTask({status, id}))
        setStatus(status)
    }
    return(
        <div onClick={changeStatus} >
            {children}
        </div>
    )
}