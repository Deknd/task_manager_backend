import React, { useEffect } from "react";
import {
  Route,
  Routes,
  Navigate,
  Outlet,
  useLocation,
  useNavigate,
} from "react-router-dom";
import { Home } from "../Home/Home";
import { Login } from "../Login/Login";
import { ROUTES } from "../../utils/routes";
import { Registration } from "../Registration/Registration";
import { Main } from "../Main/Main";
import { MainTasks } from "../Main/Tasks/MainTasks";
import { Today } from "../Main/Today/Today";
import { Calendar } from "../Main/Сalendar/Сalendar";
import { AddTask } from "../Main/AddTask/AddTask";
import { getRefreshTokenFromCookie } from "../../features/user/tokens";
import { updateRefreshToken } from "../../features/user/userSlice";
import { useDispatch, useSelector } from "react-redux";

const PrivateOutlet = () => {
  const location = useLocation();

  const isAuthenticated = useSelector((state) => state.user.isLogin);

  return isAuthenticated ? (
    <Outlet />
  ) : (
    <Navigate to={ROUTES.LOGIN} state={{ from: location }} />
  );
};

export const AppRoutes = () => {
  const dispatch = useDispatch();

  const navigate = useNavigate();

  let isLogin = useSelector((state) => state.user.isLogin);
  let refreshToken = getRefreshTokenFromCookie();

  useEffect(() => {
    if (isLogin) {
      navigate(`/${ROUTES.MAIN}${ROUTES.MAINTASK}`);
    }
  }, [isLogin]);

  useEffect(() => {
    if (refreshToken !== null && !isLogin) {
      dispatch(updateRefreshToken({ refreshToken: refreshToken }));
    }
  }, []);
  return (
    <Routes>
      <Route index element={<Home />} />
      <Route path={ROUTES.LOGIN} element={<Login />} />
      <Route path={ROUTES.REGISTRATION} element={<Registration />} />

      <Route path="/" element={<PrivateOutlet />}>
        <Route path={ROUTES.MAIN} element={<Main />}>
          <Route path={ROUTES.MAINTASK} element={<MainTasks />} />
          <Route path={ROUTES.TODAY} element={<Today />} />
          <Route path={ROUTES.CALENDAR} element={<Calendar />} />
          <Route path={ROUTES.ADDTASK} element={<AddTask />} />
        </Route>
      </Route>
    </Routes>
  );
};
