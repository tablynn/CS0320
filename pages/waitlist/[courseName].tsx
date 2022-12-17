import React from 'react'
import Header from '../../components/Header'
import Footer from '../../components/Footer';
import WaitlistHeader from '../../components/waitlist/WaitlistHeader';
import WaitlistQueue from '../../components/waitlist/WaitlistQueue';
import { useRouter } from "next/router";
 
/**
 * Page that uses dynamic routing to create unique page for each course 
 * 
 * @returns JSX.Element
 */
export default function CourseWaitlist(): JSX.Element {
 const router = useRouter();
 const { courseName } = router.query;
 
 return (
   <div id="waitlist-queue">
     <Header title='Computer Science Course Waitlists'/>
     <br></br>
     <WaitlistHeader courseName={courseName as string}/>
     <br></br>
     <WaitlistQueue courseName={courseName as string}/>
     <Footer title='Created by Calvin Eng' description='with minimal help from Tabitha Lynn'/>
   </div>
 )
}
