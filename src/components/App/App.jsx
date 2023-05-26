import React from "react";
import { AppRoutes } from "../Routes/Routes";
import { Footer } from "../Footer/Footer";

export const App = () => {
  return (
    <div className="app">
      <AppRoutes />
      <Footer />
    </div>
  );
};
