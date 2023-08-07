import React, { useEffect, useRef, useState } from "react";

import {  useDispatch, useSelector } from "react-redux";

import { AddNewTask, TaskWidget } from '../../widgets';
import { setActivity } from "./taskWidgetSlice";











export const ListTaskWidget =() => {
  



  
  
  
  // Получает таски из стора
  const tasksState = useSelector((state) => state.taskWidget.tasks);
  const activeTask = useSelector((state) => state.taskWidget.activeTask);
  const isNeedAddTask = useSelector((state) => state.taskWidget.isNeedAddTask);
  
  const [ tasks, setTasks ] = useState(tasksState);
  const [ idActiveTask, setIdActiveTask ] = useState(null);

  useEffect(()=>{
    setTasks(tasksState);
  },[tasksState])
    
    const dispatch = useDispatch();
    
    useEffect(()=>{
      isAddTask();
    },
    [isNeedAddTask])
    
    
    const isAddTask = ()=>{
      if(isNeedAddTask){
        dispatch(setActivity(-1))
      }else{
        if(activeTask !== null){
          dispatch(setActivity(-1))
          
        }}
      }

      // Получает контейнер 
     const containerRef = useRef(null);
  
      
    return(
      <div style={{
        position: 'relative',
        height: '91dvh',

      }} >
       
        

        <div 
          role="ListForTasks"
          ref={containerRef}
          style={{
            padding: '0.5rem',
            display: 'flex',
            flexDirection: 'row',
            flexWrap: "wrap",
            alignContent: 'flex-start',
            justifyContent: 'flex-start',
            overflowY: 'auto',
            height: '100%',
            filter: isNeedAddTask ? 'blur(2.5px)' : ''
          }}>
          

           { (tasks.length !== 0) ? tasks.map((task) => (
            <div key={task.id}  style={{
            margin: `0.5rem`
            }}>
            <TaskWidget taskData={task} parrentRef={containerRef} idActiveTask={idActiveTask} setIdActiveTask={setIdActiveTask}   />
            </div>
            )) : (
          <div style={{
            margin: 'auto',
            paddingTop: '5rem'
            }}>
            <span style={{
              fontSize: '3rem'
              }}>
               Нет задач

            </span>
          </div>
          )}
        </div>

       {isNeedAddTask ? (<AddNewTask/>) : null }
       
      </div>
    )
} 