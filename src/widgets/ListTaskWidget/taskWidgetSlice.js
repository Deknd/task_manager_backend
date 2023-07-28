import { createSlice } from "@reduxjs/toolkit";



const setActivityFun = (state, action) => {
  if(state.activeTask === null){
    state.activeTask = action.payload
  
    const tasksArray = state.tasks;
    const result = tasksArray.map((task) => {
      if(task.id === action.payload){
        task.isActive = true;
        state.priorityActiveTask = task.priority;
        return task;
      }else{
        task.isBlock = true;
        return task;

      }
    })
    return result;
    
    
} else {
  if(action.payload === -1 && state.activeTask !== -1){
      const active = state.activeTask;
      state.activeTask = -1;
      const tasksArray = state.tasks;

      const result = tasksArray.map((task) => {
          task.isActive = false;

          task.isBlock = true;
          return task;
      })
     return result;





    } else {
      if(action.payload === -2){
        const active = state.activeTask;
        state.activeTask = null;
        const tasksArray = state.tasks;
        const result = tasksArray.map((task) => {
          task.isActive = false;

          task.isBlock = false;
          return task;
          })
        return result;
      }

    
    if(state.activeTask === action.payload){
        state.activeTask = null;
        const tasksArray = state.tasks;
        
        const result = tasksArray.map((task) => {
          if(task.id === action.payload){
            task.isActive = false;
            state.priorityActiveTask = null;

            return task;

          }else{
            task.isBlock = false;
            return task;

          }
        })
        return result;

    }}
}
}


  


const taskWidget = createSlice({
    name: "taskWidget",
    initialState: {
        activeTask: null,
        priorityActiveTask: null,
        isNeedAddTask: false,
        tasks: [],

          
    },
    reducers: {
        setTasks: (state, action) => {
          const arrayTasks = action.payload.map((task) => ({
            id: task.id,
            title: task.title,
            description: task.description,
            status: task.status,
            priority: task.priority,
            expirationDate: task.expirationDate,
            isActive: false,
            isBlock: state.activeTask === -1 ? true : false,
          }));

          state.tasks = arrayTasks;
        },
        setIsNeedAddTask: (state, _ ) => {
          if(state.isNeedAddTask){
            state.isNeedAddTask = false;
          } else {
            state.isNeedAddTask = true;
          }
        },
        setActivity: (state, action) => {
            if(state.isNeedAddTask && state.activeTask === -1){
              state.isNeedAddTask = false;
            }
           const result = setActivityFun(state, action);
           state.tasks = result;
           
            
        },
        setPriority: (state, action) => {
          const result = state.tasks.map((task) => {
            if(task.id === action.payload){
              if(task.priority === 'STANDARD'){
                task.priority = 'HIGH';
                return task;
              }else {
                task.priority = 'STANDARD'
                return task;
              }
            } else return task;
          })
  
          state.tasks = result;
        },

       
    }
});

export const { setActivity , setTasks, setPriority, setIsNeedAddTask } = taskWidget.actions;

export const  taskWidgetSlice  = taskWidget.reducer;