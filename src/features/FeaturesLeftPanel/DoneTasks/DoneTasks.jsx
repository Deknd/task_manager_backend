import React from "react";
import { useDispatch } from "react-redux";

import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";

import { fillterDoneTasks } from "../lib/fillterDoneTasks";



export const DoneTasks = (props) => {

    const {
        tasks,
        children
    } = props;

  

    const dispatch = useDispatch();

    const onClick = ()=>{
        
       dispatch(setTasks(fillterDoneTasks(tasks)))
        
    }



    return(
        <div onClick={onClick}>
            {children}
        </div>
    )
}