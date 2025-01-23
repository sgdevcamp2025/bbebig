import { discordIntroduceVideo } from '@/libs/discordAssetsPath'

import Footer from './components/footer'
import Header from './components/header'
import HomeHero from './components/home-hero'
import VideoBanner from './components/video-banner'

function LandingPage() {
  const isLogin = false
  return (
    <div className='h-full min-h-screen w-full bg-landing-background bg-cover'>
      <Header isLogin={isLogin} />
      <HomeHero />
      {discordIntroduceVideo.map((videoBanner, index) => (
        <VideoBanner
          key={index}
          videoUrl={videoBanner.source}
          title={videoBanner.title}
          description={videoBanner.description}
          videoIsLeft={index % 2 === 0}
          isLast={index === discordIntroduceVideo.length - 1}
        />
      ))}
      <Footer />
    </div>
  )
}

export default LandingPage
