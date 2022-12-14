import { Button } from "@mui/material"
import { useSession, signIn, signOut } from "next-auth/react"

export default function GoogleOAuth() {
  const { data: session } = useSession()
  if (session) {
    console.log(session?.user?.image)
    return (
      <>
        {/* Signed in as {session?.user?.email} <br /> */}
        
        {/* <Image src={session?.user?.image} alt="" width="10" height="10"/> */}
        <Button variant="outlined" onClick={() => signOut()}>SIGN OUT</Button> 
        <img src={session?.user?.image} id ="profile-image" referrerPolicy="no-referrer"></img>
        
      </>
    )
  }
  return (
    <>
      {/* Not signed in <br /> */}
      {/* <img src={session?.user?.image} id ="profile-image"></img> */}
      {/* <Image src={session?.user?.image} alt="image"/> */}
      <Button variant="outlined"  onClick={() => signIn()}>SIGN IN</Button>
    </>
  )
}

