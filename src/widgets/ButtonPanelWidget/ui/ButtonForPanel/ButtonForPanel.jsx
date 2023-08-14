import React from "react";
import { EffectButton } from "../../../../features";
import { BigButton } from "../../../../shared/ui";


export const ButtonForPanel = (props) => {
    const {
        reactComp,
        activeFillters,
        buttonForPanelWidget,
        description,
        tasks,
        img,
        colorButtonRBG
    } = props;
    
    const ReactComp = reactComp;
    const color = colorButtonRBG ? colorButtonRBG : '250, 237, 205, 0.65';


    return(
        <div style={{margin: '0.5em'}}>
            {/* Вызывает эффект нажатия кнопки */}
            <EffectButton useClassName={buttonForPanelWidget} borderRadius='10px' >
                {/* Фильтр или компонент с логикой */}
                <ReactComp tasks={tasks} activeFillters={activeFillters} >
                    {/* Вид кнопки */}
                    <BigButton img={img} description={description} colorButtonRBG={color} />
                </ReactComp>
            </EffectButton> 
        </div>
    )


}