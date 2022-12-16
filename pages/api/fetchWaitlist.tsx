// fetch for retrieving the course waitlist for the current course
export async function fetchWaitlist(courseName: string): Promise<[string, string][]> {
  const CourseWaitlist_URL = "http://localhost:3231/getCourseWaitlist?className=" + courseName;
  const r = await fetch(CourseWaitlist_URL);
  const json = await r.json();
  return await (json as Promise<[string, string][]>);
}