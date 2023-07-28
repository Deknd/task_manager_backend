import React, { useEffect, useState } from "react";
import taskDesctopBigStandart from '../lib/image/taskDesctopBigStandard.jpg'
import taskDesctopBigHigh from '../lib/image/taskDesctopBigHigh.jpg'


export const FormForAddNewTask = (props) => {

    const {
        children,
        priority,
    } = props;
    const [ priorityImg, setPriorityImg ] = useState(taskDesctopBigStandart);
    useEffect(()=>{

        if(priority){
            switch(priority){
                case 'STANDARD': setPriorityImg(taskDesctopBigStandart);
                break;
                case 'HIGH': setPriorityImg(taskDesctopBigHigh);
                break;
            }
        }
    },[priority])


    return(
        <div  
            style={{
                position: 'absolute',
                top: '10%',
                left: '50%',
                transform: 'translate(-50%, -20%)',
                paddingTop: '5em'
            }}
            >
    
            <div
                style={{
                    backgroundImage: `url(${priorityImg})`,
                    backgroundSize: 'cover',
                    boxShadow: '0 0 3px rgba(0,0,0,0.5)',
                    borderRadius: '0.6em',
                    width: '21em',
                    padding: '0.5em'
                }}
                >
    
                {children}
      
            </div>
        </div>
            
    )
} 