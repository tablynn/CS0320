/**
 * Fetch for retrieving the course waitlist for the current course
 * 
 * @param courseName - name of the current course page
 * @returns name and email of each student in the course waitlist
 */
export async function fetchWaitlist(courseName: string): Promise<[string, string][]> {
  const CourseWaitlist_URL = "http://localhost:3231/getCourseWaitlist?className=" + courseName;
  const r = await fetch(CourseWaitlist_URL);
  const json = await r.json();
  return await (json as Promise<[string, string][]>);
}