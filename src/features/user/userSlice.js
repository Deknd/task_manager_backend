import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import {
  deleteRefreshTokenCookie,
  setAccessTokenSessionStorage,
  setRefreshTokenCookie,
} from "./tokens";
import { API, HOST, PORT } from "../../utils/constants";

export const loginUser = createAsyncThunk(
  "users/loginUser",
  async (payload, thunkAPI) => {
    const res = await axios.post(`${HOST}:${PORT}${API}auth/login`, {
      username: payload.username,
      password: payload.password,
    });

    return res.data;
  }
);
export const logoutUser = createAsyncThunk(
  "auth/logout",
  async (payload, thunkAPI) => {
    try {
      const res = await axios.delete(`${HOST}:${PORT}${API}auth/logout`, {
        data: {
          refreshToken: payload.refreshToken,
        },
      });
      return res;
    } catch (err) {
      console.log(err);
      return thunkAPI.rejectWithValue(err);
    }
  }
);

export const updateRefreshToken = createAsyncThunk(
  "auth/refresh",
  async (payload, thunkAPI) => {
    try {
      const res = await axios.post(`${HOST}:${PORT}${API}auth/refresh`, {
        refreshToken: payload.refreshToken,
      });
      return res;
    } catch (err) {
      console.log(err);
      return thunkAPI.rejectWithValue(err);
    }
  }
);

const addToken = (state, { payload }) => {
  state.id = payload.id;
  state.name = payload.name;
  setRefreshTokenCookie(payload.refreshToken);
  setAccessTokenSessionStorage(payload.accessToken, payload.expiration);
};
const logout = (state, _) => {
  state.id = "";
  state.name = "";
  deleteRefreshTokenCookie();
  state.isLogin = false;
};

const userSlice = createSlice({
  name: "user",
  initialState: {
    id: "",
    name: "",
    isLogin: false,
    isNoTryLogPass: false,
  },
  reducers: {
    setIsNoTryLogPass: (state, action) => {
      state.isNoTryLogPass = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(loginUser.fulfilled, (state, action) => {
      const meta = { ...action.meta };
      delete meta.arg.password;
      delete meta.arg.username;

      state.meta = meta;
      state.isLogin = true;
      addToken(state, action);
    });

    builder.addCase(loginUser.rejected, (state, action) => {
      const meta = { ...action.meta };
      delete meta.arg.password;
      delete meta.arg.username;
      state.meta = meta;
      state.isNoTryLogPass = true;
    });

    builder.addCase(updateRefreshToken.fulfilled, (state, action) => {
      state.id = action.payload.data.id;
      state.name = action.payload.data.name;
      setAccessTokenSessionStorage(
        action.payload.data.accessToken,
        action.payload.data.expirationTime
      );
      setRefreshTokenCookie(action.payload.data.refreshToken);
      state.isLogin = true;
    });
    builder.addCase(updateRefreshToken.rejected, (state, _) => {
      logout(state, _);
    });
    builder.addCase(logoutUser.fulfilled, (state, _) => {
      logout(state, _);
    });
    builder.addCase(logoutUser.rejected, (state, _) => {
      logout(state, _);
    });
  },
});
export const { setIsNoTryLogPass } = userSlice.actions;
export default userSlice.reducer;
