import {  createAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { API_ADD_TASK, API_DELETE_TASK, API_UPDATE } from "../../shared/lib/api";

const addTask = createAsyncThunk(
  "tasks/addTask",
  async (payload, thunkAPI) => {
   const res = await API_ADD_TASK(payload);
   return res.data;
  }
);

const deleteTaskForServer = createAsyncThunk(
  "tasks/deleteTask/server",
  async (payload, thunkAPI) => {
    
      const res = await API_DELETE_TASK(payload);
      return res;
    
  }
);
const updateTaskForServer = createAsyncThunk(
  'tasks/updateTask/server',
  async (payload, thunkAPI) => {
    const res = await API_UPDATE(payload);
    return res.data;
  }
)

const createTask = createAction('tasks/createTask');
const deleteTask = createAction('tasks/deleteTask');
const updateStatusTask = createAction('tasks/updateStatus');
const updateTask = createAction('task/update');





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
    builder.addCase(addTask.fulfilled, (state, {payload}) => {
     const arrayTasks = state.tasks;
     const task = {id: payload.id,
      title: payload.title,
      description: payload.description,
      status: payload.status,
      priority: payload.priority,
      expirationDate: payload.expirationDate,}
      const newArray = [...arrayTasks, task];
      state.tasks = newArray;

    } );
   builder.addCase(deleteTaskForServer.fulfilled, (state, action) => {
      const idTask = action.meta.arg.idTask;

     state.tasks = state.tasks.filter((task) => task.id !== idTask);

    });
    builder.addCase(updateTaskForServer.fulfilled, (state, action) => {
       const updateTask = action.payload;
       const res = state.tasks.map((task)=> task.id === updateTask.id? updateTask : task) 
       state.tasks = res;
    })
 
  },
});
export const { setTasksSlice, clear_tasks } = tasksSlice.actions;
export const actionTaskSlice = {
  addTask,
  createTask,
  deleteTask,
  deleteTaskForServer,
  updateStatusTask,
  deleteTaskForServer,
  updateTaskForServer,
  updateTask,

}

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