import React from "react";
import { ChangePriorityTask, EffectButton } from "../../../../features";
import { ContentField } from "../../../../shared/ui";

//кнопка для смены приоритета
export const Priority = (props) => {
    const{
        priority,
        setPriority,
    }=props;



    return(
        // эффект кнопки(ок)
        <EffectButton>
                    {/* компонент меняющий приоритет (ок) */}
                    <ChangePriorityTask priorityTask={priority} setPriorityTask={setPriority} >
                        {/*компонент для вывода текста (ок)  */}
                        <ContentField text={'change priority'} isVisible={true} height={2} />
                    </ChangePriorityTask>
        </EffectButton>
    )
}