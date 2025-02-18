import axiosInstance from '../config/axios-instance'
import { CommonResponseType } from '../schema/types/common'
import {
  AcceptFriendRequestSchema,
  AcceptFriendResponseSchema,
  CreateRequestForFriendRequestSchema,
  CreateResponseForFriendResponseSchema,
  DeclineFriendRequestSchema,
  DeclineFriendResponseSchema,
  DeleteFriendInRequestListRequestSchema,
  DeleteFriendInRequestListResponseSchema,
  DeleteFriendRequestSchema,
  DeleteFriendResponseSchema,
  DeleteMemberResponseSchema,
  GetFriendAcceptedListResponseSchema,
  GetFriendPendingListResponseSchema,
  GetUserRequestSchema,
  GetUserResponseSchema,
  GetUserSelfResponseSchema,
  UpdateUserPresenceStatusRequestSchema,
  UpdateUserPresenceStatusResponseSchema,
  UpdateUserRequestSchema,
  UpdateUserResponseSchema
} from '../schema/types/user'

const MEMBER_PATH = '/user-server/members'
const FRIEND_PATH = '/user-server/friends'

const userService = () => {
  const deleteMember = async () => {
    const response = await axiosInstance.delete<CommonResponseType<DeleteMemberResponseSchema>>(
      `${MEMBER_PATH}`
    )
    return response.data
  }

  const getUser = async (data: GetUserRequestSchema) => {
    const response = await axiosInstance.get<CommonResponseType<GetUserResponseSchema>>(
      `${MEMBER_PATH}/${data.memberId}`
    )
    return response.data
  }

  const getUserSelf = async () => {
    const response = await axiosInstance.get<CommonResponseType<GetUserSelfResponseSchema>>(
      `${MEMBER_PATH}/self`
    )
    return response.data
  }

  const updateUser = async (data: UpdateUserRequestSchema) => {
    const response = await axiosInstance.patch<CommonResponseType<UpdateUserResponseSchema>>(
      `${MEMBER_PATH}`,
      data
    )
    return response.data
  }

  const updateUserPresenceStatus = async (data: UpdateUserPresenceStatusRequestSchema) => {
    const response = await axiosInstance.patch<
      CommonResponseType<UpdateUserPresenceStatusResponseSchema>
    >(`${MEMBER_PATH}/presence`, data)
    return response.data
  }

  const deleteFriend = async (data: DeleteFriendRequestSchema) => {
    const response = await axiosInstance.delete<CommonResponseType<DeleteFriendResponseSchema>>(
      `${FRIEND_PATH}/${data.friendId}`
    )
    return response.data
  }

  const deleteFriendInRequestList = async (data: DeleteFriendInRequestListRequestSchema) => {
    const response = await axiosInstance.delete<
      CommonResponseType<DeleteFriendInRequestListResponseSchema>
    >(`${FRIEND_PATH}/${data.friendId}/cancel`)
    return response.data
  }

  const getFriendPendingList = async () => {
    const response = await axiosInstance.get<
      CommonResponseType<GetFriendPendingListResponseSchema>
    >(`${FRIEND_PATH}/pending`)
    return response.data
  }

  const getFriendAcceptedList = async () => {
    const response = await axiosInstance.get<
      CommonResponseType<GetFriendAcceptedListResponseSchema>
    >(`${FRIEND_PATH}/accepted`)
    return response.data
  }

  const declineFriend = async (data: DeclineFriendRequestSchema) => {
    const response = await axiosInstance.delete<CommonResponseType<DeclineFriendResponseSchema>>(
      `${FRIEND_PATH}/${data.friendId}/decline`
    )
    return response.data
  }

  const acceptFriend = async (data: AcceptFriendRequestSchema) => {
    const response = await axiosInstance.patch<CommonResponseType<AcceptFriendResponseSchema>>(
      `${FRIEND_PATH}/${data.friendId}/accepted`
    )
    return response.data
  }

  const createRequestForFriend = async (data: CreateRequestForFriendRequestSchema) => {
    const response = await axiosInstance.post<
      CommonResponseType<CreateResponseForFriendResponseSchema>
    >(`${FRIEND_PATH}`, data)
    return response.data
  }

  return {
    deleteMember,
    getUser,
    getUserSelf,
    updateUser,
    updateUserPresenceStatus,
    deleteFriend,
    getFriendPendingList,
    getFriendAcceptedList,
    declineFriend,
    acceptFriend,
    deleteFriendInRequestList,
    createRequestForFriend
  }
}

export default userService()
