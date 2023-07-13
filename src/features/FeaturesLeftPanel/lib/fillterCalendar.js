//let arrayTask = [];

export const sortArray = (arrayTask) => {
  const currentDate = new Date();

  let noSortArray = arrayTask;

  let expressArray = lessThan24HoursTasks(noSortArray, currentDate);

  let subtractedArray = noSortArray.filter(
    (item) => !expressArray.includes(item)
  );

  expressArray.sort(compareTasks);
  subtractedArray.sort(compareTasks);

  const mergedArray = mergeArrays(expressArray, subtractedArray);

  return mergedArray;
};

const lessThan24HoursTasks = (array, nowDate) => {
  return array.filter((task) => {
    const taskDate = new Date(task.expirationDate);
    const timeDiff = taskDate.getTime() - nowDate.getTime();
    const hoursDiff = timeDiff / (1000 * 3600);

    return hoursDiff < 24;
  });
};

const compareTasks = (task1, task2) => {
  if (task1.priority === "HIGH" && task2.priority !== "HIGH") {
    return -1;
  } else if (task1.priority !== "HIGH" && task2.priority === "HIGH") {
    return 1;
  } else {
    const date1 = new Date(task1.expirationDate);
    const date2 = new Date(task2.expirationDate);
    return date1 - date2;
  }
};


const mergeArrays = (arr1, arr2) => {
  const mergedArray = [...arr1, ...arr2];
  return mergedArray;
};
