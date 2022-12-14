import React from 'react'
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardContent from '@mui/material/CardContent';
import { useRouter } from 'next/router';
import { makeStyles } from "@material-ui/core/styles";
import { classes } from '../mocks/sample_classes';

interface Class {
  courseName: string;
  professor: string;
  description: string;
}

const useStyles = makeStyles((theme) => ({
  root: {
    // maxWidth: 345,
    // margin: theme.spacing(0.5),
    // padding: theme.spacing(0.8),
    // borderRadius: theme.spacing(0),
    "&:hover": {
      // backgroundColor: "rgb(198, 220, 239)"
      backgroundColor: "aliceblue"
    }
  },
}));
 
export default function ClassCard({ courseName, professor, description }: Class) {
  const router = useRouter();
  const link: string = "/waitlist/" + courseName;
  const classes = useStyles();

 
  return (
    <Grid item xs={12} md={6}>
      <CardActionArea component="a" onClick={() => router.push(link)}>
        <Card className={classes.root} sx={{ display: 'flex', borderRadius: '2'}} style={{height: "12rem"}}>
          <CardContent id="all-cards" sx={{ flex: 1}}>
            <Typography className="class-card" component="h2" variant="h5">
              {courseName}
            </Typography>
            <Typography className="class-card" variant="subtitle1" color="text.secondary">
              {professor}
            </Typography>
            <Typography className="class-card" variant="subtitle1" paragraph>
              {description}
            </Typography>
            {/* <Typography variant="subtitle1" color="primary">
              Join Waitlist
            </Typography> */}
          </CardContent>
        </Card>
      </CardActionArea>
    </Grid>
  );
}