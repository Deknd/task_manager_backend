const deleteRefreshTokenCookie = () => {
    document.cookie =
      "refreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  };

  const setRefreshTokenCookie = (refreshToken) => {
    deleteRefreshTokenCookie();
  
    const expirationDate = new Date();
    expirationDate.setTime(expirationDate.getTime() + 24 * 60 * 60 * 1000);
  
    document.cookie = `refreshToken=${refreshToken}; expires=${expirationDate.toUTCString()}; path=/`; //secure нужно будет добавить флаг, как перейдем на https
  };

  const getRefreshTokenFromCookie = () => {
    const cookies = document.cookie.split(";").map((cookie) => cookie.trim());
    for (const cookie of cookies) {
      if (cookie.startsWith("refreshToken=")) {
        return cookie.substring("refreshToken=".length);
      }
    }
    deleteRefreshTokenCookie();
    return null;
  };

  export const cookieForRefreshToken = { deleteRefreshTokenCookie, setRefreshTokenCookie, getRefreshTokenFromCookie }