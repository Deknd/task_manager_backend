export let needUpdate =[];
export const fillterAllActiveTasks = (tasks) => {
    const notDoneAndNotFailedTasks =[];
    tasks.map((task)=>{
        if(task.status !== 'FAILED' && task.status !== 'DONE'){
            notDoneAndNotFailedTasks.push(task);
        }
    });
   
    const result = [];
    notDoneAndNotFailedTasks.map((task)=>{
        const currentDate = new Date();
        const targetDate = new Date(task.expirationDate); // Целевая дата и время
       

        if (targetDate <= currentDate) {
            needUpdate.push(task);
        } else {
            result.push(task);
        }
    });
    return result;
}