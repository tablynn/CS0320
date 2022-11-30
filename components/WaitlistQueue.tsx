import { Button, Typography, Box, Grid, Stack } from "@mui/material";
import WaitlistQueueItem from "./WaitlistQueueItem";
import { students } from "../mocks/sample_students";

export default function WaitlistQueue() {
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
                    {students.map((student, index) => (
                        <WaitlistQueueItem key={student.name} student={student}/>
                    ))}
                </Stack>
            </Box>
        </Grid>
    );
}