import styles from '../styles/Home.module.css'
import React, { useEffect, useState } from 'react'
import Grid from '@mui/material/Grid';
import Header from '../components/Header'
import ClassCard from '../components/ClassCard';
import Footer from '../components/Footer'
import fetchCourses from './api/getCourses';
import { Description } from '@mui/icons-material';

export default function Home() {
  // fetches and stores the courses to be displayed on screen
  const [courses, setCourses] = useState<[string, string, string, string][]>([]);
  useEffect(() => {
    fetchCourses().then((data) => setCourses(data))
  }, [])

  return (
    <div className={styles.container}>
      <Header title='Computer Science Course Waitlists' />
      <br></br>
      <Grid container spacing={4}>
        {courses.map((course) => (
          <ClassCard key={course.at(0)} courseName={course.at(0) as string} professor={course.at(1) as string} 
            description={course.at(2) as string} email={course.at(3) as string}/>
        ))}
      </Grid>
      <Footer title='Created by Calvin Eng' description='with minimal help from Tabitha Lynn' />
    </div>
  );
}
