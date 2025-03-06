import fs from 'fs'
import path from 'path'
import sharp from 'sharp'

// 설정
const rootDir = './public/image' // 루트 이미지 디렉토리 경로
const quality = 85 // 이미지 품질 (1-100)
const targetFormats = ['webp', 'avif', 'png'] // 원하는 변환 형식
const convertableExtensions = ['jpg', 'jpeg', 'png', 'gif', 'tiff', 'bmp'] // 변환 가능한 확장자

// 재귀적으로 디렉토리 탐색하며 이미지 변환
async function processDirectory(directory: string) {
  try {
    // 디렉토리 내 모든 파일 및 폴더 가져오기
    const entries = fs.readdirSync(directory, { withFileTypes: true })

    // 하위 디렉토리 처리
    const subdirectories = entries.filter((entry) => entry.isDirectory())
    for (const subdir of subdirectories) {
      await processDirectory(path.join(directory, subdir.name))
    }

    // 현재 디렉토리의 이미지 파일 처리
    const imageFiles = entries
      .filter((entry) => entry.isFile())
      .map((entry) => entry.name)
      .filter((file) => {
        const ext = path.extname(file).toLowerCase().substring(1)
        return convertableExtensions.includes(ext)
      })

    console.log(`디렉토리 ${directory}에서 변환할 파일: ${imageFiles.join(', ')}`)

    for (const file of imageFiles) {
      const sourcePath = path.join(directory, file)
      const filename = path.parse(file).name

      try {
        const originalBuffer = fs.readFileSync(sourcePath)
        console.log(`변환 시작: ${sourcePath}`)

        // 각 형식으로 변환
        for (const format of targetFormats) {
          try {
            const targetPath = path.join(directory, `${filename}.${format}`)

            // 이미 존재하는지 확인
            if (fs.existsSync(targetPath) && file !== `${filename}.${format}`) {
              console.log(`  → 이미 존재함: ${targetPath}`)
              continue
            }

            await sharp(originalBuffer)
              .toFormat(format as keyof sharp.FormatEnum, { quality })
              .toFile(targetPath)

            console.log(`  → 생성됨: ${targetPath}`)
          } catch (formatError) {
            console.error(`  → ${format} 변환 실패:`, formatError)
          }
        }

        // 원본이 대상 형식과 다를 경우만 삭제
        const originalExt = path.extname(file).toLowerCase().substring(1)
        if (!targetFormats.includes(originalExt)) {
          fs.unlinkSync(sourcePath)
          console.log(`  → 원본 삭제됨: ${sourcePath}`)
        }
      } catch (fileError) {
        console.error(`  → 파일 처리 실패: ${sourcePath}`, fileError)
      }
    }

    if (imageFiles.length > 0) {
      console.log(`디렉토리 ${directory}에서 ${imageFiles.length}개 이미지 변환 시도 완료!`)
    }
  } catch (error) {
    console.error(`디렉토리 ${directory} 처리 중 오류:`, error)
  }
}

async function convertAllImages() {
  try {
    if (!fs.existsSync(rootDir)) {
      console.error(`루트 디렉토리 없음: ${rootDir}`)
      return
    }

    await processDirectory(rootDir)
    console.log('모든 디렉토리 처리 완료!')
  } catch (error) {
    console.error('변환 프로세스 오류:', error)
  }
}

convertAllImages()
