import { useSession, signIn, signOut } from "next-auth/react"

export default function GoogleOAuth() {
  const { data: session } = useSession()
  if (session) {
    return (
      <>
        Signed in as {session?.user?.email} <br />
        <button onClick={() => signOut()}>Sign out</button>
      </>
    )
  }
  return (
    <>
      Not signed in <br />
      <button onClick={() => signIn()}>Sign in</button>
    </>
  )
}

// import React, {useEffect, useState} from 'react';
// import { GoogleLogin, GoogleLogout } from 'react-google-login';
// import { gapi } from 'gapi-script';


// export default function GoogleOAuth(){
//     const clientId = '803442078506-u2po52484hrc1jq3vo85mf77eo88n67c.apps.googleusercontent.com';
//     const [ profile, setProfile ] = useState([])
  
//     useEffect(() => {
//       const initClient = () => {
//             gapi.auth2.init({
//             clientId: clientId,
//             scope: ''
//           });
//        };
//        gapi.load('client:auth2', initClient);
//    });
  
//   const onSuccess = (res) => {
//     setProfile(res.profileObj);
//   };
  
//   const onFailure = (err) => {
//     console.log('failed', err);
//   };
  
//   const logOut = () => {
//     setProfile([]);
//   };
  
//     return (
//       <div className="App">
//       {profile ? (
//                   <div>
//                       <GoogleLogout clientId={clientId} buttonText="Log out" onLogoutSuccess={logOut} />
//                   </div>
//               ) : (
//                   <GoogleLogin
//                       clientId={clientId}
//                       buttonText="Sign in with Google"
//                       onSuccess={onSuccess}
//                       onFailure={onFailure}
//                       cookiePolicy={'single_host_origin'}
//                       isSignedIn={true}
//                   />
//               )}
//       </div>
       
//     );
// }
