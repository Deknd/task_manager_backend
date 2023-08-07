import taskDesctopBigHigh from '../img/taskDesctopBigHigh.jpg'
import taskDesctopBigStandart from '../img/taskDesctopBigStandard.jpg'

export const setBackground = (status, priority, setBackgroundTask, setBackgroundTaskColor) => {
    if(status !== 'DONE' && status !== 'FAILED'){
  
      switch(priority){
          case 'STANDARD': setBackgroundTask(taskDesctopBigStandart);
          break;
          case 'HIGH': setBackgroundTask(taskDesctopBigHigh);
          break;
      }
    }else{
      setBackgroundTask(null);
      switch(status){
        case 'DONE': setBackgroundTaskColor('#48f173');
        break;
        case 'FAILED': setBackgroundTaskColor('#fa1e1e70')
      }
    }
  }

