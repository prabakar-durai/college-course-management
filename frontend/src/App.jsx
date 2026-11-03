import { useEffect, useState } from 'react'
import './App.css'

const PAGE_SIZE = 2

function App() {
  const [courses, setCourses] = useState([])
  const [searchText, setSearchText] = useState('')
  const [query, setQuery] = useState('')
  const [page, setPage] = useState(0)
  const [sort, setSort] = useState('code,asc')
  const [pageDetails, setPageDetails] = useState({
    number: 0,
    size: PAGE_SIZE,
    totalElements: 0,
    totalPages: 0,
  })
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

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
        const response = await fetch(
          `/api/courses?${parameters.toString()}`,
          { signal: controller.signal },
        )

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
  }, [query, page, sort])

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

  const hasPreviousPage = pageDetails.number > 0
  const hasNextPage =
    pageDetails.number + 1 < pageDetails.totalPages

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
                    onChange={(event) =>
                      setSearchText(event.target.value)
                    }
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
            <p className="message">
              No courses matched your search.
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
                    </tr>
                  </thead>
                  <tbody>
                    {courses.map((course) => (
                      <tr key={course.id}>
                        <td>{course.id}</td>
                        <td>
                          <span className="course-code">
                            {course.code}
                          </span>
                        </td>
                        <td>{course.title}</td>
                        <td>{course.department.name}</td>
                        <td>{course.department.code}</td>
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
                    onClick={() =>
                      setPage((currentPage) => currentPage - 1)
                    }
                  >
                    Previous
                  </button>

                  <button
                    type="button"
                    disabled={!hasNextPage || loading}
                    onClick={() =>
                      setPage((currentPage) => currentPage + 1)
                    }
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