import React, { useState } from "react";


import {  CloseActiveTask, ContainerForButtonIcon, EffectButton, FormForAddNewTask,  SendNewTask } from "../../features";
import { IconButton_v2 } from "../../shared/ui";

import { InputDataForTask } from "../../widgets";


export const AddNewTask = () => {

    const [ taskData, setTaskData ] = useState({
        title: '',
        description: '',
        expirationDate: new Date(),
        priority: '',
        status: 'TODO'
    })
   
  
      return(
        
                    
            <FormForAddNewTask priority={taskData.priority} >
                {/* Как участок получения данных */}
                <InputDataForTask getData={setTaskData} />
                {/* Кнопки можно перенести тоже */}
               

                {/* Можно отделить, как отдельный участок */}
                <ContainerForButtonIcon buttonIcons={[(

                    <EffectButton>
                    
                        <SendNewTask 
                            task={taskData} >
                            <IconButton_v2 type={'accept'} textIcon={'ok'} />
                        </SendNewTask>
                    </EffectButton>  

                ), (

                    <EffectButton>
                        
                        <CloseActiveTask>
                            <IconButton_v2 type={'cancel'} textIcon={'Отмена'} />
                        </CloseActiveTask>
                        
                    </EffectButton>

                )]} />
            

            </FormForAddNewTask>

       
        
    )
}