import React from "react";
import { useDispatch } from "react-redux";

import { setIsNeedAddTask } from "../../../widgets/ListTaskWidget/taskWidgetSlice";




//фильтрует(все активные таски) и посылает в ListTaskWidget таски для отображения
export const AddTask = (props) => {

    const {
        children,
    } = props;


    const dispatch = useDispatch();
    //Ставит в состояние активности данный фильтр и отправляет в таск виджет отфильтрованные таски
    const onClick = ()=>{
        dispatch(setIsNeedAddTask());
        
    }

    return(
        <div onClick={onClick} >
            {children}
        </div>

    )
}





