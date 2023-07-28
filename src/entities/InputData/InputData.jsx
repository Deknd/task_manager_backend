import React, { useEffect, useState } from "react";
import { InputField } from "../../shared/ui";



export const InputData = ( props ) => {

    const { 
        type,
        placeholder,
        dataPut,
        getData,
        clear,
        width,
        height,
        fontSize,
        noBorder
     } = props;


     const [ data, setData ] = useState(dataPut ? dataPut : '');
     useEffect(()=> {
        if(clear){
            setData('');
        }
     }, [clear])

     const handleDataChange = (e) => {
        setData(e.target.value);
        if(getData){
            getData(e.target.value)
        }
     }




    return (
        <div>
            <InputField noBorder={noBorder} type={type} placeholder={placeholder} value={data} onChange={ handleDataChange } width={width ? width : null} height={ height ? height : null} fontSize={fontSize? fontSize : null} />
        </div>
    )
}