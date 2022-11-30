import Head from 'next/head'
import styles from '../styles/Home.module.css'
import React from 'react'
import Grid from '@mui/material/Grid';
import Header from './header'
import Class from './class';
import Footer from './footer'
import { classes } from '../mocks/sample_classes';

export default function Home() {
  return (
    <div className={styles.container}>
      <Header title='Computer Science Course Waitlists'/>
      <br></br>
      <Grid container spacing={4}>
            {classes.map((course) => (
              <Class key={course.courseName} course={course} />
            ))}
      </Grid>
      <Footer title='Created by Calvin Eng' description='with minimal help from Tabitha Lynn'/>
    </div>
  );
}
