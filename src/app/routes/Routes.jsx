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

import { TasksDashboard, StartPage, LoginPage, RegistrationPage } from "../../pages";



const PrivateOutlet = (props) => {

  const { isLogin } = props;
  const location = useLocation();


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


  const needNavigate = useSelector((state) => state.authAndRegistrSlice.needNavigate);
  const isLogin = useSelector((state) => state.authAndRegistrSlice.isLogin);
  const startPage = useSelector((state) => state.authAndRegistrSlice.startPage);
  const isRegistrationSuccessful = useSelector((state) => state.authAndRegistrSlice.isRegistrationSuccessful);

  useEffect(() => {
  if (!isLogin) {
    dispatch(startPegeSelection());
  }
  }, [isLogin]);

  useEffect(() => {
      if (isLogin || needNavigate ) {
        navigate(`/${startPage}`);
      }
      if(isRegistrationSuccessful === 1){
        setTimeout( ()=>{ navigate(`/${startPage}`) }, 1000 );
        

      }
    }, [ needNavigate, isLogin, isRegistrationSuccessful ]);





  return (
    <Routes>
      <Route index element={<StartPage />} />
      <Route path={ROUTES.LOGIN} element={<LoginPage />} />
      <Route path={ROUTES.REGISTRATION} element={<RegistrationPage />} />

      <Route path="/" element={<PrivateOutlet isLogin={isLogin} />}>
        <Route path={ROUTES.MAIN} element={<TasksDashboard />}/>
      </Route>
    </Routes>
  );
};
