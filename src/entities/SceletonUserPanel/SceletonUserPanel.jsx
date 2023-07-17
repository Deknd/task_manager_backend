import React from "react";


export const SceletonUserPanel = (props) => {

    const {
        children,
        userName,
    }=props;

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
                marginLeft: '5rem',
                paddingLeft: '3.4rem',
                fontSize: '1.6rem'
                
            }} >
                <span>Hello,</span>
                <div style={{
                    paddingLeft: '2rem'
                }} >{userName}</div>
                    
            </div>
            <div style={{
                display: 'flex',
                paddingRight: '2rem'
                }} >
                
                {children} 
            </div>
        </div>
    )

}