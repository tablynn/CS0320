import { Box, Button, Paper, Stack, Typography } from "@mui/material";
import { useSession } from "next-auth/react";

interface StudentProps {
    name: string;
    email: string;
    position: number;
}

export default function WaitlistQueueItem({ name, email, position }: StudentProps) {
    // access current user session
    const { data: session } = useSession();

    return (
        <Paper variant={"outlined"}>
            <Box px={2.5} py={2}>
                <Stack direction="row" justifyContent="space-between" overflow={"hidden"}>
                    <Stack direction="row" spacing={[0, null, 2]} alignItems="center" overflow={"hidden"}>
                        <Box overflow={"hidden"}>
                            <Stack direction="row" justifyContent="space-between" alignItems="center">
                                <Typography fontSize={16} fontWeight={600}>
                                    {position}. {name} ({email})
                                </Typography>
                                <Button color="error"> X </Button>
                            </Stack>
                        </Box>
                    </Stack>
                </Stack>
            </Box>
        </Paper>
    );
}
