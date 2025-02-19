export interface Friend {
  friendId: number
  memberId: number
  memberName: string
  memberNickname: string
  memberAvatarUrl: string | null
  memberBannerUrl: string | null
  memberIntroduce: string | null
  memberEmail: string
  actualStatus: string
  globalStatus: string
}

export type FriendStatus = 'PENDING' | 'DECLINED' | 'ACCEPTED'
