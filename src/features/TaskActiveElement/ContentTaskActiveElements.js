import React, { useEffect, useState } from "react";

import { useDispatch} from "react-redux";

import { setActivity } from "../../widgets/ListTaskWidget/taskWidgetSlice";



export const ContentTaskActiveElements = (props)=>{

    const {
        children,
        taskData,
        isEditMode,
        isActiveEditMode,
        setIsActiveEditMode,
        setIsEditMode,
    } = props;
    
    const {
        id,
        isBlock,
    } = taskData;
    
    const dispatch = useDispatch();
    const [ isNowBlock, setIsNowBlock ] = useState(false);
    useEffect(()=> {
        console.log(`useEffect  !isBlock: ${!isBlock} isEditMode: ${isEditMode} isActiveEditMode: ${isActiveEditMode} isNowBlock: ${isNowBlock}`)

        const fetchData = async () => {
            if (isEditMode) {
                if(!isBlock && !isNowBlock){
                    dispatch(setActivity(id));
                    setIsActiveEditMode(isEditMode);

                    
                }else {
                    if(!isBlock && isNowBlock){
                    setIsActiveEditMode(isEditMode);

                    }
                }
               
            
            } else {
                if(isActiveEditMode && !isBlock && !isNowBlock){
                    dispatch(setActivity(id));
                    setIsActiveEditMode(isEditMode);
                } else {
                    if(isActiveEditMode && !isBlock && isNowBlock){
                    setIsActiveEditMode(isEditMode);

                    }
                }
            }
            if(isBlock && isEditMode && isActiveEditMode){
                setIsActiveEditMode(false)
                setIsEditMode(false)
            }
           
          };
        
        fetchData();
    },[isEditMode, isBlock])


    const clickTask = ()=>{
        if(!isBlock && !isEditMode){
            console.log(`clickTask  !isBlock: ${!isBlock} isEditMode: ${isEditMode} isActiveEditMode: ${isActiveEditMode} isNowBlock: ${isNowBlock}`)
            setIsNowBlock(!isNowBlock)
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

