import React from "react";



export const UpdateUser = (props) => {

    const {
        children
    } = props;

    const click = () => {
        console.log('Update User')
    }


    return(
        <div onClick={click} >
            {children}
        </div>
    )
}