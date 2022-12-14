import { Button } from "@mui/material"
import { useSession, signIn, signOut } from "next-auth/react"

export default function GoogleOAuth() {
  const { data: session } = useSession()
  if (session) {
    return (
      <>
        {/* Signed in as {session?.user?.email} <br /> */}
        {/* <img src={session?.user?.image} alt=""/> */}
        <Button variant="outlined" onClick={() => signOut()}>SIGN OUT</Button>
        
      </>
    )
  }
  return (
    <>
      {/* Not signed in <br /> */}
      {/* <img src={session?.user?.image} id ="profile-image"></img> */}
      {/* <img src={session?.user?.image} alt="image"/> */}
      <Button variant="outlined"  onClick={() => signIn()}>SIGN IN</Button>
    </>
  )
}

