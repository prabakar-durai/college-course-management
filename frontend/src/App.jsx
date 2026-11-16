import { useEffect, useState } from 'react'
import CourseForm from './components/CourseForm'
import EditCourseForm from './components/EditCourseForm'
import './App.css'

const PAGE_SIZE = 2

function App() {
  const [courses, setCourses] = useState([])
  const [searchText, setSearchText] = useState('')
  const [query, setQuery] = useState('')
  const [page, setPage] = useState(0)
  const [sort, setSort] = useState('code,asc')
  const [refreshKey, setRefreshKey] = useState(0)
  const [pageDetails, setPageDetails] = useState({
    number: 0,
    size: PAGE_SIZE,
    totalElements: 0,
    totalPages: 0,
  })
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [courseBeingEdited, setCourseBeingEdited] = useState(null)
  const [deletingCode, setDeletingCode] = useState(null)
  const [deleteError, setDeleteError] = useState('')

  useEffect(() => {
    const controller = new AbortController()

    async function loadCourses() {
      setLoading(true)
      setError('')

      const parameters = new URLSearchParams({
        page: String(page),
        size: String(PAGE_SIZE),
        sort,
      })

      if (query) {
        parameters.set('query', query)
      }

      try {
        const response = await fetch(`/api/courses?${parameters.toString()}`, {
          signal: controller.signal,
        })

        if (!response.ok) {
          throw new Error(`Unable to load courses (${response.status})`)
        }

        const data = await response.json()

        setCourses(data.content ?? [])
        setPageDetails(
          data.page ?? {
            number: 0,
            size: PAGE_SIZE,
            totalElements: 0,
            totalPages: 0,
          },
        )
      } catch (requestError) {
        if (requestError.name !== 'AbortError') {
          setError(requestError.message)
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false)
        }
      }
    }

    loadCourses()

    return () => controller.abort()
  }, [query, page, sort, refreshKey])

  function handleSearch(event) {
    event.preventDefault()
    setPage(0)
    setQuery(searchText.trim())
  }

  function handleClearSearch() {
    setSearchText('')
    setQuery('')
    setPage(0)
  }

  function handleSortChange(event) {
    setSort(event.target.value)
    setPage(0)
  }

  function handleCourseCreated() {
    setSearchText('')
    setQuery('')
    setPage(0)
    setSort('code,asc')
    setRefreshKey((currentKey) => currentKey + 1)
  }

  function handleCourseUpdated() {
    setCourseBeingEdited(null)
    setRefreshKey((currentKey) => currentKey + 1)
  }
  async function handleDeleteCourse(course) {
    const confirmed = window.confirm(
      `Delete course ${course.code} — ${course.title}?`,
    )

    if (!confirmed) {
      return
    }

    setDeletingCode(course.code)
    setDeleteError('')

    try {
      const response = await fetch(
        `/api/courses/${encodeURIComponent(course.code)}`,
        {
          method: 'DELETE',
        },
      )

      if (!response.ok) {
        let message = `Unable to delete course ${course.code}.`

        try {
          const errorResponse = await response.json()
          message =
            errorResponse.detail ||
            errorResponse.message ||
            errorResponse.title ||
            message
        } catch {
          // The response did not contain JSON.
        }

        throw new Error(message)
      }

      if (courseBeingEdited?.code === course.code) {
        setCourseBeingEdited(null)
      }

      if (courses.length === 1 && page > 0) {
        setPage((currentPage) => currentPage - 1)
      } else {
        setRefreshKey((currentKey) => currentKey + 1)
      }
    } catch (deleteFailure) {
      setDeleteError(deleteFailure.message)
    } finally {
      setDeletingCode(null)
    }
  }
  const hasPreviousPage = pageDetails.number > 0
  const hasNextPage = pageDetails.number + 1 < pageDetails.totalPages

  const displayedPage =
    pageDetails.totalPages === 0 ? 0 : pageDetails.number + 1

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
        <CourseForm onCourseCreated={handleCourseCreated} />
        {courseBeingEdited && (
          <EditCourseForm
            key={courseBeingEdited.code}
            course={courseBeingEdited}
            onCourseUpdated={handleCourseUpdated}
            onCancel={() => setCourseBeingEdited(null)}
          />
        )}

        <section className="course-panel">
          <div className="panel-heading">
            <div>
              <h2>Courses</h2>
              <p>{pageDetails.totalElements} course(s) found</p>
            </div>

            <div className="course-controls">
              <form className="search-form" onSubmit={handleSearch}>
                <label htmlFor="course-search">Search courses</label>

                <div className="search-controls">
                  <input
                    id="course-search"
                    type="search"
                    value={searchText}
                    placeholder="Code or title"
                    onChange={(event) => setSearchText(event.target.value)}
                  />

                  <button type="submit">Search</button>

                  {(searchText || query) && (
                    <button
                      className="secondary-button"
                      type="button"
                      onClick={handleClearSearch}
                    >
                      Clear
                    </button>
                  )}
                </div>
              </form>

              <div className="sort-control">
                <label htmlFor="course-sort">Sort by</label>
                <select
                  id="course-sort"
                  value={sort}
                  onChange={handleSortChange}
                >
                  <option value="code,asc">Code: A–Z</option>
                  <option value="code,desc">Code: Z–A</option>
                  <option value="title,asc">Title: A–Z</option>
                  <option value="title,desc">Title: Z–A</option>
                </select>
              </div>
            </div>
          </div>

          {loading && <p className="message">Loading courses…</p>}

          {error && (
            <p className="message error-message" role="alert">
              {error}
            </p>
          )}

          {!loading && !error && courses.length === 0 && (
            <p className="message">No courses matched your search.</p>
          )}

          {deleteError && (
            <p className="error-message" role="alert">
              {deleteError}
            </p>
          )}

          {!loading && !error && courses.length > 0 && (
            <>
              <div className="table-container">
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Code</th>
                      <th>Title</th>
                      <th>Department</th>
                      <th>Department code</th>
                      <th>Actions</th>
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
                        <td>
                          <div className="table-actions">
                            <button
                              className="table-action-button"
                              type="button"
                              onClick={() => setCourseBeingEdited(course)}
                            >
                              Edit
                            </button>

                            <button
                              className="table-action-button delete-button"
                              type="button"
                              disabled={deletingCode === course.code}
                              onClick={() => handleDeleteCourse(course)}
                            >
                              {deletingCode === course.code
                                ? 'Deleting…'
                                : 'Delete'}
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              <div className="pagination">
                <p>
                  Page {displayedPage} of {pageDetails.totalPages}
                </p>

                <div className="pagination-buttons">
                  <button
                    type="button"
                    disabled={!hasPreviousPage || loading}
                    onClick={() => setPage((currentPage) => currentPage - 1)}
                  >
                    Previous
                  </button>

                  <button
                    type="button"
                    disabled={!hasNextPage || loading}
                    onClick={() => setPage((currentPage) => currentPage + 1)}
                  >
                    Next
                  </button>
                </div>
              </div>
            </>
          )}
        </section>
      </main>
    </div>
  )
}

export default App
