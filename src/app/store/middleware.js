import { logger } from "../../shared/lib/logger";
import { cookieForRefreshToken } from "../../shared/lib/cookieForRefreshToken";
import { actionAuthRegistrSlice , actionTaskSlice, clear_tasks, clear_user, setTasksSlice, setUserSlice } from '../../features'
import { ROUTES } from "../../shared/lib/constants/routes";
import { setIsNeedAddTask } from "../../widgets/ListTaskWidget";
import { deleteTaskForServer } from "../../features/tasksSlice/tasksSlice";

const {
    setRefreshTokenCookie,
    getRefreshTokenFromCookie,
    deleteRefreshTokenCookie
 } = cookieForRefreshToken;
const {
    setAuthAndRegistrSlice,
    setStartPage,
    updateRefreshToken,
    clear_data
} =actionAuthRegistrSlice;
 
 //Добавляет данные при Логине пользователя во все слайсы
const addDataFor_AuthAndRegistrSlice_UserSlice_TasksSlice_Cookie = (store) => (next) => (action) => {
     
  if (action.type === 'auth/loginUser/fulfilled') {
    updateData(action.payload, store);

    }
    return next(action);
};


const startPegeSelectionMiddleware = (store) => (next) => (action) => {
    if (action.type === 'auth/startPegeSelection'){
        const { getState } = store;
        const { dispatch } = store;
        const state = getState();

        if(!state.authAndRegistrSlice.isLogin){

            const refreshToken = getRefreshTokenFromCookie();
            if( refreshToken !== null ){
                dispatch(updateRefreshToken({refreshToken: refreshToken}))
    
            } else {
                dispatch(setStartPage({startPage: ''}))
            }
            return;
        }
    }
    return next(action);
  }


  const updateRefreshToken_Add_Data = (store) => (next) => (action) => {

    if (action.type === 'auth/refresh/fulfilled'){

        updateData(action.payload.data, store)
        

        return;
    }
    if (action.type === 'auth/refresh/rejected'){

        const { dispatch } = store;
        dispatch(setStartPage({startPage: ROUTES.LOGIN}))
        return;

    }
        
    return next(action);

  }

  const logout = (store) => (next) => (action) => {

    if(action.type === 'logout_user'){
        const { dispatch } = store;
        dispatch(clear_tasks());
        dispatch(clear_user());
        dispatch(clear_data());
        deleteRefreshTokenCookie();
    }
    return next(action);

  }
  const checkAccessToken = (store) => (next) => (action) => {

    if(action.type === 'tasks/createTask'){
      const { getState } = store;

      const state = getState();

      const task = action.payload;
      const id = state.user.id;
      const accessToken = state.authAndRegistrSlice.accessToken;


        checkIsNeedRefresh(store,actionTaskSlice.addTask({id, task, accessToken}) );
        
        return;
   
    }
    if(action.type === 'tasks/deleteTask'){

        const { getState } = store;
        const state = getState();

        const idTask = action.payload;
        const accessToken = state.authAndRegistrSlice.accessToken;

        checkIsNeedRefresh( store, actionTaskSlice.deleteTaskForServer({idTask, accessToken}) );

     return;
    }
    if(action.type === 'task/update'){
      const { getState } = store;
      const state = getState();

      const{
        id,
        title,
        description,
        status,
        priority,
        expirationDate,
      }=action.payload;

      const task = {
        id: id,
        title: title? title : null,
        description: description? description : null,
        status: status? status : null,
        priority: priority? priority : null,
        expirationDate: expirationDate? expirationDate : null,

      }
      const accessToken = state.authAndRegistrSlice.accessToken;

      checkIsNeedRefresh( store, actionTaskSlice.updateTaskForServer({task, accessToken}) );



    }

  return next(action); 

  }

  const checkIsNeedRefresh = async (store, fun) => {
    const { getState } = store;
    const { dispatch } = store;

    const state = getState();

    const now = new Date();
    const expirationAccessToken = state.authAndRegistrSlice.expiration;
    const otherDate = new Date(expirationAccessToken);

    if(otherDate > now) {
      dispatch(fun)
    } else {
      const refreshToken = state.authAndRegistrSlice.refreshToken;
      await dispatch(updateRefreshToken({refreshToken: refreshToken}))
      dispatch(fun)

    }
  }

  const closeWindowAddTask = (store) => (next) => (action) => {
    if(action.type === 'tasks/addTask/fulfilled'){
      const { dispatch } = store;

      dispatch(setIsNeedAddTask())
    }
    return next(action)
  }



 
  const timers = {}; 
const isActive = {};
  const updateOrNotUpdateTask = (store) => (next) => (action) => {

    if(action.type === 'tasks/updateStatus'){
      const taskId = action.payload.id;
      

      if(!isActive[`${taskId}:${action.type}`]){
        startTimer(store, action, 'status');
      } else {
        stopTimer(action);
        startTimer(store, action, 'status');
      }
    }
    if(action.type ==='tasks/updatePriority'){
      const taskId = action.payload.id;
      console.log('taskId: ',taskId, ' priority: ',action.payload.priority)
      if(!isActive[`${taskId}:${action.type}`]){
        startTimer(store, action, 'priority');
      } else {
        stopTimer(action);
        startTimer(store, action, 'priority');
      }


    }




    return next(action);
  }
  const startTimer = (store, action, type ) => {
    const taskId = action.payload.id;
    const idTimer = `${taskId}:${action.type}`;
    
    if (!isActive[idTimer]) {
      isActive[idTimer] = true;  
  
      timers[idTimer] = setTimeout(() => {
        isActive[idTimer] = false;  

        const { getState } = store;
        const state = getState();
        const taskOld = state.tasks.tasks.find((task) => task.id === action.payload.id)
        let quest = null;
        switch (type) {
          case 'status': { 
            if(action.payload.status !== taskOld.status){
            const { dispatch } = store;
            dispatch(actionTaskSlice.updateTask({id: action.payload.id, status: action.payload.status}))
          } 
        }
          break;
          case 'priority': {

           
            if(action.payload.priority !== taskOld.priority){
              const { dispatch } = store;
              dispatch(actionTaskSlice.updateTask({id: action.payload.id, priority: action.payload.priority}))
            } 
          };
          break;
        }
        
       

      }, 1000); 
    }
  }
  const stopTimer = (action) => {
    const taskId = action.payload.id;
    const idTimer = `${taskId}:${action.type}`;
    if (isActive[idTimer]) {
      isActive[idTimer] = false;
      clearTimeout(timers[idTimer]);
    }
  }








  const updateData = (data, store) => {
    const auth = { accessToken: data.accessToken, expiration: data.expiration, refreshToken: data.refreshToken };
    const user = { id: data.id, name: data.name };
    const tasks = data.tasks;
    const { dispatch } = store;

    dispatch( setAuthAndRegistrSlice(auth) );
    setRefreshTokenCookie(data.refreshToken)
    dispatch( setUserSlice(user) );
    dispatch( setTasksSlice( tasks ));
    dispatch(setStartPage({startPage: ROUTES.MAIN}))

  }




const  middleware = [ 
    addDataFor_AuthAndRegistrSlice_UserSlice_TasksSlice_Cookie,
    startPegeSelectionMiddleware,
     updateRefreshToken_Add_Data,
     logout,
     checkAccessToken,
     closeWindowAddTask,
     updateOrNotUpdateTask,
    ]


export const middlewares = [logger, ...middleware];