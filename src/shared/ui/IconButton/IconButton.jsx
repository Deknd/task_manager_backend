import React, { useState, useEffect } from 'react';
import style from './IconButton.module.css';
import {
	scale_onClick_Active,
	scale_onHover_Active,
	scale_AtRest,
	transformation_Time,
	margin_OnClick,
	margin_atRest,
	emptyText,
	textWhenNoFunction
 } from './IconButtonConstants';
// Кнопка - иконка, для использования в тасках. 
// Принимает:   img - иконка, которую нужно отобразить; 
//              onClickSent - функция, которая выполняется при нажатии;
//              textIcon - текст который используется если изображение не доступно.
// Отображает иконку, когда на нее наводится указатель мыши, иконка увеличивается.
// При нажатие на кноку, изображение уменьшается и смещается вниз
// Если изображения не загрузятся, будет выводится описание(textIcon) данной кнопки со всей функциональности

export const IconButton = (props) => {

  const { 
    img, 
    onClickSent, 
    textIcon 
  } = props;

  

  //Проверяет, есть ли изображение в пропсах, если есть тру
	const [isImageLoaded, setIsImageLoaded] = useState(false);
	useEffect(() => {
		img ? setIsImageLoaded(true) : setIsImageLoaded(false);
	 }, [img]);

	//проверяет, передается ли сообщение в пропсах, если да то выводит сообщение из пропс, если нет, то выдает сообщение 'Empty text'
	//const [isTextIsEmpty, setIsTextIsEmpty] = useState(false);
	useEffect(()=>{
		!isImageLoaded && !textImage ? setTextImage(emptyText) : setTextImage(textIcon)
	},[isImageLoaded]);


	//Текст для вывода вместо кнопки
	const [textImage, setTextImage] = useState(textIcon);

	//Функция для обработки события
	const shouldDoOnClick = 
		onClickSent ? onClickSent : ()=>{console.log(textWhenNoFunction)}
	

 

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

  const imageStyle = {
    transform: isActive ? `scale(${scale_onClick_Active})` : isHovered ? `scale(${scale_onHover_Active})` : `scale(${scale_AtRest})`,
    transition: `transform ${transformation_Time}s ease-in-out`,
    paddingTop: isActive ? `${margin_OnClick}px` : `${margin_atRest}`,
  };
  
//
  return (
    <div className={style.container}>
      <div role="button" onClick={shouldDoOnClick} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave} onMouseDown={handleMouseDown} onMouseUp={handleMouseUp}>
         {isImageLoaded ? <img src={img} alt={textImage} style={imageStyle} /> : <p className={style.textNoImg} style={imageStyle}>{textImage}</p>}
        
      </div>
    </div>
  );
};
