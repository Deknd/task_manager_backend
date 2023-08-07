import React, { useEffect, useState } from "react";
import style from './sizeTask.module.css'
import { changeZ_pozition } from "./models";


//меняет размер таска при активации его
export const SizeTask = (props)=> {

    const {
        //Активный таск
        isActive,
        //блокирован или нет таск
         isBlock,
         //вложенный компонент
        children,
    } = props;

   

 //состояние нахождения таска по координате Z
 const [ zPosition, setZPosition ] = useState(false);
 
 //перемещает по координате z, поднимает и опускает через 0.2 секунды
  useEffect(() => {
    changeZ_pozition(isActive, setZPosition);
   }, [isActive]);

    return(
        <div className={style.fix_container}>
            {/* див для описание всего скелета таска */}
            <div 
            className={style.content}
                style={{
                  
                    opacity: isBlock ? '10%' : '' ,
                    width: isActive ? '21em' : '',
                    height: isActive ? '25.9em' : '9.9em',
                    zIndex: zPosition? '5' : '',
                   
                  }}
                
            >
            
                  {children}
            </div>
                
        </div>
    )
}