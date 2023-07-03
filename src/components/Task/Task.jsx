
import { getAccessToken } from "../../features/user/tokens";
import { useDispatch, useSelector } from "react-redux";
import { deleteTask } from "../../features/task/taskSlice";
import taskPriorityStandardImg from "../../images/taskPriorityStandardImg.jpg";
import pozitive from "../../images/positive-vote.png";
import negative from "../../images/negative-vote.png";
import edit from "../../images/edit.png";
import garbage from "../../images/garbage.png";
import { IconButton } from "../../shared/ui";

{/*Компонент для отображение Task. Отображает title и expirationDate, плюс кнопки взаимодействие с Task. Принимает task.*/}
export const Task = ({ task }) => {
  const isLogin = useSelector((state) => state.user.isLogin);
  const dispatch = useDispatch();
  const deleteChange = () => {
    let accessToken = getAccessToken(isLogin);
    if (accessToken !== null) {
      dispatch(deleteTask({ id: task.id, accessToken: accessToken }));
    }
  };

  return (
    <div
      className="justify-content-center align-items-center"
      style={{
        borderRadius: "10px",
        display: "inline-block",
        backgroundImage: `url(${taskPriorityStandardImg})`,
        width: "15rem",
      }}
    >
      {/* Отображает title */}
      <div
      className="text-center justify-content-center"
      style={{
        borderRadius: "0.3rem",
        marginTop: "1rem",
        marginRight: "0.6rem",
        marginLeft: "0.6rem",
        backgroundColor: "#FFFFFF73",
        height: "3.3rem",
        display: "flex",
        alignItems: "center",
        
      }}>
            <p style={{fontSize: "1.3rem"}}>
              {task.title}
              </p>

      </div>

      {/* Отображает expirationDate */}
      <div className="text-center justify-content-center"
      style={{
        borderRadius: "0.3rem",
        marginTop: "0.8rem",
        marginRight: "0.6rem",
        marginLeft: "0.6rem",
        backgroundColor: "#FFFFFF73",
        height: "2rem",
        display: "flex",
        alignItems: "center",
        
      }}>
                    <p style={{fontSize: "1rem"}}>{task.expirationDate}</p>

      </div>
        {/* Отображает функциональные кнопки, для взаимодействия с Task  //*/}
       <div style={{
        marginTop: "0.8rem",
        marginRight: "0.6rem",
        marginLeft: "0.6rem",
        marginBottom: "1rem",
       }}>
        {/*    */}
        <div className="row">
          <div className="col">
           <IconButton img={pozitive} textIcon={"pozitive vote"} onClick={()=>console.log("pozitive")}/> 
          </div>
          <div className="col">
          <IconButton img={negative} textIcon={"negative vote"} onClick={()=>console.log("negative")}/>
          </div>
          <div className="col">
          <IconButton img={edit} textIcon={"edit"} onClick={()=>console.log("edit")}/>
          </div>
          <div className="col">
          <IconButton img={garbage} textIcon={"garbage"} onClick={()=>console.log("garbage")}/>
          </div>

        </div>
       </div>
    </div>
  );
};
