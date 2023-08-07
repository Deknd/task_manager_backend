import React from "react";
import { UserPanelWidget, ButtonPanelWidget, ListTaskWidget } from '../../widgets'


//главная страница для работы с тасками
export const TasksDashboard = () => {



    return (
        <div>
          {/* Виджет для работы с данными пользователя (ok) */}
          <UserPanelWidget/>
          <div style={{ display: 'flex',}}>
            <div >
              {/* панель управление тасками */}
              <ButtonPanelWidget/>
            </div>
            <div style={{ flex: 1}}>
              <ListTaskWidget/>
            </div>
          </div>

        </div>
    )
}