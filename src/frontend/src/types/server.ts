export type ChannelType = 'TEXT' | 'VOICE'

export interface Category {
  serverId: number
  serverName: string
  ownerId: number
  serverImageUrl: string | null
  categoryInfoList: CategoryInfo[]
}

export interface CategoryInfo {
  categoryId: number
  categoryName: string
  position: number
  channelInfoList: ChannelInfo[]
}

export interface ChannelInfo {
  channelId: number
  categoryId: number
  position: number
  channelType: ChannelType
  privateStatus: boolean
}

export type ServerCategoryList = Pick<Category, 'serverId' | 'serverName' | 'serverImageUrl'>[]
