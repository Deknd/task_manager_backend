import React from "react";


export const InputField = (props) => {

    const {
        type,
        placeholder,
        value,
        onChange,
        width,
        height,
        fontSize,
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