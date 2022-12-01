import React from 'react';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardContent from '@mui/material/CardContent';
import { ClassProps } from '../util/interfaces/Class';

export default function Class(props: ClassProps) {
  const { course } = props;

  return (
    <Grid item xs={12} md={6}>
      <CardActionArea component="a" href="/waitlist/course_waitlist">
        <Card sx={{ display: 'flex' }}>
          <CardContent sx={{ flex: 1 }}>
            <Typography component="h2" variant="h5">
              {course.courseName}
            </Typography>
            <Typography variant="subtitle1" color="text.secondary">
              {course.professor}
            </Typography>
            <Typography variant="subtitle1" paragraph>
              {course.description}
            </Typography>
            <Typography variant="subtitle1" color="primary">
              Join Waitlist
            </Typography>
          </CardContent>
        </Card>
      </CardActionArea>
    </Grid>
  );
}