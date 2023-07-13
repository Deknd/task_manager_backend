import React, { useState } from "react";




export const EffectButton =(props)=> {

    const{
        children,
        isSelect,
        borderRadius,
       
    } = props;


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


return(


    <div style={{
        overflow: 'hidden',

        transform: isActive
         ? `scale(1) translateY(0)`
          : isHovered
           ? `scale(1.02) translateY(-3px)`
            : `scale(1)`,
        transition: `transform 0.1s ease-in-out`,
        cursor: 'pointer',
        position: 'relative',
        boxShadow: isSelect ?'0 0 4px rgba(0, 0, 0, 0.6)' : '',
        borderRadius: borderRadius ? borderRadius : '',
     
        
      }} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave} onMouseDown={handleMouseDown} onMouseUp={handleMouseUp} >
        {children}
    </div>

)
}



