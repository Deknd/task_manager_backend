import { addDays } from 'date-fns';
import { fillterAllActiveTasks } from './fillterAllActiveTasks'

export const fillterTodayActiveTasks = (tasks) => {
    const activeTask = fillterAllActiveTasks(tasks);

    
    const result = [];
    activeTask.map((task)=> {
        const currentDate = addDays(new Date(),1) ;
        const targetDate = new Date(task.expirationDate); // Целевая дата и время

        if(currentDate > targetDate){
            result.push(task);
        }
    });
    return result;
    


  
}