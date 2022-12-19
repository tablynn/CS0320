import React from 'react';
import { render, screen } from '@testing-library/react';
import Home from '../public/pages/index';
import userEvent from '@testing-library/user-event';
import '@testing-library/jest-dom';
// import {homeButtonAria} from "../components/Header";

// Setup to make sure the page gets rendered
beforeEach(() => {
    render(<Home />);
});

test('basic test', () => {
    expect(2+2).toBe(4)
})

test('home button existing on screen', () => {
    const homeButton = screen.getByRole('button', {name: homeButtonAria});
    expect(homeButton).toBeInTheDocument();
}); 

// test('renders repl command history', () => {
//     const replHistory = screen.getByRole(/.*/, {name: historyAria});
//     expect(replHistory).toBeInTheDocument();
// });
