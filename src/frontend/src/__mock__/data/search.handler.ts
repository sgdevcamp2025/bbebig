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

export const mockMessagesByChannel = {
  serverId: 1,
  channelId: 1,
  lastMessageId: null,
  totalCount: 43,
  messages: [
    {
      id: 25530793929740290,
      serverId: 1,
      channelId: 1,
      sequence: 43,
      sendMemberId: 1,
      content: 'ㅊㅊ',
      attachedFiles: null,
      createdAt: '2025-03-12T10:50:15',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 25530790221975550,
      serverId: 1,
      channelId: 1,
      sequence: 42,
      sendMemberId: 1,
      content: 'ㅁㅁ',
      attachedFiles: null,
      createdAt: '2025-03-12T10:50:14',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 25530787659255810,
      serverId: 1,
      channelId: 1,
      sequence: 41,
      sendMemberId: 1,
      content: 'ㄴㄴ',
      attachedFiles: null,
      createdAt: '2025-03-12T10:50:13',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 25530784417058816,
      serverId: 1,
      channelId: 1,
      sequence: 40,
      sendMemberId: 1,
      content: 'ㅇㅇ',
      attachedFiles: null,
      createdAt: '2025-03-12T10:50:12',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21146333658353664,
      serverId: 1,
      channelId: 1,
      sequence: 39,
      sendMemberId: 1,
      content: 'ㅎㅇㅎㅇ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:27:58',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21144030708764670,
      serverId: 1,
      channelId: 1,
      sequence: 38,
      sendMemberId: 1,
      content: 'ㅇㅇ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:18:49',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21143788768595970,
      serverId: 1,
      channelId: 1,
      sequence: 37,
      sendMemberId: 2,
      content: '최고최고',
      attachedFiles: null,
      createdAt: '2025-02-28T08:17:51',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21143777653690370,
      serverId: 1,
      channelId: 1,
      sequence: 36,
      sendMemberId: 2,
      content: '너무 고생하셨어요',
      attachedFiles: null,
      createdAt: '2025-02-28T08:17:48',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21143765872021504,
      serverId: 1,
      channelId: 1,
      sequence: 35,
      sendMemberId: 1,
      content: '고생하셨어요/!!!!',
      attachedFiles: null,
      createdAt: '2025-02-28T08:17:46',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21143754098479104,
      serverId: 1,
      channelId: 1,
      sequence: 34,
      sendMemberId: 2,
      content: '와',
      attachedFiles: null,
      createdAt: '2025-02-28T08:17:43',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21143743877091330,
      serverId: 1,
      channelId: 1,
      sequence: 33,
      sendMemberId: 1,
      content: '~!!!!!!!!',
      attachedFiles: null,
      createdAt: '2025-02-28T08:17:40',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21143743147151360,
      serverId: 1,
      channelId: 1,
      sequence: 32,
      sendMemberId: 2,
      content: '!!',
      attachedFiles: null,
      createdAt: '2025-02-28T08:17:40',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21143661521801216,
      serverId: 1,
      channelId: 1,
      sequence: 31,
      sendMemberId: 2,
      content: '안녕',
      attachedFiles: null,
      createdAt: '2025-02-28T08:17:21',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21143641234083840,
      serverId: 1,
      channelId: 1,
      sequence: 30,
      sendMemberId: 1,
      content: '안녕하세여~~~',
      attachedFiles: null,
      createdAt: '2025-02-28T08:17:16',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21143045399646210,
      serverId: 1,
      channelId: 1,
      sequence: 29,
      sendMemberId: 2,
      content: 'ㅎㄹㅎㄹ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:14:54',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21142627097382910,
      serverId: 1,
      channelId: 1,
      sequence: 28,
      sendMemberId: 1,
      content: 'ㅗ어로',
      attachedFiles: null,
      createdAt: '2025-02-28T08:13:15.112499563',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21141833694580736,
      serverId: 1,
      channelId: 1,
      sequence: 27,
      sendMemberId: 2,
      content: 'ㄹㄹㄹ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:10:05',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21141828049047550,
      serverId: 1,
      channelId: 1,
      sequence: 26,
      sendMemberId: 2,
      content: 'ㅇㄹ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:10:04',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21141826664927230,
      serverId: 1,
      channelId: 1,
      sequence: 25,
      sendMemberId: 2,
      content: 'ㅇㄹ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:10:03',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21141824920096770,
      serverId: 1,
      channelId: 1,
      sequence: 24,
      sendMemberId: 2,
      content: 'ㅇㄹ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:10:03',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21141822931996670,
      serverId: 1,
      channelId: 1,
      sequence: 23,
      sendMemberId: 2,
      content: 'ㅇㄹ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:10:02',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21141816485351424,
      serverId: 1,
      channelId: 1,
      sequence: 22,
      sendMemberId: 2,
      content: 'ㄹㅇㄹㅇㄹㅇㄹ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:10:01',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21141777302163456,
      serverId: 1,
      channelId: 1,
      sequence: 21,
      sendMemberId: 2,
      content: 'ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:09:52',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21141706665758720,
      serverId: 1,
      channelId: 1,
      sequence: 20,
      sendMemberId: 1,
      content: 'ㅇㅇㅇ',
      attachedFiles: null,
      createdAt: '2025-02-28T08:09:35',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21140260620734464,
      serverId: 1,
      channelId: 1,
      sequence: 19,
      sendMemberId: 1,
      content: '하위',
      attachedFiles: null,
      createdAt: '2025-02-28T08:03:50.900628893',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21140143947780096,
      serverId: 1,
      channelId: 1,
      sequence: 18,
      sendMemberId: 1,
      content: 'dbdb',
      attachedFiles: null,
      createdAt: '2025-02-28T08:03:23.083932832',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21140130404372480,
      serverId: 1,
      channelId: 1,
      sequence: 17,
      sendMemberId: 1,
      content: 'dndbd',
      attachedFiles: null,
      createdAt: '2025-02-28T08:03:19.853917625',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21140124486340610,
      serverId: 1,
      channelId: 1,
      sequence: 16,
      sendMemberId: 2,
      content: '안녕하세요',
      attachedFiles: null,
      createdAt: '2025-02-28T08:03:17',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21140108015177730,
      serverId: 1,
      channelId: 1,
      sequence: 15,
      sendMemberId: 1,
      content: 'dndbd',
      attachedFiles: null,
      createdAt: '2025-02-28T08:03:14.517509908',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21139739113558016,
      serverId: 1,
      channelId: 1,
      sequence: 14,
      sendMemberId: 1,
      content: 'sndndbd',
      attachedFiles: null,
      createdAt: '2025-02-28T08:01:46.563220205',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21139644364230656,
      serverId: 1,
      channelId: 1,
      sequence: 13,
      sendMemberId: 1,
      content: '나야',
      attachedFiles: null,
      createdAt: '2025-02-28T08:01:23',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21139582162702336,
      serverId: 1,
      channelId: 1,
      sequence: 12,
      sendMemberId: 1,
      content: 'dndn',
      attachedFiles: null,
      createdAt: '2025-02-28T08:01:09.14379056',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21139570905190400,
      serverId: 1,
      channelId: 1,
      sequence: 11,
      sendMemberId: 1,
      content: 'djsjd',
      attachedFiles: null,
      createdAt: '2025-02-28T08:01:06.458949602',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21010532496838656,
      serverId: 1,
      channelId: 1,
      sequence: 10,
      sendMemberId: 4,
      content: '네.. 저. ㅕ기있어요 안녕하세요',
      attachedFiles: null,
      createdAt: '2025-02-27T23:28:20',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 21010504084492290,
      serverId: 1,
      channelId: 1,
      sequence: 9,
      sendMemberId: 1,
      content: '안녕하세요 테3님 거기계신가요',
      attachedFiles: null,
      createdAt: '2025-02-27T23:28:14',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 20892764027883520,
      serverId: 1,
      channelId: 1,
      sequence: 8,
      sendMemberId: 3,
      content: 'fh',
      attachedFiles: null,
      createdAt: '2025-02-27T15:40:23.113944717',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 20879352082337790,
      serverId: 1,
      channelId: 1,
      sequence: 7,
      sendMemberId: 1,
      content: '안녕하세여',
      attachedFiles: null,
      createdAt: '2025-02-27T14:47:04',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 20879320998481920,
      serverId: 1,
      channelId: 1,
      sequence: 6,
      sendMemberId: 3,
      content: 'dn',
      attachedFiles: null,
      createdAt: '2025-02-27T14:46:58.046718825',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 20870099116494850,
      serverId: 1,
      channelId: 1,
      sequence: 5,
      sendMemberId: 1,
      content: '하이루',
      attachedFiles: null,
      createdAt: '2025-02-27T14:10:18',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 20870082309787650,
      serverId: 1,
      channelId: 1,
      sequence: 4,
      sendMemberId: 2,
      content: '안녕ㄴ테스트2',
      attachedFiles: null,
      createdAt: '2025-02-27T14:10:14',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 20870062655410176,
      serverId: 1,
      channelId: 1,
      sequence: 3,
      sendMemberId: 1,
      content: '안녕테스트야',
      attachedFiles: null,
      createdAt: '2025-02-27T14:10:10',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 20870042975604736,
      serverId: 1,
      channelId: 1,
      sequence: 2,
      sendMemberId: 2,
      content: '안녕 하이루',
      attachedFiles: null,
      createdAt: '2025-02-27T14:10:05',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    },
    {
      id: 20868769161416704,
      serverId: 1,
      channelId: 1,
      sequence: 1,
      sendMemberId: 1,
      content: '안녕하세요 여기 하이루 서버인가연',
      attachedFiles: null,
      createdAt: '2025-02-27T14:05:01',
      updatedAt: null,
      messageType: 'TEXT',
      deleted: false
    }
  ]
}
