

import React, { useState, useEffect } from 'react';
import style from "./bigButton.module.css"
import {
	scale_onClick_Active,
	scale_onHover_Active,
	scale_AtRest,
	transformation_Time,
	margin_OnClick,
	margin_atRest,
    widthShadow,
    opacityShadow,
    colorShdowRBG,
    textWhenNoFunction,
    noDescription,
    noColorButtonRBG,
 } from './bigButtonConstants';
import { Link, useLocation } from 'react-router-dom';


export const BigButton = (props) => {

    const {
        toRoute,
        description,
        img,
        onClickSent,
        colorButtonRBG,
    } =props;

    

    //ХУк для проверки, активена ли данная кнопка, если да, то выделяет ее тенью
    const location = useLocation();
    useEffect(() => {
        const currentPath = location.pathname;
        if (currentPath.endsWith(`/${toRoute}`)) {
            setSelect(true)
          } else setSelect(false) 
	 }, [location]);



    //Хук для слежения выбран данный компонент или нет
    const [isSelect, setSelect] = useState(false);
    const descriptionButton = description ? description : noDescription
    const colorButtonRBGSet = colorButtonRBG ? colorButtonRBG : noColorButtonRBG;




    //Метод обработки нажатия кнопки
    const shouldDoOnClick =!onClickSent && !toRoute ? ()=>{console.log(textWhenNoFunction,description)} : onClickSent

    //Хуки для слежения взаимодействия с кнопкой
    const [isHovered, setIsHovered] = useState(false);
    const [isActive, setIsActive] = useState(false);

  const handleMouseEnter = () => {
    setIsHovered(true);
  };

  const handleMouseLeave = () => {
    setIsHovered(false);
    setIsActive(false); 
  };

  const handleMouseDown = () => {
    setIsActive(true);
  };

  const handleMouseUp = () => {
    setIsActive(false);
  };



  //Изменяемый стиль, для кнопки
  const imageStyle = {
    transform: isActive
     ? `scale(${scale_onClick_Active})`
      : isHovered
       ? `scale(${scale_onHover_Active})`
        : `scale(${scale_AtRest})`,
    transition: `transform ${transformation_Time}s ease-in-out`,
    paddingTop: isActive ? `${margin_OnClick}px` : `${margin_atRest}`,
    boxShadow: isSelect ? `0 0 ${widthShadow}px rgba(${colorShdowRBG}, ${opacityShadow})` : 'none',
    backgroundColor: `rgba(${colorButtonRBGSet})`,
  };



return (
    <Link 
    to={toRoute}
    className={style.big_button} 
    role="button"
    onClick={shouldDoOnClick} 
    onMouseEnter={handleMouseEnter} 
    onMouseLeave={handleMouseLeave} 
    onMouseDown={handleMouseDown} 
    onMouseUp={handleMouseUp}
    style={imageStyle}>
        <div className={style.img_big_button}>
             {img ? <img className={style.image} src={img} alt={descriptionButton} /> : null}
        </div>
        <div className={style.text_big_button}>
            <span>
             {descriptionButton}
            </span>
             
        </div>
    </Link>
    );
}