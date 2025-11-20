package com.taoge.framework.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Twitter的SnowFlake算法,使用SnowFlake算法生成一个整数
 */
public class SnowFlake {

    //起始的时间戳
    private final static long START_TIMESTAMP = 1598889600000L;
    //每一部分占用的位数
    private final static long SEQUENCE_BIT = 12;   //序列号占用的位数
    private final static long MACHINE_BIT = 1;     //机器标识占用的位数
    private final static long DATA_CENTER_BIT = 1; //数据中心占用的位数
    //每一部分的最大值
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_DATA_CENTER_NUM = ~(-1L << DATA_CENTER_BIT);
    //每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    private static final long dataCenterId = 1;  //数据中心
    private static final long machineId = 1;     //机器标识
    private static long sequence = 0L; //序列号
    private static long lastTimeStamp = -1L;  //上一次时间戳

    private static final char[] CHARS = new char[]{'D', 'M', 'W', 'U', 'u', '4', 'L', 'k', 'N', 'o', 'y', 'q', 'v', 'P', 'H', '2', 'g'
            , 'd', '1', '9', 'm', 'F', 'I', 'B', 'X', 'j', 'K', 'p', 'e', 'n', 'l', 'i', 'S', 'z', 'T', '7', '0', 'Z', '6', 'f', 'b', 'C', 'J', '3'
            , 'x', 'E', 'a', 't', 'V', 'h', 'O', 'R', 'w', '5', 's', 'c', '8', 'A', 'G', 'Q', 'r', 'Y'};
    /**
     * A补位字符，不能与自定义重复
     */
    private static final char SUFFIX_CHAR = 'A';
    /**
     * 进制长度
     */
    private static final int BIN_LEN = CHARS.length;

    /**
     * 生成邀请码最小长度
     */
    private static final int CODE_LEN = 6;

    private static long getNextMill() {
        long mill = getNewTimeStamp();
        while (mill <= lastTimeStamp) {
            mill = getNewTimeStamp();
        }
        return mill;
    }

    private static long getNewTimeStamp() {
        return System.currentTimeMillis();
    }

    //    /**
//     * 根据指定的数据中心ID和机器标志ID生成指定的序列号
//     *
//     * @param dataCenterId 数据中心ID
//     * @param machineId    机器标志ID
//     */
//    public SnowFlake(long dataCenterId, long machineId) {
//        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
//            throw new IllegalArgumentException("DtaCenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0！");
//        }
//        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
//            throw new IllegalArgumentException("MachineId can't be greater than MAX_MACHINE_NUM or less than 0！");
//        }
//        this.dataCenterId = dataCenterId;
//        this.machineId = machineId;
//    }
    //产生下一个ID
    public synchronized static long nextId() {
        long currTimeStamp = getNewTimeStamp();
        if (currTimeStamp < lastTimeStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }
        if (currTimeStamp == lastTimeStamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;//相同毫秒内，序列号自增
            if (sequence == 0L) //同一毫秒的序列数已经达到最大
            {
                currTimeStamp = getNextMill();
            }
        } else {
            sequence = 0L;//不同毫秒内，序列号置为0
        }
        lastTimeStamp = currTimeStamp;
        return (currTimeStamp - START_TIMESTAMP) << TIMESTAMP_LEFT //时间戳部分
                | dataCenterId << DATA_CENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }


    /**
     * ID转换为code码(需要判断是否重复，重复时继续调用重新生成即可)
     *
     * @param id
     * @return
     */
    public static String idToCode(Long id, Integer length) {
        char[] buf = new char[BIN_LEN];
        int charPos = BIN_LEN;

        // 当id除以数组长度结果大于0，则进行取模操作，并以取模的值作为数组的坐标获得对应的字符
        while (id / BIN_LEN > 0) {
            int index = (int) (id % BIN_LEN);
            buf[--charPos] = CHARS[index];
            id /= BIN_LEN;
        }

        buf[--charPos] = CHARS[(int) (id % BIN_LEN)];
        // 将字符数组转化为字符串
        String result = new String(buf, charPos, BIN_LEN - charPos);

        // 长度不足指定长度则随机补全
        int len = result.length();
        if (len < length) {
            StringBuilder sb = new StringBuilder();
            sb.append(SUFFIX_CHAR);
            ThreadLocalRandom random = ThreadLocalRandom.current();
            // 去除SUFFIX_CHAR本身占位之后需要补齐的位数
            for (int i = 0; i < length - len - 1; i++) {
                sb.append(CHARS[random.nextInt(BIN_LEN)]);
            }

            result += sb.toString();
        }
        return result;
    }

    public static String idToCode(Long id) {
        return idToCode(id, CODE_LEN);
    }


}