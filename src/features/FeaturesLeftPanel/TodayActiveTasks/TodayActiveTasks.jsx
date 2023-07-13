import React from "react";
import { useDispatch } from "react-redux";

import { fillterTodayActiveTasks } from "../lib/fillterTodayActiveTask";
import { sortArray } from "../lib/fillterCalendar";

import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";


export const TodayActiveTasks = (props) => {

    const {
        children,
        tasks,
    } = props;


    const dispatch = useDispatch();

    const onClick = ()=>{
        
        dispatch(setTasks(sortArray(fillterTodayActiveTasks(tasks)) ));
        
    }

    return(
        <div onClick={onClick} >
            {children}
        </div>

    )
}