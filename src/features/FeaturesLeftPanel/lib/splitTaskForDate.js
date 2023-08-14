import style from './test.module.css'
export const splitTaskForDate = (array) => {
    const mapDateTasks = new Map();

    array.forEach((task) => {
        const formattedDate = formatDate(task.expirationDate);
        const iterator = mapDateTasks.keys();
        let haveKey = false;
        for(const key of iterator){
          if(`${key}`=== `${formattedDate}`){
            haveKey = true;
          }
        }
        if (!haveKey) {
            mapDateTasks.set(`${formattedDate}`, []);
        }
        mapDateTasks.get(`${formattedDate}`).push(task);
    });
    
    return mapDateTasks;
}
export const splitTaskForPastAndFeature = (map)=>{
    const iterator = map.keys();

    const dateToStyle = [];

    for(const date of iterator){
      const arrayTaskForDate = map.get(date);
      let countDone = 0;
      let countFailed = 0;
      let countHigh = 0;
      let countStandard =0;
      arrayTaskForDate.map(task => {
        if(task.status === 'DONE'){
          countDone+=1;
        } else{
          if(task.status === 'FAILED'){
            countFailed+=1;
          }else{
            if(task.priority === 'HIGH'){
              countHigh+=1;
            }
            if(task.priority === 'STANDARD'){
              countStandard+=1;
            }
          }
        }
      })
      
      const onePercent = (countDone+countFailed+countHigh+countStandard)/100;
      const donePercent = countDone/onePercent;
      const failedPercent = countFailed/onePercent;
      const highPercent = countHigh/onePercent;
      const standardPercent = countStandard/onePercent;
      const gradientStops = [
        `#fedfda 0%, #fedfda ${highPercent}%,`,
        `#c1e5d9 ${highPercent}%, #c1e5d9 ${standardPercent + highPercent}%,`,
        `#48f17370 ${standardPercent + highPercent}%, #48f17370 ${standardPercent + highPercent + donePercent}%,`,
        `#fa1e1e70 ${standardPercent + highPercent + donePercent}%, #fa1e1e70 ${standardPercent + highPercent + failedPercent + donePercent}%`
      ];
      const gradientString = `linear-gradient(to bottom, ${gradientStops.join(' ')})`;

      const style = {
        background: gradientString,
        borderRadius: '0.3em'
      };
      const dateString = dateToString(formatDate(date));


      dateToStyle.push (
        [{[`${dateString}`]: [formatDate(date)]},
        dateString,
        style]
        
      )

      
      
    }
    
        return dateToStyle;
}
const dateToString = (date) => {
  const day = date.getDate().toString().padStart(2, '0');
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const year = date.getFullYear().toString();
  const dayStyle = 'my_day_style';
  
  return `${dayStyle}_${day}_${month}_${year}`;
}

export const formatDate = (date) => {
    const dates = new Date(date);
      const year = dates.getFullYear();
      const month = dates.getMonth() + 1;
      const day = dates.getDate();
  
      const formattedDate = new Date(year, month - 1, day);
      return formattedDate;
}
export const compareDates= (date1, date2) => {
    const year1 = date1.getFullYear();
    const month1 = date1.getMonth();
    const day1 = date1.getDate();
    
    const year2 = date2.getFullYear();
    const month2 = date2.getMonth();
    const day2 = date2.getDate();
    
    if (year1 === year2 && month1 === month2 && day1 === day2) {
      return true; // Даты равны
    } else if (year1 < year2 || (year1 === year2 && month1 < month2) || (year1 === year2 && month1 === month2 && day1 < day2)) {
      return false; // Первая дата меньше второй
    } else {
      return false; // Первая дата больше второй
    }
  }