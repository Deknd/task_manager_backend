import React from "react";
import { ContentTaskActiveElements } from "../../../../features";
import { InputDataForTask } from "../../../../widgets";
import { ContentTask } from "../../../../entities";


//Выводит на экран данные о таске и инпут поля для изменения 
export const DisplayingData = (props) => {
    const {
        //таск со всеми данными
        taskData,


        //идикатор режима редактированния
        isEditMode,
        //id активного таска
        idActiveTask,
        //метод для активации таска
        setIdActiveTask,
        
        //запись данных для редактирования данных таска
        setTaskDataEdit
    } = props;



    return(
        // Контейнер для контента таска, активирует таск и при включении режима редактирование тоже активируется (ок)
        <ContentTaskActiveElements 
            id={taskData.id}
            isEditMode={isEditMode} 
            isActive={idActiveTask===taskData.id} 
            isBlock={(idActiveTask !== null && idActiveTask !== taskData.id)}
            setIdActiveTask={setIdActiveTask}
        >
            {
                isEditMode ? (
                    // используется в режиме редактирования, служит для получение информации от пользователя (ок)
                    <InputDataForTask 
                        getData={setTaskDataEdit} 
                        cameTitle={taskData.title} 
                        cameDescription={taskData.description} 
                        camePriority={taskData.priority}
                        cameExpirationDate={taskData.expirationDate} />
                

                ):(
                    //выводит информацию таска (ок)
                    <ContentTask 
                    isBlock={(idActiveTask !== null && idActiveTask !== taskData.id)} 
                    isActive={idActiveTask===taskData.id} taskData={taskData}/>
                )
            }
        </ContentTaskActiveElements>
    )
}