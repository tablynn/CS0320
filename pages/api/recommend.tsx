/**
 * Fetch for informing the backend that a new student is attempting to join
 * 
 * @param courseName - name of current course page
 * @returns string array containing recommended course information
 */
export async function recommendCourse(courseName: string) {
    const Recommendation_URL = "http://localhost:3231/recommendCourse?";
    const className = "className=" + courseName;
    const r = await fetch(Recommendation_URL + className);
    const json = await r.json();
    return await (json as Promise<string[]>);
}