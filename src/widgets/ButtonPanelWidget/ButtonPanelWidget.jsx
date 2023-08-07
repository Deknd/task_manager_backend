import React from "react";
import { complectButtonForUserPanelWidget } from './ui'
import { UsedButton } from "../";

import { ContainerForButton } from "../../entities";
import { Footer } from "../../entities";

import addTaskImg from './addTaskImg.png'
import { ActiveOneOfMany, EffectButton } from "../../features";
import { BigButton } from "../../shared/ui";
import { useDispatch, useSelector } from "react-redux";
import { setIsNeedAddTask } from "../ListTaskWidget";

//панель для управление и разделения тасков
export const ButtonPanelWidget = () => {
    const dispatch = useDispatch();
    const tasks = useSelector((state)=>state.tasks.tasks);


    


    return(

        //контейнер для кнопок (ок) TODO 
        <ContainerForButton>

             <div style={{borderRadius: '15px'}}>
            <ActiveOneOfMany elements={complectButtonForUserPanelWidget(tasks)} />
            </div>
            <div style={{margin: '0.5em'}}>
                <EffectButton isSelect={null} borderRadius='10px'  >
                    <div onClick={()=>{dispatch(setIsNeedAddTask())}} >
                        <BigButton img={addTaskImg}  description='Добавить таск' colorButtonRBG='168, 218, 220, 0.451' />
                    </div>
                </EffectButton>
            </div>
            <Footer/>
            
        </ContainerForButton>
    );
}