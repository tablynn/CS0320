import React, { useEffect, useState } from 'react'
import { useSession } from 'next-auth/react';
import { Button, Typography, Box, Grid, Stack, Modal } from "@mui/material";
import WaitlistQueueItem from "./WaitlistQueueItem";
import { fetchWaitlist } from "../../pages/api/fetchWaitlist"
import { addToWaitlist } from "../../pages/api/addStudent"

// Parameters for a queue
interface WaitlistProps {
    courseName: string;
}

/**
 * Creates queue for the waitlists, which has a join queue button and displays those
 * who are currently in the waitlist
 * 
 * @param courseName - name of the current pages course 
 * @returns JSX.Element
 */
export default function WaitlistQueue({ courseName }: WaitlistProps) {
    // access current user session
    const { data: session } = useSession();
    const userName: string = session?.user?.name as string;
    const userEmail: string = session?.user?.email as string;

    // fetches and stores the current waitlist in order to display on page
    const [waitlist, setWaitlist] = useState<[string, string][]>([]);
    useEffect(() => {
        fetchWaitlist(courseName).then((data) => setWaitlist(data))
    }, [])

    return (
        <Grid item xs={12} md={9}>
            <Stack direction="row" justifyContent="space-between" alignItems="center">
                <Typography variant="h6" fontWeight={600}>
                    Queue
                </Typography>
                <Button aria-label="Click to join queue" variant="contained" onClick={() => {
                    addToWaitlist(userName, userEmail, courseName);
                    fetchWaitlist(courseName).then((data) => setWaitlist(data));
                }}>
                    Join Queue
                </Button>
            </Stack>
            <Box mt={1}>
                <Stack spacing={1}>
                    {waitlist.map((student, index) => (
                        <WaitlistQueueItem key={student.at(0) as string} name={student.at(0) as string} 
                            email={student.at(1) as string} courseName={courseName} position={index + 1} 
                            setWaitlist={setWaitlist} />
                    ))}
                </Stack>
            </Box>
        </Grid>
    );
}
