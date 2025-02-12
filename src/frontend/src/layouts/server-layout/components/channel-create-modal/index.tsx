import CustomModal from '@/components/custom-modal'

interface Props {
  isOpen: boolean
  onClose: () => void
}

function ChannelCreateModal({ isOpen, onClose }: Props) {
  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}>
      <CustomModal.Header onClose={onClose}>
        <h1>채널 생성</h1>
      </CustomModal.Header>
    </CustomModal>
  )
}

export default ChannelCreateModal
