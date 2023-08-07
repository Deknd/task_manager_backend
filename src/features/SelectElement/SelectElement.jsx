import React, { useEffect, useState } from "react";

//выводит один комплект кнопок 
export const SelectElement = (props) => {

    const { 
        //массив кнопок
        elements,
        //индекс элемента, который нужно открыть
        openElement,
     } = props;
     //состояние с элементом для отображения
     const [ vision, setVision ] = useState();
     //обновляет элемент в состоянии если меняется индекс для открытия
     useEffect(()=>{
        if(elements)
        setVision(elements[openElement])
     },[openElement])





    return(
        <div style={{
            borderRadius: '0.3em', 

            }}>
             
                {vision}
            </div>
    )
}