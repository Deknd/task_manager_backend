import React from "react";
import { BackgroundTask, ShiftTask, SizeTask } from "../../../../features";

//Контейнер для таскаов, умеет смещаться от краев, менять размер и взависимости от статуса и приоритета меняет фон
export const SceletonForTask = (props) => {

    const{
        id,
        idActiveTask,
        //вложенные компоненты
        children,
        //родительский компонент, нужен для определения краев, чтоб таск не заходил за края
        parrentRef,
       
        isEditMode,
       
        //статус таска
        statusTask,
        //приоритет таска
        priorityTask,
        //приоритет таска в режиме редактирования
        priorityTaskEdit


    } = props;




    return(
        //для смещения таска от краев
        <ShiftTask parrentRef={parrentRef} isActive={ idActiveTask===id } >
            {/* для изменения размера таска при активации */}
            <SizeTask isActive={idActiveTask===id} isBlock={(idActiveTask !== null && idActiveTask !== id)} >
                {/* Для выбора фона таска */}
                <BackgroundTask isEditMode={isEditMode} status={statusTask} priority={isEditMode? priorityTaskEdit : priorityTask} >
                    {children}
                </BackgroundTask>
            </SizeTask>
        </ShiftTask>
    )
}