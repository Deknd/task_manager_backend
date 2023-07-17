import { logger } from "../../shared/lib/logger";
import { cookieForRefreshToken } from "../../shared/lib/cookieForRefreshToken";
import { actionAuthRegistrSlice , setTasksSlice, setUserSlice } from '../../features'
import { ROUTES } from "../../shared/lib/constants/routes";

const {
    setRefreshTokenCookie,
    getRefreshTokenFromCookie,
 } = cookieForRefreshToken;
const {
    setAuthAndRegistrSlice,
    setStartPage,
    updateRefreshToken,
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




const  middleware = [ addDataFor_AuthAndRegistrSlice_UserSlice_TasksSlice_Cookie, startPegeSelectionMiddleware, updateRefreshToken_Add_Data ]


export const middlewares = [logger, ...middleware];