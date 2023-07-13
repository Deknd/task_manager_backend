import { needUpdate } from "./fillterAllActiveTasks";

export const updateTaskForOverdueDateActionAsync = () => {
    return async (dispatch) => {
        if(needUpdate.length !== 0){
            while(needUpdate.length !== 0){
                const task = needUpdate.shift();
                console.log(task)
                //await  dispatch(update(task));
    
    
            }
        }

       
    };
  };