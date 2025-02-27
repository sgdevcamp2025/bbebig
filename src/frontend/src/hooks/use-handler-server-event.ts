import { useQueryClient } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetServerMemebersResponseSchema } from '@/apis/schema/types/service'
import { useChatStore, useMessageIdStore, useMessageSequenceStore } from '@/stores/use-chat-store'
import { ServerSubscribeResponse } from '@/types/chat-stomp-event'
import { CustomPresenceStatus } from '@/types/user'

export const useHandleServerEvent = (serverId: string) => {
  const queryClient = useQueryClient()
  const addMessage = useChatStore((state) => state.addMessage)
  const addMessageId = useMessageIdStore((state) => state.addMessageId)
  const addMessageSequence = useMessageSequenceStore((state) => state.addMessageSequence)

  const handleServerEvent = (message: ServerSubscribeResponse) => {
    switch (message.type) {
      case 'SERVER_CHANNEL':
        break
      case 'SERVER_CATEGORY':
        break
      case 'SERVER_ACTION':
        break
      case 'SERVER_MEMBER_ACTION':
        break
      case 'SERVER_MEMBER_PRESENCE':
        queryClient.cancelQueries({
          queryKey: ['serverMemebersData', serverId]
        })
        queryClient.setQueryData(
          ['serverMemebersData', serverId],
          (old: CommonResponseType<GetServerMemebersResponseSchema>) => {
            const newData = {
              ...old,
              result: {
                ...old.result,
                serverMemberInfoList: old.result.serverMemberInfoList.map((user) =>
                  user.memberId === message.memberId
                    ? { ...user, globalStatus: message.globalStatus as CustomPresenceStatus }
                    : user
                )
              }
            }
            return newData
          }
        )
        break
      case 'MESSAGE_CREATE':
        if (!message.sendMemberId) return
        addMessage(message.channelId, {
          ...message,
          sendMemberId: message.sendMemberId
        })
        addMessageId(message.channelId, message.id || 0)
        addMessageSequence(message.channelId, message.sequence || 0)
        break
      default:
        break
    }
  }

  return handleServerEvent
}
