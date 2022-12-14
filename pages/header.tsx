import React from 'react';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import Typography from '@mui/material/Typography';
import Link from 'next/link';
import Image from 'next/image'
import GoogleOAuth from './GoogleOAuth';
import { useSession} from "next-auth/react"

interface HeaderProps {
    title: string;
}

export default function Header(props: HeaderProps) {
  const { title } = props;
  const { data: session } = useSession()


  return (
    <React.Fragment>
      <Toolbar id = "toolbar" sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Link href='/'>
          <Button size="small">Home</Button>
        </Link>
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
        <IconButton>
          <SearchIcon />
        </IconButton>
        {/* <Image
      src={session?.user.image}
      alt="Image"
      width={10}
      height={10}
    /> */}
        <GoogleOAuth/>
      </Toolbar>
    </React.Fragment>
  );
}
