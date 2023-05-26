import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { API, HOST, PORT } from "../../utils/constants";

export const createUser = createAsyncThunk(
  "auth/createUser", //название для дальнейших до функций
  async (payload, thunkAPI) => {
    try {
      const res = await axios.post(
        `${HOST}:${PORT}${API}auth/register`,
        payload
      ); //`${AUTH_URL}/register`
      const registrationData = res.data;

      return registrationData;
    } catch (err) {
      console.log(err);
      return thunkAPI.rejectWithValue(err);
    }
  }
);

const registrationSlice = createSlice({
  name: "registration",
  initialState: {
    isRegistrationSuccess: false,
  },
  reducers: {
    setRegistrationSuccess: (state, action) => {
      state.isRegistrationSuccess = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(createUser.fulfilled, (state, action) => {
      state.isRegistrationSuccess = true;
    });
  },
});

export const { setRegistrationSuccess } = registrationSlice.actions;
export default registrationSlice.reducer;
