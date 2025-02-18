export interface Friend {
  friendId: number
  friendMemberId: number
  friendName: string
  friendNickname: string
  friendAvatarUrl: string
  friendBannerUrl: string
  friendIntroduce: string
  friendEmail: string
}

export type FriendStatus = 'PENDING' | 'DECLINED' | 'ACCEPTED'
