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
    <div className="container-fluid">
      <div className="d-flex flex-wrap justify-content" style={{
        fontSize: "15px"
      }}>
        {array.tasks.map((task) => (
          <div className="m-1 p-2" >
          <Task key={task.id} task={task} />
          </div>
        ))}
      </div>
    </div>
  );
};
