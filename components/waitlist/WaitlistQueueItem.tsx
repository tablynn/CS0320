import { Box, Button, Paper, Stack, Typography } from "@mui/material";
import { useSession } from "next-auth/react";
import { fetchWaitlist } from "../../pages/api/fetchWaitlist"
import { removeFromWaitlist } from "../../pages/api/removeStudent"

// Parameters for a queue item
interface StudentProps {
    name: string;
    email: string;
    position: number;
    courseName: string;
    setWaitlist: any;
}

/**
 * Creates a card for someone in the waitlist given their informaiton, adding a remove
 * button if the current user is one of the people on the waitlist
 * 
 * @params name, email, position, courseName, and setWaitlist function 
 * @returns JSX.Element
 */
export default function WaitlistQueueItem({ name, email, position, courseName, setWaitlist }: StudentProps) {
    // access current user session
    const { data: session } = useSession();
    const userName = session?.user?.name;
    const userEmail = session?.user?.email;

    const removeButton: JSX.Element = <Button color="error" onClick={() => {
        removeFromWaitlist(userName, userEmail, courseName)
        fetchWaitlist(courseName).then((data) => setWaitlist(data));
    }}> X </Button>

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
                                {(userEmail == email) && removeButton}
                            </Stack>
                        </Box>
                    </Stack>
                </Stack>
            </Box>
        </Paper>
    );
}
