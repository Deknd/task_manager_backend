import React, { useEffect, useState } from "react";

import { useDispatch } from "react-redux";

import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import style from "./calendarTasks.module.css";



import { calendarModuels } from './models/calendarModels'

// фильтрует данные по дате указаной в календаре
export const CalendarTasks = (props) => {
    const {
        children,
        tasks,
        activeFillters,
        

    } = props;
    const{
        activeFillter,
         setActiveFillter
    } = activeFillters;

    const dispatch = useDispatch();
    //выбраная дата
    const [startDate, setStartDate] = useState(new Date());

    //отдает в календарь стили для дат, чтоб потом к ним применились динамические стили
    const [highlightWithRanges, setHighlightWithRanges ]= useState([]);
    
    
    //функция по сортировки тасков
    const [ isOpen, setIsOpen ] = useState(false);
   

    //сортирует таски по датам, добавляет к датам стили, 
    useEffect(()=>{
        const resultMap = calendarModuels.splitMapDateTasks(tasks);
        const  mapTasks = resultMap.mapDateTasks;
        setHighlightWithRanges(resultMap.highlightWithRangesArray);
       
        if(activeFillter === 'CalendarTasks'){

           
            if(isOpen ){
                calendarModuels.changeStyle();

                calendarModuels.dispathSetTask(startDate, mapTasks, dispatch)

            }
        } else {
            setIsOpen(false)
        }
        
    },[startDate, activeFillter, tasks, isOpen,])
    //открывает, закрывает календарь, активирует фильтр
    const onClick = () => {
     setIsOpen(!isOpen);
     setActiveFillter('CalendarTasks')
    }
  
    

    return (
        <div>
            <div onClick={onClick}>
                {children}
            </div>  
            {isOpen && (
                    <DatePicker 
                        calendarClassName={style.datepicker}
                        onMonthChange={calendarModuels.changeStyle}
                        selected={startDate} 
                        onChange={(date)=>setStartDate(date)} 
                        highlightDates={highlightWithRanges}
                        inline 
                    />    
            )}
        </div>
    );
}
