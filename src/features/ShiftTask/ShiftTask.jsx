import React, { useEffect,  useRef,  useState } from "react";

import { shiftTask } from "./models";





export const ShiftTask = (props)=>{

    const {
      //родительский компонент
      parrentRef,
      //активный или нет таск
      isActive,
    }=props;

    
    
    //состояния нужного смещения таска
    const [moveShift, setMoveShift] = useState()
    //реф объекты родителя и данного элемента, чтоб вычислить смещение
    const formTaskWidget = useRef(null);
    const [ parrent, setParrent ] = useState(parrentRef)
    
    
    //Обновляет при необходимости состояние реф объекта
   useEffect(()=>{
     setParrent(parrentRef);
   },[parrentRef])

   //при активации смещает таск на нужную велечину, поднимает по координате Z
   useEffect(() => {
     shiftTask(isActive, parrent, formTaskWidget, setMoveShift)
    }, [isActive]);
 
    
    return(
      <div ref={formTaskWidget} style={{  
        position: 'relative',
        left: `${moveShift}em`,
        transition: '0.5s',
        
        }} >
            {props.children}
      </div>
    )
}





