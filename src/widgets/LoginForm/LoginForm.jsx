import React, { useState } from "react";
import { LinkButton, TextDisplay } from "../../shared/ui";
import { EffectButton, FrameError, LoginFeatures } from "../../features";
import { InputData } from "../../entities";
import { validate } from "../../shared/lib/validation";


export const LoginForm = () => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
  
    return(
       

        <form   
            style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center'    
            }}>
              <div style={{ paddingTop: '0.5em' }} >
                <TextDisplay text={'Enter your email'} fontSize={1.4} />
              </div>
              <FrameError dataCorrect={username.length > 3 && !validate.validateEmail(username) } textError='Email введен не верно' >
                <InputData type={'email'} placeholder={'email'} getData={setUsername}/>
              </FrameError>

              <div style={{ paddingTop: '0.5em' }} >
                <TextDisplay text={'Password'} fontSize={1.4} />
              </div>

              <FrameError dataCorrect={ false } textError='не менее 8 символов , цифры, буквы в верхнем и нижнем регистрах.' >
                    <InputData type={'password'} placeholder={'password'} getData={setPassword}/>
              </FrameError>
               
                <EffectButton>
                    

                        <TextDisplay text={'forgot your password? Click'} fontSize={1.1} />
                    
                </EffectButton>
                <div style={{
                    padding: '0.5rem'
                }} >
                    
                    <EffectButton>
                        <LoginFeatures user={{username: username, password: password}} >
                            <LinkButton to={''} description={'Submit'} />
                        </LoginFeatures>
                    </EffectButton>

                </div>
            </form>
       
    )
}

