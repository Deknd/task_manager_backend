import React from 'react';
import { ContentField } from '../../../shared/ui';


export const ContentTask = (props)=>{

    const {
        taskData,
        
    } = props;

    const {
        title,
        description,
        expirationDate,
        isBlock,
        isActive

    } = taskData;


 {/* div для контентной части */}
    return(
         <div 
         >

             {/* Див для отображения title */}
             <ContentField text={title} isVisible={true} height={3} fontSize={1.3} noMargin={true} />
             
             {/* div для description */}
             {!isBlock ? <ContentField text={description} isVisible={isActive} height={13}  /> : null}
 
             {/* Отображение даты  */}
             <ContentField text={expirationDate} isVisible={true} height={2} />
         
         </div>
    );
}