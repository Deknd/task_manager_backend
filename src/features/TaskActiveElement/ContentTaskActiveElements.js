import React from "react";

import { useDispatch} from "react-redux";

import { setActivity } from "../../widgets/ListTaskWidget/taskWidgetSlice";



export const ContentTaskActiveElements = (props)=>{

    const {
        children,
        taskData,
    } = props;
    
    const {
        id,
        isBlock,
    } = taskData;

    const dispatch = useDispatch();


    const clickTask = ()=>{
        if(!isBlock){
            dispatch(setActivity(id))

        }
      
    }


    return(
        <div 
         style={{cursor: isBlock ? 'default' : 'pointer'}}
         onClick={clickTask}
         >
            {children}
        </div>
    )
}

