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
            padding: '0.5em',
        }} >
            <input
            style={{
                fontSize: '1.3em',
                width: '15em',
                borderRadius: '15px',
                padding: '0.2em',
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