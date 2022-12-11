import { Box, Paper, Stack, Typography } from "@mui/material";

interface StudentProps {
    name: string;
    email: string;
    position: number;
}

export default function WaitlistQueueItem({ name, email, position }: StudentProps) {
    return (
        <Paper variant={"outlined"}>
            <Box px={2.5} py={2}>
                <Stack direction="row" justifyContent="space-between" overflow={"hidden"}>
                    <Stack direction="row" spacing={[0, null, 2]} alignItems="center" overflow={"hidden"}>
                        <Box overflow={"hidden"}>
                            <Stack direction="row" spacing={1}>
                                <Typography fontSize={16} fontWeight={600}>
                                    {position}. {name} ({email})
                                </Typography>
                            </Stack>
                        </Box>
                    </Stack>
                </Stack>
            </Box>
        </Paper>
    );
}
