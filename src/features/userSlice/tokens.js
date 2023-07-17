//import { API, HOST, PORT } from "../../utils/constants";

import axios from "axios";

export const setRefreshTokenCookie = (refreshToken) => {
  deleteRefreshTokenCookie();

  const expirationDate = new Date();
  expirationDate.setTime(expirationDate.getTime() + 24 * 60 * 60 * 1000);

  document.cookie = `refreshToken=${refreshToken}; expires=${expirationDate.toUTCString()}; path=/`; //secure нужно будет добавить флаг, как перейдем на https
};

// Получение refreshToken из cookie
export const getRefreshTokenFromCookie = () => {
  const cookies = document.cookie.split(";").map((cookie) => cookie.trim());
  for (const cookie of cookies) {
    if (cookie.startsWith("refreshToken=")) {
      return cookie.substring("refreshToken=".length);
    }
  }
  deleteRefreshTokenCookie();
  return null;
};
export const deleteRefreshTokenCookie = () => {
  document.cookie =
    "refreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  deleteAccessToken();
};

export const setAccessTokenSessionStorage = (accessToken, expiration) => {
  deleteAccessToken();
  const tokenData = {
    accessToken: accessToken,
    expirationTime: expiration,
  };

  sessionStorage.setItem("accessToken", JSON.stringify(tokenData));
};

export const getAccessToken = (isLogin) => {
   const refreshToken = (e) =>{};//async (payload) => {
  //   try {
  //     const response = await axios.post(
  //       `${HOST}:${PORT}${API}auth/refresh`,
  //       payload
  //     );
  //     return response.data;
  //   } catch (error) {
  //     console.log(error);
  //     throw error;
  //   }
  // };

  const storedTokenData = JSON.parse(sessionStorage.getItem("accessToken"));

  if (storedTokenData.accessToken !== null && isLogin) {
    const targetDate = new Date(storedTokenData.expirationTime);
    const currentDate = new Date();
    if (targetDate > currentDate) {
      return storedTokenData.accessToken;
    } else {
      const refreshTok = getRefreshTokenFromCookie();

      if (refreshTok !== null) {
        refreshToken(refreshTok)
          .then((res) => {
            setRefreshTokenCookie(res.refreshToken);
            setAccessTokenSessionStorage(res.accessToken, res.expiration);
            return getAccessToken();
          })
          .catch((err) => {
            deleteRefreshTokenCookie();
          });
      } else {
        deleteRefreshTokenCookie();
      }
    }
  } else {
    deleteRefreshTokenCookie();
    deleteAccessToken();
  }
};

export const deleteAccessToken = () => {
  sessionStorage.removeItem("aceessToken");
};
