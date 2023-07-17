import React, { useEffect, useRef, useState } from "react";

import { UsedButton } from "../";

import { ContainerForButton } from "../../entities";
import { Footer } from "../../entities";

export const ButtonPanelWidget = () => {


    


    return(
        <ContainerForButton>
            <UsedButton/>
            <Footer/>
            
        </ContainerForButton>
    );
}