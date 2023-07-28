import React, { useEffect, useState } from "react";


export const SelectElement = (props) => {

    const { 
        elements,
        openElement,
     } = props;

     const [ vision, setVision ] = useState();
     useEffect(()=>{
        if(elements)
        setVision(elements[openElement])
     },[openElement])





    return(
        <div style={{
            borderRadius: '0.3em', 

            }}>
             
                {vision}
            </div>
    )
}