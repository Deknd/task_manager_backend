import { configureStore } from "@reduxjs/toolkit";


import userSlice from "../../features/user/userSlice";
import registrationSlice from "../../features/registration/registrationSlice";
import taskSlice from "../../features/task/taskSlice";
import { taskWidgetSlice } from '../../widgets/ListTaskWidget'

import { middleware } from "./middleware";



export const store = configureStore({
  reducer: {
    user: userSlice,
    registration: registrationSlice,
    task: taskSlice,
    taskWidget: taskWidgetSlice,
    
  },
  middleware: (getDefaultMiddleware) =>
  getDefaultMiddleware().concat(middleware),
  devTools: true,
});
