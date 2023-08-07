import React from "react";
import { ChangePriorityTask, ContainerForButtonIcon } from "../../../../features";
import { ContentField } from "../../../../shared/ui";
import { complectButtonsForEditMode } from "../complectButtonsForEditMode";
import { complectButtonsForVisual } from "../complectButtonForVisual";

//Выводит на экран функциональные кнопки
export const ButtonsForTask = (props) => {

    const {
        //данные таска
        taskData,
        //индикатор режима редактирования
        isEditMode,
        //метод включение и выключения режима редактирования
        setIsEditMode,
        //обновленые данные пользователя из режима редактирования
        taskDataEdit,
        //айди активного таска
        idActiveTask,
        //статус таска
        statusTask,
        //приоритет таска
        priorityTask,
        //метод для изменения приоритета таска
        setPriorityTask,
        //метод для изменения статуса таска
        setStatusTask,

    } = props;



    return(
        <div>
                {/* Кнопка изменения приоритета таска (ок)*/}
                <ChangePriorityTask 
                    taskData={taskData} 
                    setPriorityTask={setPriorityTask} 
                    priorityTask={priorityTask} 
                    isActive={idActiveTask===taskData.id}>
                        {/* Если не включен режим редакитроваия и таск активный выводится даная кнопка (ок) */}
                     {idActiveTask===taskData.id && !isEditMode ?( 
                        <ContentField text={'change priority'} isVisible={idActiveTask===taskData.id} height={2} />
                    )  : null}
                </ChangePriorityTask>
            <div style={{                   
                paddingBottom: '0.4em',
                height: '3em',
        
                }}>
                    {/* Отображает панель с кнопками, Если таск заблокирован, панель кнопок не появляется */}
                    {!(idActiveTask !== null && idActiveTask !== taskData.id) ? (
                        //контенер для кнопок (ок)
                        <ContainerForButtonIcon buttonIcons={ 
                            isEditMode ? 
                                complectButtonsForEditMode( taskDataEdit, taskData.id, setIsEditMode )
                                : 
                                complectButtonsForVisual( statusTask, setStatusTask, taskData.id, setIsEditMode ) } />
                        ) : null}
            </div>
        </div>
    )
}