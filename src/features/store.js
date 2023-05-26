import { configureStore } from "@reduxjs/toolkit";

import userSlice from "./user/userSlice";
import registrationSlice from "./registration/registrationSlice";
import taskSlice from "./task/taskSlice";

export const store = configureStore({
  reducer: {
    user: userSlice,
    registration: registrationSlice,
    task: taskSlice,
  },
  devTools: true,
});
