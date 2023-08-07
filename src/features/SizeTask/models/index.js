

  export const changeZ_pozition = (isActive, setZPosition) => {
    if(isActive){
      setZPosition(true);
      }else{
        const interval = setTimeout(() => {
            
              setZPosition(false);
          
          }, 100);
      
          return () => {
            clearTimeout(interval);
        };
  }
  }