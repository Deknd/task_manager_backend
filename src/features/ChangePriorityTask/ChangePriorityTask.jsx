import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";


import style from './changePriority.module.css'
import { actionTaskSlice } from "../tasksSlice";




export const ChangePriorityTask = (props) =>{

    const {
        children,
        //данные таска
        taskData,
        //приоритет таска
        priorityTask,
        //метод для изменения приоритета таска
        setPriorityTask,
        //инидкатор активный ли таск
        isActive,
    } = props;

    
    const dispatch = useDispatch();

  
    
   
//следит за тем, чтоб при изменения приоритета таска и выхода из активности, оставался актуальный приоритет
    useEffect(()=>{
      
        if(!isActive && taskData)            
                if(taskData.priority !== priorityTask){
                    setPriorityTask(taskData.priority)
                    dispatch(actionTaskSlice.updatePriorityTask({id: taskData.id,priority: priorityTask}))
                }
            
        
    },[isActive])





//меняет приоритет, но не посылает на сервер запрос
    const click = () =>{
        if(setPriorityTask){
            if(priorityTask === 'STANDARD'){
                setPriorityTask('HIGH')
            } else {
                setPriorityTask('STANDARD')

            }
            return;
        }
    }

    return(
        <div className={priorityTask ? style.prioritytask : null} onClick={click}>
            {children}
        </div>
    )
}


  
  