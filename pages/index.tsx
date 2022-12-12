import styles from '../styles/Home.module.css'
import React, { useEffect, useState } from 'react'
import Grid from '@mui/material/Grid';
import Header from './header'
import ClassCard from './classCard';
import Footer from './footer'

export default function Home() {
  // fetches and stores the courses to be displayed on screen
  const [courses, setCourses] = useState<[string, string, string][]>([]);
  useEffect(() => {
    fetchCourses().then((data) => setCourses(data))
  }, []) 

  return (
    <div className={styles.container}>
      <Header title='Computer Science Course Waitlists'/>
      <br></br>
      <Grid container spacing={4}>
        {courses.map((course) => (
          <ClassCard key={course.at(0)} courseName={course.at(0)} professor={course.at(1)} description={course.at(2)}/>
        ))}
      </Grid>
      <Footer title='Created by Calvin Eng' description='with minimal help from Tabitha Lynn'/>
    </div>
  );
}

// fetch to retrive courses to display on the home page
const CourseData_URL = "http://localhost:3231/getCourseData";
async function fetchCourses(): Promise<[string, string, string][]> {
  const r = await fetch(CourseData_URL);
  const json = await r.json();
  return await (json as Promise<[string, string, string][]>);
}
