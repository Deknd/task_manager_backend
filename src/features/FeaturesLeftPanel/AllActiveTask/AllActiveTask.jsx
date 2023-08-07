import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";

import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";

import { fillter } from "../lib/fillterAllActiveTasks";
import { sortArray } from "../lib/fillterCalendar";



export const AllActiveTask = (props) => {

    const {
        children,
        tasks,
        isSelect,
    } = props;

        useEffect(()=>{
            if(isSelect){

                if(tasks.lenght !== 0){

                    onClick();
                }
            }
        },[tasks])
    const dispatch = useDispatch();

    const onClick = ()=>{
        dispatch(setTasks(sortArray(fillter(tasks)) ))
        
    }

    return(
        <div onClick={onClick} >
            {children}
        </div>

    )
}





