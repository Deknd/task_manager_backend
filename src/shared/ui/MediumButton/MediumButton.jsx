import React from "react";

import style from './mediumButton.module.css'

//отображает текст и изображение
export const MediumButton = (props)=>{

    const {
        description,
        img,
    } = props;

    return(
        <div className={ style.main_container_for_medium_button } >
            <div className={ style.for_img_container } >
                <img className={ style.img_stuly } src={img} alt={description} />
            </div>
            <div> {description} </div>
        </div>
    )



}