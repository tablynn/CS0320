import React from "react";
import { Box, Paper, Stack, Typography } from "@mui/material";

// Parameters for header
interface WaitlistProps {
    courseName: string
}

/**
 * Creates header for the waitlists, which is a box containing the name of the course.
 * 
 * @param courseName - name of the current pages course 
 * @returns JSX.Element
 */
export default function WaitlistHeader({ courseName }: WaitlistProps): JSX.Element {

    return (
        <Paper sx={{ overflow: "hidden" }}>
            <Box width="100%" p={[2, null, 3]} color="#fff" position="relative" sx={{ bgcolor: "#7393B3" }}>
                <Box height={120}></Box>
                <Box>
                    <Typography id="header-box-text" variant="body1" noWrap>
                        {courseName}
                    </Typography>
                    <Stack direction="row" spacing={1} alignItems="center">
                    </Stack>
                </Box>
            </Box>
        </Paper>
    );
}
