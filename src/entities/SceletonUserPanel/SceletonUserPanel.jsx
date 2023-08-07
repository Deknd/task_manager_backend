import React from "react";

import style from './sceletonUserPanel.module.css'

//создает контойнер для пользовательского интерфейса
export const SceletonUserPanel = (props) => {

    const {
        children,
        userName,
    }=props;

    return(
        <div className={style.main_container} >
            <div className={ style.user_name } >
                <span>Hello,</span>
                <div className={ style.name } > 
                    {userName}
                </div>   
            </div>
            <div className={ style.other } >
                {children} 
            </div>
        </div>
    )

}