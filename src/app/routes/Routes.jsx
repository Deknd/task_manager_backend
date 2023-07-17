import React, { useEffect } from "react";

import {
  Route,
  Routes,
  Navigate,
  Outlet,
  useLocation,
  useNavigate,
} from "react-router-dom";

import { useDispatch, useSelector } from "react-redux";

import { actionAuthRegistrSlice } from "../../features";

import { ROUTES } from "../../shared/lib/constants/routes";


import { Registration } from "../../components/Registration/Registration";

import { TasksDashboard, StartPage, LoginPage } from "../../pages";


//import { getRefreshTokenFromCookie } from "../../features/user/tokens";
//import { updateRefreshToken } from "../../features/user/userSlice";


const PrivateOutlet = (props) => {

  const { isLogin } = props;
  const location = useLocation();

 // const isAuthenticated = useSelector((state) => state.user.isLogin);

  return isLogin ? (
    <Outlet />
  ) : (
    <Navigate to={ROUTES.LOGIN} state={{ from: location }} />
  );
};

export const AppRoutes = () => {

  const dispatch = useDispatch();
  const {
    startPegeSelection,
  }=actionAuthRegistrSlice;

  const navigate = useNavigate();

 // let isLogin = useSelector((state) => state.user.isLogin);
 // let refreshToken = getRefreshTokenFromCookie();

  // useEffect(() => {
  //   if (isLogin) {
  //     navigate(`/${ROUTES.MAIN} `);
  //   }
  // }, [isLogin]);
  const needNavigate = useSelector((state) => state.authAndRegistrSlice.needNavigate);
  const isLogin = useSelector((state) => state.authAndRegistrSlice.isLogin);
  const startPage = useSelector((state) => state.authAndRegistrSlice.startPage);

  useEffect(() => {
  console.log( isLogin)
  if (!isLogin) {
    dispatch(startPegeSelection());
  }
  }, [isLogin]);

  useEffect(() => {
      if (isLogin || needNavigate) {
        console.log(startPage)
        navigate(`/${startPage}`);
      }
    }, [needNavigate, isLogin]);





  return (
    <Routes>
      <Route index element={<StartPage />} />
      <Route path={ROUTES.LOGIN} element={<LoginPage />} />
      <Route path={ROUTES.REGISTRATION} element={<Registration />} />

      <Route path="/" element={<PrivateOutlet isLogin={isLogin} />}>
        <Route path={ROUTES.MAIN} element={<TasksDashboard />}/>
      </Route>
    </Routes>
  );
};
