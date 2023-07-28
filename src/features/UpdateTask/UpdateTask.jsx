import { format } from "date-fns";
import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionTaskSlice } from "../tasksSlice";


export const UpdateTask = (props) => {

    const{
        children,
        task,
        id,
        closeEditMode
    } = props;

    
    const tasks= useSelector((state) => state.tasks.tasks)
    const dispatch = useDispatch();

    const taskNew = () => {
        const taskOld = tasks.find((oldTask)=> oldTask.id === id )
        const dateNew = new Date(task.expirationDate);
        const dateOld = new Date(taskOld.expirationDate);
        const newDate = format(dateNew, 'yyyy-MM-dd HH:mm')
        const oldDate = format(dateOld, 'yyyy-MM-dd HH:mm')
       
       
       return {
        id: id,
        title: task.title === taskOld.title ? null : task.title,
        description: task.description === taskOld.description  ? null : task.description,
        expirationDate: newDate === oldDate  ? null : newDate,
        priority: task.priority === taskOld.priority ? null : task.priority,
       }

    }
    const click = () => {

        const result = taskNew();
        if(result.title !== null || result.description !== null || result.expirationDate !== null || result.priority !== null ){

            dispatch(actionTaskSlice.updateTask(result))
            closeEditMode(false)
        }

        
    }
    



    return(
        <div onClick={click} >
            {children}
        </div>
    )
} 