import React from "react";


//выводит текст на экран
export const TextDisplay = (props) => {

    const {
        //размер шрифта в em
        fontSize,
        //текст для вывода
        text
    } = props;

    return(
        <div style={{
            display: 'flex',
            alignContent: 'center',
            justifyContent: 'center',
            alignItems: 'center',
            fontSize: fontSize ? `${fontSize}em` : '1em',
            }} >
            {text}
        </div>
    )


}