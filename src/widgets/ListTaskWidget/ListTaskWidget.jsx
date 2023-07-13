import React, { useEffect, useRef, useState } from "react";

import {  useSelector } from "react-redux";

import { TaskWidget } from '../index';









export const ListTaskWidget =() => {
   
  // Получает таски из стора
    const tasks = useSelector((state) => state.taskWidget.tasks)
     // Получает контейнер 
    const containerRef = useRef(null);
    // данные о размере контейнера
    const [elementRect, setElementRect] = useState({ top: 0, left: 0, right: 0, bottom: 0, });
    const [height, setHeight] = useState(0);


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
        calculateHeight();
      };
      const calculateHeight = () => {
        if (containerRef.current) {
          const windowHeight = window.innerHeight;
          
          const height = windowHeight - elementRect.top;
  
          setHeight(height);
        }
      };

    //подписывается на обновление контейнера
    useEffect(() => {
        
          handleResize();
          

        window.addEventListener("resize", handleResize);


        return () => {
        window.removeEventListener("resize", handleResize);
     };
    }, []);

    
    
  


    return(
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
            height: height-1,
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
    )
} 