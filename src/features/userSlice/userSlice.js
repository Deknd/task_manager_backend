import {  createSlice } from "@reduxjs/toolkit";




const userSlice = createSlice({
  name: "user",
  initialState: {
    id: "",
    name: "",
    
  },
  reducers: {
    setUserSlice: (state, action) => {
      state.id = action.payload.id;
      state.name = action.payload.name;
    },
    clear_user: ( state, action ) => {
      state.id = '';
      state.name = '';
    }
   
  },
  extraReducers: (builder) => {
   
  },
});
export const { setUserSlice, clear_user } = userSlice.actions;
export const userSliceR =  userSlice.reducer;



// builder.addCase(LoginUser.fulfilled, (state, action) => {
//   const meta = { ...action.meta };
//   delete meta.arg.password;
//   delete meta.arg.username;

//   state.meta = meta;
//   state.isLogin = true;
//   addToken(state, action);
// });

// builder.addCase(LoginUser.rejected, (state, action) => {
//   const meta = { ...action.meta };
//   delete meta.arg.password;
//   delete meta.arg.username;
//   state.meta = meta;
//   state.isNoTryLogPass = true;
// });

// builder.addCase(updateRefreshToken.fulfilled, (state, action) => {
//   state.id = action.payload.data.id;
//   state.name = action.payload.data.name;

//   setAccessTokenSessionStorage(
//     action.payload.data.accessToken,
//     action.payload.data.expiration
//   );
//   setRefreshTokenCookie(action.payload.data.refreshToken);
//   state.isLogin = true;
// });
// builder.addCase(updateRefreshToken.rejected, (state, _) => {
//   logout(state, _);
// });
// builder.addCase(logoutUser.fulfilled, (state, _) => {
//   logout(state, _);
// });
// builder.addCase(logoutUser.rejected, (state, _) => {
//   logout(state, _);
// });


// export const LoginUser = createAsyncThunk(
//   "users/loginUser",
//   async (payload, thunkAPI) => {
//     const res = await axios.post(`${HOST}:${PORT}${API}auth/login`, {
//       username: payload.username,
//       password: payload.password,
//     });

//     return res.data;
//   }
// );
// export const logoutUser = createAsyncThunk(
//   "auth/logout",
//   async (payload, thunkAPI) => {
//     try {
//       const res = await axios.delete(`${HOST}:${PORT}${API}auth/logout`, {
//         data: {
//           refreshToken: payload.refreshToken,
//         },
//       });
//       return res;
//     } catch (err) {
//       console.log(err);
//       return thunkAPI.rejectWithValue(err);
//     }
//   }
// );

// export const updateRefreshToken = createAsyncThunk(
//   "auth/refresh",
//   async (payload, thunkAPI) => {
//     try {
//       const res = await axios.post(`${HOST}:${PORT}${API}auth/refresh`, {
//         refreshToken: payload.refreshToken,
//       });
//       return res;
//     } catch (err) {
//       console.log(err);
//       return thunkAPI.rejectWithValue(err);
//     }
//   }
// );

// const addToken = (state, { payload }) => {
//   state.id = payload.id;
//   state.name = payload.name;
//   setRefreshTokenCookie(payload.refreshToken);
//   setAccessTokenSessionStorage(payload.accessToken, payload.expiration);
// };
// const logout = (state, _) => {
//   state.id = "";
//   state.name = "";
//   deleteRefreshTokenCookie();
//   state.isLogin = false;
// };