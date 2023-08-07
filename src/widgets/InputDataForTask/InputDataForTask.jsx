import React, { useEffect, useState } from "react";

import { format } from "date-fns";
import { Title, DescriptionAndCalendar, Priority } from './ui'

//виджет для редактирования таска
export const InputDataForTask = (props) => {

    const { 
        //входящие данные о заголовке
        cameTitle,
        //входящие данные о описании
        cameDescription,
        //входячие данные о времени завершения
        cameExpirationDate,
        //входящие данные о приоритете
        camePriority,
        //исходящие данные при изменении
        getData,
     } = props;

//следит за состоянием заголовка
    const [ title, setTitle ] = useState(cameTitle ? cameTitle : '');
//следит за состоянием описании
    const [ description, setDescription ] = useState(cameDescription ? cameDescription : '');
//следит за состоянием времени завершения
    const [ expirationDate, setExpirationDate ] = useState(cameExpirationDate ? new Date(cameExpirationDate) : new Date());
//следит за состоянием приоритете
    const [ priority, setPriority ] = useState(camePriority? camePriority : 'STANDARD');


    //отправляет данные через проперти наверх
    useEffect(()=>{
        getData({ title: title, description: description, expirationDate: format(expirationDate, "yyyy-MM-dd HH:mm"), priority: priority })

    },[ title, description, expirationDate, priority ])

   
    return(
        <div>
            {/* изменяет title и проводит валидацию (ok) */}
            <Title title={title} setTitle={setTitle} />
            {/*  изменяет или календарь, или description( плюс валидация данных ) (ок) */}
            <DescriptionAndCalendar 
                description={description} 
                setDescription={setDescription} 
                expirationDate={expirationDate} 
                setExpirationDate={setExpirationDate} />
        

            {/* Меняет приоритет (ок) */}

            <Priority priority={priority} setPriority={setPriority} />
         
        </div>
    )
}