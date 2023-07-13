import React, { useState, useEffect } from 'react';

import style from './contentField.module.css'

export const ContentField = (props) =>{

    const{
        text,
        height,
        isVisible,
        fontSize,
        noMargin,
    }=props;
    const heightField = height ? `${height}rem` : 'auto'

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
            height: show ? heightField : '0rem',
            fontSize: sz !== '' ? `${sz}rem` : sz,
            opacity: show ? '' : '0%',
            padding: show ? '' : '0rem',
            marginTop: show ? '' : '0rem',
            marginTop: noMargin ? '0rem' : show ? '' : '0rem',
            
            }} >
            <span>
                {text}    
            </span>
        </div>
    )
}