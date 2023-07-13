import React from "react";

import style from './sceletonTaskWidget.module.css'

export const SceletonTaskWidget = (props) => {
    const{
        controller,
        children
    }=props;
    
    
    const {
        handleMouseEnter, 
        handleMouseLeave,
        handleMouseDown,
        handleMouseUp,
        styleChange,
    } = controller;


    return(
        <div className={style.fix_container}>
            {/* див для описание всего скелета таска */}
            <div 
            className={style.content}
            style={styleChange}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            onMouseDown={handleMouseDown}
            onMouseUp={handleMouseUp}
            >
              
                {children}
            </div>
                
        </div>
    )
}
