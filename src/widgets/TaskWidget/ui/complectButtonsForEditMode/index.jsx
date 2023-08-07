import React from "react"
import { EffectButton, UpdateTask } from "../../../../features"
import { IconButton_v2 } from "../../../../shared/ui"



export const complectButtonsForEditMode = (taskDataEdit, id, setIsEditMode) =>{

  
    
    return [(
    
            <EffectButton>
            
                <UpdateTask 
                    task={taskDataEdit} id={id} closeEditMode={setIsEditMode} >
                    <IconButton_v2 type={'accept'} textIcon={'ok'} />
                </UpdateTask>
            </EffectButton>  
    
        ), (
    
            <EffectButton>
                
                <div onClick={()=>{setIsEditMode(false)}} >
    
                    <IconButton_v2 type={'cancel'} textIcon={'Отмена'} />
                </div>
                
                
            </EffectButton>
    
        )]}

