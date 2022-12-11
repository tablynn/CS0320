import { useSession, signIn, signOut } from "next-auth/react"

export default function GoogleOAuth() {
  const { data: session } = useSession()
  if (session) {
    return (
      <>
        {/* Signed in as {session?.user?.email} <br /> */}
        <img src={session?.user?.image} id ="profile-image"></img>
        <button id = "logout" onClick={() => signOut()}>Sign out</button>
        
      </>
    )
  }
  return (
    <>
      {/* Not signed in <br /> */}
      <img src={session?.user?.image} id ="profile-image"></img>
      <button id = "login" onClick={() => signIn()}>Sign in</button>
    </>
  )
}

