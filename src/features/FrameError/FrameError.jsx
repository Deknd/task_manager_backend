import React from "react";
import { TextDisplay } from "../../shared/ui";


//проводит валидацию и выводит сообщения об ошибках
export const FrameError = (props) => {

    const { 
        children,
        //индикатор коректности данных
        dataCorrect,
        //текст ошибки который нужно вывести
        textError
     } = props;

     //проверяет, приходит ли индикатор ошибки
    const errorData = dataCorrect ? dataCorrect : false;


    return (
        <div style={{
        }} >
            <div style={{
                visibility: errorData ? 'visible': 'hidden',
                color: 'rgb(254, 167, 187)',
                }} >
                    {/* выводит текст (ок)*/}
                <TextDisplay text={textError? textError : ' Error '} fontSize={0.9} />
            </div>
            <div style={{
            borderRadius: '0.5em',
            boxShadow: errorData ? '0 0 6px rgba(254, 167, 187, 1)' : ''
        }} >
                {children}
            </div>
        </div>
    )
}