import React, { useEffect, useState } from "react";
import { TextDisplay } from "../../shared/ui";
import { FrameError} from "../../features";
import { InputData } from "../../entities";
import { validate } from "../../shared/lib/validation";
import { useSelector } from "react-redux";



export const RegistrationForm = (props) => {

    const { 
        setUser,
        isCleared,
    } = props;



    const [username, setUsername] = useState("");
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [confirmation, setConfirmation ] = useState('')


    useEffect(()=>{
        setUser({name: name, username: username, password: password, confirmation: confirmation})
    },[ username, name, password, confirmation ])


    return ( 
      
        <div>

            <div style={{ paddingTop: '0em' }} >
                <TextDisplay text={'Enter your name'} fontSize={1.4} />
            </div>
            <FrameError dataCorrect={ name.length !== 0 && !validate.isValidSymbol(name) } textError='Используются запрещенные символы' >
                <InputData type={'text'} placeholder={'name'} getData={setName} clear={isCleared} />
            </FrameError>

            <div style={{ paddingTop: '0.5em' }} >
                <TextDisplay text={'Enter your email'} fontSize={1.4} />
            </div>
            <FrameError dataCorrect={username.length > 3 && !validate.validateEmail(username) } textError='Email введен не верно' >
                <InputData type={'email'} placeholder={'email'} getData={setUsername} clear={isCleared} />
            </FrameError>

            <div style={{paddingTop: '1.5em'}} >
                <div style={{ paddingTop: '0.5em' }} >
                    <TextDisplay text={'Password and confirmation password'} fontSize={1.4} />
                </div>

                <FrameError dataCorrect={password.length > 0 && !validate.validatePassword(password) } textError='не менее 8 символов , цифры, буквы в верхнем и нижнем регистрах.' >
                    <InputData type={'password'} placeholder={'password'} getData={setPassword} clear={isCleared} />
                </FrameError>

                <FrameError dataCorrect={password !== confirmation && confirmation.length > 0 } textError= 'пароли не совпадают' >
                    <InputData type={'password'} placeholder={'confirmation'} getData={setConfirmation} clear={isCleared} />
                </FrameError>
            </div>

          
        </div>
    )
}