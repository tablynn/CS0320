/**
 * Fetch for informing the backend that a new student is attempting to join
 * 
 * @param name - student's name
 * @param email - student's email
 * @param courseName - current course page
 */
export async function removeFromWaitlist(name: string, email: string, courseName: string) {
    const WaitlistUpdate_URL = "http://localhost:3231/removeStudent?";
    const studentName = "studentName=" + name;
    const studentEmail = "email=" + email;
    const className = "className=" + courseName;
    await fetch(WaitlistUpdate_URL + studentName + "&" + studentEmail + "&" + className);
}