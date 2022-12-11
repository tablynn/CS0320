import React, { useEffect, useState } from 'react'
import { Button, Typography, Box, Grid, Stack } from "@mui/material";
import WaitlistQueueItem from "./WaitlistQueueItem";
 
interface WaitlistProps {
   courseName: string;
}
 
export default function WaitlistQueue({ courseName }: WaitlistProps) {
    const [waitlist, setWaitlist] = useState<[string, string][]>([]);
    useEffect(() => {
        fetchWaitlist().then((data) => setWaitlist(data))
    }, []) 

    const CourseWaitlist_URL = "http://localhost:3231/getCourseWaitlist?className=" + courseName;
    async function fetchWaitlist(): Promise<[string, string][]> {
        const r = await fetch(CourseWaitlist_URL);
        const json = await r.json();
        return await (json as Promise<[string, string][]>);
    }
 
    const WaitlistUpdate_URL = "http://localhost:3231/getCourseWaitlist?className="
    async function addToWaitlist() {
        const r = await fetch(WaitlistUpdate_URL);
    }
    
    return (
        <Grid item xs={12} md={9}>
            <Stack direction="row" justifyContent="space-between" alignItems="center">
                <Typography variant="h6" fontWeight={600}>
                   Queue
               </Typography>
               <Button variant="contained">
                       Join Queue
               </Button>
           </Stack>
           <Box mt={1}>
               <Stack spacing={1}>
                   {waitlist.map((student, index) => (
                       <WaitlistQueueItem key={student.at(0)} name={student.at(0)} email={student.at(1)} position={index + 1}/>
                   ))}
               </Stack>
           </Box>
       </Grid>
   );
}
