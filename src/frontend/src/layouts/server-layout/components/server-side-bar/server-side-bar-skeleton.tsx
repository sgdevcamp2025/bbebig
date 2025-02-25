import { ChevronDown } from 'lucide-react'
import { Plus } from 'lucide-react'

import { Skeleton } from '@/components/skeleton'

export function ServerSideBarSkeleton() {
  return (
    <div>
      <button
        type='button'
        className='h-12 w-full flex text-discord-brand items-center justify-between border-b duration-200 border-discord-gray-800 hover:bg-discord-gray-600'>
        <h2 className='text-discord-font-color-normal font-semibold w-full flex justify-center'>
          <Skeleton className='w-[90%] h-6 bg-discord-gray-800' />
        </h2>
      </button>
      {/* 카테고리 & 채널 목록 */}
      <div className='flex-1 overflow-y-auto'>
        {Array.from({ length: 2 }).map((_, index) => (
          <div
            key={index}
            className='mt-4'>
            <div className='w-full px-2 flex items-center justify-between group'>
              <div className='flex items-center'>
                <span>
                  <ChevronDown className='w-4 h-4 text-discord-font-color-muted' />
                </span>
                <span className='text-xs font-semibold text-discord-font-color-muted uppercase'>
                  <Skeleton className='w-[30%] h-4' />
                </span>
              </div>
              <button type='button'>
                <Plus className='w-4 h-4 text-discord-font-color-muted' />
              </button>
            </div>

            <div className='px-2 mt-1'>
              <div
                key={index}
                className='w-full flex items-center px-2 py-1 rounded transition-colors'>
                <span className='flex-1 ml-1 text-left'>
                  <Skeleton className='w-full h-6' />
                </span>
              </div>
              <div
                key={index}
                className='w-full flex items-center px-2 py-1 rounded transition-colors'>
                <span className='flex-1 ml-1 text-left'>
                  <Skeleton className='w-[30%] h-6' />
                </span>
              </div>
              <div
                key={index}
                className='w-full flex items-center px-2 py-1 rounded transition-colors'>
                <span className='flex-1 ml-1 text-left'>
                  <Skeleton className='w-[60%] h-6' />
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default ServerSideBarSkeleton
