import React, { useEffect, useState } from "react";
import { ChangePriorityTask, EffectButton, FrameError, SelectElement } from "../../features";
import { Calendar, InputData, InputDescription } from "../../entities";
import { validate } from "../../shared/lib/validation";
import { ContentField } from "../../shared/ui";
import { format } from "date-fns";


export const InputDataForTask = (props) => {

    const { 
        cameTitle,
        cameDescription,
        cameExpirationDate,
        getData,
     } = props;


    const [ title, setTitle ] = useState(cameTitle ? cameTitle : '');
    const [ description, setDescription ] = useState(cameDescription ? cameDescription : '');
    const [ expirationDate, setExpirationDate ] = useState(cameExpirationDate ? new Date(cameExpirationDate) : new Date());
    const [ priority, setPriority ] = useState('STANDARD');



    useEffect(()=>{
        console.log(title, description, expirationDate)
        getData({ title: title, description: description, expirationDate: format(expirationDate, "yyyy-MM-dd HH:mm"), priority: priority })

    },[ title, description, expirationDate, priority ])

    const [isOpen, setIsOpen] = useState(0);
   

    const handleClick = (e) => {
        e.preventDefault();
        if(isOpen === 1){
            setIsOpen(0);
        } else setIsOpen(1)
      }; 



    return(
        <div>

            <FrameError 
                dataCorrect={title.length !== 0 && title.length>25 ||  !validate.isValidSymbol(title)} 
                textError={validate.errorMessanger([
                    {errorText: 'title больше 25 символов',functionValidation: (title.length>25)},
                    {errorText: 'использованы запрещенные символы', functionValidation: !validate.isValidSymbol(title)}
                    ])} >  

                        <InputData dataPut={title} placeholder='Enter title' noBorder={true} width='100%' fontSize='1.1em' height='3em' getData={setTitle} />
            </FrameError>

            <FrameError 
                dataCorrect={description.length !== 0 && !validate.isValidSymbol(description)} 
                textError={validate.errorMessanger([
                    {errorText: 'использованы запрещенные символы', functionValidation: !validate.isValidSymbol(description)}
                    ])} >
                        <SelectElement elements={[ (
                            <InputDescription dataPut={description} getDiscription={setDescription} />
                        ), (
                            <Calendar getDate={setExpirationDate} startDate={expirationDate}  />
                        ), ]} openElement={isOpen} />
            </FrameError>
            <div 
                    style={{ 
                        paddingTop: '0.25em',
                        }}
                    onClick={handleClick}>
                    <EffectButton>
                        <ContentField text={format(expirationDate, "dd-MM-yyyy HH:mm")} isVisible={true} height={2} noMargin={true} />   
                    </EffectButton>

                </div>
                <EffectButton>
                    <ChangePriorityTask priority={priority} change={setPriority} >
                        <ContentField text={'change priority'} isVisible={true} height={2} />
                    </ChangePriorityTask>
                </EffectButton>
        </div>
    )
}