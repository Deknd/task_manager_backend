import React from 'react';

import edit from './img/edit.png'
import garbage from './img/garbage.png'
import negative from './img/negative.png'
import positive from './img/positive.png'
import accept from './img/accept.png'
import cancel from './img/cancel.png'


export const IconButton_v2 = (props) => {

  const { 
    img,
    type,
    textIcon 
  } = props;

  const imgType = ()=>{
    if(type){
      switch(type){
        case 'edit': return edit;
        case 'garbage': return garbage;
        case 'negative': return negative;
        case 'positive': return positive;
        case 'accept': return accept;
        case 'cancel': return cancel;

      }
    }
  }

//
  return (
   img ? (
          <img src={img} alt={textIcon} style={{
              width: '2em',
              height: '2em',
          }} />
      ): type ? (
        <img src={imgType()} alt={textIcon} style={{
          width: '2em',
          height: '2em',
      }} />
      ) : (
          <p style={{
              width: '3.5em',
              height: '2em',
              userSelect: 'none',
              fontSize: '0.6em'
              }}>
              {textIcon}
          </p>
      )
      
   
        
  );
};
