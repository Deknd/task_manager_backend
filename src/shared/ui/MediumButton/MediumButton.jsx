import React from "react";


export const MediumButton = (props)=>{

    const {
        description,
        img,
    } = props;

    return(
        <div style={{
            display: 'flex',
            width: '8rem',
            height: '2.1rem',
            alignContent: 'center',
            justifyContent: 'center',
            alignItems: 'center',
            borderRadius: '10px',
            backgroundColor: '#A8DADC45',
            marginLeft: '1rem',
            userSelect: 'none',
            
            paddingLeft: '0.5rem',
            paddingRight: '0.5rem',
            }}>
            <div style={{
                margin: '0.2rem'
            }} >
                <img style={{
                    width: '1.4rem',
                    height: '1.4rem'
                }} src={img} alt={description} />
            </div>
            <div> {description} </div>
        </div>
    )



}