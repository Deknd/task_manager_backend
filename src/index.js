import React from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";

import "bootstrap/dist/css/bootstrap.min.css";
import "./index.css";

import { App } from "./app/app";

import { store } from "./app/store";

createRoot(document.getElementById("root")).render(
  <Provider store={store}>
    <BrowserRouter>
      <React.Fragment>
        <App />
      </React.Fragment>
    </BrowserRouter>
  </Provider>
);
