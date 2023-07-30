import React, { useState, useEffect, useRef } from 'react';



import { ChangeStatusTask, ContainerForButtonIcon, ContentTaskActiveElements, DeleteTask, EffectButton,  UpdateTask } from '../../features';
import { FormTaskWidget } from '../../features';
import { ChangePriorityTask } from '../../features';

import { ContentTask } from "../../entities";

import {  ContentField, IconButton_v2 } from '../../shared/ui'
import { InputDataForTask } from '../InputDataForTask';





export const TaskWidget = (props) => {

    

// Просы которые приходят
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
        expirationDate,
        isBlock,
        isActive,
    } = taskData;
    
    //размер шрифта, для измерения rem
    // координаты текущего таска
    //хук, для получения объекта таска
    //стандартный отступ для активации таска
    const leftShiftDefault = -2.835;
    const taskWidget = useRef(null);
    
    //состояния внутрение
    const [ statusTask, setStatusTask ] = useState(status);
    const [ priorityTask, setPriorityTask ] = useState(priority);
    const [ elementRect, setElementRect ] = useState({ top: 0, left: 0, right: 0, bottom: 0, rightDisplay: 0  });
    const [ fontSize, setFontSize ] = useState(0);
    const [ move, setMove ] = useState(0);
    const [ isEditMode, setIsEditMode ] = useState(false);
    const [ isActiveEditMode, setIsActiveEditMode ] = useState(false);
    const [ taskDataEdit, setTaskDataEdit ] = useState({
        title: '',
        description: '',
        expirationDate: new Date(),
        priority: '',
    })
    
    
    
    
    useEffect(()=>{
        
        if(!isActive)
        setPriorityTask(priority)
    },[ priority, isActive ])
    
    //метод для получение начальных координат таска и подписка на их обновление
    useEffect(() => {
        handleResize(taskWidget, setElementRect, setFontSize); 
        
        window.addEventListener("resize", ()=>{handleResize(taskWidget, setElementRect, setFontSize)});
        
        return () => {
            window.removeEventListener("resize", ()=>{handleResize(taskWidget, setElementRect, setFontSize)});
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
        
    //переменная для смещение объекта
    //метод для обновление значений коордиат таска и размера шрифта
    
    
    return(

        <div ref={taskWidget} >
            <FormTaskWidget status={statusTask} priority={isActiveEditMode? taskDataEdit.priority : priorityTask} taskData={taskData} move={move}>
                {/* div для контентной части */}
                
                    <ContentTaskActiveElements 
                        taskData={taskData} 
                        isEditMode={isEditMode} 
                        isActiveEditMode={isActiveEditMode}   
                        setIsActiveEditMode={setIsActiveEditMode}
                        setIsEditMode={setIsEditMode} >
                            {
                                isActiveEditMode ? (

                                    <InputDataForTask 
                                    getData={setTaskDataEdit} 
                                    cameTitle={taskData.title} 
                                    cameDescription={taskData.description} 
                                    camePriority={priority}
                                    cameExpirationDate={expirationDate} />
                                  

                                ):(
                                    <ContentTask taskData={taskData}/>
                                )
                            }
                    </ContentTaskActiveElements>
                
                

                {/* Фуекциональная часть */}
                
                    {/* Кнопка изменения приоритета */}
                {isActive && !isActiveEditMode ?( 
                    <ChangePriorityTask taskData={taskData} change={setPriorityTask} priority={priorityTask}>
                        <ContentField text={'change priority'} isVisible={isActive} height={2} />
                    </ChangePriorityTask>
                )  : null}

                <div style={{
                   // height: '4em',
                    //marginTop: '0.5rem',
                   paddingBottom: '0.4em',
                        
                }}>
                    {!isBlock ? (
                        <ContainerForButtonIcon buttonIcons={isActiveEditMode ? buttonComplectEditMode(taskDataEdit, id, setIsEditMode) : buttonComplect(statusTask, setStatusTask, id, setIsEditMode )} />
                        ) : null}
                </div>
            </FormTaskWidget>
        </div>

    )
}

const handleResize = (refElement, setElementRect, setFontSize) => {
    if (refElement.current) {
      const rect = refElement.current.getBoundingClientRect();
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
        const computedStyle = window.getComputedStyle(refElement.current);
        const fontSizeValue = computedStyle.getPropertyValue("font-size");

        const parsedFontSize = parseFloat(fontSizeValue);
        setFontSize(parsedFontSize);
       }
   };



    const buttonComplectEditMode = (taskDataEdit, id, setIsEditMode) =>{
    
    
    return [(
    
            <EffectButton>
            
                <UpdateTask 
                    task={taskDataEdit} id={id} closeEditMode={setIsEditMode} >
                    <IconButton_v2 type={'accept'} textIcon={'ok'} />
                </UpdateTask>
            </EffectButton>  
    
        ), (
    
            <EffectButton>
                
                <div onClick={()=>{setIsEditMode(false)}} >
    
                    <IconButton_v2 type={'cancel'} textIcon={'Отмена'} />
                </div>
                
                
            </EffectButton>
    
        )]}




        const buttonComplect = (statusTask, setStatusTask, id, setIsEditMode ) => { 
            
            
            return [(
    
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
    
}