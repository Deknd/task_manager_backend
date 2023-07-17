import React from "react";
import { TextDisplay } from "../../shared/ui";


export const SceletonStartWidget = (props) => {


    const {
        children,
        text1,
        text2,
        text3,
    } = props;




    return(
        <div>

                <div >
                    <TextDisplay text={text1} fontSize={1.7} />
                    <TextDisplay text={text2} fontSize={3} />
                    <TextDisplay text={text3} fontSize={1.7} />
                </div>
                <div>
                    {children}
                </div> 
        </div>
        
    )
}