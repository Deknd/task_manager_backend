import { createAction } from "@reduxjs/toolkit";
import React from "react";
import { useDispatch } from "react-redux";


//делает логаут пользователя
export const LogOutUser = (props) => {


    const dispatch = useDispatch();

    const {
        children
    } = props;

    const click = () => {
        dispatch(logout());
    }


    return(
        <div onClick={click} >
            {children}
        </div>
    )
}

const logout = createAction('logout_user');