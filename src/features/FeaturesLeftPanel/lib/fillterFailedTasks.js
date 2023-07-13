const fillterFailedTasks = (tasksAll) => {

    console.log(tasksAll)
    const taskFailed = []
    tasksAll.map((task)=>{
        if(task.status === 'FAILED'){
            taskFailed.push(task)
        }
        
    })
    taskFailed.sort((a,b) => {
        const dataA = new Date(a.expirationDate);
        const dataB = new Date(b.expirationDate);
        return dataA - dataB;
    })
    return taskFailed;

}
export { fillterFailedTasks }