import React  from "react";
import style from './effectButton.module.css'



//создает эффект нажатие и анимацию кнопки
export const EffectButton =(props)=> {

    const{
        children,
        //если выбрана данная кнопка, появляется тень
        isSelect,
        //округление границ
        borderRadius,
       
    } = props;


return(


    <div 
    className={style.effect_button}
    style={{
       
        boxShadow: isSelect ?'0 0 4px rgba(0, 0, 0, 0.6)' : '',
        borderRadius: borderRadius ? borderRadius : '',
     
        
      }}  >
        {children}
    </div>

)
}



