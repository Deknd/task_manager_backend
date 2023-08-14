import React  from "react";
import style from './effectButton.module.css'



//создает эффект нажатие и анимацию кнопки
export const EffectButton =(props)=> {

    const{
        children,
        //если выбрана данная кнопка, появляется тень
        useClassName,
        //округление границ
        borderRadius,
       
    } = props;


//Стили для кнопки
    const buttonClassName = `${style.effect_button} ${useClassName}`;

return(


    <div 
    className={buttonClassName}
    style={{
       
        borderRadius: borderRadius ? borderRadius : '',
     
        
      }}  >
        {children}
    </div>

)
}



