import { createAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { API_LOGIN, API_UPDATE_REFRESH_TOKEN, API_REGISTRATION } from "../../shared/lib/api";
import { ROUTES } from "../../shared/lib/constants/routes";


const registrationUser = createAsyncThunk(
  "auth/registrationUser", //название для дальнейших до функций
  async (payload, thunkAPI) => {
    
      const res = await API_REGISTRATION(payload);
      const registrationData = res.data;

      return registrationData;
    
  }
);

const loginUser = createAsyncThunk(
  "auth/loginUser",
   async (payload, thunkAPI) => {
  const res = await API_LOGIN(payload);
   return res.data;
  }
);


const updateRefreshToken = createAsyncThunk(
  "auth/refresh",
  async (payload, thunkAPI) => {
      const res = await API_UPDATE_REFRESH_TOKEN(payload);
      return res;
  }
);








const startPegeSelection = createAction('auth/startPegeSelection');

const authAndRegistrSlice = createSlice({

  name: "authAndRegistrSlice",
  initialState: {
    //Если пользователь успешно залогинился, то true, и переходит на страничку тасков
    isLogin: false,
    //токены доступа
    accessToken: '',
    expiration: '',
    refreshToken: '',
    //старничка, на которую автоматически переходит
    startPage: '',
    // метка при начальной загрузки, проверяет на какую страницу нужен переход
    needNavigate: false,

    // метка показывающая успешная ли регистрация
    isRegistrationSuccessful: 0,


    //метка, показывает отправлены даные или нет
    isDataSentRegistration: false,
  },
  reducers: {
    setAuthAndRegistrSlice: ( state, action ) => {
      state.accessToken = action.payload.accessToken;
      state.expiration = action.payload.expiration;
      state.refreshToken = action.payload.refreshToken;
      state.isLogin = true;

    },
    setStartPage: ( state, action ) => {
      state.startPage = action.payload.startPage;
      state.needNavigate = true;
    },
    clear_data: ( state, action ) => {
      state.accessToken = '';
      state.expiration = '';
      state.refreshToken = ''
      state.isLogin = false;
    }

   
  },
  extraReducers: (builder) => {
    builder.addCase(registrationUser.pending, (state, action) => {
      state.isDataSentRegistration = true;
      state.isRegistrationSuccessful = 0;
    } );
    builder.addCase(registrationUser.rejected, (state, action) => {
      state.isDataSentRegistration = false;
      state.isRegistrationSuccessful = -1;

    } );
    builder.addCase(registrationUser.fulfilled, (state, action) => {
      state.isDataSentRegistration = false;
      state.startPage = ROUTES.LOGIN;
      state.isRegistrationSuccessful = 1;

    } )
  



    builder.addCase(loginUser.fulfilled, (state, action) => {
      const meta = { ...action.meta };
      delete meta.arg.password;
      delete meta.arg.username;

      state.meta = meta;

    });

    builder.addCase(loginUser.rejected, (state, action) => {

      const meta = { ...action.meta };
      delete meta.arg.password;
      delete meta.arg.username;
      state.meta = meta;

    });



  },
});


export const authRegistrSlice = authAndRegistrSlice.reducer;
const { setAuthAndRegistrSlice, setStartPage, clear_data } = authAndRegistrSlice.actions;
export const actionAuthRegistrSlice = { loginUser, startPegeSelection, setAuthAndRegistrSlice, setStartPage, updateRefreshToken, registrationUser, clear_data };
