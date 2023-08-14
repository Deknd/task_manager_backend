import React, { useEffect } from "react";
import { useDispatch } from "react-redux";

import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";

import { fillterDoneTasks } from "../lib/fillterDoneTasks";


//Выполненные таски
export const DoneTasks = (props) => {

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
    //Следит за изменениями тасков и обновляет данные в ListTaskWidget
    useEffect(()=>{ 
        if(activeFillter === 'DoneTasks'){
            if(tasks.lenght !== 0){
                onClick();
            }
        }  
    },[tasks])
    //обновляет данные в ListTaskWidget
    const onClick = ()=>{
       dispatch(setTasks(fillterDoneTasks(tasks)));
        setActiveFillter('DoneTasks');
    }



    return(
        <div onClick={onClick}>
            {children}
        </div>
    )
}