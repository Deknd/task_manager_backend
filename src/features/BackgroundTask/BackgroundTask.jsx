import React, { useEffect, useState } from "react";
import { setBackground } from "./models";
import style from './backgroundTask.module.css'



//Меняет фон, взависимости от приоритета и статуса
export const BackgroundTask = (props) => {

    const {
       //вложеный компонент
        children,
        //статус таска
        status,
        //приоритет таска
        priority,
        //активен или нет режим изменения таска
        isEditMode
    } = props;

   


 //хранит состояние бекграунда или фона для него
 const [backgroundTask, setBackgroundTask] = useState(null);
 const [backgroundTaskColor, setBackgroundTaskColor] = useState();

 //меняет фон, при изменении статуса или приоритета
 useEffect(() => {
    setBackground(status, priority, setBackgroundTask, setBackgroundTaskColor)
  }, [priority, status]);
 

    return(
       
            <div className={style.container}
                style={{
                  // при активном режиме редактирования уберает отступ сверху
                  paddingTop: isEditMode ? '0em' : '1em',
                  backgroundImage: ( backgroundTask !== null ) ? `url(${backgroundTask})` : '',
                  backgroundColor: ( backgroundTask === null )? `${backgroundTaskColor}` : '',
                  }}
                
            >
            
                  {children}
            </div>
                
    )
}

