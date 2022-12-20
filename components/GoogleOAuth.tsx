import { Button } from "@mui/material"
import { useSession, signIn, signOut } from "next-auth/react"

/**
 * Creates a Google authentication sign in and sign out button for the header. 
 * @returns a sign in/sign out button
 */
export default function GoogleOAuth() {
  const { data: session } = useSession()
  // if the user is already signed in, then returns a sign out button and the user's profile image
  if (session) {
    console.log(session?.user?.image)
    return (
      <>
        <Button variant="outlined" onClick={() => signOut()}>SIGN OUT</Button> 
        <img src={session?.user?.image as string} id ="profile-image" referrerPolicy="no-referrer"></img>
        
      </>
    )
  }
  // if the user isn't signed in, returns just a sign in button
  return (
    <>
      <Button variant="outlined"  onClick={() => signIn()}>SIGN IN</Button>
    </>
  )
}

