import React from "react";
import { useDispatch, useSelector } from "react-redux";



import { Button } from "react-bootstrap";

import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";
import { ROUTES } from "../../utils/routes";
import { logoutUser } from "../../features/user/userSlice";

import { getRefreshTokenFromCookie } from "../../features/user/tokens";

import {} from "../../features/user/userSlice";
import { logoutUserDelTasks } from "../../features/task/taskSlice";
import _default from "react-bootstrap/esm/CardGroup";
import { BigButton } from "../../shared/ui";
import { ButtonPanelWidget } from "../../widgets/ButtonPanelWidget";
import { ListTaskWidget } from "../../widgets/ListTaskWidget";
import { UserPanelWidget } from "../../widgets";

export const Main = () => {
  const name = useSelector((state) => state.user.name);


  

  const dispatch = useDispatch();

  return (
    <div className="container-fluid" style={{ padding: '0'}}>
      <UserPanelWidget/>
      {/* <div className="row">
        <div className="col-3 col-sm-4 col-lg-3 d-flex align-items-center justify-content-center border-end border-bottom border-4">
          <p className="fs-4">Hello, {name}</p>
        </div>
        <div className="col-9 col-sm-7 border-start border-bottom border-4">
          <nav className="navbar">
            <div className="container-fluid">
              <div className="row">
                <form className="d-flex col-8 mx-auto" role="search">
                  <input className="form-control me-2" aria-label="Search" />
                  <button className="btn btn-outline-success" type="submit">
                    Search
                  </button>
                </form>

                <div className="col ">
                  <Button
                    onClick={() => {
                      dispatch(logoutUserDelTasks());
                      const token = getRefreshTokenFromCookie();
                      dispatch(
                        logoutUser({
                          refreshToken: token,
                        })
                      );
                    }}
                    className="ms-auto"
                  >
                    Выход
                  </Button>
                </div>
              </div>
            </div>
          </nav>
        </div>
      </div> */}
      {/* Отображение на вертикальных экранах */}
      {/* Отображение на самых маленьких экранах */}
      <div className=" row flex-grow-1 d-block d-sm-none">
        <div className="col-12 border-4" style={{}}>
          <ul className="nav nav-pills nav-justified">
            <li className="nav-item">
              <BigButton />
            </li>
            <li className="nav-item">
              <NavLink
                exact="true"
                to={ROUTES.TODAY}
                className="nav-link"
                activeclassname="active"
              >
                Сегодня
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                exact="true"
                to={ROUTES.CALENDAR}
                className="nav-link"
                activeclassname="active"
              >
                Календарь
              </NavLink>
            </li>
          </ul>

          <div className="container-fluid mt-4">
            <div className="row">
              <div className="col d-flex justify-content-center">
                <Link href="#">
                  <img className="small-icon" alt="aewaega" />
                </Link>
              </div>
              <div className="col d-flex justify-content-center">
                <Link>
                  <img
                    
                    className="small-icon"
                    alt="aewaega"
                  />
                </Link>
              </div>
            </div>
          </div>
        </div>
        <div
          className="col-sm-9 col-12 border-start border-4"
          style={{ height: "50vh", overflowY: "auto" }}
        >
          {/* <Notification /> */}

          <Outlet />
        </div>
      </div>

      {/* Отображение на экранах больше 576px */}
      {/* <div className="row flex-grow-1 d-none d-sm-flex">
        <ButtonPanelWidget/> */}
        
        {/* <div className="col-sm-4 col-lg-3 border-end border-4">
          <div
            className="btn-group-vertical w-100 mt-2"
            role="group"
            aria-label="Vertical button group"
          >
           
            
            <BigButton colorButtonRBG='250, 237, 205, 0.65' img='' description='Все таски' toRoute={ROUTES.MAINTASK}/>
            
            
            <Link
              to={ROUTES.TODAY}
              type="button"
              className="btn btn-primary w-100 mb-2 fs-4"
            >
              Сегодня
            </Link>
            <Link
              to={ROUTES.CALENDAR}
              type="button"
              className="btn btn-primary w-100 mb-2 fs-4"
            >
              Календарь
            </Link>
            <Link
              to={ROUTES.ADDTASK}
              type="button"
              className="btn btn-primary w-100 mb-2 fs-4"
            >
              Добавить задачу
            </Link>
            <Link type="button" className="btn btn-primary w-100 mb-2 fs-4">
              Добавить напоминание
            </Link>
          </div>
        </div>
          */}


        {/* <div
          className="col-lg-9 col-sm-7 border-start border-4"
          style={{ 
           height: "80vh",
           overflowY: "auto",
           paddingRight: '0rem',
           paddingLeft: '0rem',
          }}
        > */}
          {/* <Notification /> */}
          {/* <Outlet />
        </div>  */}
        <div style={{ display: 'flex',}}>
          <div >
             <ButtonPanelWidget/>
          </div>
          <div style={{ flex: 1}}>
             <ListTaskWidget/>
          </div>



        </div>




      </div>
    
  );
};
