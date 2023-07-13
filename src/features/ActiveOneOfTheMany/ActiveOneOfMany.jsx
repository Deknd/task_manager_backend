import React, { useEffect, useState } from "react";


export const ActiveOneOfMany = (props)=> {

    const {
        elements
    } = props;

    const [ selectElement, setSelectElement ] = useState(0);

    const click = (index) => {
        setSelectElement(index)
    }

    

    return (
        <div>
            {
                elements.map((el, index)=>{
                    return (
                        <div style={{
                            margin: '0.5rem',
                            
                            }} key={index} onClick={() => {click(index)}}>
                            {el(selectElement === index)}
                        </div>
                    )
                })
            }
        </div>
    )
}