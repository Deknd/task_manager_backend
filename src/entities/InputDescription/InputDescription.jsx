import React, { useEffect, useState } from "react";


export const InputDescription = (props) => {

    const {
        getDiscription,
        dataPut,
        clear
    } = props;

    const [ description, setDescription ] = useState(dataPut? dataPut : '');
    const handleChangeDescription = (e) => {
        setDescription(e.target.value);
        if(getDiscription){
            getDiscription(e.target.value);
        }
      };
    
    useEffect(()=> {
        if(clear){
            setDescription('');
            }
     }, [clear])
    



    return(
        <textarea
            value={description}
            onChange={handleChangeDescription}
            placeholder= 'Enter description'

            style={{
                whiteSpace: 'pre-wrap',
                width: '100%',
                height: '13em',
                padding: '0.3em',
                resize: 'none',
                border: 'none',
                borderRadius: '0.3em',
                fontSize: '1em',
                opacity: '60%',
                textAlign: 'center',
                }}
        />
    )
}