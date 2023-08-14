

import React from 'react';
import { noDescription } from './bigButtonConstants';
import style from './bigButton.module.css'

//Форма большой кнопки
export const BigButton = (props) => {

    const {
        description,
        img,
        colorButtonRBG,
        noImg,
        
    } =props;


    //Описание кнопки( текст )
    const descriptionButton = description ? description : noDescription


  
return (
    <div 
    className={style.container_main_big_button}
    style={{ backgroundColor: colorButtonRBG ? `rgba(${colorButtonRBG}` : 'none', }}>
      {noImg ? 
      <></> 
      : 
      (<div className={style.container_for_img_big_button}>
             {img ? <img 
                      className={style.img_big_button}
                      src={img} 
                      alt={descriptionButton} 
                    /> : null}
        </div>)
        }
        
        <div className={style.container_for_description_big_button}>
            <span>
             {descriptionButton}
            </span>
             
        </div>
    </div>
    );
}