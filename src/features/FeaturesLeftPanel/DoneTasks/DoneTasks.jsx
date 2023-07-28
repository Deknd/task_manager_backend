import React, { useEffect } from "react";
import { useDispatch } from "react-redux";

import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";

import { fillterDoneTasks } from "../lib/fillterDoneTasks";



export const DoneTasks = (props) => {

    const {
        tasks,
        children,
        isSelect
    } = props;

  

    const dispatch = useDispatch();
    useEffect(()=>{

        if(isSelect){

            if(tasks.lenght !== 0){

                onClick();
            }
        }
    },[tasks])
    const onClick = ()=>{
        
       dispatch(setTasks(fillterDoneTasks(tasks)))
        
    }



    return(
        <div onClick={onClick}>
            {children}
        </div>
    )
}