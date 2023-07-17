import React from "react";
import { EffectButton } from "../../features";
import { ChangeSize } from "../../features";
import { Footer } from "../../entities";
import { SceletonStartWidget } from "../../entities";
import { LinkButton } from "../../shared/ui";
import { ROUTES } from "../../shared/lib/constants/routes";



export const StartWidget = () => {
         
    
   
    const startFontSize = 32;
    const endFontSize = 14;


   
    return(
        <div >

            <ChangeSize widthParam={{ 
        startFontSize: startFontSize,
        endFontSize: endFontSize,
  
      }} >
                   
                        <SceletonStartWidget text1={'Help make your life easier'} text2={'My Task Manager '} text3={'Change your life today'} >
                        <div style={{
                            display: 'flex',
                            alignContent: 'center',
                            justifyContent: 'center',
                            alignItems: 'center',
                            margin: '3em'
                            }} >

                            <EffectButton>
                                <LinkButton to={ROUTES.LOGIN} description={'Get Start'} />
                            </EffectButton>
                        </div>
                        </SceletonStartWidget>

            </ChangeSize>
            
            <div>
                <Footer/>
            </div>
           
         </div>
    )
}