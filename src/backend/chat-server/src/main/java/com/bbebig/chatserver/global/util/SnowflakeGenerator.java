package com.bbebig.chatserver.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

@Component
public class SnowflakeGenerator {

	private static final long BBEBIG_EPOCH = 1735689600000L; // 2025-01-01T00:00:00Z
	private static final int WORKER_ID_BITS = 5;
	private static final int PROCESS_ID_BITS = 5;
	private static final int SEQUENCE_BITS = 12;

	private static final long MAX_WORKER_ID = (1 << WORKER_ID_BITS) - 1; // 31
	private static final long MAX_PROCESS_ID = (1 << PROCESS_ID_BITS) - 1; // 31
	private static final long MAX_SEQUENCE = (1 << SEQUENCE_BITS) - 1;     // 4095

	private final long workerId;
	private final long processId;

	private long lastTimestamp = -1L;
	private long sequence = 0L;

	public SnowflakeGenerator(
			@Value("${snowflake.worker-id:#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(0, 32)}}") long workerId) {

		this.workerId = workerId;
		this.processId = calculateProcessId();

		if (workerId < 0 || workerId > MAX_WORKER_ID) {
			throw new IllegalArgumentException("Worker ID out of range: 0-" + MAX_WORKER_ID);
		}
		if (processId < 0 || processId > MAX_PROCESS_ID) {
			throw new IllegalArgumentException("Process ID out of range: 0-" + MAX_PROCESS_ID);
		}
	}

	private long calculateProcessId() {
		try {
			// JVM 프로세스 ID 가져오기
			String runtimeName = ManagementFactory.getRuntimeMXBean().getName(); // 예: 12345@hostname
			int jvmPid = Integer.parseInt(runtimeName.split("@")[0]); // PID 추출
			return jvmPid & MAX_PROCESS_ID; // 5비트 범위 (0~31)로 제한
		} catch (Exception e) {
			throw new RuntimeException("Failed to calculate processId from JVM process info", e);
		}
	}

	public synchronized long nextId() {
		long currentTime = currentTimeMillis();

		if (currentTime < lastTimestamp) {
			throw new RuntimeException("Clock moved backwards. Refuse to generate id.");
		}

		if (currentTime == lastTimestamp) {
			sequence = (sequence + 1) & MAX_SEQUENCE;
			if (sequence == 0) {
				currentTime = waitNextMillis(currentTime);
			}
		} else {
			sequence = 0;
		}

		lastTimestamp = currentTime;

		long timeOffset = currentTime - BBEBIG_EPOCH; // ms offset

		return (timeOffset << (WORKER_ID_BITS + PROCESS_ID_BITS + SEQUENCE_BITS))
				| (workerId << (PROCESS_ID_BITS + SEQUENCE_BITS))
				| (processId << SEQUENCE_BITS)
				| sequence;
	}

	private long currentTimeMillis() {
		return System.currentTimeMillis();
	}

	private long waitNextMillis(long currentTime) {
		while (currentTime == lastTimestamp) {
			currentTime = currentTimeMillis();
		}
		return currentTime;
	}
}
