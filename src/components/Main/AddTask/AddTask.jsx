import React, { useState } from "react";
import { Form } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { addTask } from "../../../features/task/taskSlice";
import { getAccessToken } from "../../../features/user/tokens";

export const AddTask = () => {
  const dispatch = useDispatch();

  let userId = useSelector((state) => state.user.id);
  let isLogin = useSelector((state) => state.user.isLogin);

  const handleSubmit = (event) => {
    event.preventDefault();

    if (task.title.length !== 0 && task.title.length < 20) {
      const currentDate = new Date();
      const inputDate = new Date(task.date);
      if (currentDate > inputDate) {
        setErrors((prevErrors) => ({
          ...prevErrors,
          date: " Некоректная дата",
        }));
        return;
      } else {
        if (task.time.length === "") {
          setTask({ ...task, time: "00:00" });
        }
        const datetimeString = `${task.date} ${task.time}`;
        const formattedDatetime = new Date(datetimeString).toISOString();
        setTask({ ...task, expirationDate: { formattedDatetime } });
        const aToken = getAccessToken(isLogin);
        if (aToken !== null) {
          dispatch(
            addTask({
              id: userId,
              accessToken: aToken,

              title: task.title,
              description: task.description,
              priority: task.priorityTask,
              expirationDate: datetimeString,
            })
          )
            .then(() => {
              // Сбросить значения формы
              setTask({
                title: "",
                description: "",
                expirationDate: "",
                time: "",
                date: "",
                priorityTask: "STANDART",
              });
            })
            .catch((error) => {
              // Обработка ошибки отправки задачи
              console.log("Ошибка отправки задачи:", error);
            });
        }
      }
    } else return;
  };

  const [task, setTask] = useState({
    title: "",
    description: "",
    expirationDate: "",
    time: "",
    date: "",
    priorityTask: "STANDART",
  });

  const handleChange = ({ target: { value, name } }) => {
    if (name === "title" && task.title.length > 20) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        title: " Заголовок слишком длинный (максимум 20 символов)",
      }));
    } else {
      setErrors((prevErrors) => ({
        ...prevErrors,
        title: "",
      }));
    }

    if (name === "date") {
      const currentDate = new Date();
      const inputDate = new Date(value);
      if (currentDate > inputDate) {
        setErrors((prevErrors) => ({
          ...prevErrors,
          date: " Некоректная дата",
        }));
      } else {
        setErrors((prevErrors) => ({
          ...prevErrors,
          date: "",
        }));
      }
    }

    setTask({ ...task, [name]: value });
  };

  const [errors, setErrors] = useState({
    title: "",
    description: "",
    expirationDate: "",
    time: "",
    date: "",
    priorityTask: "",
  });

  return (
    <div className="container">
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="username" className="form-label">
            Введите краткое описание задачи
          </label>
          <p style={{ color: "red", fontSize: "13px" }}>{errors.title}</p>
          <div className="input-group">
            <input
              type="text"
              className="form-control"
              name="title"
              placeholder="Title"
              aria-describedby="basic-addon1"
              value={task.title}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <div className="input-group">
          <span className="input-group-text">Описание задачи</span>
          <textarea
            className="form-control"
            aria-label="With textarea"
            name="description"
            onChange={handleChange}
            value={task.description}
          ></textarea>
        </div>

        <div className="container">
          <div className="row">
            <div className="col">
              <div className="mb-3">
                <label htmlFor="datepicker" className="form-label">
                  Дата дедлайна
                </label>
                <p style={{ color: "red", fontSize: "13px" }}>{errors.date}</p>

                <input
                  type="date"
                  className="form-control"
                  id="datepicker"
                  name="date"
                  onChange={handleChange}
                  required
                />
              </div>
            </div>
            <div className="col">
              <div className="mb-3">
                <label htmlFor="timepicker" className="form-label">
                  Время дедлайна( если есть )
                </label>
                <input
                  type="time"
                  className="form-control"
                  id="timepicker"
                  value={task.time}
                  name="time"
                  onChange={handleChange}
                />
              </div>
            </div>
          </div>
        </div>

        <div className="container">
          <p>Выберите приоритет задачи:</p>
          <Form.Group controlId="prioritySelect">
            <Form.Select
              value={task.priorityTask}
              onChange={handleChange}
              name="priorityTask"
            >
              <option value="STANDART">Стандартный</option>

              <option value="HIGH">Важный</option>
            </Form.Select>
          </Form.Group>

          <div className="d-flex justify-content-center my-4">
            <button type="submit" className="btn btn-primary">
              Сохранить
            </button>
          </div>
        </div>
      </form>
    </div>
  );
};
