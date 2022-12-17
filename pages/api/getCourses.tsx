/**
 * Fetch to retrive courses to display on the home page
 * 
 * @returns information about each of the courses
 */
export default async function fetchCourses(): Promise<[string, string, string][]> {
    const CourseData_URL = "http://localhost:3231/getCourseData";
    const r = await fetch(CourseData_URL);
    const json = await r.json();
    return await (json as Promise<[string, string, string][]>);
}