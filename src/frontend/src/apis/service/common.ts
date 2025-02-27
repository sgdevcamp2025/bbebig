import { FILE_SERVER_URL } from '@/constants/env'

export const registerImage = async (file: File) => {
  try {
    // Ensure file name is properly encoded for the URL
    const encodedFileName = encodeURIComponent(file.name)

    // Include contentType in the request
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
      // Use the presigned URL to upload the file directly to S3
      const uploadResponse = await fetch(presignedUrl, {
        method: 'PUT',
        headers: {
          'Content-Type': file.type // Ensure the correct file type is set
        },
        body: file
      })

      if (uploadResponse.ok) {
        const fileUrl = presignedUrl.split('?')[0] // Extract base S3 URL

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
