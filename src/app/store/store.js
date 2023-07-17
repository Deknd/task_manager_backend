import { configureStore } from "@reduxjs/toolkit";


import { authRegistrSlice, userSliceR } from "../../features";
import { sliceTasks }from "../../features";
import { taskWidgetSlice } from '../../widgets/ListTaskWidget'

import { middlewares } from "./middleware";



export const store = configureStore({
  reducer: {
    user: userSliceR,
    authAndRegistrSlice: authRegistrSlice,
    tasks: sliceTasks,
    taskWidget: taskWidgetSlice,
    
  },
  middleware: (getDefaultMiddleware) =>
  getDefaultMiddleware().concat(middlewares),
  devTools: true,
});
