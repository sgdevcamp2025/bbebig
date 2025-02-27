#!/bin/bash

KAFKA_BROKER="kafka00:9092"

# 토픽별 파티션 개수 설정
declare -A TOPICS
TOPICS["channelChatEvent"]=3
TOPICS["dmChatEvent"]=1
TOPICS["connectionEvent"]=1
TOPICS["presenceEvent"]=1
TOPICS["notificationEvent"]=2
TOPICS["serverEvent"]=2
TOPICS["channelEvent"]=2
TOPICS["memberEvent"]=1

# 각 토픽을 설정된 파티션 개수로 생성
for TOPIC in "${!TOPICS[@]}"; do
  PARTITIONS=${TOPICS[$TOPIC]}
  echo "토픽 생성 중: $TOPIC (파티션: $PARTITIONS)"
  kafka-topics.sh --create --topic "$TOPIC" \
    --bootstrap-server $KAFKA_BROKER \
    --partitions "$PARTITIONS" --replication-factor 2 \
    --if-not-exists
done

echo "Kafka 토픽이 생성되었습니다!"

# 생성된 토픽 리스트 확인
kafka-topics.sh --list --bootstrap-server $KAFKA_BROKER