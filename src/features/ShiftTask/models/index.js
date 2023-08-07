export const shiftTask = (isActive, parrent, formTaskWidget, setMoveShift)=> {
    const defaultMove = -2.835;
    if(isActive){
      
      if (parrent.current) {
        //стандартное смещение таска
        const rectParrent = parrent.current.getBoundingClientRect();
        const computedStyleParrent = window.getComputedStyle(parrent.current);
        const fontSizeValue = computedStyleParrent.getPropertyValue("font-size");
  
        const fontSize = parseFloat(fontSizeValue);
       
        if(formTaskWidget.current){
  
          const rectTask = formTaskWidget.current.getBoundingClientRect();
  
          const windowWidth = window.innerWidth;
          let rightCoordinate = windowWidth;
            if(rectParrent.left > (rectTask.left + defaultMove*fontSize)){
              setMoveShift(0)
              } else {
  
                if(rightCoordinate<=(rectTask.right + (defaultMove-1.2)*-fontSize)){
                    const difference = ((rectTask.right + (defaultMove)*-fontSize)-rightCoordinate)/fontSize+1;
                    setMoveShift(-difference+defaultMove);
                } else {
                  setMoveShift(defaultMove)
                }
              }
        }
      }
  } else { setMoveShift(0); }
  
  }