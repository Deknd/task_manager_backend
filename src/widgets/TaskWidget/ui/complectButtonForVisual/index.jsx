
import { ChangeStatusTask, DeleteTask, EffectButton } from "../../../../features"
import { IconButton_v2 } from "../../../../shared/ui"



export const complectButtonsForVisual = (statusTask, setStatusTask, id, setIsEditMode ) => { 
   
            
    return [(

    <EffectButton>
        <ChangeStatusTask activeStatus={statusTask} setStatus={setStatusTask} forStatus={'DONE'} id={id} >
            <IconButton_v2 type={'positive'}  textIcon={'Done'} />
        </ChangeStatusTask>
    </EffectButton>  

), (

    <EffectButton>
        <ChangeStatusTask activeStatus={statusTask} setStatus={setStatusTask} forStatus={'FAILED'} id={id} >
            <IconButton_v2 type={'negative'}  textIcon={'Failed'} />
        </ChangeStatusTask>
    </EffectButton>

),(

    <EffectButton>
        <div onClick={()=>{setIsEditMode(true)}} >
            <IconButton_v2 type={'edit'}  textIcon={'Edit'} />
        </div>
    </EffectButton>  

), (

    <EffectButton>
        <DeleteTask idTask={id} >
            <IconButton_v2 type={'garbage'}  textIcon={'Delete'} />
        </DeleteTask>
    </EffectButton>

)]
}