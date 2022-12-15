import React from 'react';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import Typography from '@mui/material/Typography';
import Link from 'next/link';
import GoogleOAuth from './GoogleOAuth';

interface HeaderProps {
    title: string;
}

export default function Header(props: HeaderProps) {
  const { title } = props;


  return (
    <React.Fragment>
      <Toolbar id = "toolbar" sx={{ borderBottom: 1, borderColor: 'divider' }}>
       <div id="left-side-header">
       <Link href='/'>
          <Button size="small">Home</Button>
        </Link>
       </div>
       
        <Typography
          component="h2"
          variant="h5"
          color="inherit"
          align="center"
          id = "header"
          noWrap
          sx={{ flex: 1 }}
        >
          {title}
        </Typography>
        <div id = "right-side-header">
        <IconButton>
          <SearchIcon />
        </IconButton>
        <GoogleOAuth />
        </div>
       
      </Toolbar>

      
    </React.Fragment>
  );
}
