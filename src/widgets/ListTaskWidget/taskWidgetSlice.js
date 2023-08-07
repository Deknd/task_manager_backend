import { createSlice } from "@reduxjs/toolkit";



const setActivityFun = (state, action) => {
  //если в activeTask находится null
  //сохраняет id ативного таск в сторе
  //активному таску ставит переменную isActive в true
  //остальным таскам ставит переменную isBlock в true
  //сохраняет приоритет, который был у активного таска

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
    
    //иначе
} else {
  //если нужно добавить таск 
  // и метод вызван с аргументом -1
  //в пееменую activeTask ставится -1
  //всем таскам снимается метка активности
  // isActive = false
  //и ставится метка блока
  // isBlock = true
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




//иначе
    } else {
       //если нужно ...
      // и метод вызван с аргументом -2
      //в пееменую activeTask ставится -2
      //всем таскам снимается метка активности
      // isActive = false
      //и ставится метка блока
      // isBlock = false
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

    //если метод вызвается с аргументом равным 
    //activeTask, то есть с таска с таким же айди
    //то таску ставится метка
    //isActive = false
    //и все остальным таскам метка блока ставится
    //isBlock = false
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
       

       
    }
});

export const { setActivity , setTasks,  setIsNeedAddTask } = taskWidget.actions;

export const  taskWidgetSlice  = taskWidget.reducer;