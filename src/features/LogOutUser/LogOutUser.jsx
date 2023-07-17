import React from "react";



export const LogOutUser = (props) => {

    const {
        children
    } = props;

    const click = () => {
        console.log('LogOutUser')
    }


    return(
        <div onClick={click} >
            {children}
        </div>
    )
}