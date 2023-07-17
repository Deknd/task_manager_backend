import React from "react";
import { UserPanelWidget, ButtonPanelWidget, ListTaskWidget } from '../../widgets'



export const TasksDashboard = () => {



    return (
        <div>
        <UserPanelWidget/>
        <div style={{ display: 'flex',}}>
          <div >
             <ButtonPanelWidget/>
          </div>
          <div style={{ flex: 1}}>
             <ListTaskWidget/>
          </div>
        </div>

        </div>
    )
}