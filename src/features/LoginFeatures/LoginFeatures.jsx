import React from "react";
import { validate } from "../../shared/lib/validation";
import { useDispatch } from "react-redux";
import { actionAuthRegistrSlice } from "../../features";




export const LoginFeatures = (props) => {

    const {
        children,
        user,
    } = props;

    const dispatch = useDispatch();

    const handleSubmit = (e) => {
        e.preventDefault();

        const isNotEmpty = Object.values(user).every((val) => val);

        if (!isNotEmpty) return;
        console.log('Click Login Form: ',user)

        dispatch(
          actionAuthRegistrSlice.loginUser(user)
        );
      };
  

    return(
        <div onClick={handleSubmit} >
            {children}
        </div>
    )
}