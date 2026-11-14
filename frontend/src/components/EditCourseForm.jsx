import { useState } from 'react'

function EditCourseForm({
  course,
  onCourseUpdated,
  onCancel,
}) {
  const [formData, setFormData] = useState({
    id: course.id,
    code: course.code,
    title: course.title,
    departmentId: course.department.id,
    departmentName: course.department.name,
    departmentCode: course.department.code,
  })

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
      id: Number(formData.id),
      title: formData.title,
      departmentId: Number(formData.departmentId),
      departmentName: formData.departmentName,
      departmentCode: formData.departmentCode,
    }

    try {
      const response = await fetch(
        `/api/courses/${encodeURIComponent(course.code)}`,
        {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(requestBody),
        },
      )

      if (!response.ok) {
        const errorResponse = await response.json()

        if (response.status === 400 && errorResponse.errors) {
          setFieldErrors(errorResponse.errors)
          return
        }

        throw new Error(
          errorResponse.detail ??
            errorResponse.message ??
            `Unable to update course (${response.status})`,
        )
      }

      const updatedCourse = await response.json()
      onCourseUpdated(updatedCourse)
    } catch (requestError) {
      setMessage(requestError.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <section className="course-form-panel edit-course-panel">
      <div className="form-heading">
        <div>
          <p className="eyebrow form-eyebrow">
            Course administration
          </p>
          <h2>Edit {course.code}</h2>
          <p>Update the course and its department information.</p>
        </div>
      </div>

      <form className="course-form" onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="form-field">
            <label htmlFor="edit-course-id">Course ID</label>
            <input
              id="edit-course-id"
              name="id"
              type="number"
              value={formData.id}
              readOnly
            />
          </div>

          <div className="form-field">
            <label htmlFor="edit-course-code">Course code</label>
            <input
              id="edit-course-code"
              name="code"
              type="text"
              value={formData.code}
              readOnly
            />
          </div>

          <div className="form-field form-field-wide">
            <label htmlFor="edit-course-title">Course title</label>
            <input
              id="edit-course-title"
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
            <label htmlFor="edit-department-id">
              Department ID
            </label>
            <input
              id="edit-department-id"
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
            <label htmlFor="edit-department-code">
              Department code
            </label>
            <input
              id="edit-department-code"
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
            <label htmlFor="edit-department-name">
              Department name
            </label>
            <input
              id="edit-department-name"
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
            {submitting ? 'Updating…' : 'Update course'}
          </button>

          <button
            className="secondary-button"
            type="button"
            disabled={submitting}
            onClick={onCancel}
          >
            Cancel
          </button>

          {message && (
            <p className="form-message error-message" role="alert">
              {message}
            </p>
          )}
        </div>
      </form>
    </section>
  )
}

export default EditCourseForm