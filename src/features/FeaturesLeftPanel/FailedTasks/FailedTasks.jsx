import React, { useEffect } from "react";
import { useDispatch } from "react-redux";

import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";

import { fillterFailedTasks } from "../lib/fillterFailedTasks";



export const FailedTasks = (props) => {

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
       
       dispatch(setTasks(fillterFailedTasks(tasks)))
        
    }



    return(
        <div onClick={onClick}>
            {children}
        </div>
    )
}