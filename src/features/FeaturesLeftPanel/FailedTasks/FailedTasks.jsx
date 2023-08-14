import React, { useEffect } from "react";
import { useDispatch } from "react-redux";

import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";

import { fillterFailedTasks } from "../lib/fillterFailedTasks";


//фильтрует таски выбирая проваленные и отправляет их
export const FailedTasks = (props) => {

    const {
        tasks,
        children,
        activeFillters,
        
    } = props;
    const{
        activeFillter,
         setActiveFillter
    } = activeFillters;
  

    const dispatch = useDispatch();
    //если есть обновление в тасках, отправляет их в ListTaskWidget
    useEffect(()=>{

        if(activeFillter === 'FailedTasks'){

            if(tasks.lenght !== 0){

                onClick();
            }
        }
    },[tasks])

    // активирует данный фильтр и отправляет их в ListTaskWidget
    const onClick = ()=>{
       setActiveFillter('FailedTasks');
       dispatch(setTasks(fillterFailedTasks(tasks)));
        
    }



    return(
        <div onClick={onClick}>
            {children}
        </div>
    )
}