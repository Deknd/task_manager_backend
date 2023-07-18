import React from "react";
import { validate } from "../../shared/lib/validation";
import { useDispatch } from "react-redux";
import { actionAuthRegistrSlice } from "../../features";




export const RegistrationFeatures = (props) => {

    const {
        children,
        user,
    } = props;

    const dispatch = useDispatch();
    const { name, username, password, confirmation } = user;

    const handleSubmit = (event) => {
        event.preventDefault();
  
      const user = {
        name: "",
        username: "",
        password: "",
      };
  
      if (password === confirmation) {
          if(validate.isValidSymbol(name)){
              user.name = name;
          }else return;
          if(validate.validateEmail(username)){
              user.username = username;
          }else return;
        user.password = password;
  
        const isNotEmpty = Object.values(user).every((val) => val);
        if (!isNotEmpty) return;
          dispatch(actionAuthRegistrSlice.registrationUser(user))
      } else {
        return;
      }
      };
  

    return(
        <div onClick={handleSubmit} >
            {children}
        </div>
    )
}