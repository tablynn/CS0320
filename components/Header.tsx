import React, { useEffect, useState } from 'react';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import TipsAndUpdatesIcon from '@mui/icons-material/TipsAndUpdates';
import Typography from '@mui/material/Typography';
import Link from 'next/link';
import GoogleOAuth from '../pages/GoogleOAuth';
import { Box, Modal } from '@mui/material';
import { recommendCourse } from "../pages/api/recommend";
import { useRouter } from 'next/router';

interface HeaderProps {
  title: string;
}

const explanation: string = "Based on your current waitlists, we suggest you take a look at the following course"
const instructions: string = "Navigate to a course page and click the lightbulb icon to receive a course recommendation!"

/**
 * Creates a header for the web application
 * 
 * @param props - title of the website
 * @returns 
 */
export default function Header(props: HeaderProps) {
  const { title } = props;
  const router = useRouter();
  const { courseName } = router.query;
  const coursePage: boolean = (courseName as string) !== undefined ? true : false;

  // state for Modal recommendation display
  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  // fetches and stores the course recommendation to be displayed on screen
  const [recommendation, setRecommendation] = useState<string[]>([]);
  useEffect(() => {
    if (coursePage) {
      recommendCourse(courseName as string).then((data) => setRecommendation(data))
    }
  }, [])

  return (
    <React.Fragment>
      <Toolbar id="toolbar" sx={{ borderBottom: 1, borderColor: 'divider' }}>
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
          id="header"
          noWrap
          sx={{ flex: 1 }}
        >
          {title}
        </Typography>
        <div id="right-side-header">
          <IconButton aria-label='Click to receive a recommendation' onClick={handleOpen}>
            <TipsAndUpdatesIcon/>
          </IconButton>
          <GoogleOAuth/>
        </div>
      </Toolbar>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            {coursePage ? explanation : instructions}
          </Typography>
          <Typography id="modal-modal-description" sx={{ mt: 2 }}>
            {coursePage && recommendation.at(0)}
            {recommendation.at(1) !== undefined && " taught by " + recommendation.at(1)}
          </Typography>
        </Box>
      </Modal>
    </React.Fragment>
  );
}

const style = {
  position: 'absolute' as 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};
