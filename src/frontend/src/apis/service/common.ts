import toast from 'react-hot-toast'

import { ENABLE_SERVER, FILE_SERVER_URL } from '@/constants/env'
export const uploadFile = async (file: File) => {
  try {
    if (!ENABLE_SERVER) {
      toast.error('이미지 업로드 기능은 현재 비활성화 되어 있습니다.')
      return
    }

    const encodedFileName = encodeURIComponent(file.name)

    const response = await fetch(
      `${FILE_SERVER_URL}/api/files/upload/${encodedFileName}?contentType=${encodeURIComponent(file.type)}`,
      {
        method: 'GET',
        headers: {
          Accept: 'application/json'
        }
      }
    )

    if (!response.ok) {
      throw new Error(`Failed to get presigned URL: ${response.statusText}`)
    }

    const presignedUrl = await response.text()

    if (presignedUrl) {
      const uploadResponse = await fetch(presignedUrl, {
        method: 'PUT',
        headers: {
          'Content-Type': file.type
        },
        body: file
      })

      if (uploadResponse.ok) {
        const fileUrl = presignedUrl.split('?')[0]

        return fileUrl
      } else {
        const errorText = await uploadResponse.text()
        console.error('❌ Error uploading file:', errorText)
        alert('Error uploading file')
      }
    } else {
      console.error('❌ Error getting presigned URL')
      alert('Error getting presigned URL')
    }
  } catch (error) {
    console.error('❌ Error:', error)
    alert('An error occurred during file upload.')
  }
}
