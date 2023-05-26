import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import { createUser } from "../../features/registration/registrationSlice";
import { setRegistrationSuccess } from "../../features/registration/registrationSlice";
import { loginUser } from "../../features/user/userSlice";

export const Registration = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(setRegistrationSuccess(false));
  }, [dispatch]);

  const isRegistrationSuccess = useSelector(
    (state) => state.registration.isRegistrationSuccess
  );

  const handleSubmit = (e) => {
    e.preventDefault();

    const user = {
      name: "",
      username: "",
      password: "",
    };

    if (values.password === values.passwordConfirmation) {
      user.password = values.password;
      user.name = values.name;
      user.username = values.username;

      const isNotEmpty = Object.values(user).every((val) => val);
      if (!isNotEmpty) return;

      dispatch(createUser(user));
    } else {
      return;
    }
  };

  useEffect(() => {
    if (isRegistrationSuccess) {
      const isNotEmpty = Object.values({
        username: values.username,
        password: values.password,
      }).every((val) => val);
      if (!isNotEmpty) return;

      dispatch(
        loginUser({
          username: values.username,
          password: values.password,
        })
      ).then(
        setValues({
          name: "",
          username: "",
          password: "",
          passwordConfirmation: "",
        })
      );
    }
  }, [isRegistrationSuccess]);

  function handleChange({ target: { value, name } }) {
    if (name === "passwordConfirmation") {
      setIsPasswordMatch(value === values.password);
    }

    if (name === "name" && value.length < 3) {
      if (value.length > 0) {
        setErrors((prevErrors) => ({
          ...prevErrors,
          name: "Имя должно быть не меньше 3 символов",
        }));
      } else {
        setErrors((prevErrors) => ({
          ...prevErrors,
          name: "", // Очистка ошибки, если имя проходит проверку
        }));
      }
    } else {
      setErrors((prevErrors) => ({
        ...prevErrors,
        name: "", // Очистка ошибки, если имя проходит проверку
      }));
    }

    if (name === "username" && !validateEmail(value)) {
      if (value.length > 0) {
        setErrors((prevErrors) => ({
          ...prevErrors,
          username: "Введите коректный email",
        }));
      } else {
        setErrors((prevErrors) => ({
          ...prevErrors,
          username: "", // Очистка ошибки, если имя проходит проверку
        }));
      }
    } else {
      setErrors((prevErrors) => ({
        ...prevErrors,
        username: "", // Очистка ошибки, если имя проходит проверку
      }));
    }

    setValues({ ...values, [name]: value });
  }

  const [isPasswordMatch, setIsPasswordMatch] = useState(true);

  function validateEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
  }

  const [values, setValues] = useState({
    name: "",
    username: "",
    password: "",
    passwordConfirmation: "",
  });

  const [errors, setErrors] = useState({
    name: "",
    username: "",
    password: "",
  });

  return (
    <div>
      <main>
        <section>
          <div className="container-fluid text-center mt-4">
            <div className="col">
              <p className="display-1">Registration</p>
            </div>
            <div className="row">
              <div className="container-fluid text-center mx-auto col-8">
                <p className="fs-4">Enter your email.</p>
                <p className="fs-4">
                  We'll send you a code that you must enter in the app to verify
                  your identity. If the code is entered correctly, you will be
                  able to enter a password
                </p>
              </div>
            </div>
          </div>

          {isRegistrationSuccess ? (
            <div className="container-fluid text-center mx-auto col-8 my-5 py-5">
              <p className="fs-1">Registration Successful!</p>
            </div>
          ) : (
            <div className="d-grid gap-3 col-3 mx-auto text-center my-3">
              <form onSubmit={handleSubmit}>
                <div className="mb-1">
                  <label htmlFor="exampleUserName" className="form-label">
                    User name
                  </label>
                  <p style={{ color: "red", fontSize: "13px" }}>
                    {errors.name}
                  </p>
                  <input
                    type="name"
                    placeholder="User name"
                    name="name"
                    value={values.name}
                    autoComplete="off"
                    onChange={handleChange}
                    required
                    className="form-control"
                    id="exampleUserName"
                  />
                </div>
                <div className="mb-1">
                  <label htmlFor="exampleInputEmail1" className="form-label">
                    Email address
                  </label>
                  <p style={{ color: "red", fontSize: "13px" }}>
                    {errors.username}
                  </p>
                  <input
                    type="email"
                    placeholder="Email address"
                    name="username"
                    value={values.username}
                    autoComplete="off"
                    onChange={handleChange}
                    required
                    className="form-control"
                    id="exampleInputEmail1"
                  />
                </div>
                <div className="mb-1">
                  <label htmlFor="exampleInputPassword1" className="form-label">
                    Password
                  </label>
                  <input
                    type="password"
                    placeholder="Password"
                    name="password"
                    value={values.password}
                    autoComplete="off"
                    onChange={handleChange}
                    required
                    className="form-control"
                    id="exampleInputPassword1"
                  />
                </div>
                <div className="mb-1">
                  <label
                    htmlFor="exampleInputPasswordСonfirmation"
                    className="form-label"
                  >
                    Password confirmation
                  </label>
                  <input
                    type="password"
                    placeholder="Password confirmation"
                    name="passwordConfirmation"
                    value={values.passwordConfirmation}
                    autoComplete="off"
                    onChange={handleChange}
                    required
                    id="exampleInputPasswordСonfirmation"
                    className="form-control"
                    style={
                      !isPasswordMatch
                        ? { borderColor: "red", borderWidth: "3px" }
                        : null
                    }
                  />
                </div>
                <button type="submit" className="btn btn-primary">
                  Submit
                </button>
              </form>
            </div>
          )}
        </section>
      </main>
    </div>
  );
};
