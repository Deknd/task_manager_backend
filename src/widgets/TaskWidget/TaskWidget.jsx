import React, { useState, useEffect, useRef } from 'react';



import { ContentTaskActiveElements } from '../../features';
import { FormTaskWidget } from '../../features';
import { ChangePriorityTask } from '../../features';

import { FourIconButton } from '../../entities';
import { ContentTask } from "../../entities";

import {  ContentField } from '../../shared/ui'





export const TaskWidget = (props) => {

    


    const {
        taskData,
        parrentRect,
    } = props;

    const{
        left,
    } = parrentRect;

    const {
        isBlock,
        isActive,
    } = taskData || {};

    //стандартный отступ для активации таска
    const leftShiftDefault = -2.835;
    //размер шрифта, для измерения rem
    const [fontSize, setFontSize] = useState(0);
    // координаты текущего таска
    const [elementRect, setElementRect] = useState({ top: 0, left: 0, right: 0, bottom: 0, rightDisplay: 0  });
    //хук, для получения объекта таска
    const taskWidget = useRef(null);
    //переменная для смещение объекта
    const [move, setMove] = useState(0);
    //метод для обновление значений коордиат таска и размера шрифта
     const handleResize = () => {
         if (taskWidget.current) {
           const rect = taskWidget.current.getBoundingClientRect();
           const windowWidth = window.innerWidth;
            let rightCoordinate = windowWidth;
            if(rightCoordinate === 0){
                const windowWidth = window.innerWidth;
                rightCoordinate = windowWidth;
            }
           setElementRect({
             top: rect.top,
             left: rect.left,
             right: rect.right,
             bottom: rect.bottom,
             rightDisplay: rightCoordinate,

           });
             const computedStyle = window.getComputedStyle(taskWidget.current);
             const fontSizeValue = computedStyle.getPropertyValue("font-size");
 
             const parsedFontSize = parseFloat(fontSizeValue);
             setFontSize(parsedFontSize);
            }
        };
        //метод для получение начальных координат таска и подписка на их обновление
        useEffect(() => {
          handleResize(); 
      
          window.addEventListener("resize", handleResize);
      
          return () => {
            window.removeEventListener("resize", handleResize);
        };
    }, []);
    //редактирование отступа, чтоб таск не заходил за границы
    useEffect(()=>{
        if(!isActive){setMove(0)}else{
            if(left > (elementRect.left + leftShiftDefault*fontSize)){
                const difference = (left - (elementRect.left + leftShiftDefault*fontSize))/fontSize+1;
                setMove(difference)
            }
            if(elementRect.rightDisplay<=(elementRect.right + (leftShiftDefault+1)*-fontSize)){
                const difference = ((elementRect.right + (leftShiftDefault)*-fontSize)-elementRect.rightDisplay)/fontSize+1.3;
                setMove(-difference);
            }
        }
      },[isActive]);
    

    return(

        <div ref={taskWidget} >
            <FormTaskWidget taskData={taskData} move={move}>
                {/* div для контентной части */}
                <ContentTaskActiveElements taskData={taskData}>
                    <ContentTask taskData={taskData}/>
                </ContentTaskActiveElements>

                {/* Фуекциональная часть */}
                
                    {/* Кнопка изменения приоритета */}
                {isActive ?( 
                    <ChangePriorityTask taskData={taskData}>
                        <ContentField text={'change priority'} isVisible={isActive} height={2} />
                    </ChangePriorityTask>
                )  : null}

                <div style={{
                    height: '2.5rem',
                    marginTop: '0.5rem',
                    padding: '0.3rem',
                        
                }}>
                    {!isBlock ? <FourIconButton/> : null}
                </div>
            </FormTaskWidget>
        </div>

    )
}


