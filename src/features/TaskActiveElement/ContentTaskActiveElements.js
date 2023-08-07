import React, { useEffect, useState } from "react";




//компонент для активации таска
export const ContentTaskActiveElements = (props)=>{

    const {
        children,
        //id таска
        id,
        // индикатор режима редактирования
        isEditMode,
        //индикатор активности таска
        isActive,
        //индикатор того, что таск заблокирован
        isBlock,
        //активация активного таска
        setIdActiveTask,

    } = props;
    
   
    //индикатор что был включен мод редактирование
    const [ editMode, setEditMode ] = useState(false);
   
    //следит за состоянием режима редактирования
    useEffect(()=> {
        if(isEditMode && !isActive && !isBlock){
            setIdActiveTask(id)
            setEditMode(isEditMode);
        }
        console.log(isEditMode)
        if( isActive && !isBlock && editMode && !isEditMode ){
            setIdActiveTask(null)
            setEditMode(isEditMode);

        }
        
    },[isEditMode, isBlock])


    //активация и дезактивация таска
    const clickTask = ()=>{
        if(!isActive && !isBlock && !isEditMode){
            setIdActiveTask(id)
        }
        if( isActive && !isBlock && !isEditMode ) {
            setIdActiveTask(null)
        }
    }


    return(
        <div 
         style={{cursor: isBlock ? 'default' : 'pointer'}}
         onClick={clickTask}
         >          
            {children}
        </div>
    )
}

