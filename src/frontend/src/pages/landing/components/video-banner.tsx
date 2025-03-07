interface Props {
  videoUrl: string
  title: string
  description: string
  videoIsLeft?: boolean
  isLast?: boolean
}

function VideoBanner({ videoUrl, title, description, videoIsLeft = false, isLast = false }: Props) {
  return (
    <div
      className={`max-h-[1030px] bg-[url("/background/shine-2.webp")] bg-cover bg-no-repeat py-[6.25rem] pb-[7.1875rem] ${isLast ? 'pb-[296px]' : ''}`}>
      <div className='padding-global'>
        <div
          className={`mx-auto flex w-full max-w-[840px] justify-between gap-[2.1625rem] rounded-[40px] bg-video-banner-background p-5 shadow-video-banner-box-shadow mobile-range:flex-col ${
            videoIsLeft ? 'desktop-range:flex-row-reverse desktop-range:text-left' : ''
          }`}>
          <div className='flex max-w-[280px] flex-col items-center justify-center text-left text-white mobile-range:w-full mobile-range:max-w-full mobile-range:p-5'>
            <h2 className='mb-4 block w-full text-[1.6875rem] font-bold uppercase leading-[1.2]'>
              {title}
            </h2>
            <p className='text-[1.125rem] leading-[1.75rem]'>{description}</p>
          </div>
          <div className='h-full w-full overflow-hidden rounded-[40px]'>
            <video
              autoPlay
              muted
              loop
              className='bg-position-center bg-size-cover'>
              <source src={videoUrl + '.mp4'} />
              <source src={videoUrl + '.webm'} />
            </video>
          </div>
        </div>
      </div>
    </div>
  )
}

export default VideoBanner
