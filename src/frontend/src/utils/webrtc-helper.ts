import sdpTransform from 'sdp-transform'

export const onChangeDefaultCodecs = (
  data: RTCSessionDescriptionInit,
  broswerH264Codec: number[]
) => {
  const res = sdpTransform.parse(data.sdp || '')

  const setCodec = sdpTransform.parsePayloads(res.media[1].payloads || '').reduce((acc, cur) => {
    if (broswerH264Codec.includes(cur)) {
      acc.unshift(cur)
      return acc
    }
    acc.push(cur)
    return acc
  }, [] as number[])

  res.media[1].payloads = setCodec.join(' ')

  return sdpTransform.write(res)
}

export const filterAMR = (sdp: string) => {
  const parsedSdp = sdpTransform.parse(sdp)

  parsedSdp.media.forEach((media) => {
    if (media.payloads) {
      // AMR 제거 (payload 8, 118 등)
      const payloads = media.payloads.split(' ').map(Number)
      const filtered = payloads.filter((p) => {
        const rtpmap = media.rtp?.find((rtp) => rtp.payload === p)
        return !rtpmap?.codec?.includes('AMR')
      })
      media.payloads = filtered.join(' ')
    }

    // fmtp 및 rtcp-fb에서 AMR 관련 설정 제거
    media.fmtp = media.fmtp?.filter((fmtp) => {
      return !fmtp.payload.toString().includes('AMR') && !fmtp.config?.includes('AMR')
    })

    media.rtcpFb = media.rtcpFb?.filter(
      (fb) =>
        !media.payloads?.toString().includes(fb.payload.toString()) || !fb.type.includes('AMR')
    )
  })

  return sdpTransform.write(parsedSdp)
}
