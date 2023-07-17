import React from "react";
import { Link } from "react-router-dom";


export const LinkButton = (props) => {
    const {
        to,
        description
    } = props;



    return(
        <Link
            to={to}
            style={{
                textDecoration: 'none'
            }}
            >
                <div style={{
                    width: '8em',
                    height: '1.6em',
                    backgroundColor: '#A8DADCA6',
                    display: 'flex',
                    alignContent: 'center',
                    justifyContent: 'center',
                    alignItems: 'center',
                    borderRadius: '10px',
                    color: 'black',
                    fontSize: '1.7rem',
                    }}>
                     {description}
                </div>
        </Link>
    )
}