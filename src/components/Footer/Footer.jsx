import React from "react";
import { Link } from "react-router-dom";
import { ROUTES } from "../../utils/routes";

export const Footer = () => (
  <footer className="d-grid   mt-4 pt-3 pb-5 px-3">
    <div className="container-fluid text-center mx-auto">
      <div className="row">
        <div className="col">
          <Link to={ROUTES.HOME}>Products</Link>
        </div>
        <div className="col">
          <Link to={ROUTES.HOME}>FAQ</Link>
        </div>
        <div className="col">
          <Link to={ROUTES.HOME}>About us & support</Link>
        </div>
      </div>
    </div>
  </footer>
);
