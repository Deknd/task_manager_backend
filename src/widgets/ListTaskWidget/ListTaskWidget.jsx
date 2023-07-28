import React, { useEffect, useRef, useState } from "react";

import {  useDispatch, useSelector } from "react-redux";

import { AddNewTask, TaskWidget } from '../../widgets';
import { setActivity, setIsNeedAddTask } from "./taskWidgetSlice";













export const ListTaskWidget =() => {
   
  // Получает таски из стора
    const tasksState = useSelector((state) => state.taskWidget.tasks);
    const activeTask = useSelector((state) => state.taskWidget.activeTask);
    const isNeedAddTask = useSelector((state) => state.taskWidget.isNeedAddTask);
     // Получает контейнер 
    const containerRef = useRef(null);
    // данные о размере контейнера
    const [elementRect, setElementRect] = useState({ top: 0, left: 0, right: 0, bottom: 0, });
    const [ tasks, setTasks ] = useState([]);
    useEffect(()=>{
      tasksState ? setTasks(tasksState) : setTasks([]);
    },[tasksState])

    const dispatch = useDispatch();
    //Функция для обновления размера контейнера
    const handleResize = () => {
        if (containerRef.current) {
            const rect = containerRef.current.getBoundingClientRect();
            setElementRect({
                top: rect.top,
                left: rect.left,
                right: rect.right,
                bottom: rect.bottom,
            });
            
        }
      };

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

    //подписывается на обновление контейнера
    useEffect(() => {
        
          handleResize();
          

        window.addEventListener("resize", handleResize);


        return () => {
        window.removeEventListener("resize", handleResize);
     };
    }, []);

    
   
      
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
            <TaskWidget taskData={task} parrentRect={elementRect} />
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