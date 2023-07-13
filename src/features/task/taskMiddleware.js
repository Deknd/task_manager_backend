import { addAllTask } from "./taskSlice";

const tasksMiddleware = (store) => (next) => (action) => {

    if(action.type === 'users/loginUser/fulfilled'){


        const { dispatch } = store;
        
        dispatch(addAllTask(action.payload.tasks))
    }
    if(action.type === 'auth/refresh/fulfilled'){
        const { dispatch } = store;
       dispatch(addAllTask(action.payload.data.tasks))
    }


    return next(action)

}


export const taskSliceMiddleware = [ tasksMiddleware ]