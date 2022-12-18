import React from 'react';
import { render, screen } from '@testing-library/react';
import Home from '../pages/index';
import { beforeEach } from 'node:test';
import userEvent from '@testing-library/user-event';
import '@testing-library/jest-dom'
import Header, {homeButtonAria} from "../components/Header";

// Setup to make sure the page gets rendered
// beforeEach(() => {
//     render(<Home/>);
// });


// test('renders repl overarching div', () => {
//     const homeButton = screen.getByRole('button', {name: homeButtonAria});
//     expect(homeButton).toBeInTheDocument();
// }); 

// test('renders repl command history', () => {
//     const replHistory = screen.getByRole(/.*/, {name: historyAria});
//     expect(replHistory).toBeInTheDocument();
// });
