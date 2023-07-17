import React, { useState } from "react";
import { LinkButton, TextDisplay, InputField } from "../../shared/ui";
import { EffectButton, actionAuthRegistrSlice} from "../../features";
import { useDispatch } from "react-redux";


export const LoginForm = () => {

    const dispatch = useDispatch();
    const [username, setUsername] = useState("johndoe@mail.com");
    const [password, setPassword] = useState("12345");
  
    const handleUsernameChange = (event) => {
      setUsername(event.target.value);
    };
  
    const handlePasswordChange = (event) => {
      setPassword(event.target.value);
    };
  
    const handleSubmit = (event) => {
      event.preventDefault();
      // Здесь можно выполнить дополнительные действия, связанные с отправкой формы
    };
    

    const click = (e) => { 
        e.preventDefault();
        const user = {username: username, password: password}

        const isNotEmpty = Object.values(user).every((val) => val);

        if (!isNotEmpty) return;
        console.log('Click Login Form: ',user)

        dispatch(
          actionAuthRegistrSlice.loginUser(user)
        );
    }

    return(
       

        <form 
            style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center'    
            }}
            onSubmit={handleSubmit}>
                <InputField type={'email'} placeholder={'email'} value={username} onChange={handleUsernameChange} />
                <InputField type={'password'} placeholder={'password'} value={password} onChange={handlePasswordChange} />
               
                <EffectButton>
                    

                        <TextDisplay text={'forgot your password? Click'} fontSize={1.1} />
                    
                </EffectButton>
                <div style={{
                    padding: '0.5rem'
                }} >
                    
                    <EffectButton>
                        <div onClick={click} >
                            <LinkButton to={''} description={'Submit'} />
                        </div>
                    </EffectButton>

                </div>
            </form>
       
    )
}

