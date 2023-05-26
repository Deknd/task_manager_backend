import React from "react";
import { Link } from "react-router-dom";

export const Button = ({ urlBtn, textBtn }) => (
  <div className="container-fluid">
    <div className="row">
      <div className="d-flex justify-content-center align-items-center">
        <Link
          to={urlBtn}
          className="btn btn-primary  text-center col-6 col-lg-2 col-md-4 col-sm-5"
        >
          <span className="fs-6">{textBtn}</span>
        </Link>
      </div>
    </div>
  </div>
);
