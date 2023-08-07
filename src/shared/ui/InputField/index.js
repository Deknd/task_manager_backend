import React from "react";

//поле для ввода данных
export const InputField = (props) => {


    const {
        //тип вводимых данных
        type,
        //информация которая должна отображаться без данных
        placeholder,
        //данные
        value,
        //метод для отправки данных
        onChange,
        //ширина
        width,
        //высота
        height,
        //размер шрифта
        fontSize,
        //индикатор нужности границ
        noBorder,
    } = props;




    return(
        <div style={{
           opacity: '60%'
        }} >
            <input
            style={{
                fontSize: fontSize? fontSize : '1.7em',
                width: width ? width : '17em',
                height: height ? height : '', 
                borderRadius: '0.3em',
                border: noBorder ? 'none' : '',
                padding: '0.1em',
                textAlign: 'center',
                whiteSpace: 'pre-wrap',




            }}
            type={type}
            placeholder={placeholder}
            autoComplete="on"
            required

            value={value}
            onChange={onChange}
            />
        </div>
    )
}