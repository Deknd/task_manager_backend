import React from "react";
import { useSelector } from "react-redux";
import { BigButton } from "../../shared/ui";


export const UserPanelWidget = (props) => {

    const user = useSelector((state)=> state.user)

    return(
        <div style={{
            display: 'flex',
            flexDirection: 'row',
            flexWrap: 'wrap',
            alignContent: 'center',
            justifyContent: 'space-between',
            

            height: '3rem',
            width: '100%',
            borderBottom: "3px solid #00000030",
        }}>
            <div style={{
                display: 'flex',
                paddingLeft: '3.4rem',
                fontSize: '1.6rem'
                
            }} >
                <span>Hello,</span>
                <div style={{
                    paddingLeft: '2rem'
                }} >{user.name}</div>
                    
            </div>
            <div style={{
                backgroundColor: 'green',
                display: 'flex',
                paddingRight: '3.4rem'
                }} >
                <BigButton/>
                <BigButton/>
            </div>
        </div>
    )
}