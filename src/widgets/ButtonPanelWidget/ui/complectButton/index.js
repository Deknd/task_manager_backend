import { EffectButton } from "../../../../features"
import { AllActiveTask } from "../../../../features/FeaturesLeftPanel/AllActiveTask"
import { CalendarTasks } from "../../../../features/FeaturesLeftPanel/CalendarTasks"
import { DoneTasks } from "../../../../features/FeaturesLeftPanel/DoneTasks"
import { FailedTasks } from "../../../../features/FeaturesLeftPanel/FailedTasks"
import { TodayActiveTasks } from "../../../../features/FeaturesLeftPanel/TodayActiveTasks"
import { BigButton } from "../../../../shared/ui"

import all  from '../../img/all.png'
import today from '../../img/today.png'
import calendar from '../../img/calendar.png'
import done from '../../img/done.png'
import failed from '../../img/failed.png'



export const complectButtonForUserPanelWidget = (tasks)=>
{





return [
    (isSelect) => {  
        return (
            <EffectButton borderRadius='10px' isSelect={ isSelect ? isSelect : null } >
                <AllActiveTask tasks={tasks} isSelect={isSelect}  >
                    <BigButton img={all} description='Активные таски' colorButtonRBG='250, 237, 205, 0.65' />
                </AllActiveTask>
            </EffectButton> 
        )
    },
    (isSelect) => {  
        return (
            <EffectButton borderRadius='10px' isSelect={ isSelect ? isSelect : null } >
                <TodayActiveTasks tasks={tasks} isSelect={isSelect}  >
                    <BigButton img={today} description='Задачи на сегодня' colorButtonRBG='250, 237, 205, 0.65' />
                </TodayActiveTasks>
            </EffectButton> 
        )
    },
    (isSelect) => {
        return(
            <EffectButton borderRadius='10px' isSelect={ isSelect ? isSelect : null } >
                <CalendarTasks tasks={tasks}  isSelect={ isSelect ? isSelect : null }>
                    <BigButton img={calendar} description='Календарь' colorButtonRBG='250, 237, 205, 0.65' />
                </CalendarTasks>
            </EffectButton>
        )
    },
    (isSelect) => {  
        return (
            <EffectButton borderRadius='10px' isSelect={ isSelect ? isSelect : null } >
                <DoneTasks tasks={tasks} isSelect={isSelect}  >
                    <BigButton img={done} description='Выполненые' colorButtonRBG='250, 237, 205, 0.65' />
                </DoneTasks>
            </EffectButton> 
        )
    },(isSelect) => {  
        return (
            <EffectButton borderRadius='10px' isSelect={ isSelect ? isSelect : null } >
                <FailedTasks tasks={tasks} isSelect={isSelect} >
                    <BigButton img={failed} description='Проваленные' colorButtonRBG='250, 237, 205, 0.65' />
                </FailedTasks>
            </EffectButton> 
        )
    }, 
]}