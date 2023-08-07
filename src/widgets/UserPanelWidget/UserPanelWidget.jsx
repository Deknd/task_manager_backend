import React from "react";
import { useSelector } from "react-redux";


import logout from './img/logout.png'
import settings from './img/settings.png'


import { EffectButton, LogOutUser, UpdateUser } from '../../features'


import { SceletonUserPanel } from "../../entities";

import { MediumButton } from "../../shared/ui";

//Виджет для работы с данными пользователя
export const UserPanelWidget = (props) => {

    //слежение за данными в сторе
    const user = useSelector((state)=> state.user)

    return(
        // скелет для панели пользователя (ок)
        <SceletonUserPanel userName={user.name} >
            {/* Эффект кнопки (ок) */}
            <EffectButton>
                {/*   */}
                <UpdateUser>
                    {/* отображает текст и изображение (ок) */}
                    <MediumButton description='Настройка' img={settings} />
                </UpdateUser>
            </EffectButton>  

            {/* Эффект кнопки (ок) */}
            <EffectButton>
                {/* функция для логаута пользователя (ок) */}
                <LogOutUser>

                    {/* отображает текст и изображение (ок) */}
                    <MediumButton description='Выход' img={logout} />
                </LogOutUser>
            </EffectButton>    

        </SceletonUserPanel>
        
    )
}