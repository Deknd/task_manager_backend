export const splitTaskForDate = (array) => {
    const mapDateTasks = new Map();

    array.forEach((task) => {
        const formattedDate = formatDate(task.expirationDate);
        if (!mapDateTasks.has(formattedDate)) {
            mapDateTasks.set(formattedDate, []);
        }
        mapDateTasks.get(formattedDate).push(task);
    });
    return mapDateTasks;
}
export const splitTaskForPastAndFeature = (map)=>{
    const iterator = map.keys();
        const red = { "react-datepicker__day--highlighted-custom-1": [] };
        const green = { "react-datepicker__day--highlighted-custom-2": [] };
        const dateNow = formatDate(new Date());

        for (const date of iterator) {
            if (dateNow <= date) {
                green["react-datepicker__day--highlighted-custom-2"].push(date);
            } else {
                red["react-datepicker__day--highlighted-custom-1"].push(date);
            }
        }
        return [red, green]
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