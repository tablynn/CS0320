import './App.css';
import Waitlist from './Waitlist';
import { BrowserRouter as Router, Routes, Route}
    from 'react-router-dom';
import DetailPage from '../src/pages/DetailPage';
import ReactDOM from 'react-dom'

function App(){
  return(
    <div className="App">
      <Waitlist />
    </div>
  )
}
export default App;

// function App() {
//   return (
//     <div className="Waitlist">
//       <header className="waitlist-header">
//         <p> Hello </p>
//       </header>
//       <div className="CourseData" id="dataRoot"> </div>
//       <Waitlist />
//     </div>
//   );
// }

// export default App;
/**
 * 
      {<Router>
        <Routes>
        <Route path="/detail/:id" element={<DetailPage />} />
        </Routes>
      </Router>  }
      </div>
 */
