import React, { useEffect,  useState } from "react";


import { SceletonTaskWidget } from "../../entities";

import taskDesctopBigStandart from './image/taskDesctopBigStandard.jpg'
import taskDesctopBigHigh from './image/taskDesctopBigHigh.jpg'


export const FormTaskWidget = (props)=>{

    const {
      taskData,
      move,
      

      

    }=props;

    const{
      status,
      priority,
      isActive,
      isBlock,

    } = taskData;

   
   //таск выше по Z оси или нет
   const [ zPosition, setZPosition ] = useState(false);
   

   //следит за тем, какой таск увеличен
   useEffect(() => {
       
           if(isActive){
                setZPosition(true);
            }else{
                const interval = setTimeout(() => {
                    
                      setZPosition(false);
                   
                  }, 100);
              
                  return () => {
                    clearTimeout(interval);
                  };
            }
   }, [isActive]);
    

  // слежение за взаимодействием ользователя и таска
  const [isHovered, setIsHovered] = useState(false);
  const [isActiveMouse, setIsActiveMouse] = useState(false);

  const handleMouseEnter = () => {
    setIsHovered(true);
  };

  const handleMouseLeave = () => {
    setIsHovered(false);
    setIsActiveMouse(false); 
  };

  const handleMouseDown = () => {
    setIsActiveMouse(true);
  };

  const handleMouseUp = () => {
    setIsActiveMouse(false);
  };
  // следит какой риоритет у таска


  const [backgroundTask, setBackgroundTask] = useState(null);
  const [backgroundTaskColor, setBackgroundTaskColor] = useState(taskDesctopBigStandart);

  useEffect(() => {
    if(status !== 'DONE' && status !== 'FAILED'){

      switch(priority){
          case 'STANDARD': setBackgroundTask(taskDesctopBigStandart);
          break;
          case 'HIGH': setBackgroundTask(taskDesctopBigHigh);
          break;
      }
    }else{
      setBackgroundTask(null);
      switch(status){
        case 'DONE': setBackgroundTaskColor('#48f173');
        break;
        case 'FAILED': setBackgroundTaskColor('#fa1e1e70')
      }
    }
  }, [priority, status]);
  useEffect(() => {
    if(backgroundTask === null){
      if(status !== 'DONE' && status !== 'FAILED'){

        switch(priority){
            case 'STANDARD': setBackgroundTaskColor('#c4e5da');
            break;
            case 'HIGH': setBackgroundTaskColor('#fedfda');
            break;
        }
      }
    }
  }, [priority]);
  //стандартное смещение таска
  const defaultMove = -2.835;
  //нужное смещение таска
  const [moveShift, setMoveShift] = useState(defaultMove)
  //следит за изменением нужного смещения
  useEffect(()=>{
    setMoveShift(defaultMove+move)
  },[move])
//стиль для формы, с разными настройками
const styleChange={
    opacity: isBlock ? '10%' : '' ,
    width: isActive ? '21rem' : '',
  
    zIndex: zPosition? '5' : '',
    left: isActive ? `${moveShift}rem` : '',
    backgroundImage: ( backgroundTask !== null ) ? `url(${backgroundTask})` : '',
    backgroundColor: ( backgroundTask === null )? `${backgroundTaskColor}` : '',
}
const controller = {
    handleMouseEnter, 
    handleMouseLeave,
    handleMouseDown,
    handleMouseUp,
    styleChange,
}


    return(
        <SceletonTaskWidget controller={controller} >
            {props.children}
        </SceletonTaskWidget>
    )
}

