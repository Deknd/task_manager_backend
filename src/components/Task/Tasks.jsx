import React, { useEffect, useState } from "react";
import { Task } from "./Task";
import { useSelector } from "react-redux";
import { sortArray } from "./taskSort";

export const Tasks = () => {
  let taskss = useSelector((state) => state.task.tasks);

  useEffect(() => {
    let sort = sortArray(taskss);
    setArray({ tasks: sort });
  }, [taskss]);

  const [array, setArray] = useState({
    tasks: [],
  });

  return (
    <div className="container">
      <div className="row " style={{}}>
        {array.tasks.map((task) => (
          <Task key={task.id} task={task} />
        ))}
      </div>
    </div>
  );
};
