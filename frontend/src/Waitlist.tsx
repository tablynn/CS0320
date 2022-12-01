import ReactDOM from 'react-dom';


// Mocked backend data that would contain all course information
const courseList = ["CSCI0150", "CSCI0200", "CSCI320"];

const myList1 = (<ul>{courseList.map((item, i) => <li key={item+i}>{item}</li>)}</ul>)
ReactDOM.render(myList1, document.getElementById('dataRoot'));

export default function Waitlist(){
  return (
    <div> 
      <header className="waitlist-header">
        <p>hiiiiii</p>
        </header>
    </div>
  )
}

//export(Waitlist);
