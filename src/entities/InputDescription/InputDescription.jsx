import React, { useEffect, useState } from "react";

//принимает описание от пользователя
export const InputDescription = (props) => {

    const {
        //метод для возврата описание
        getDiscription,
        //принимает описание
        dataPut,
        //инидкатор того, что нужно очистить данные
        clear
    } = props;

    //следит за состоянием описания
    const [ description, setDescription ] = useState(dataPut? dataPut : '');
    //метод обновляющий состояние и возвращающий данные
    const handleChangeDescription = (e) => {
        setDescription(e.target.value);
        if(getDiscription){
            getDiscription(e.target.value);
        }
      };
    //следит за индикатором очистки и очищает данные, если это требуется
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