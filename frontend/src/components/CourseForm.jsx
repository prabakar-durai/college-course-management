import { useState } from 'react'

const initialForm = {
  id: '',
  code: '',
  title: '',
  departmentId: '',
  departmentName: '',
  departmentCode: '',
}

function CourseForm({ onCourseCreated }) {
  const [formData, setFormData] = useState(initialForm)
  const [fieldErrors, setFieldErrors] = useState({})
  const [message, setMessage] = useState('')
  const [submitting, setSubmitting] = useState(false)

  function handleChange(event) {
    const { name, value } = event.target

    setFormData((currentForm) => ({
      ...currentForm,
      [name]: value,
    }))

    setFieldErrors((currentErrors) => ({
      ...currentErrors,
      [name]: '',
    }))
  }

  async function handleSubmit(event) {
    event.preventDefault()

    setSubmitting(true)
    setFieldErrors({})
    setMessage('')

    const requestBody = {
      ...formData,
      id: Number(formData.id),
      departmentId: Number(formData.departmentId),
    }

    try {
      const response = await fetch('/api/courses', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      })

      if (!response.ok) {
        const errorResponse = await response.json()

        if (response.status === 400 && errorResponse.errors) {
          setFieldErrors(errorResponse.errors)
          return
        }

        throw new Error(
          errorResponse.detail ??
            errorResponse.message ??
            `Unable to create course (${response.status})`,
        )
      }

      const createdCourse = await response.json()

      setFormData(initialForm)
      setMessage(`Course ${createdCourse.code} created successfully.`)
      onCourseCreated(createdCourse)
    } catch (requestError) {
      setMessage(requestError.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <section className="course-form-panel">
      <div className="form-heading">
        <div>
          <p className="eyebrow form-eyebrow">
            Course administration
          </p>
          <h2>Add a course</h2>
          <p>Create a course and associate it with a department.</p>
        </div>
      </div>

      <form className="course-form" onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="form-field">
            <label htmlFor="course-id">Course ID</label>
            <input
              id="course-id"
              name="id"
              type="number"
              min="1"
              required
              value={formData.id}
              onChange={handleChange}
            />
            {fieldErrors.id && (
              <span className="field-error">{fieldErrors.id}</span>
            )}
          </div>

          <div className="form-field">
            <label htmlFor="course-code">Course code</label>
            <input
              id="course-code"
              name="code"
              type="text"
              required
              value={formData.code}
              onChange={handleChange}
            />
            {fieldErrors.code && (
              <span className="field-error">{fieldErrors.code}</span>
            )}
          </div>

          <div className="form-field form-field-wide">
            <label htmlFor="course-title">Course title</label>
            <input
              id="course-title"
              name="title"
              type="text"
              required
              value={formData.title}
              onChange={handleChange}
            />
            {fieldErrors.title && (
              <span className="field-error">{fieldErrors.title}</span>
            )}
          </div>

          <div className="form-field">
            <label htmlFor="department-id">Department ID</label>
            <input
              id="department-id"
              name="departmentId"
              type="number"
              min="1"
              required
              value={formData.departmentId}
              onChange={handleChange}
            />
            {fieldErrors.departmentId && (
              <span className="field-error">
                {fieldErrors.departmentId}
              </span>
            )}
          </div>

          <div className="form-field">
            <label htmlFor="department-code">Department code</label>
            <input
              id="department-code"
              name="departmentCode"
              type="text"
              required
              value={formData.departmentCode}
              onChange={handleChange}
            />
            {fieldErrors.departmentCode && (
              <span className="field-error">
                {fieldErrors.departmentCode}
              </span>
            )}
          </div>

          <div className="form-field form-field-wide">
            <label htmlFor="department-name">Department name</label>
            <input
              id="department-name"
              name="departmentName"
              type="text"
              required
              value={formData.departmentName}
              onChange={handleChange}
            />
            {fieldErrors.departmentName && (
              <span className="field-error">
                {fieldErrors.departmentName}
              </span>
            )}
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" disabled={submitting}>
            {submitting ? 'Creating…' : 'Create course'}
          </button>

          {message && (
            <p className="form-message" role="status">
              {message}
            </p>
          )}
        </div>
      </form>
    </section>
  )
}

export default CourseForm