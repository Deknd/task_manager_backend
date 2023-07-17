

import React from 'react';
import { noDescription } from './bigButtonConstants';


export const BigButton = (props) => {

    const {
        description,
        img,
        colorButtonRBG,
        noImg,
        
    } =props;


    
    const descriptionButton = description ? description : noDescription


  
return (
    <div style={{ 
      display: 'flex',
      backgroundColor: colorButtonRBG ? `rgba(${colorButtonRBG}` : 'none',
      height: '3rem',
      userSelect: 'none'
     }}>
      {noImg ? <></> : (<div style={{
          width:'4rem',
          display: 'flex',
          justifyContent: 'flex-end',
          alignItems: 'center',
          
        }}>
             {img ? <img 
                      style={{
                      width: '2rem',
                      height: '2rem',
                      }} 
                      src={img} 
                      alt={descriptionButton} 
                    /> : null}
        </div>)}
        
        <div style={{
          display: 'flex',
          justifyContent: 'flex-end',
          alignItems: 'center',
          paddingLeft: '0.7rem',
          fontSize: '1.6rem',
        }}>
            <span>
             {descriptionButton}
            </span>
             
        </div>
    </div>
    );
}