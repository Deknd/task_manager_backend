import { addAllTask } from "./tasksSlice";

const tasksMiddleware = (store) => (next) => (action) => {

    if(action.type === 'users/loginUser/fulfilled'){


        const { dispatch } = store;
        
        //dispatch(addAllTask(action.payload.tasks))
    }
    if(action.type === 'auth/refresh/fulfilled'){
        const { dispatch } = store;
        console.log('auth/refresh/fulfilled ', action.payload.data.tasks)
       // dispatch(addAllTask(action.payload.tasks))
    }


    return next(action)

}


export const taskSliceMiddleware = [ tasksMiddleware ]