import React, { useEffect, useRef, useState } from "react";

import { UsedButton } from "../";

import { ContainerForButton } from "../../entities";
import { FooterButton } from "../../entities/FooterButton";

export const ButtonPanelWidget = () => {


    


    return(
        <ContainerForButton>
            <UsedButton/>
            <FooterButton/>
            
        </ContainerForButton>
    );
}