import { useEffect, useState } from 'react'
import './App.css'

function App() {
  const [courses, setCourses] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const controller = new AbortController()

    async function loadCourses() {
      try {
        const response = await fetch(
          '/api/courses?page=0&size=10&sort=code,asc',
          { signal: controller.signal },
        )

        if (!response.ok) {
          throw new Error(`Unable to load courses (${response.status})`)
        }

        const data = await response.json()
        setCourses(data.content ?? [])
      } catch (requestError) {
        if (requestError.name !== 'AbortError') {
          setError(requestError.message)
        }
      } finally {
        setLoading(false)
      }
    }

    loadCourses()

    return () => controller.abort()
  }, [])

  return (
    <div className="app-shell">
      <header className="page-header">
        <div>
          <p className="eyebrow">Academic Administration</p>
          <h1>College Course Management</h1>
          <p className="subtitle">
            View and manage courses offered by the college.
          </p>
        </div>
      </header>

      <main>
        <section className="course-panel">
          <div className="panel-heading">
            <div>
              <h2>Courses</h2>
              <p>{courses.length} course(s) displayed</p>
            </div>
          </div>

          {loading && <p className="message">Loading courses…</p>}

          {error && (
            <p className="message error-message" role="alert">
              {error}
            </p>
          )}

          {!loading && !error && courses.length === 0 && (
            <p className="message">No courses are currently available.</p>
          )}

          {!loading && !error && courses.length > 0 && (
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Code</th>
                    <th>Title</th>
                    <th>Department</th>
                    <th>Department code</th>
                  </tr>
                </thead>
                <tbody>
                  {courses.map((course) => (
                    <tr key={course.id}>
                      <td>{course.id}</td>
                      <td>
                        <span className="course-code">{course.code}</span>
                      </td>
                      <td>{course.title}</td>
                      <td>{course.department.name}</td>
                      <td>{course.department.code}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </main>
    </div>
  )
}

export default App