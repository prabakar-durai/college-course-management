import { cleanup, render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { afterEach, describe, expect, it, vi } from 'vitest'

import CourseForm from './CourseForm'

const courseRequest = {
  id: 9011,
  code: 'TEST101',
  title: 'Frontend Testing',
  departmentId: 900,
  departmentName: 'Computer Applications',
  departmentCode: 'MCA',
}

async function completeForm(user) {
  await user.type(screen.getByLabelText('Course ID'), String(courseRequest.id))
  await user.type(screen.getByLabelText('Course code'), courseRequest.code)
  await user.type(screen.getByLabelText('Course title'), courseRequest.title)
  await user.type(
    screen.getByLabelText('Department ID'),
    String(courseRequest.departmentId),
  )
  await user.type(
    screen.getByLabelText('Department code'),
    courseRequest.departmentCode,
  )
  await user.type(
    screen.getByLabelText('Department name'),
    courseRequest.departmentName,
  )
}

describe('CourseForm', () => {
  afterEach(() => {
    cleanup()
    vi.unstubAllGlobals()
  })

  it('renders the course creation fields', () => {
    render(<CourseForm onCourseCreated={vi.fn()} />)

    expect(
      screen.getByRole('heading', { name: 'Add a course' }),
    ).toBeInTheDocument()
    expect(screen.getByLabelText('Course ID')).toBeRequired()
    expect(screen.getByLabelText('Course code')).toBeRequired()
    expect(screen.getByLabelText('Course title')).toBeRequired()
    expect(screen.getByLabelText('Department ID')).toBeRequired()
    expect(screen.getByLabelText('Department code')).toBeRequired()
    expect(screen.getByLabelText('Department name')).toBeRequired()
  })

  it('submits the course and reports successful creation', async () => {
    const user = userEvent.setup()
    const onCourseCreated = vi.fn()

    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue({
        ok: true,
        json: async () => courseRequest,
      }),
    )

    render(<CourseForm onCourseCreated={onCourseCreated} />)

    await completeForm(user)
    await user.click(screen.getByRole('button', { name: 'Create course' }))

    await waitFor(() => {
      expect(globalThis.fetch).toHaveBeenCalledWith('/api/courses', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(courseRequest),
      })
    })

    expect(onCourseCreated).toHaveBeenCalledWith(courseRequest)
    expect(
      await screen.findByText('Course TEST101 created successfully.'),
    ).toBeInTheDocument()
  })

  it('displays validation errors returned by the backend', async () => {
    const user = userEvent.setup()

    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue({
        ok: false,
        status: 400,
        json: async () => ({
          errors: {
            code: 'Course code is invalid',
            title: 'Course title is invalid',
          },
        }),
      }),
    )

    render(<CourseForm onCourseCreated={vi.fn()} />)

    await completeForm(user)
    await user.click(screen.getByRole('button', { name: 'Create course' }))

    expect(
      await screen.findByText('Course code is invalid'),
    ).toBeInTheDocument()
    expect(
      await screen.findByText('Course title is invalid'),
    ).toBeInTheDocument()
  })
})
