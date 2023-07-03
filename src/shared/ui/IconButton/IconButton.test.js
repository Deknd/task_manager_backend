import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { IconButton } from './IconButton';
import iconTest from './test.png';
import { unmountComponentAtNode } from "react-dom";




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

    render(<IconButton img={iconTest} onClick={click} textIcon={altText} />, container);

    const img = screen.getByAltText(altText);
    const button = screen.getByRole('button');

    fireEvent.click(button);

    expect(click).toHaveBeenCalledTimes(1);
    expect(img.src).toContain(imgSrc); 
    expect(img.alt).toBe(altText); 
  });

//   it('calls onClick function when the button is clicked', () => {
//     const handleClick = jest.fn();
//     const { getByRole } = render(<IconButton onClick={handleClick} />);
//     const button = getByRole('button');
//     fireEvent.click(button);
//     expect(handleClick).toHaveBeenCalledTimes(1);
//   });

//   it('increases the icon size on mouse enter', () => {
//     const { getByRole } = render(<IconButton />);
//     const button = getByRole('button');
//     fireEvent.mouseEnter(button);
//     expect(button).toHaveStyle('transform: scale(1.1)');
//   });

//   it('resets the icon size on mouse leave', () => {
//     const { getByRole } = render(<IconButton />);
//     const button = getByRole('button');
//     fireEvent.mouseEnter(button);
//     fireEvent.mouseLeave(button);
//     expect(button).toHaveStyle('transform: scale(1)');
//   });

//   it('decreases the icon size and adds margin on mouse down', () => {
//     const { getByRole } = render(<IconButton />);
//     const button = getByRole('button');
//     fireEvent.mouseDown(button);
//     expect(button).toHaveStyle('transform: scale(0.9)');
//     expect(button).toHaveStyle('margin: 2px 0');
//   });

//   it('resets the icon size and margin on mouse up', () => {
//     const { getByRole } = render(<IconButton />);
//     const button = getByRole('button');
//     fireEvent.mouseDown(button);
//     fireEvent.mouseUp(button);
//     expect(button).toHaveStyle('transform: scale(1)');
//     expect(button).toHaveStyle('margin: 0');
//   });


afterEach(() => {
    unmountComponentAtNode(container);
    container.remove();
    container = null;
  });
});
