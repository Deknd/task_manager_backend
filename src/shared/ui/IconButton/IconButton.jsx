import React, { useState, useEffect } from 'react';
import style from './IconButton.module.css';
// Кнопка - иконка, для использования в тасках. 
// Принимает:   img - иконка, которую нужно отобразить; 
//              onClick - функция, которая выполняется при нажатии;
//              textIcon - текст который используется если изображение не доступно.
// Отображает иконку, когда на нее наводится указатель мыши, иконка увеличивается.
// При нажатие на кноку, изображение уменьшается и смещается вниз
// Если изображения не загрузятся, будет выводится описание данной кнопки со всей функциональности

export const IconButton = (props) => {

const { 
    img, 
    onClick, 
    textIcon 
} = props;

    const scale_onClick_Active = 0.9; 
    const scale_onHover_Active = 1.1;
    const scale_AtRest = 1;

    const transformation_Time = 0.1;

    const margin_OnClick = 2;
    const margin_atRest = 0;

    const [isImageLoaded, setIsImageLoaded] = useState(false);

  useEffect(() => {
    img ? setIsImageLoaded(true) : setIsImageLoaded(false);
  }, [img]);

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
  

  return (
    <div className={style.container}>
      <div role="button" onClick={onClick} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave} onMouseDown={handleMouseDown} onMouseUp={handleMouseUp}>
         {isImageLoaded ? <img src={img} alt={textIcon} style={imageStyle} /> : <label className={style.textNoImg} style={imageStyle}>{textIcon}</label>}
        
      </div>
    </div>
  );
};
