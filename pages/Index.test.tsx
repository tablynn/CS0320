import React from 'react';
import { render, screen } from '@testing-library/react';
import Home from './index';
import { beforeEach } from 'node:test';
import userEvent from '@testing-library/user-event';

// Setup to make sure the page gets rendered
beforeEach(() => {
    render(<Home />);
});

// test('renders repl overarching div', () => {
//     const replDiv = screen.getByRole(/.*/, {name: });
//     expect(replDiv).toBeInTheDocument();
// });