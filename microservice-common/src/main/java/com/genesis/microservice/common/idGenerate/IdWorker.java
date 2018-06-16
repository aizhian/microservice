package com.genesis.microservice.common.idGenerate;

/**
 * Created by Aizhanglin on 2017/9/8.
 * 推特Id生成算法
 */
public class IdWorker {
    private long workerId;
    private long datacenterId;
    private long sequence = 0L;

    private static long twepoch = 1288834974657L;

    private static long workerIdBits = 5L;
    private static long datacenterIdBits = 5L;
    private static long maxWorkerId = -1L ^ (-1L << (int) workerIdBits);
    private static long maxDatacenterId = -1L ^ (-1L << (int) datacenterIdBits);
    private static long sequenceBits = 12L;

    private long workerIdShift = sequenceBits;
    private long datacenterIdShift = sequenceBits + workerIdBits;
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private long sequenceMask = -1L ^ (-1L << (int) sequenceBits);

    private long lastTimestamp = -1L;
    private static Object syncRoot = new Object();

    public IdWorker(IdGenerateProperty idGenerateConfigration){
        this(idGenerateConfigration.getWorkerId(),idGenerateConfigration.getDatacenterId());
    }

    public IdWorker(long workerId, long datacenterId) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new RuntimeException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new RuntimeException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }
    public long nextId() {
        synchronized (syncRoot) {
            long timestamp = timeGen();

            if (timestamp < lastTimestamp) {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }

            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            return ((timestamp - twepoch) << (int) timestampLeftShift) | (datacenterId << (int) datacenterIdShift) | (workerId << (int) workerIdShift) | sequence;
        }
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
