import React, { useEffect, useRef, useState } from "react";

import { UsedButton } from "../";

import { ContainerForButton } from "../../entities";
import { Footer } from "../../entities";

import addTaskImg from './addTaskImg.png'
import { EffectButton } from "../../features";
import { BigButton } from "../../shared/ui";
import { useDispatch } from "react-redux";
import { setIsNeedAddTask } from "../ListTaskWidget";


export const ButtonPanelWidget = () => {
    const dispatch = useDispatch();


    


    return(
        <ContainerForButton>
            <UsedButton/>
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