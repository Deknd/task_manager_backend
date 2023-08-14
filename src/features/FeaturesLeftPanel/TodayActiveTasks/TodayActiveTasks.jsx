import React, { useEffect } from "react";
import { useDispatch } from "react-redux";

import { fillterTodayActiveTasks } from "../lib/fillterTodayActiveTask";
import { sortArray } from "../lib/fillterCalendar";

import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";

//Фильтрует таски и передает их в ListTaskWidget
export const TodayActiveTasks = (props) => {

    const {
        children,
        tasks,
        activeFillters,
        

    } = props;

    const{
        activeFillter,
         setActiveFillter
    } = activeFillters;
    const dispatch = useDispatch();
    // если набор тасков обновится, то сразу обновится и в ListTaskWidget
    useEffect(()=>{

        if(activeFillter === 'TodayActiveTasks'){

                if(tasks.lenght !== 0){

                    onClick();
                }
            }
    },[tasks])
//Обновляет состояние активного фильтра и отправляет даные в таск лист
    const onClick = ()=>{
        setActiveFillter('TodayActiveTasks');
        dispatch(setTasks(sortArray(fillterTodayActiveTasks(tasks)) ));
        
    }

    return(
        <div onClick={onClick} >
            {children}
        </div>

    )
}