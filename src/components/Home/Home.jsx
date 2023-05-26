import React from "react";
import { Button } from "../Button/Button";
import { ROUTES } from "../../utils/routes";

export const Home = () => (
  <div>
    <main>
      <section>
        <div className="container-fluid text-center mt-4">
          <div className="col">
            <p className="fs-1">Help make your life easier</p>
          </div>
          <div className="col">
            <p className="display-1">My Task Manager</p>
          </div>
          <div className="col">
            <p className="fs-1">Change your life today</p>
          </div>
        </div>
        <div>
          <Button urlBtn={ROUTES.LOGIN} textBtn="Get start" />
        </div>
      </section>
    </main>
  </div>
);
