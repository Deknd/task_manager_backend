import React from "react";
import { useDispatch } from "react-redux";


import { setPriority } from "../../widgets/ListTaskWidget/taskWidgetSlice";

import style from './changePriority.module.css'
import { actionTaskSlice } from "../tasksSlice";




export const ChangePriorityTask = (props) =>{

    const {
        children,
        taskData,
        priority,
        change,
    } = props;

    const {
        id,

    } = taskData || {id: null};




    const dispatch = useDispatch();




    const click = () =>{
        if(change){
            if(priority === 'STANDARD'){
                change('HIGH')
                dispatch(actionTaskSlice.updatePriorityTask({id: id,priority: 'HIGH'}))
            } else {
                change('STANDARD')
                dispatch(actionTaskSlice.updatePriorityTask({id: id,priority: 'STANDARD'}))

            }
            return;
        }


        if(id){

            dispatch(setPriority(id));    }
        }



    return(
        <div className={priority ? style.prioritytask : null} onClick={click}>
            {children}
        </div>
    )
}


  
  