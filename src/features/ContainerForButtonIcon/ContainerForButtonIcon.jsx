import React from "react";



export const ContainerForButtonIcon = (props) => {
    const {
        buttonIcons,
    } = props;




    return(
        <div
                style={{
                    height: '2.5rem',
                    marginTop: '0.5rem',
                    padding: '0.3rem',        
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