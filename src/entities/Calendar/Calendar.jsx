import React, { useEffect, useState } from "react";


import ReactDatePicker from "react-datepicker";

import styles from './calendar.module.css'; 

//календарь
export const Calendar = (props) => {

    const {
        //метод для возврата данных
        getDate,
        //данные принятые 
        startDate,
    } = props;

    //метод обновляющий данные
    const handleChangeDate = (e) => {
        setStartDateCalendar(e)
        if(getDate) {
            getDate(e);
        }
      };
     
      //состояние данных 
    const [ startDateCalendar, setStartDateCalendar ] = useState(startDate)
    return(
        <ReactDatePicker
            selected={startDateCalendar}
            onChange={handleChangeDate}
            calendarClassName={styles.calendar}
            dayClassName={(date)=> true ?  styles.day : undefined}
            showTimeSelect
            timeFormat="HH:mm"
            timeIntervals={30}
            timeClassName={(date) => true ? styles.time : undefined}
            minDate={new Date()}
            inline 
        />
    )
}