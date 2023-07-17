import React, { useEffect, useState } from "react";


export const ChangeSize = (props) => {

   const {
        children,
        widthParam,

    } = props;

    const { 
      startFontSize,
      endFontSize,

    } = widthParam;


    const startWidth = 1536;
    const endWidth = 412;
   

    const startHeight = 754;
    const endHeight = 335;
    const startPadding = 10;
    const endPadding = 0;




    const [size, setSize] = useState({
        height: 0,
        width: 0,
        fontSize: 0,
        padding: 0,
    });

    const calculateSize = () => {
       const height = window.innerHeight;
       const width = window.innerWidth;
       const fontSize = calculateFontSize(width);
       const padding = calculatePadding(height);
          setSize({
            height: height,
            width: width,
            fontSize: fontSize,
            padding: padding,
          });
      };
      const calculateFontSize = (targetWidth) => {
       
      
        const fontSize = ((targetWidth - startWidth) * (endFontSize - startFontSize) / (endWidth - startWidth)) + startFontSize;
      
        return fontSize;
      };
      const calculatePadding = (targetHeight) => {
        
        const padding = ((targetHeight - startHeight) * (endPadding - startPadding) / (endHeight- startHeight)) + startPadding;
      
        return padding;
      };


      useEffect(() => {
        
        calculateSize();
        

      window.addEventListener("resize", calculateSize);


      return () => {
      window.removeEventListener("resize", calculateSize);
   };
  }, []);


    return (
        <div style={{
            display: 'flex',
            alignContent: 'center',
            justifyContent: 'center',
            alignItems: 'center',
            width: '100%',
            height: `100dvh`,
            fontSize: `${size.fontSize}px`, 
            }} >
            <div  style={{paddingBottom: `${size.padding}rem`,}}>
                    {children}
            </div>
        </div>
    )
}