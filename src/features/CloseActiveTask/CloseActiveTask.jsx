import React from "react";
import { useDispatch } from "react-redux";
import { setActivity } from "../../widgets/ListTaskWidget/taskWidgetSlice";


export const CloseActiveTask = (props) => {

    const{
        children
    }=props;

    const dispatch = useDispatch();


    const onClick = () => {
        dispatch(setActivity(-2))

    }


    return (
        <div onClick={onClick} >
            {children}
        </div>
    )
}