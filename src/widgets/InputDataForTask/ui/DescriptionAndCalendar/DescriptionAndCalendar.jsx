import React, { useState } from "react";
import { EffectButton, FrameError, SelectElement } from "../../../../features";
import { Calendar, InputDescription } from "../../../../entities";
import { ContentField } from "../../../../shared/ui";
import { validate } from "../../../../shared/lib/validation";
import { format } from "date-fns";

//Отображает описание или календарь
export const DescriptionAndCalendar = (props) => {
    const {

        description,
        setDescription,
        expirationDate,
        setExpirationDate

    } = props;


     //состояние календаря, открыт или нет
     const [isOpen, setIsOpen] = useState(0);
   
     //выберает какую панель рисовать, здесь это календарь или описание
         const handleClick = (e) => {
             e.preventDefault();
             if(isOpen === 1){
                 setIsOpen(0);
             } else setIsOpen(1)
           }; 

    return(
        <div>
            {/* Индикатор валидации данных (ок) */}
            <FrameError 
                dataCorrect={description.length !== 0 && !validate.isValidSymbol(description)} 
                textError={validate.errorMessanger([
                    {errorText: 'использованы запрещенные символы', functionValidation: !validate.isValidSymbol(description)}
                    ])} >
                        {/* выводит один комплект кнопок из переданых ему (ок) */}
                        <SelectElement elements={[ (
                            //ввод оисания (ок)
                            <InputDescription dataPut={description} getDiscription={setDescription} />
                        ), (
                            //календарь отдает данные которые выбрал пользователь (ок)
                            <Calendar getDate={setExpirationDate} startDate={expirationDate}  />
                        ), ]} openElement={isOpen} />
            </FrameError>

            {/* создает эффект нажатия (ок)  */}
            <EffectButton>
            {/* Кнопка отоображает дату и переключает между календарем и description */}
                <div 
                    style={{ 
                        paddingTop: '0.25em',
                        }}
                    onClick={handleClick}>
                         {/* компонент для вывода текстовой информации (ок) */}
                        <ContentField text={format(expirationDate, "dd-MM-yyyy HH:mm")} isVisible={true} height={2} noMargin={true} />   

                </div>
            </EffectButton>
        </div>
    )
}