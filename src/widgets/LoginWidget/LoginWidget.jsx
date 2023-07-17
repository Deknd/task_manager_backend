import React, { useState } from "react";
import { LoginForm } from "../../widgets";
import { ChangeSize, EffectButton} from "../../features";
import { Footer } from "../../entities";
import { LinkButton, TextDisplay } from "../../shared/ui";


export const LoginWidget = () => {
    const startFontSize = 20;
    const endFontSize = 12;
      

    return(
        <div >
            <ChangeSize widthParam={{ 
        startFontSize: startFontSize,
        endFontSize: endFontSize, 
  
      }} >
                <TextDisplay text={'Log in'} fontSize={3} />

                <LoginForm/>
                

                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    marginTop: '1em'
                }} >

                <TextDisplay text={'If you are not yet registered, we suggest you complete a simple registration'} fontSize={1} />
                
                <EffectButton>
                        <LinkButton to={''} description={'Registration'} />
                </EffectButton>
                </div>
              

               


            </ChangeSize>
        
            <div>
                <Footer/>
            </div>
       
        </div>
    )
}