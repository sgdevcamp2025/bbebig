export const mockMessages = [
  {
    id: 0,
    serverId: 0,
    sequence: 0,
    channelId: 0,
    sendMemberId: 0,
    content: '테스트 메시지 1',
    createdAt: '2025-03-12T14:14:54.827Z'
  },
  {
    id: 1,
    serverId: 0,
    sequence: 1,
    channelId: 0,
    sendMemberId: 1,
    content: '테스트 메시지 2',
    createdAt: '2025-03-12T14:14:54.827Z'
  },
  {
    id: 2,
    serverId: 0,
    sequence: 2,
    channelId: 0,
    sendMemberId: 2,
    content: '테스트 메시지 3',
    createdAt: '2025-03-12T14:14:54.827Z'
  },
  {
    id: 3,
    serverId: 0,
    sequence: 3,
    channelId: 0,
    sendMemberId: 3,
    content: '테스트 메시지 4',
    createdAt: '2025-03-12T14:14:54.827Z'
  }
]

export const mockUnreadChannels = [
  {
    channelId: 0,
    unreadCount: 1
  },
  {
    channelId: 1,
    unreadCount: 2
  },
  {
    channelId: 2,
    unreadCount: 3
  }
]

export const mockUnreadMessages = [
  {
    serverId: 1,
    channels: [
      {
        channelId: 0,
        unreadCount: 1
      },
      {
        channelId: 1,
        unreadCount: 2
      },
      {
        channelId: 2,
        unreadCount: 3
      }
    ],
    totalUnread: 6
  },
  {
    serverId: 2,
    channels: [
      {
        channelId: 0,
        unreadCount: 1
      },
      {
        channelId: 1,
        unreadCount: 2
      },
      {
        channelId: 2,
        unreadCount: 3
      }
    ],
    totalUnread: 6
  }
]

export const mockMessagesByChannel = [
  {
    id: 0,
    serverId: 0,
    channelId: 0,
    sequence: 0,
    sendMemberId: 2,
    content: '테스트 메시지 1',
    attachedFiles: [],
    createdAt: '2025-03-12T14:26:08.537Z',
    updatedAt: '2025-03-12T14:26:08.537Z',
    messageType: 'TEXT',
    deleted: false
  },
  {
    id: 1,
    serverId: 0,
    channelId: 0,
    sequence: 1,
    sendMemberId: 2,
    content: '테스트 메시지 2',
    attachedFiles: [],
    createdAt: '2025-03-12T14:26:08.537Z',
    updatedAt: '2025-03-12T14:26:08.537Z',
    messageType: 'TEXT',
    deleted: false
  },
  {
    id: 2,
    serverId: 0,
    channelId: 0,
    sequence: 2,
    sendMemberId: 1,
    content: '테스트 메시지 3',
    attachedFiles: [],
    createdAt: '2025-03-12T14:26:08.537Z',
    updatedAt: '2025-03-12T14:26:08.537Z',
    messageType: 'TEXT',
    deleted: false
  },
  {
    id: 3,
    serverId: 0,
    channelId: 0,
    sequence: 3,
    sendMemberId: 1,
    content: '테스트 메시지 4',
    attachedFiles: [],
    createdAt: '2025-03-12T14:26:08.537Z',
    updatedAt: '2025-03-12T14:26:08.537Z',
    messageType: 'TEXT',
    deleted: false
  }
]
