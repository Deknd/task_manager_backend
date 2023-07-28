import React from "react";
import { TextDisplay } from "../../shared/ui";



export const FrameError = (props) => {

    const { 
        children,
        dataCorrect,
        textError
     } = props;

    const errorData = dataCorrect ? dataCorrect : false;


    return (
        <div style={{
            //paddingTop: '0.1em'
        }} >
            <div style={{
                visibility: errorData ? 'visible': 'hidden',
                color: 'rgb(254, 167, 187)',
               // padding: '0.1em',
                }} >
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