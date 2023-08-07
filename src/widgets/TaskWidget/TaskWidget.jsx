import React, { useState, useEffect } from 'react';

import { SceletonForTask, ButtonsForTask, DisplayingData } from './ui';






export const TaskWidget = (props) => {

    

// Просы которые приходят
    const {
        taskData,
        parrentRef,
        idActiveTask,
        setIdActiveTask,
    } = props;

   
    
    const {
        id,
        status,
        priority,
    } = taskData;
    
  
    //состояния внутрение
    const [ statusTask, setStatusTask ] = useState(status);

    const [ priorityTask, setPriorityTask ] = useState(priority);
    

    useEffect(()=>{setPriorityTask(priority)},[priority])
    
    const [ isEditMode, setIsEditMode ] = useState(false);
    const [ taskDataEdit, setTaskDataEdit ] = useState({
        priority: priority
    });

    //следит чтоб приоритет всегда соотвествовал действительности
   
    
    return(

        //Создает контейнер для таска, который умеет смещаться от краев и менять фон (ок)
        <SceletonForTask 
            parrentRef={parrentRef} 
            id={id}
            idActiveTask={idActiveTask}
            isEditMode={isEditMode}
            statusTask={statusTask} 
            priorityTask={priorityTask}
            priorityTaskEdit={taskDataEdit.priority}
        >
                    {/* div для контентной части , еще выводит поля импутов для ввода информации при редактировании таска (ок) */}
                    <DisplayingData
                    taskData={taskData}
                    isEditMode={isEditMode} 
                    idActiveTask={idActiveTask}
                    setIdActiveTask={setIdActiveTask}
                    setTaskDataEdit={setTaskDataEdit}
                    />
                    
                   
                    {/* Фуекциональная часть (ок) */}
                    <ButtonsForTask
                        taskData={taskData}
                        isEditMode={isEditMode}
                        setIsEditMode={setIsEditMode}
                        taskDataEdit={taskDataEdit}
                        idActiveTask={idActiveTask}
                        statusTask={statusTask}
                        priorityTask={priorityTask}
                        setPriorityTask={setPriorityTask}
                        setStatusTask={setStatusTask}
                    />
                    
        </SceletonForTask>

    )
}


   