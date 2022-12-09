import { Button, Typography, Box, Grid, Stack } from "@mui/material";
import WaitlistQueueItem from "./WaitlistQueueItem";
import { students } from "../mocks/sample_students";
import { useState } from "react";
import { Student } from "../util/interfaces/Student";
 
interface WaitlistProps {
   courseName: string;
}
 
export default function WaitlistQueue() {
   const [users, setUsers] = useState<Student[]>(students);
 
   function addToWaitlist() {
       const newStudent: Student = {name:'Mr. Peaches', email:'mr.peaches@brown.edu', position:users.length + 1}
       const newUsers = users.slice();
       newUsers.push(newStudent)
       setUsers(newUsers);
   }
  
   return (
       <Grid item xs={12} md={9}>
           <Stack direction="row" justifyContent="space-between" alignItems="center">
               <Typography variant="h6" fontWeight={600}>
                   Queue
               </Typography>
               <Button variant="contained"
                       onClick={addToWaitlist}>
                       Join Queue
               </Button>
           </Stack>
           <Box mt={1}>
               <Stack spacing={1}>
                   {users.map((student, index) => (
                       <WaitlistQueueItem key={student.name} student={student}/>
                   ))}
               </Stack>
           </Box>
       </Grid>
   );
}
