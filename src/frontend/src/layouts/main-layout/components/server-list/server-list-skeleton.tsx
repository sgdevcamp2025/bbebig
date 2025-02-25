import { Skeleton } from '@/components/skeleton'
export function ServerListSkeleton() {
  return (
    <ul className='w-[72px] flex flex-col gap-2'>
      <li>
        <button
          type='button'
          className='flex items-center justify-center relative w-full'>
          <Skeleton
            className='w-[48px] h-[48px] bg-discord-gray-500'
            variant='circle'
          />
        </button>
      </li>
      <div className='w-full flex justify-center'>
        <div className='h-[2px] w-8 rounded-[1px] bg-gray-80' />
      </div>
      {Array.from({ length: 4 }).map((_, index) => (
        <li
          key={index}
          className='flex w-full justify-center'>
          <Skeleton
            className='w-[48px] h-[48px] bg-discord-gray-500'
            variant='circle'
          />
        </li>
      ))}
    </ul>
  )
}
