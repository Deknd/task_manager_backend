import React, { useEffect, useRef, useState } from "react";
import style from './containerForButton.module.css'

//контейнер для кнопок
export const ContainerForButton = (props)=>{
    return(
        <div 
        className={style.main_container}
       >
            {props.children}
        </div>
    )
}