import React from "react";

import { useSelector } from "react-redux";



import all  from './img/all.png'
import today from './img/today.png'
import calendar from './img/calendar.png'
import done from './img/done.png'
import failed from './img/failed.png'



import { EffectButton } from "../../features";
import { AllActiveTask, FailedTasks, TodayActiveTasks, CalendarTasks, DoneTasks } from '../../features/FeaturesLeftPanel/index '
import { ActiveOneOfMany } from "../../features";

import { BigButton } from "../../shared/ui";


export const UsedButton = (props) => {

    

    const tasks = useSelector((state)=>state.tasks.tasks);
    
    


    const elements = [
        (isSelect) => {  
            return (
                <EffectButton borderRadius='10px' isSelect={ isSelect ? isSelect : null } >
                    <AllActiveTask tasks={tasks}  >
                        <BigButton img={all} description='Активные таски' colorButtonRBG='250, 237, 205, 0.65' />
                    </AllActiveTask>
                </EffectButton> 
            )
        },
        (isSelect) => {  
            return (
                <EffectButton borderRadius='10px' isSelect={ isSelect ? isSelect : null } >
                    <TodayActiveTasks tasks={tasks}  >
                        <BigButton img={today} description='Задачи на сегодня' colorButtonRBG='250, 237, 205, 0.65' />
                    </TodayActiveTasks>
                </EffectButton> 
            )
        },
        (isSelect) => {
            return(
                <EffectButton borderRadius='10px' isSelect={ isSelect ? isSelect : null } >
                    <CalendarTasks tasks={tasks} isSelect={ isSelect ? isSelect : null }>
                        <BigButton img={calendar} description='Календарь' colorButtonRBG='250, 237, 205, 0.65' />
                    </CalendarTasks>
                </EffectButton>
            )
        },
        (isSelect) => {  
            return (
                <EffectButton borderRadius='10px' isSelect={ isSelect ? isSelect : null } >
                    <DoneTasks tasks={tasks} >
                        <BigButton img={done} description='Выполненые' colorButtonRBG='250, 237, 205, 0.65' />
                    </DoneTasks>
                </EffectButton> 
            )
        },(isSelect) => {  
            return (
                <EffectButton borderRadius='10px' isSelect={ isSelect ? isSelect : null } >
                    <FailedTasks tasks={tasks} >
                        <BigButton img={failed} description='Проваленные' colorButtonRBG='250, 237, 205, 0.65' />
                    </FailedTasks>
                </EffectButton> 
            )
        },
    ]


    return(
        <div style={{borderRadius: '15px'}}>
            <ActiveOneOfMany elements={elements} />
        </div>
           
        
    )
}
