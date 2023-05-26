import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

import { Tasks } from "../../Task/Tasks";
import { getAllTasks } from "../../../features/task/taskSlice";
import { getAccessToken } from "../../../features/user/tokens";

export const MainTasks = () => {
  const dispatch = useDispatch();

  const isLoadTask = useSelector((state) => state.task.isLoadTasks);
  const userId = useSelector((state) => state.user.id);
  const isLogin = useSelector((state) => state.user.isLogin);

  useEffect(() => {
    console.log(!isLoadTask && isLogin);
    if (!isLoadTask && isLogin) {
      const access = getAccessToken(isLogin);
      dispatch(
        getAllTasks({
          id: userId,
          accessToken: access,
        })
      );
    }
  }, []);

  return <Tasks />;
};
