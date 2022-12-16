import React from 'react';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';

interface FooterProps {
  description: string;
  title: string;
}

/**
 * Creates a footer for the web application
 * 
 * @param props - description and title of the footer
 * @returns 
 */
export default function Footer(props: FooterProps) {
  const { description, title } = props;

  return (
    <Box aria-label="footer" component="footer" sx={{ bgcolor: 'F0F8FF', py: 6 }}>
      <Container maxWidth="lg">
        <Typography variant="h6" align="center" gutterBottom>
          {title}
        </Typography>
        <Typography
          variant="subtitle1"
          align="center"
          color="text.secondary"
          component="p"
        >
          {description}
        </Typography>
      </Container>
    </Box>
  );
}