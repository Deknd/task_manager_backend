import React, { useEffect, useRef, useState } from "react";


export const ContainerForButton = (props)=>{


    const containerRef = useRef(null);
    const [height, setHeight] = useState(0);

    useEffect(() => {
    const calculateHeight = () => {
      if (containerRef.current) {
        const { top } = containerRef.current.getBoundingClientRect();
        const windowHeight = window.innerHeight;
        
        const heightt = windowHeight - top;

        setHeight(heightt);
      }
    };

    calculateHeight();

    window.addEventListener("resize", calculateHeight);

    return () => {
      window.removeEventListener("resize", calculateHeight);
    };
    }, []);



    return(
        <div 
        ref={containerRef}
        style={{
            height: height-1,
            width: '21rem',
            padding: '0.5rem',
            borderRight: "3px solid #00000030"
         }}>
            {props.children}
        </div>
    )
}