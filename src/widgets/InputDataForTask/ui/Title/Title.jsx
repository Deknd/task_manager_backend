import React from "react";
import { FrameError } from "../../../../features";
import { validate } from "../../../../shared/lib/validation";
import { InputData } from "../../../../entities";

//Отображает заголовок для виджета InptDataForTask
export const Title = (props) => {

    const { 
        //данные заголовка
        title,
        //метод для изменения заголовка
        setTitle,
    } = props;

    return(
        //проводит валидацию и выводит ошибки (ок)
        <FrameError 
                dataCorrect={title.length !== 0 && title.length>25 ||  !validate.isValidSymbol(title)} 
                textError={validate.errorMessanger([
                    {errorText: 'title больше 25 символов',functionValidation: (title.length>25)},
                    {errorText: 'использованы запрещенные символы', functionValidation: !validate.isValidSymbol(title)}
                    ])} >  
                        {/* Инпут форма для приема данных от пользователя (ok) */}
                        <InputData 
                            dataPut={title} 
                            placeholder='Enter title' 
                            noBorder={true} 
                            width='100%' 
                            fontSize='1.1em' 
                            height='3em' 
                            getData={setTitle} />
        </FrameError>
    )
}