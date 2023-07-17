import { createAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { API_LOGIN, API_UPDATE_REFRESH_TOKEN } from "../../shared/lib/api";


// const createUser = createAsyncThunk(
//   "auth/createUser", //название для дальнейших до функций
//   async (payload, thunkAPI) => {
//     try {
//       const res = await axios.post(
//         `API_SERVERauth/register`,
//         payload
//       ); //`${AUTH_URL}/register`
//       const registrationData = res.data;

//       return registrationData;
//     } catch (err) {
//       console.log(err);
//       return thunkAPI.rejectWithValue(err);
//     }
//   }
// );

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
    isLogin: false,

    accessToken: '',
    expiration: '',
    refreshToken: '',

    startPage: '',

    needNavigate: false,

    isNoTryLogPass: false,


    isRegistrationSuccess: false,
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
    }

   
  },
  extraReducers: (builder) => {
  



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

      state.isNoTryLogPass = true;
    });



  },
});


export const authRegistrSlice = authAndRegistrSlice.reducer;
const { setAuthAndRegistrSlice, setStartPage } = authAndRegistrSlice.actions;
export const actionAuthRegistrSlice = { loginUser, startPegeSelection, setAuthAndRegistrSlice, setStartPage, updateRefreshToken  };
