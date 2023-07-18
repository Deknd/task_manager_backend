import {  createSlice } from "@reduxjs/toolkit";



const tasksSlice = createSlice({
  name: "tasks",
  initialState: {
    tasks: [],
  },
  reducers: {
    setTasksSlice: (state, action) => {
      if(!action.payload){
        state.tasks = [];
        return}

       const arrayTasks = action.payload.map((task) => ({
          id: task.id,
          title: task.title,
          description: task.description,
          status: task.status,
          priority: task.priority,
          expirationDate: task.expirationDate,
          
        }));
      

      state.tasks = arrayTasks;
    },
    clear_tasks: ( state, action ) => {
      state.tasks = [];
    }
   
  },

  extraReducers: (builder) => {
  },
});
export const { setTasksSlice, clear_tasks } = tasksSlice.actions;

export const sliceTasks = tasksSlice.reducer;


// export const getAllTasks = createAsyncThunk(
//   "tasks/getAllTask",
//   async (payload, thunkAPI) => {
//     try {
//       const res = await axios.get(
//         `${HOST}:${PORT}${API}users/${payload.id}/tasks`,
//         {
//           headers: {
//             Authorization: `Bearer ${payload.accessToken}`,
//           },
//         }
//       );
//       const allTask = res.data;

//       return allTask;
//     } catch (err) {
//       console.log(err);
//       return thunkAPI.rejectWithValue(err.message);
//     }
//   }
// );
// export const deleteTask = createAsyncThunk(
//   "tasks/deleteTask",
//   async (payload, thunkAPI) => {
//     try {
//       const res = await axios.delete(
//         `${HOST}:${PORT}${API}tasks/${payload.id}`,
//         {
//           headers: {
//             Authorization: `Bearer ${payload.accessToken}`,
//           },
//         }
//       );
//       return res;
//     } catch (err) {
//       console.log(err);
//       return thunkAPI.rejectWithValue(err);
//     }
//   }
// );

// export const addTask = createAsyncThunk(
//   "tasks/addTask",
//   async (payload, thunkAPI) => {
//     try {
//       const res = await axios.post(
//         `${HOST}:${PORT}${API}users/${payload.id}/tasks`,
//         {
//           title: payload.title,
//           description: payload.description,
//           expirationDate: payload.expirationDate,
//           priority: payload.priority,
//         },
//         {
//           headers: {
//             Authorization: `Bearer ${payload.accessToken}`,
//           },
//         }
//       );
//       const allTask = res.data;

//       return allTask;
//     } catch (err) {
//       console.log(err);
//       return thunkAPI.rejectWithValue(err.message);
//     }
//   }
// );
// logoutUserDelTasks: (state, _) => {
//   state.tasks = [];
//   state.isLoadTasks = false;
// },
// addAllTask: ( state, action ) => {
//   state.tasks = action.payload;
// }

// builder.addCase(getAllTasks.pending, (state, action) => {
//   state.isLoadTasks = true;
// });
// builder.addCase(getAllTasks.fulfilled, (state, action) => {
//   state.tasks = action.payload;
// });
// builder.addCase(getAllTasks.rejected, (state, action) => {
//   state.isLoadTasks = false;
// });

// builder.addCase(addTask.fulfilled, (state, action) => {
//   state.tasks.push(action.payload);
// });
// builder.addCase(deleteTask.fulfilled, (state, action) => {
//   state.tasks = state.tasks.filter(
//     (task) => task.id !== action.meta.arg.id
//   );
// });