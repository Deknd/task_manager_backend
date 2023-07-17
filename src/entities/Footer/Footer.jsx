import React from "react";
import { githab, linkedin } from '../../shared/lib/links'
import './footer.module.css';


export const Footer = () => {



    return(
        <div style={{
            position: 'absolute',
            paddingBottom: '1em',
            bottom: '0'
        }} >
            <ul className="footer-links">
                <li><a href={linkedin} target="_blank" rel="noopener noreferrer">LinkedIn</a></li>
                <li><a href={githab} target="_blank" rel="noopener noreferrer">GitHub</a></li>
            </ul>
        </div>
    )
}