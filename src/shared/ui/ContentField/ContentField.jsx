import React, { useState, useEffect } from 'react';

import style from './contentField.module.css'

export const ContentField = (props) =>{

    const{
        //текст который будет отображаться
        text,
        //высота объекта
        height,
        //видим или нет он
        isVisible,
        //размер шрифта
        fontSize,
        //убирает стандартный отступ
        noMargin,
    }=props;

    //ставит высоту по стандарту, если нет других данных
    const heightField = height ? `${height}em` : 'auto'

    //Видим или не видим
    const [ show, setShow ] = useState(false);
    useEffect(()=>{
        setShow(isVisible);
    },[isVisible])

    //Размер шрифта, вдруг во время работы захочу поменять
    const [sz, setSZ] = useState('none')
    useEffect(()=>{
        if(fontSize){
            setSZ(fontSize)
        }
    },[fontSize])

    return(
        <div 
        className={style.field} 
        style={{
            height: show ? heightField : '0em',
            fontSize: sz !== '' ? `${sz}em` : sz,
            opacity: show ? '' : '0%',
            padding: show ? '' : '0em',
            marginTop: show ? '' : '0em',
            marginTop: noMargin ? '0em' : show ? '' : '0em',
            
            }} >
            <span>
                {text}    
            </span>
        </div>
    )
}