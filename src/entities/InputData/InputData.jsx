import React, { useEffect, useState } from "react";
import { InputField } from "../../shared/ui";



export const InputData = ( props ) => {

    const { 
        type,
        placeholder,
        getData,
        clear,
     } = props;


     const [ data, setData ] = useState('');
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
            <InputField type={type} placeholder={placeholder} value={data} onChange={ handleDataChange } />
        </div>
    )
}