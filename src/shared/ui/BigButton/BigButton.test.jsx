import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { unmountComponentAtNode } from "react-dom";
import '@testing-library/jest-dom/extend-expect';

import iconTest from './test.png';

import { BigButton } from './BigButton';
import {
	scale_onClick_Active,
	scale_onHover_Active,
	scale_AtRest,
	transformation_Time,
	margin_OnClick,
	margin_atRest,
    widthShadow,
    opacityShadow,
    colorShdowRBG,
    textWhenNoFunction,
    noColorButtonRBG,
    noDescription,
 } from './bigButtonConstants';
import { MemoryRouter } from 'react-router-dom';


 describe('BigButton', () => {
    let container = null;
    beforeEach(() => {
        container = document.createElement("div");
        document.body.appendChild(container);
    });
    afterEach(() => {
        unmountComponentAtNode(container);
        container.remove();
        container = null;
      });
     
    it('Проверка, как рисуется кнопка со всеми данными', () => {
        const testRoute = '/test/route';
        const testDescription = 'test description';
        const click = jest.fn();
        const testColor = '100, 50, 30, 0.65';


        render(
        (
        <MemoryRouter>
            <BigButton toRoute={testRoute} description={testDescription} img={iconTest} onClickSent={click} colorButtonRBG={testColor} />
        </MemoryRouter>
        ), container )

        const button = screen.getByRole('button');
        const link = button.closest('a');
        const image = screen.getByAltText(testDescription);
        const description = screen.getByText(testDescription);

        fireEvent.click(link);



        expect(click).toHaveBeenCalledTimes(1);

        expect(link.getAttribute('href')).toBe(`${testRoute}`);
        expect(link.getAttribute('style')).toContain(`background-color: rgba(${testColor})`);
        expect(image.getAttribute('src')).toBe('test.png');
        expect(description.textContent).toBe(testDescription);

    })
    it('Проверка, как рисуется кнопка без функции', () => {
        const testRoute = '/test/route';
        const testDescription = 'test description';
        const click = jest.fn();
        const testColor = '100, 50, 30, 0.65';


        render(
        (
        <MemoryRouter>
            <BigButton toRoute={testRoute} description={testDescription} img={iconTest} colorButtonRBG={testColor} />
        </MemoryRouter>
        ), container )

        const button = screen.getByRole('button');
        const link = button.closest('a');
        const image = screen.getByAltText(testDescription);
        const description = screen.getByText(testDescription);

        fireEvent.click(link);



        expect(click).toHaveBeenCalledTimes(0);

        expect(link.getAttribute('href')).toBe(`${testRoute}`);
        expect(link.getAttribute('style')).toContain(`background-color: rgba(${testColor})`);
        expect(image.getAttribute('src')).toBe('test.png');
        expect(description.textContent).toBe(testDescription);
    })

    it('Проверка, как рисуется кнопка без иконки', () => {
        const testRoute = '/test/route';
        const click = jest.fn();
        const testColor = '100, 50, 30, 0.65';
        const testDescription = 'test description';



        render(
        (
        <MemoryRouter>
            <BigButton toRoute={testRoute} description={testDescription} colorButtonRBG={testColor} />
        </MemoryRouter>
        ), container )

        const button = screen.getByRole('button');
        const link = button.closest('a');
        const image = screen.queryByAltText(testDescription);
        const description = screen.getByText(testDescription);

        fireEvent.click(link);



        expect(click).toHaveBeenCalledTimes(0);

        expect(link.getAttribute('href')).toBe(`${testRoute}`);
        expect(link.getAttribute('style')).toContain(`background-color: rgba(${testColor})`);
        expect(image).toBeNull();
        expect(description.textContent).toBe(testDescription);
    })

    it('Проверка, как рисуется кнопка без оисания', () => {
        const testRoute = '/test/route';
        const click = jest.fn();
        const testColor = '100, 50, 30, 0.65';
        const testDescription = 'test description';


        render(
        (
        <MemoryRouter>
            <BigButton toRoute={testRoute} colorButtonRBG={testColor} />
        </MemoryRouter>
        ), container )

        const button = screen.getByRole('button');
        const link = button.closest('a');
        const image = screen.queryByAltText(testDescription);
        const description = screen.queryByText(testDescription);
        const noDescriptionTest = screen.queryByText(noDescription);

        fireEvent.click(link);



        expect(click).toHaveBeenCalledTimes(0);

        expect(link.getAttribute('href')).toBe(`${testRoute}`);
        expect(link.getAttribute('style')).toContain(`background-color: rgba(${testColor})`);
        expect(noDescriptionTest).not.toBeNull();
        expect(noDescriptionTest.textContent).toBe(noDescription);

        expect(image).toBeNull();
        expect(description).toBeNull();
    })
    it('Проверка, как рисуется кнопка без оисания', () => {
        const testRoute = '/test/route';
        const click = jest.fn();
        const testDescription = 'test description';


        render(
        (
        <MemoryRouter>
            <BigButton toRoute={testRoute} />
        </MemoryRouter>
        ), container )

        const button = screen.getByRole('button');
        const link = button.closest('a');
        const image = screen.queryByAltText(testDescription);
        const description = screen.queryByText(testDescription);
        const noDescriptionTest = screen.queryByText(noDescription);

        fireEvent.click(link);



        expect(click).toHaveBeenCalledTimes(0);

        expect(link.getAttribute('href')).toBe(`${testRoute}`);
        expect(link.getAttribute('style')).toContain(`background-color: rgba(${noColorButtonRBG})`);
        expect(noDescriptionTest).not.toBeNull();
        expect(noDescriptionTest.textContent).toBe(noDescription);

        expect(image).toBeNull();
        expect(description).toBeNull();
    })

    it('Проверка, как рисуется анимация кнопки', () => {
        const testRoute = '/test/route';
        const testDescription = 'test description';
        const click = jest.fn();
        const testColor = '100, 50, 30, 0.65';


        render(
        (
        <MemoryRouter>
            <BigButton toRoute={testRoute} description={testDescription} img={iconTest} onClickSent={click} colorButtonRBG={testColor} />
        </MemoryRouter>
        ), container )

        const button = screen.getByRole('button');
        const link = button.closest('a');
        const image = screen.getByAltText(testDescription);
        const description = screen.getByText(testDescription);


        expect(link.style.transform).toContain(`scale(${scale_AtRest})`);
        expect(link.style.paddingTop).toContain(`${margin_atRest}px`);


        fireEvent.mouseEnter(link);

        expect(link.style.transform).toContain(`scale(${scale_onHover_Active})`);
        expect(link.style.paddingTop).toContain(`${margin_atRest}px`);

        fireEvent.mouseDown(link);

        expect(link.style.transform).toContain(`scale(${scale_onClick_Active})`);
        expect(link.style.paddingTop).toContain(`${margin_OnClick}px`);

        fireEvent.mouseUp(link);

        expect(link.style.transform).toContain(`scale(${scale_onHover_Active})`);
        expect(link.style.paddingTop).toContain(`${margin_atRest}px`);

        fireEvent.mouseLeave(link);

        expect(link.style.transform).toContain(`scale(${scale_AtRest})`);
        expect(link.style.paddingTop).toContain(`${margin_atRest}px`);

        fireEvent.click(link);



        expect(click).toHaveBeenCalledTimes(1);

        expect(link.getAttribute('href')).toBe(`${testRoute}`);
        expect(link.getAttribute('style')).toContain(`background-color: rgba(${testColor})`);
        expect(image.getAttribute('src')).toBe('test.png');
        expect(description.textContent).toBe(testDescription);

    })

    it('BigButton snapshot', () => {

        const testRoute = '/test/route';
        const testDescription = 'test description';
        const click = jest.fn();
        const testColor = '100, 50, 30, 0.65';
    
        const button =  render(
            (
            <MemoryRouter>
                <BigButton toRoute={testRoute} description={testDescription} img={iconTest} onClickSent={click} colorButtonRBG={testColor} />
            </MemoryRouter>
            ), container )
    
        expect(button).toMatchSnapshot();
    
      });

      it('BigButton snapshot мышка наведена', () => {

        const testRoute = '/test/route';
        const testDescription = 'test description';
        const click = jest.fn();
        const testColor = '100, 50, 30, 0.65';
    
        const button =  render(
            (
            <MemoryRouter>
                <BigButton toRoute={testRoute} description={testDescription} img={iconTest} onClickSent={click} colorButtonRBG={testColor} />
            </MemoryRouter>
            ), container );

        const buttonTest = screen.getByRole('button');
        fireEvent.mouseEnter(buttonTest);

    
        expect(button).toMatchSnapshot();
    
      });

      it('BigButton snapshot кнопка нажата', () => {

        const testRoute = '/test/route';
        const testDescription = 'test description';
        const click = jest.fn();
        const testColor = '100, 50, 30, 0.65';
    
        const button =  render(
            (
            <MemoryRouter>
                <BigButton toRoute={testRoute} description={testDescription} img={iconTest} onClickSent={click} colorButtonRBG={testColor} />
            </MemoryRouter>
            ), container );

        const buttonTest = screen.getByRole('button');
        fireEvent.mouseDown(buttonTest);

    
        expect(button).toMatchSnapshot();
    
      });
 })