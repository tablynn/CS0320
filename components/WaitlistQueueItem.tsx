import { Box, Paper, Stack, Typography } from "@mui/material";

interface StudentProps {
    student: {
      name: string;
      email: string;
      position: number;
    };
}

export default function WaitlistQueueItem(props: StudentProps) {
    const { student } = props;

    return (
        <Paper variant={"outlined"}>
            <Box px={2.5} py={2}>
                <Stack direction="row" justifyContent="space-between" overflow={"hidden"}>
                    <Stack direction="row" spacing={[0, null, 2]} alignItems="center" overflow={"hidden"}>
                        <Box overflow={"hidden"}>
                            <Stack direction="row" spacing={1}>
                                <Typography fontSize={16} fontWeight={600}>
                                    {student.position}. {student.name} ({student.email})
                                </Typography>
                            </Stack>
                        </Box>
                    </Stack>
                </Stack>
            </Box>
        </Paper>
    );
}
