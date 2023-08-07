import React from 'react';
import { ContentField } from '../../../shared/ui';
import { format } from 'date-fns';

//отображает информацию из таска
export const ContentTask = (props)=>{

    const {
        //данные таска
        taskData,
        //блокирован или нет таск
        isBlock,
        //активный или нет таск
        isActive
        
    } = props;

    const {
        title,
        description,
        expirationDate,
       

    } = taskData;

    
 {/* div для контентной части */}
    return(
         <div 
         >

             {/* Див для отображения title (ок) */}
             <ContentField text={title} isVisible={true} height={2.5} fontSize={1.2} noMargin={true} />
             
             {/* div для description (ок) */}
             {!isBlock ? <ContentField text={description} isVisible={isActive} height={13}  /> : null}
 
             {/* Отображение даты (ок) */}
             <ContentField text={ format(new Date(expirationDate), ' dd/MM/yyyy  HH:mm  ')} isVisible={true} height={1.8} />
         
         </div>
    );
}