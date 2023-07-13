import React from "react";
import { useDispatch } from "react-redux";

import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";

import { fillterFailedTasks } from "../lib/fillterFailedTasks";



export const FailedTasks = (props) => {

    const {
        tasks,
        children
    } = props;

  

    const dispatch = useDispatch();

    const onClick = ()=>{
       
       dispatch(setTasks(fillterFailedTasks(tasks)))
        
    }



    return(
        <div onClick={onClick}>
            {children}
        </div>
    )
}