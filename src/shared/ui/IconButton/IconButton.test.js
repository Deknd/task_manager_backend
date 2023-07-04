import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { IconButton } from './IconButton';
import iconTest from './test.png';
import { unmountComponentAtNode } from "react-dom";
import '@testing-library/jest-dom/extend-expect';
import {
	scale_onClick_Active,
	scale_onHover_Active,
	scale_AtRest,
	margin_OnClick,
	margin_atRest,
	emptyText,
 } from './IconButtonConstants';





describe('IconButton', () => {
    let container = null;
beforeEach(() => {
  container = document.createElement("div");
  document.body.appendChild(container);
});

  it('Проверка рендера кнопки и правильность передачи пропсов', () => {
    const click = jest.fn();
    const imgSrc = iconTest;
    const altText = 'Icon';

    render(<IconButton img={iconTest} onClickSent={click} textIcon={altText} />, container);

    const img = screen.getByAltText(altText);
    const button = screen.getByRole('button');

    fireEvent.click(button);

    expect(click).toHaveBeenCalledTimes(1);
    expect(img.src).toContain(imgSrc); 
    expect(img.alt).toBe(altText); 
  });

  it('Если не будет изображения, то на экран выдаст текст(textIcon) с описанием данного изображения', () => {

    const click = jest.fn();
    const altText = 'Icon';

    render(<IconButton onClickSent={click} textIcon={altText} />, container);

    const text = screen.getByText(altText);
    const button = screen.getByRole('button');

    fireEvent.click(button);

    expect(click).toHaveBeenCalledTimes(1);
    expect(text).toBeInTheDocument();
  });

  it('Если не будет изображения и не будет текста описания, то выдаст сообщение "Empty text"', () => {

    const click = jest.fn();

    render(<IconButton onClickSent={click}  />, container);

    const text = screen.getByText(emptyText);
    const button = screen.getByRole('button');

    fireEvent.click(button);

    expect(click).toHaveBeenCalledTimes(1);
    expect(text).toBeInTheDocument();
  });

  it('Если не будет отправленна функция, то напишет сообщение в консоль: "Нет функции для обработки"', () => {
    const imgSrc = iconTest;
    const altText = 'Icon';

    
    render(<IconButton img={iconTest}  textIcon={altText} />, container);
   
    const consoleSpy = jest.spyOn(console, 'log');
    const img = screen.getByAltText(altText);
    const button = screen.getByRole('button');

    fireEvent.click(button);


    expect(img.src).toContain(imgSrc); 
    expect(img.alt).toBe(altText); 
    expect(consoleSpy).toHaveBeenCalledTimes(1);
    consoleSpy.mockRestore();
  });

  it('Проверка анимации кнопки, при наведении на нее увеличивается, при нажатии, она уменьшается и смещается вниз, отуская нажатие, она возвращается в обычное состояние', () => {
    const click = jest.fn();
    const imgSrc = iconTest;
    const altText = 'Icon';

    render(<IconButton img={iconTest} onClickSent={click} textIcon={altText} />, container);

    const img = screen.getByAltText(altText);
    const button = screen.getByRole('button');

    fireEvent.mouseEnter(button);
    
    expect(img.style.transform).toContain(`scale(${scale_onHover_Active})`);
    expect(img.style.paddingTop).toContain(`${margin_atRest}px`);


    fireEvent.mouseDown(button);

    expect(img.style.transform).toContain(`scale(${scale_onClick_Active})`);
    expect(img.style.paddingTop).toContain(`${margin_OnClick}px`);

    fireEvent.mouseUp(button);

    expect(img.style.transform).toContain(`scale(${scale_onHover_Active})`);
    expect(img.style.paddingTop).toContain(`${margin_atRest}px`);

    fireEvent.mouseLeave(button);

    expect(img.style.transform).toContain(`scale(${scale_AtRest})`);
    expect(img.style.paddingTop).toContain(`${margin_atRest}px`);

    fireEvent.click(button);

   expect(click).toHaveBeenCalledTimes(1);
    expect(img.src).toContain(imgSrc); 
    expect(img.alt).toBe(altText); 
  });

  it('Проверка анимации кнопки(нет изображения), при наведении на нее увеличивается, при нажатии, она уменьшается и смещается вниз, отуская нажатие, она возвращается в обычное состояние', () => {
    const click = jest.fn();
    const altText = 'Icon';

    render(<IconButton onClickSent={click} textIcon={altText} />, container);

    const text = screen.getByText(altText);
    const button = screen.getByRole('button');

    fireEvent.mouseEnter(button);
    
    expect(text.style.transform).toContain(`scale(${scale_onHover_Active})`);
    expect(text.style.paddingTop).toContain(`${margin_atRest}px`);


    fireEvent.mouseDown(button);

    expect(text.style.transform).toContain(`scale(${scale_onClick_Active})`);
    expect(text.style.paddingTop).toContain(`${margin_OnClick}px`);

    fireEvent.mouseUp(button);

    expect(text.style.transform).toContain(`scale(${scale_onHover_Active})`);
    expect(text.style.paddingTop).toContain(`${margin_atRest}px`);

    fireEvent.mouseLeave(button);

    expect(text.style.transform).toContain(`scale(${scale_AtRest})`);
    expect(text.style.paddingTop).toContain(`${margin_atRest}px`);

    fireEvent.click(button);

   expect(click).toHaveBeenCalledTimes(1);
    expect(text).toBeInTheDocument();
    
  });
  it('Проверка анимации кнопки(нет изображения и нет текста описания), при наведении на нее увеличивается, при нажатии, она уменьшается и смещается вниз, отуская нажатие, она возвращается в обычное состояние', () => {
    const click = jest.fn();

    render(<IconButton onClickSent={click} />, container);

    const text = screen.getByText(emptyText);
    const button = screen.getByRole('button');

    fireEvent.mouseEnter(button);
    
    expect(text.style.transform).toContain(`scale(${scale_onHover_Active})`);
    expect(text.style.paddingTop).toContain(`${margin_atRest}px`);


    fireEvent.mouseDown(button);

    expect(text.style.transform).toContain(`scale(${scale_onClick_Active})`);
    expect(text.style.paddingTop).toContain(`${margin_OnClick}px`);

    fireEvent.mouseUp(button);

    expect(text.style.transform).toContain(`scale(${scale_onHover_Active})`);
    expect(text.style.paddingTop).toContain(`${margin_atRest}px`);

    fireEvent.mouseLeave(button);

    expect(text.style.transform).toContain(`scale(${scale_AtRest})`);
    expect(text.style.paddingTop).toContain(`${margin_atRest}px`);

    fireEvent.click(button);

   expect(click).toHaveBeenCalledTimes(1);
    expect(text).toBeInTheDocument();
    
  });

  it('Проверка анимации кнопки(нет функции для вызова), при наведении на нее увеличивается, при нажатии, она уменьшается и смещается вниз, отуская нажатие, она возвращается в обычное состояние', () => {
    const imgSrc = iconTest;
    const altText = 'Icon';
    const consoleSpy = jest.spyOn(console, 'log');


    render(<IconButton img={iconTest} textIcon={altText} />, container);

    const img = screen.getByAltText(altText);
    const button = screen.getByRole('button');

    fireEvent.mouseEnter(button);
    
    expect(img.style.transform).toContain(`scale(${scale_onHover_Active})`);
    expect(img.style.paddingTop).toContain(`${margin_atRest}px`);


    fireEvent.mouseDown(button);

    expect(img.style.transform).toContain(`scale(${scale_onClick_Active})`);
    expect(img.style.paddingTop).toContain(`${margin_OnClick}px`);

    fireEvent.mouseUp(button);

    expect(img.style.transform).toContain(`scale(${scale_onHover_Active})`);
    expect(img.style.paddingTop).toContain(`${margin_atRest}px`);

    fireEvent.mouseLeave(button);

    expect(img.style.transform).toContain(`scale(${scale_AtRest})`);
    expect(img.style.paddingTop).toContain(`${margin_atRest}px`);

    fireEvent.click(button);

    expect(consoleSpy).toHaveBeenCalledTimes(1);
    consoleSpy.mockRestore();
    expect(img.src).toContain(imgSrc); 
    expect(img.alt).toBe(altText); 
  });

  it('IconButton snapshot', () => {

    const click = jest.fn();
    const imgSrc = iconTest;
    const altText = 'Icon';

    const button = render(<IconButton img={iconTest} onClickSent={click} textIcon={altText} />, container);

    expect(button).toMatchSnapshot();

  });

  it('IconButton snapshot(без изображения)', () => {

    const click = jest.fn();
    const altText = 'Icon';

    const button = render(<IconButton  onClickSent={click} textIcon={altText} />, container);

    expect(button).toMatchSnapshot();

  });

  it('IconButton snapshot(без изображения, без текста описания)', () => {

    const click = jest.fn();

    const button = render(<IconButton  onClickSent={click} />, container);

    expect(button).toMatchSnapshot();

  });

  it('при наведение на кнопку мыши IconButton snapshot', () => {

    const click = jest.fn();
    const imgSrc = iconTest;
    const altText = 'Icon';

    const iconButton = render(<IconButton img={iconTest} onClickSent={click} textIcon={altText} />, container);

    const button = screen.getByRole('button');

    fireEvent.mouseEnter(button);
    expect(iconButton).toMatchSnapshot();

  });

  it('при наведение на кнопку мыши IconButton snapshot(без изображения)', () => {

    const click = jest.fn();
    const altText = 'Icon';

    const iconButton = render(<IconButton  onClickSent={click} textIcon={altText} />, container);
    const button = screen.getByRole('button');

    fireEvent.mouseEnter(button);
    expect(iconButton).toMatchSnapshot();

  });

  it('при наведение на кнопку мыши IconButton snapshot(без изображения, без текста описания)', () => {

    const click = jest.fn();

    const iconButton = render(<IconButton  onClickSent={click} />, container);
    const button = screen.getByRole('button');

    fireEvent.mouseEnter(button);
    expect(iconButton).toMatchSnapshot();

  });

  it('при нанажатии на кнопку мышью IconButton snapshot', () => {

    const click = jest.fn();
    const imgSrc = iconTest;
    const altText = 'Icon';

    const iconButton = render(<IconButton img={iconTest} onClickSent={click} textIcon={altText} />, container);

    const button = screen.getByRole('button');

    fireEvent.mouseDown(button);
    expect(iconButton).toMatchSnapshot();

  });

  it('при нанажатии на кнопку мышью IconButton snapshot(без изображения)', () => {

    const click = jest.fn();
    const altText = 'Icon';

    const iconButton = render(<IconButton  onClickSent={click} textIcon={altText} />, container);
    const button = screen.getByRole('button');

    fireEvent.mouseDown(button);
    expect(iconButton).toMatchSnapshot();

  });

  it('при нанажатии на кнопку мышью IconButton snapshot(без изображения, без текста описания)', () => {

    const click = jest.fn();

    const iconButton = render(<IconButton  onClickSent={click} />, container);
    const button = screen.getByRole('button');

    fireEvent.mouseDown(button);
    expect(iconButton).toMatchSnapshot();

  });

  
  
 

afterEach(() => {
    unmountComponentAtNode(container);
    container.remove();
    container = null;
  });
});
