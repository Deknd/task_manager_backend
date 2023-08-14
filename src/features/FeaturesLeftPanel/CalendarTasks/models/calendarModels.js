import { setTasks } from "../../../../widgets/ListTaskWidget/taskWidgetSlice";
import { sortArray } from "../../lib/fillterCalendar";


//стили которые надо подменить
let styleChange = null;
//динамическая подмена стилей
const changeStyle = ()=>{
   
    styleChange.forEach(el => {
        const dateString = el[0];
        const style = el[1];
        const elements = document.querySelectorAll(`.${dateString}`); 
    
        elements.forEach(calendarDate => {
            for (const property in style) {
                if (style.hasOwnProperty(property)) {
                    calendarDate.style[property] = style[property];
                }
                }
        })
    })
    
}
  //разделение тасков по датам, присваивание стилей датам и выписка их для подмены динамической
const splitMapDateTasks = (array) => {
  styleChange = null;
    const mapDateTasks = splitTaskForDate(array);
    const arrayDate = splitTaskForPastAndFeature(mapDateTasks);
    const highlightWithRangesArray = [];
    const styleForHighlightWithRangesArray = [];
    arrayDate.forEach(
        el => {
        highlightWithRangesArray.push(el[0]);
        styleForHighlightWithRangesArray.push([el[1],el[2]])
        }
    )
   
   styleChange = styleForHighlightWithRangesArray;
    return {mapDateTasks,  highlightWithRangesArray};
};



//Диспатчит таски после фильтрования
const dispathSetTask = (startDate, mapTasks, dispatch) => {
    const dateActive = formatDate(startDate);
    const iterator = mapTasks.keys();
    let key = null;
    for (const date of iterator) {
        const resDate = formatDate(date);
        if(compareDates(dateActive, resDate)){
            key=date
            //dispatch(setTasks(mapTasks.get(date)))
        } 
    }
    if(key !== null){
        const result = mapTasks.get(`${key}`);
        dispatch(setTasks(sortArray(result) ))
    }else { 

        dispatch(setTasks([])) }
}
//деление тасков по дате
const splitTaskForDate = (array) => {
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

//создание динамических стилей для дат
const splitTaskForPastAndFeature = (map)=>{
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


//мапит дату в стринг, для динамического название стилей
const dateToString = (date) => {
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear().toString();
    const dayStyle = 'my_day_style';
    
    return `${dayStyle}_${day}_${month}_${year}`;
  }
  
  //форматирует дату под стандард
 const formatDate = (date) => {
      const dates = new Date(date);
        const year = dates.getFullYear();
        const month = dates.getMonth() + 1;
        const day = dates.getDate();
    
        const formattedDate = new Date(year, month - 1, day);
        return formattedDate;
  }
  //сравнивает даты
  const compareDates= (date1, date2) => {
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
    export const calendarModuels = { dispathSetTask, splitMapDateTasks, changeStyle, }