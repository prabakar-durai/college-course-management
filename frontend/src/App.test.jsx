import { cleanup, render, screen } from '@testing-library/react'
import { afterEach, describe, expect, it, vi } from 'vitest'

import App from './App'

const coursePage = {
  content: [
    {
      id: 9001,
      code: 'FLY101',
      title: 'Database Migration with Flyway',
      department: {
        id: 900,
        code: 'MCA',
        name: 'Computer Applications',
      },
    },
  ],
  page: {
    number: 0,
    size: 2,
    totalElements: 1,
    totalPages: 1,
  },
}

describe('App', () => {
  afterEach(() => {
    cleanup()
    vi.unstubAllGlobals()
  })

  it('displays courses returned by the backend', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue({
        ok: true,
        json: async () => coursePage,
      }),
    )

    render(<App />)

    expect(screen.getByText('Loading courses…')).toBeInTheDocument()
    expect(await screen.findByText('FLY101')).toBeInTheDocument()
    expect(
      screen.getByText('Database Migration with Flyway'),
    ).toBeInTheDocument()
    expect(screen.getByText('Computer Applications')).toBeInTheDocument()
    expect(screen.getByText('1 course(s) found')).toBeInTheDocument()
  })

  it('displays an error when courses cannot be loaded', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue({
        ok: false,
        status: 500,
      }),
    )

    render(<App />)

    expect(await screen.findByRole('alert')).toHaveTextContent(
      'Unable to load courses (500)',
    )
  })
})
