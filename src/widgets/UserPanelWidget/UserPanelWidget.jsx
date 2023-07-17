import React from "react";
import { useSelector } from "react-redux";


import logout from './img/logout.png'
import settings from './img/settings.png'


import { EffectButton } from '../../features'
import { LogOutUser } from "../../features";
import { UpdateUser } from "../../features";

import { SceletonUserPanel } from "../../entities";

import { MediumButton } from "../../shared/ui";


export const UserPanelWidget = (props) => {

    const user = useSelector((state)=> state.user)

    return(
        <SceletonUserPanel userName={user.name} >
            <EffectButton>
                <UpdateUser>
                    <MediumButton description='Настройка' img={settings} />
                </UpdateUser>
            </EffectButton>    
            <EffectButton>
                <LogOutUser>
                    <MediumButton description='Выход' img={logout} />
                </LogOutUser>
            </EffectButton>    
        </SceletonUserPanel>
        
    )
}