import React from "react";

export const Notification = () => {
  return (
    <div className="container mt-2 px-4 align-items-center justify-content-center">
      <div
        className="row "
        style={{
          backgroundColor: "green",
          height: "40px",
          borderRadius: "10px",
        }}
      >
        <p
          className="text-center m-0 fs-2"
          style={{ lineHeight: "40px", verticalAlign: "middle" }}
        >
          Напоминание
        </p>
      </div>
    </div>
  );
};
