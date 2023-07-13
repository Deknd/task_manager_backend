import React, { useEffect, useState, useRef } from "react";
import { useDispatch } from "react-redux";
import { setTasks } from "../../../widgets/ListTaskWidget/taskWidgetSlice";
import DatePicker from "react-datepicker";

import { sortArray } from "../lib/fillterCalendar";
import { splitTaskForDate } from "../lib/splitTaskForDate";
import { splitTaskForPastAndFeature } from "../lib/splitTaskForDate";
import { compareDates } from "../lib/splitTaskForDate";
import { formatDate } from "../lib/splitTaskForDate";

import "react-datepicker/dist/react-datepicker.css";
import style from "./calendarTasks.module.css";

export const CalendarTasks = (props) => {
    const {
        children,
        tasks,
        isSelect,
    } = props;

    const dispatch = useDispatch();
    //выбраная дата
    const [startDate, setStartDate] = useState(new Date());

 
    //функция по сортировки тасков
     const splitMapDateTasks = (array) => {
        const mapDateTasks = splitTaskForDate(array);
        setHighlightWithRanges(splitTaskForPastAndFeature(mapDateTasks));
        return mapDateTasks;
    };


   
    //метод вызывающийся при изменение даты
    const handleChange = (date) => {
        setStartDate(date);
       // setIsOpen(false);
        
    };
    //следит, какая дата выбрана
    useEffect(()=>{
        const mapTasks = splitMapDateTasks(tasks);

        if(isSelect){
            const dateActive = formatDate(startDate);
            const iterator = mapTasks.keys();
            let key = null;
            for (const date of iterator) {
                if(compareDates(dateActive, date)){
                    key=date
                    //dispatch(setTasks(mapTasks.get(date)))
                } 
            }
            console.log('Calendar')
            if(key !== null){
                const result = mapTasks.get(key);
                dispatch(setTasks(sortArray(result) ))
            }else { dispatch(setTasks([])) }
        }
        
    },[startDate, isSelect])

   
    //следит за тем, какие стили должны быть
    const [highlightWithRanges, setHighlightWithRanges ]= useState([]);
      
   

    return (
        <>  
            {children}
            {isSelect && (

                      

                          <DatePicker 
                              calendarClassName={style.datepicker}
                              selected={startDate} 
                              onChange={handleChange} 
                              highlightDates={highlightWithRanges}
          
                              inline 
                          />    
                     
            )}
        </>
    );
}




