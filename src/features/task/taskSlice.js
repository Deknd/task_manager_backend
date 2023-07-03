import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { API, HOST, PORT } from "../../utils/constants";

export const getAllTasks = createAsyncThunk(
  "tasks/getAllTask",
  async (payload, thunkAPI) => {
    try {
      const res = await axios.get(
        `${HOST}:${PORT}${API}users/${payload.id}/tasks`,
        {
          headers: {
            Authorization: `Bearer ${payload.accessToken}`,
          },
        }
      );
      const allTask = res.data;

      return allTask;
    } catch (err) {
      console.log(err);
      return thunkAPI.rejectWithValue(err.message);
    }
  }
);
export const deleteTask = createAsyncThunk(
  "tasks/deleteTask",
  async (payload, thunkAPI) => {
    try {
      const res = await axios.delete(
        `${HOST}:${PORT}${API}tasks/${payload.id}`,
        {
          headers: {
            Authorization: `Bearer ${payload.accessToken}`,
          },
        }
      );
      return res;
    } catch (err) {
      console.log(err);
      return thunkAPI.rejectWithValue(err);
    }
  }
);

export const addTask = createAsyncThunk(
  "tasks/addTask",
  async (payload, thunkAPI) => {
    try {
      const res = await axios.post(
        `${HOST}:${PORT}${API}users/${payload.id}/tasks`,
        {
          title: payload.title,
          description: payload.description,
          expirationDate: payload.expirationDate,
          priority: payload.priority,
        },
        {
          headers: {
            Authorization: `Bearer ${payload.accessToken}`,
          },
        }
      );
      const allTask = res.data;

      return allTask;
    } catch (err) {
      console.log(err);
      return thunkAPI.rejectWithValue(err.message);
    }
  }
);

const taskSlice = createSlice({
  name: "task",
  initialState: {
    tasks: [],
    isLoadTasks: false,
  },
  reducers: {
    logoutUserDelTasks: (state, _) => {
      state.tasks = [];
      state.isLoadTasks = false;
    },
  },

  extraReducers: (builder) => {
    builder.addCase(getAllTasks.pending, (state, action) => {
      state.isLoadTasks = true;
    });
    builder.addCase(getAllTasks.fulfilled, (state, action) => {
      state.tasks = action.payload;
    });
    builder.addCase(getAllTasks.rejected, (state, action) => {
      state.isLoadTasks = false;
    });

    builder.addCase(addTask.fulfilled, (state, action) => {
      state.tasks.push(action.payload);
    });
    builder.addCase(deleteTask.fulfilled, (state, action) => {
      console.log(action.meta.arg.id);
      state.tasks = state.tasks.filter(
        (task) => task.id !== action.meta.arg.id
      );
    });
  },
});
export const { logoutUserDelTasks, setIsLoadTasks } = taskSlice.actions;

export default taskSlice.reducer;