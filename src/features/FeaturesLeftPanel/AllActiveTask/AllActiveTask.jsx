import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";

import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";

import { fillterAllActiveTasks } from "../lib/fillterAllActiveTasks";
import { sortArray } from "../lib/fillterCalendar";


//фильтрует(все активные таски) и посылает в ListTaskWidget таски для отображения
export const AllActiveTask = (props) => {

    const {
        children,
        tasks,
        activeFillters,
    } = props;
    const{
        activeFillter,
         setActiveFillter
    } = activeFillters;
//следит за обновлением массива тасков, если он обновляется, то сразу отправляет обновленые таски для отображения
        useEffect(()=>{
            if(activeFillter === 'AllActiveTask'){

                if(tasks.lenght !== 0){
                    onClick();
                }
            }

            
        },[tasks])
    const dispatch = useDispatch();
    //Ставит в состояние активности данный фильтр и отправляет в таск виджет отфильтрованные таски
    const onClick = ()=>{
        setActiveFillter('AllActiveTask')
        dispatch(setTasks(sortArray(fillterAllActiveTasks(tasks)) ))
        
    }

    return(
        <div onClick={onClick} >
            {children}
        </div>

    )
}





