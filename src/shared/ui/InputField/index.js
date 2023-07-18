import React from "react";


export const InputField = (props) => {

    const {
        type,
        placeholder,
        value,
        onChange,
    } = props;




    return(
        <div style={{
           opacity: '60%'
        }} >
            <input
            style={{
                fontSize: '1.7em',
                width: '17em',
                borderRadius: '0.3em',
                padding: '0.1em',
                textAlign: 'center'

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