import React from "react";



export const TextDisplay = (props) => {

    const {
        fontSize,
        text
    } = props;

    return(
        <div style={{
            display: 'flex',
            alignContent: 'center',
            justifyContent: 'center',
            alignItems: 'center',
            fontSize: `${fontSize}em`,
            }} >
            {text}
        </div>
    )


}