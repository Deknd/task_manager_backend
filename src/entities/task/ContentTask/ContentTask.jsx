import React from 'react';
import { ContentField } from '../../../shared/ui';
import { format } from 'date-fns';


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

    const date = new Date(expirationDate);
    const resDate = format(date, ' dd/MM/yyyy  HH:mm  ')

 {/* div для контентной части */}
    return(
         <div 
         >

             {/* Див для отображения title */}
             <ContentField text={title} isVisible={true} height={3} fontSize={1.3} noMargin={true} />
             
             {/* div для description */}
             {!isBlock ? <ContentField text={description} isVisible={isActive} height={13}  /> : null}
 
             {/* Отображение даты  */}
             <ContentField text={resDate} isVisible={true} height={2} />
         
         </div>
    );
}