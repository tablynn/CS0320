import React from 'react'
import Grid from '@mui/material/Grid';
import Header from '../header'
import Footer from '../footer';
import WaitlistHeader from '../../components/WaitlistHeader';
import WaitlistStack from '../../components/WaitlistQueue';

export default function CourseWaitlist() {
    return (
      <div>
        <Header title='Computer Science Course Waitlists'/>
        <br></br>
        <WaitlistHeader></WaitlistHeader>
        <WaitlistStack></WaitlistStack>
        <Footer title='Created by Calvin Eng' description='with minimal help from Tabitha Lynn'/>
      </div>
    )
  }
  
