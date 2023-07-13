const fillterDoneTasks = (tasksAll) => {

    const taskDone = []
    tasksAll.map((task)=>{
        if(task.status === 'DONE'){
            taskDone.push(task)
        }
        
    })
    taskDone.sort((a,b) => {
        const dataA = new Date(a.expirationDate);
        const dataB = new Date(b.expirationDate);
        return dataA - dataB;
    })
    return taskDone;

}
export { fillterDoneTasks }