import React, { useEffect, useState } from "react";
import { InputField } from "../../shared/ui";


// инпут форма для приема данных от пользователя
export const InputData = ( props ) => {

    const { 
        //тип инпут поля
        type,
        // поле что будет отображаться без данных
        placeholder,
        //данные которые должны быть вложены в инпуте
        dataPut,
        //возврат данных после изменения
        getData,
        // инидкатор очистки данных
        clear,
        // ширина
        width,
        //высота
        height,
        //шрифт
        fontSize,
        //нужны ли рамки
        noBorder
     } = props;

     //хранит состояние данных
     const [ data, setData ] = useState(dataPut ? dataPut : '');
     //очищает данные если инидкатор в тру
     useEffect(()=> {
        if(clear){
            setData('');
        }
     }, [clear])

     //метод соханяющий изменения
     const handleDataChange = (e) => {
        setData(e.target.value);
        if(getData){
            getData(e.target.value)
        }
     }




    return (
        <div>
            {/* поле для ввода данных (ok)  */}
            <InputField noBorder={noBorder} type={type} placeholder={placeholder} value={data} onChange={ handleDataChange } width={width ? width : null} height={ height ? height : null} fontSize={fontSize? fontSize : null} />
        </div>
    )
}