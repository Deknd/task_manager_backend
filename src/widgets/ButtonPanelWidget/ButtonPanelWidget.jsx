import React, { useEffect, useState } from "react";

import {  ButtonForPanel } from './ui'

import { AllActiveTask, TodayActiveTasks, CalendarTasks, DoneTasks, FailedTasks, AddTask } from "../../features";
import { all, today, calendar, done, failed, addTaskImg } from './img'


import style from './buttonPanelWidget.module.css'
import { useSelector } from "react-redux";




//панель кнопок для управление и разделения тасков
export const ButtonPanelWidget = () => {
    const tasks = useSelector((state)=>state.tasks.tasks);
    const [ activeFillter, setActiveFillter ] = useState("AllActiveTask")

    //стиль для поиска элементов по нему
    const buttonForPanelWidget = 'button_for_panel_widget';

    //Добавляет и убирает выделение на активную кнопку
    useEffect(() => {
        const divs = document.querySelectorAll(`.${buttonForPanelWidget}`);
        divs.forEach(div => {
          div.addEventListener('click', function () {
            divs.forEach(dv => dv.classList.remove(style.active));
            this.classList.add(style.active);
          });
        });
      }, []); 

    return(
        
        <div className={style.main_container} >
            {/* отображает кнопку (ОК) */}
            <ButtonForPanel 
            reactComp={AllActiveTask} 
            activeFillters={{activeFillter, setActiveFillter}} 
            buttonForPanelWidget={`${buttonForPanelWidget} ${style.active}`} 
            description='Активные таски' 
            tasks={tasks}
            img={all} />

          {/* отображает кнопку (ОК) */}
            <ButtonForPanel 
            reactComp={TodayActiveTasks} 
            activeFillters={{activeFillter, setActiveFillter}} 
            buttonForPanelWidget={buttonForPanelWidget} 
            description='Задачи на сегодня' 
            tasks={tasks}
            img={today} />

          {/* отображает кнопку (ОК) */}  
            <ButtonForPanel 
            reactComp={CalendarTasks} 
            activeFillters={{activeFillter, setActiveFillter}} 
            buttonForPanelWidget={buttonForPanelWidget} 
            description='Календарь' 
            tasks={tasks}
            img={calendar} />

          {/* отображает кнопку (ОК) */}
            <ButtonForPanel 
            reactComp={DoneTasks} 
            activeFillters={{activeFillter, setActiveFillter}} 
            buttonForPanelWidget={buttonForPanelWidget} 
            description='Выполненые' 
            tasks={tasks}
            img={done} />

        {/* отображает кнопку (ОК) */}
            <ButtonForPanel 
            reactComp={FailedTasks} 
            activeFillters={{activeFillter, setActiveFillter}} 
            buttonForPanelWidget={buttonForPanelWidget} 
            description='Проваленные' 
            tasks={tasks}
            img={failed} />

        {/* Кнопка добавления таска (ОК) */}
            <ButtonForPanel
            reactComp={AddTask}
            description='Добавить таск' 
            img={addTaskImg} 
            colorButtonRBG='168, 218, 220, 0.451'/>
 
        </div>
    );
}