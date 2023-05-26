import React from "react";
import { Task } from "./Task";
import { useSelector } from "react-redux";

export const Tasks = () => {
  let tasks = useSelector((state) => state.task.tasks);

  const tasksd = [
    {
      id: "1",
      title: "Учеба",
      description: "Description length must be smaller than 255 symbols",
      status: "TODO",
      expirationDate: "2023.25.05 13:56",
    },
    {
      id: "2",
      title: "Покупки",
      description: "Купить продукты для завтрака",
      status: "IN_PROGRESS",
      expirationDate: "2023.25.05 14:30",
    },
    {
      id: "3",
      title: "Тренировка",
      description: "Сходить в спортзал",
      status: "IN_PROGRESS",
      expirationDate: "2023.25.05 18:00",
    },
    {
      id: "4",
      title: "Прогулка",
      description: "Прогуляться по парку",
      status: "TODO",
      expirationDate: "2023.26.05 15:00",
    },
    {
      id: "5",
      title: "Работа",
      description: "Завершить проект",
      status: "DONE",
      expirationDate: "2023.27.05 12:00",
    },
    {
      id: "6",
      title: "Отдых",
      description: "Посмотреть новый фильм",
      status: "TODO",
      expirationDate: "2023.27.05 20:00",
    },
    {
      id: "7",
      title: "Сборы",
      description: "Подготовиться к важной встрече",
      status: "IN_PROGRESS",
      expirationDate: "2023.28.05 09:00",
    },
  ];

  return (
    <div className="container">
      <div className="row " style={{}}>
        {tasks.map((task) => (
          <Task key={task.id} task={task} />
        ))}
      </div>
    </div>
  );
};
