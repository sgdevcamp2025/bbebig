import { http, HttpResponse } from 'msw'

import {
  CreateCategoryRequestSchema,
  CreateChannelRequestSchema,
  UpdateCategoryRequestSchema,
  UpdateChannelRequestSchema,
  UpdateServerRequestSchema
} from '@/apis/schema/types/service'
import { SERVER_URL } from '@/constants/env'

import {
  mockCategoriesData,
  mockCategory,
  mockChannelsData,
  mockServerMembers,
  mockServersData
} from '../data/service.mock'

let copyMockServers = [...mockServersData]
let copyMockCategories = [...mockCategoriesData]
let copyMockChannels = [...mockChannelsData]

const SERVER_PATH = `/service-server/servers`
const CHANNEL_PATH = `/service-server/channels`
const CATEGORY_PATH = `/service-server/categories`

export const serviceHandler = [
  http.delete(`${SERVER_URL}${SERVER_PATH}/:serverId`, ({ params }) => {
    const serverId = Number(params.serverId)
    const server = copyMockServers.find((server) => server.serverId === serverId)
    if (!server) {
      return new HttpResponse(
        JSON.stringify({
          code: 'SERVER_NOT_FOUND',
          message: 'Server not found'
        }),
        { status: 404 }
      )
    }

    copyMockServers = copyMockServers.filter((server) => server.serverId !== serverId)

    return new HttpResponse(
      JSON.stringify({
        code: 'SERVER_DELETED',
        message: 'Server deleted',
        result: {
          serverId
        }
      })
    )
  }),
  http.delete(`${SERVER_URL}${SERVER_PATH}/:serverId/withdraw`, ({ params }) => {
    const serverId = Number(params.serverId)
    const server = copyMockServers.find((server) => server.serverId === serverId)
    if (!server) {
      return new HttpResponse(
        JSON.stringify({
          code: 'SERVER_NOT_FOUND',
          message: 'Server not found'
        }),
        { status: 404 }
      )
    }
    copyMockServers = copyMockServers.filter((server) => server.serverId !== serverId)
    return new HttpResponse(
      JSON.stringify({
        code: 'SERVER_WITHDRAWN',
        message: 'Server withdrawn',
        result: {
          serverId
        }
      })
    )
  }),
  http.get(`${SERVER_URL}${SERVER_PATH}/:serverId`, ({ params }) => {
    const { serverId } = params as { serverId: string }
    return new HttpResponse(
      JSON.stringify({
        code: 'SERVER_FOUND',
        message: 'Server found',
        result: {
          server: copyMockServers.find((server) => server.serverId === Number(serverId))
        }
      })
    )
  }),
  http.get(`${SERVER_URL}${SERVER_PATH}`, () => {
    return new HttpResponse(
      JSON.stringify({
        code: 'SERVER_LIST_SUCCESS',
        message: 'Server list success',
        result: {
          servers: copyMockServers
        }
      })
    )
  }),
  http.get(`${SERVER_URL}${SERVER_PATH}/:serverId/members`, () => {
    return new HttpResponse(
      JSON.stringify({
        code: 'SERVER_MEMBERS_FOUND',
        message: 'Server members found',
        result: {
          members: mockServerMembers
        }
      })
    )
  }),
  http.get(`${SERVER_URL}${SERVER_PATH}/:serverId/channels/info`, () => {
    return new HttpResponse(
      JSON.stringify({
        code: 'CHANNEL_INFO_FOUND',
        message: 'Channel info found',
        result: {
          channels: copyMockChannels
        }
      })
    )
  }),
  http.post(`${SERVER_URL}${SERVER_PATH}`, () => {
    const mockServerId = Math.floor(Math.random() * 1000000)
    copyMockServers.push({
      serverId: mockServerId,
      serverName: 'test',
      serverImageUrl: null
    })
    return new HttpResponse(
      JSON.stringify({
        code: 'SERVER_CREATED',
        message: 'Server created',
        result: {
          serverId: mockServerId
        }
      })
    )
  }),
  http.post(`${SERVER_URL}${SERVER_PATH}/:serverId/participate`, ({ params }) => {
    const { serverId } = params as { serverId: string }
    return new HttpResponse(
      JSON.stringify({
        code: 'SERVER_PARTICIPATED',
        message: 'Server participated',
        result: {
          serverId
        }
      })
    )
  }),
  http.post(`${SERVER_URL}${SERVER_PATH}/:serverId/invite`, ({ params }) => {
    const { serverId } = params as { serverId: string }
    return new HttpResponse(
      JSON.stringify({
        code: 'SERVER_INVITED',
        message: 'Server invited',
        result: { serverId }
      })
    )
  }),
  http.put(`${SERVER_URL}${SERVER_PATH}/:serverId`, async ({ params, request }) => {
    const { serverId } = params as { serverId: string }
    const { serverName, serverImageUrl } = (await request.json()) as UpdateServerRequestSchema
    const server = copyMockServers.find((server) => server.serverId === Number(serverId))
    if (!server) {
      return new HttpResponse(
        JSON.stringify({
          code: 'SERVER_NOT_FOUND',
          message: 'Server not found'
        }),
        { status: 404 }
      )
    }
    server.serverName = serverName
    server.serverImageUrl = serverImageUrl ?? null
    return new HttpResponse(
      JSON.stringify({
        code: 'SERVER_UPDATED',
        message: 'Server updated',
        result: { serverId }
      })
    )
  }),
  http.delete(`${SERVER_URL}${CHANNEL_PATH}/:channelId`, ({ params }) => {
    const { channelId } = params as { channelId: string }
    copyMockChannels = copyMockChannels.filter((channel) => channel.channelId !== Number(channelId))
    return new HttpResponse(
      JSON.stringify({
        code: 'CHANNEL_DELETED',
        message: 'Channel deleted',
        result: { channelId: Number(channelId) }
      })
    )
  }),
  http.get(`${SERVER_URL}${CHANNEL_PATH}/:channelId`, ({ params }) => {
    const { channelId } = params as { channelId: string }
    const channel = copyMockChannels.find((channel) => channel.channelId === Number(channelId))
    if (!channel) {
      return new HttpResponse(
        JSON.stringify({
          code: 'CHANNEL_NOT_FOUND',
          message: 'Channel not found'
        }),
        { status: 404 }
      )
    }
    return new HttpResponse(
      JSON.stringify({
        code: 'CHANNEL_FOUND',
        message: 'Channel found',
        result: { channel }
      })
    )
  }),
  http.post(`${SERVER_URL}${CHANNEL_PATH}`, async ({ request }) => {
    const { channelName, channelType, privateStatus, categoryId } =
      (await request.json()) as CreateChannelRequestSchema
    const mockChannelId = Math.floor(Math.random() * 1000000)
    copyMockChannels.push({
      channelId: mockChannelId,
      categoryId: categoryId ?? 1,
      channelName,
      channelType,
      position: 1,
      privateStatus: privateStatus ?? false,
      channelMemberIdList: [1, 2, 3],
      lastSequence: 0
    })
    return new HttpResponse(
      JSON.stringify({
        code: 'CHANNEL_CREATED',
        message: 'Channel created',
        result: { channelId: mockChannelId }
      })
    )
  }),
  http.put(`${SERVER_URL}${CHANNEL_PATH}/:channelId`, async ({ params, request }) => {
    const { channelId } = params as { channelId: string }
    const { channelName, privateStatus } = (await request.json()) as UpdateChannelRequestSchema
    const channel = copyMockChannels.find((channel) => channel.channelId === Number(channelId))
    if (!channel) {
      return new HttpResponse(
        JSON.stringify({
          code: 'CHANNEL_NOT_FOUND',
          message: 'Channel not found'
        }),
        { status: 404 }
      )
    }
    channel.channelName = channelName
    channel.privateStatus = privateStatus
    return new HttpResponse(
      JSON.stringify({
        code: 'CHANNEL_UPDATED',
        message: 'Channel updated',
        result: { channelId: Number(channelId) }
      })
    )
  }),
  http.delete(`${SERVER_URL}${CATEGORY_PATH}/:categoryId`, ({ params }) => {
    const { categoryId } = params as { categoryId: string }
    copyMockCategories = copyMockCategories.filter(
      (category) => category.categoryId !== Number(categoryId)
    )
    return new HttpResponse(
      JSON.stringify({
        code: 'CATEGORY_DELETED',
        message: 'Category deleted',
        result: { categoryId: Number(categoryId) }
      })
    )
  }),
  http.get(`${SERVER_URL}${CATEGORY_PATH}/:categoryId`, ({ params }) => {
    const { categoryId } = params as { categoryId: string }
    const category = copyMockCategories.find(
      (category) => category.categoryId === Number(categoryId)
    )
    if (!category) {
      return new HttpResponse(
        JSON.stringify({
          code: 'CATEGORY_NOT_FOUND',
          message: 'Category not found'
        }),
        { status: 404 }
      )
    }
    return new HttpResponse(JSON.stringify({ ...mockCategory, categoryId: Number(categoryId) }))
  }),
  http.post(`${SERVER_URL}${CATEGORY_PATH}`, async ({ request }) => {
    const { categoryName } = (await request.json()) as CreateCategoryRequestSchema
    const mockCategoryId = Math.floor(Math.random() * 1000000)
    copyMockCategories.push({
      categoryId: mockCategoryId,
      categoryName,
      position: copyMockCategories.length + 1
    })
    return new HttpResponse(
      JSON.stringify({
        code: 'CATEGORY_CREATED',
        message: 'Category created',
        result: { categoryId: mockCategoryId }
      })
    )
  }),
  http.put(`${SERVER_URL}${CATEGORY_PATH}/:categoryId`, async ({ params, request }) => {
    const { categoryId } = params as { categoryId: string }
    const { categoryName } = (await request.json()) as UpdateCategoryRequestSchema
    const category = copyMockCategories.find(
      (category) => category.categoryId === Number(categoryId)
    )
    if (!category) {
      return new HttpResponse(
        JSON.stringify({
          code: 'CATEGORY_NOT_FOUND',
          message: 'Category not found'
        }),
        { status: 404 }
      )
    }
    category.categoryName = categoryName
    return new HttpResponse(
      JSON.stringify({
        code: 'CATEGORY_UPDATED',
        message: 'Category updated',
        result: { categoryId: Number(categoryId) }
      })
    )
  })
]
