import React, { useState } from "react";


import { RegistrationForm } from '../../widgets'

import { ChangeSize, EffectButton, RegistrationFeatures } from "../../features";

import { LinkButton, TextDisplay } from "../../shared/ui";
import { useSelector } from "react-redux";



export const RegistrationWidget = () => {

    const startFontSize = 13;
    const endFontSize = 11;
    const isDataSentRegistration  = useSelector((state) => state.authAndRegistrSlice.isDataSentRegistration)


    const [ user, setUser ] = useState({
        name: '',
        username: '',
        password: '',
        confirmation: '',
    });
  
   

   
    
    return( 
        <ChangeSize widthParam={{ 
            startFontSize: startFontSize,
            endFontSize: endFontSize, 
      
          }}  >
            <div  style={{
                opacity: isDataSentRegistration ? '20%' : '100%',
                userSelect: isDataSentRegistration ? 'none' : '',

                translate: '0.2s',

            }} >

                <TextDisplay text={'Registration'} fontSize={4} />
            
                <div style={{ paddingTop: '2em' }} >

                    <form 
                    style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'    
                    }}
                    >
                    <RegistrationForm setUser={setUser} isCleared={isDataSentRegistration} />

                        <div style={{
                            paddingTop: '2em'
                        }} >
                        
                            <EffectButton>
                                <RegistrationFeatures user={user}>
                                    <LinkButton to={''} description={'Submit'} />
                                </RegistrationFeatures>
                            </EffectButton>
                        </div>
                    </form>
                </div>
            </div>



        </ChangeSize>
        
        
    )
}