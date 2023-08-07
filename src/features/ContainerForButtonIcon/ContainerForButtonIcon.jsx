import React from "react";


//Контейнер для кнопок
export const ContainerForButtonIcon = (props) => {
    const {
        buttonIcons,
    } = props;




    return(
        <div
                style={{
                    height: '2.5em',
                    marginTop: '0.5em',
                    padding: '0.3em',        
                }}>
                <div style={{
                    display: 'flex',
                    justifyContent: 'space-around',
                    }}>

                    {
                    buttonIcons ? buttonIcons.map((buttonIcon, index)=>{

                        return(

                            <div key={index} >
                                {buttonIcon}
                            </div>
                        )
                    }) : null}
                    

                </div>

        </div>
    )
}