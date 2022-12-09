import React from "react";
import {Box, Paper, Stack, Typography} from "@mui/material";
 
interface WaitlistProps {
   courseName: string
}
 
export default function WaitlistHeader({ courseName }: WaitlistProps) {
 
   return (
       <Paper sx={{overflow: "hidden"}}>
           <Box width="100%" p={[2, null, 3]} color="#fff" position="relative" sx={{bgcolor: 'RGB(153, 255, 153)'}}>
           <Box height={120}></Box>
           <Box>
               <Typography variant="body1" noWrap>
                   {courseName}
               </Typography>
               <Stack direction="row" spacing={1} alignItems="center">
                   {/* <Typography variant="h4" fontWeight={600}>
                       {queue.title}
                   </Typography> */}
               </Stack>
           </Box>
           </Box>
       </Paper>
   );
}
