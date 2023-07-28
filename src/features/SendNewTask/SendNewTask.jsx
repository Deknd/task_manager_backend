import React from "react";
import { validate } from "../../shared/lib/validation";
import { useDispatch } from "react-redux";
import { actionTaskSlice } from "../tasksSlice";



export const SendNewTask = (props) => {

    const {
        children,
        task,
    } = props;
    const{
        title, 
        description, 
        
    }=task;

    const dispatch = useDispatch();
    const sendTask = () => {
        if(title.length !== 0 && title.length<40){
            if(validate.isValidSymbol(title)){

                if(validate.isValidSymbol(description)){
                    
                    dispatch(actionTaskSlice.createTask(task))




                }
            }
        }

    }



    return(
        <div onClick={sendTask} >
            {children}
        </div>
    )
}