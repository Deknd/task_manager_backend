import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom"; // Import useHistory hook
import { useDispatch, useSelector } from "react-redux";
import { Modal, Button } from "react-bootstrap";

import { loginUser, setIsNoTryLogPass } from "../../features/user/userSlice";

import { ROUTES } from "../../utils/routes";

export const Login = () => {
  const dispatch = useDispatch();

  let iisNoTryLogPass = useSelector((state) => state.user.isNoTryLogPass);

  const handleSubmit = (e) => {
    e.preventDefault();

    const isNotEmpty = Object.values(user).every((val) => val);
    if (!isNotEmpty) return;

    dispatch(
      loginUser({
        username: user.username,
        password: user.password,
      })
    );
  };

  useEffect(() => {
    setShowModal(iisNoTryLogPass);
  }, [iisNoTryLogPass]);

  const [showModal, setShowModal] = useState(false);

  const [user, setUser] = useState({
    username: "johndoe@mail.com",
    password: "12345",
  });

  const handleChange = ({ target: { value, name } }) => {
    setUser({ ...user, [name]: value });
  };

  return (
    <div>
      <main>
        <section>
          <div className="container-fluid text-center mt-4">
            <p className="display-1 mb-3">Log in</p>
          </div>
          <div>
            <Modal
              show={showModal}
              onHide={() => dispatch(setIsNoTryLogPass(false))}
              backdrop="static"
              keyboard={false}
              centered
            >
              <Modal.Header closeButton>
                <Modal.Title>Ошибка</Modal.Title>
              </Modal.Header>
              <Modal.Body>Не правильный Логин или Пароль</Modal.Body>
              <Modal.Footer>
                <Button
                  variant="secondary"
                  onClick={() => dispatch(setIsNoTryLogPass(false))}
                >
                  Close
                </Button>
              </Modal.Footer>
            </Modal>
          </div>

          <div className="container-fluid col-8 col-lg-3 col-md-5 col-sm-6 mx-auto">
            <form onSubmit={handleSubmit}>
              <div className=" text-center">
                <label htmlFor="exampleInputEmail1" className="form-label">
                  <p className="fs-5"> Email address</p>
                </label>
                <input
                  type="email"
                  placeholder="Email address"
                  name="username"
                  value={user.username}
                  autoComplete="off"
                  onChange={handleChange}
                  required
                  className="form-control"
                  id="exampleInputEmail1"
                />
                <div id="emailHelp" className="form-text">
                  <p className=" myPtext fs-8">
                    We'll never share your email with anyone else.
                  </p>
                </div>
              </div>
              <div className="mb-2 text-center">
                <label htmlFor="exampleInputPassword1" className="form-label">
                  <p className="fs-5">Password</p>
                </label>
                <input
                  type="password"
                  placeholder="Password"
                  name="password"
                  value={user.password}
                  autoComplete="off"
                  onChange={handleChange}
                  required
                  className="form-control"
                  id="exampleInputPassword1"
                />
              </div>
              <div className="d-flex justify-content-center my-4">
                <button type="submit" className="btn btn-primary ">
                  Submit
                </button>
              </div>
            </form>
          </div>

          <div className="container-fluid">
            <div className="row mt-2">
              <div className="container-fluid text-center mx-auto ">
                <p className="fs-6">
                  If you are not yet registered, we suggest you complete a
                  simple registration
                </p>
              </div>
            </div>
          </div>
          <div className="d-flex justify-content-center my-4">
            <Link to={`/${ROUTES.REGISTRATION}`}>
              <Button>Registration</Button>
            </Link>
          </div>
        </section>
      </main>
    </div>
  );
};
