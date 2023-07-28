import React from "react";
import { useDispatch } from "react-redux";
import { actionTaskSlice } from "../tasksSlice";

export const DeleteTask = (props) => {
    const{
        children,
        idTask,
    }=props;

    const dispatch = useDispatch();

    const click = ()=>{
        dispatch(actionTaskSlice.deleteTask(idTask))
    }

    return(
        <div onClick={click} >
            {children}
        </div>
    )
}