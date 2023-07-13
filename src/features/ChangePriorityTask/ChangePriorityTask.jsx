import React from "react";
import { useDispatch } from "react-redux";


import { setPriority } from "../../widgets/ListTaskWidget/taskWidgetSlice";

import style from './changePriority.module.css'




export const ChangePriorityTask = (props) =>{

    const {
        children,
        taskData,
    } = props;

    const {
        id,

    } = taskData;




    const dispatch = useDispatch();




    const click = () =>{
        dispatch(setPriority(id));    }



    return(
        <div className={style.prioritytask} onClick={click}>
            {children}
        </div>
    )
}


  
  