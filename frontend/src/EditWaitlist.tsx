const URL_Start = "http://localhost:3232/";
const add = "add";
const remove = "remove";

function addToWaitlist(courseName:string, studentID: string, studentEmail: string) {
    const course = "course" + courseName;
    const student = "student" + studentID;
    const email = "email" + studentEmail;
    const parameters = course + "&" + student + "&" + email;
    fetch(URL_Start + add + "?" + parameters);
}

function removeFromWaitlist(courseName:string, studentID: string, studentEmail: string) {
    const course = "course" + courseName;
    const student = "student" + studentID;
    const email = "email" + studentEmail;
    const parameters = course + "&" + student + "&" + email;
    fetch(URL_Start + remove + "?" + parameters);
}

export{}
//export addToWaitlist;
//export removeFromWaitlist;