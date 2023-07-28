import React, { useState, useEffect, useRef } from 'react';



import { ChangeStatusTask, ContainerForButtonIcon, ContentTaskActiveElements, DeleteTask, EffectButton, SendNewTask } from '../../features';
import { FormTaskWidget } from '../../features';
import { ChangePriorityTask } from '../../features';

import { ContentTask } from "../../entities";

import {  ContentField, IconButton_v2 } from '../../shared/ui'
import { InputDataForTask } from '../InputDataForTask';
import { format } from 'date-fns';





export const TaskWidget = (props) => {

    


    const {
        taskData,
        parrentRect,
    } = props;

    const{
        left,
    } = parrentRect;

    const {
        id,
        status,
        priority,
        isBlock,
        isActive,
    } = taskData || {};
    const [ statusTask, setStatusTask ] = useState(status);
    const [ priorityTask, setPriorityTask ] = useState(priority);

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
    const [ isEditMode, setIsEditMode ] = useState(false);
    const [ isActiveEditMode, setIsActiveEditMode ] = useState(false);
    const [ taskDataEdit, setTaskData ] = useState({
        title: '',
        description: '',
        expirationDate: new Date(),
        priority: '',
        status: 'TODO'
    })

    const buttonComplect = [(

        <EffectButton>
            <ChangeStatusTask activeStatus={statusTask} setStatus={setStatusTask} forStatus={'DONE'} id={id} >
                <IconButton_v2 type={'positive'}  textIcon={'Done'} />
            </ChangeStatusTask>
        </EffectButton>  

    ), (

        <EffectButton>
            <ChangeStatusTask activeStatus={statusTask} setStatus={setStatusTask} forStatus={'FAILED'} id={id} >
                <IconButton_v2 type={'negative'}  textIcon={'Failed'} />
            </ChangeStatusTask>
        </EffectButton>

    ),(

        <EffectButton>
            <div onClick={()=>{setIsEditMode(true)}} >
                <IconButton_v2 type={'edit'}  textIcon={'Edit'} />
            </div>
        </EffectButton>  

    ), (

        <EffectButton>
            <DeleteTask idTask={id} >
                <IconButton_v2 type={'garbage'}  textIcon={'Delete'} />
            </DeleteTask>
        </EffectButton>

    )]

    const buttonComplectEditMode = [(

        <EffectButton>
        
            <SendNewTask 
                task={taskDataEdit} >
                <IconButton_v2 type={'accept'} textIcon={'ok'} />
            </SendNewTask>
        </EffectButton>  

    ), (

        <EffectButton>
            
            <div onClick={()=>{setIsEditMode(false)}} >

                <IconButton_v2 type={'cancel'} textIcon={'Отмена'} />
            </div>
            
            
        </EffectButton>

    )]

    return(

        <div ref={taskWidget} >
            <FormTaskWidget status={statusTask} priority={priorityTask} taskData={taskData} move={move}>
                {/* div для контентной части */}
                
                    <ContentTaskActiveElements 
                        taskData={taskData} 
                        isEditMode={isEditMode} 
                        isActiveEditMode={isActiveEditMode}   
                        setIsActiveEditMode={setIsActiveEditMode}
                        setIsEditMode={setIsEditMode} >
                            {
                                isActiveEditMode ? (

                                    <InputDataForTask getData={setTaskData} cameTitle={taskData.title} cameDescription={taskData.description}  />
                                  

                                ):(
                                    <ContentTask taskData={taskData}/>
                                )
                            }
                    </ContentTaskActiveElements>
                
                

                {/* Фуекциональная часть */}
                
                    {/* Кнопка изменения приоритета */}
                {isActive && !isActiveEditMode ?( 
                    <ChangePriorityTask taskData={taskData}>
                        <ContentField text={'change priority'} isVisible={isActive} height={2} />
                    </ChangePriorityTask>
                )  : null}

                <div style={{
                   // height: '4em',
                    //marginTop: '0.5rem',
                   paddingBottom: '0.4em',
                        
                }}>
                    {!isBlock ? (
                        <ContainerForButtonIcon buttonIcons={isActiveEditMode ? buttonComplectEditMode : buttonComplect} />
                    ) : null}
                </div>
            </FormTaskWidget>
        </div>

    )
}


