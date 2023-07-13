

const taskWidgetUpdateTask = (store) => (next) => (action) => {
    // Проверяем тип действия
    if (action.type === 'taskWidgfet/setActivity') {

      
      const { getState } = store;
      const state = getState();
      
      //Проверяем что таск хочет стать не активным
      if(state.taskWidget.activeTask === action.payload){

        //достаем таски из слоя taskWidget
        state.taskWidget.tasks.map((taskVisual) => {
          //находим активный таск
          if(taskVisual.id === action.payload){
            //достаем таски из слоя task
            state.task.tasks.map((task) => {
              //находим активный таск
              if(task.id === action.payload){
                //если он поменялся, то вызываем функцию обновления и сортировки нового таска
                if(task.priority !== taskVisual.priority){
                  const { dispatch } = store;
                  console.log("Должен вызываться редукс обновление тасков")
                }
              }
            })
          }
        })
      }
    }
    return next(action);
  };





  
  export const taskWidgetMiddleware = [ taskWidgetUpdateTask];
   