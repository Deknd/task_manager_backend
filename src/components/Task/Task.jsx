import deleteImg from "../../images/delete.png";
import yesImg from "../../images/yes.png";
import noImg from "../../images/no.png";

export const Task = ({ task }) => {
  return (
    <div
      className="col-5 col-sm-5 col-md-4 col-lg-3 col-xl-2 justify-content-center align-items-center m-2 px-4 mt-2"
      style={{
        borderRadius: "10px",
        display: "inline-block",
        backgroundColor: "rgba(0, 0, 0, 0.2)",
        maxWidth: "100%",
      }}
    >
      <div className="row">
        <div className="col">
          <div className="text-center ">
            <p>{task.title}</p>
            <p>{task.expirationDate}</p>
          </div>
        </div>
        <div
          className="col-1"
          style={{
            backgroundColor: task.priority === "HIGH" ? "red" : "green",
          }}
        ></div>
      </div>
      <div className="row">
        <div className="col-4">
          <img
            src={yesImg}
            alt=""
            style={{
              width: "30px",
            }}
          />
        </div>
        <div className="col-4">
          <img
            src={noImg}
            alt=""
            style={{
              width: "30px",
            }}
          />
        </div>
        <div className="col-4">
          <img
            src={deleteImg}
            style={{
              width: "30px",
            }}
            alt=""
          />
        </div>
      </div>
    </div>
  );
};
