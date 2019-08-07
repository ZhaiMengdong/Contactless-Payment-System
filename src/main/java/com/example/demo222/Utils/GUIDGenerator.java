package com.example.demo222.Utils;


import cpcn.institution.tools.system.CodeException;
import cpcn.institution.tools.util.GUID;
import cpcn.institution.tools.util.RandomNumberGenerator;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 夏路遥
 * 7/31/2019 2:47 PM
 */
public class GUIDGenerator {
    private static final SimpleDateFormat SDF_MILLISENCOND = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final SimpleDateFormat sdf_second2 = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat sdf_day = new SimpleDateFormat("yyMMdd");
    public static final String CHECKPASS_FLAG = "OK";

    public GUIDGenerator() {
    }

    public static String genGUID() throws Exception {
        return GUID.genTxNo(27);
    }

    public static String genHSGUID() throws Exception {
        return genBankOrderNo32(800109);
    }

    public static String genHSGUID20() throws Exception {
        return genBankOrderNo20();
    }

    public static String genGUID10() throws Exception {
        return genOrderNo10();
    }

    private static String genBankOrderNo32(int i) throws CodeException {
        String timeString = SDF_MILLISENCOND.format(Calendar.getInstance().getTime());
        return i + timeString + RandomNumberGenerator.getRandomNumber(9);
    }

    private static String genBankOrderNo20() throws CodeException {
        String timeString = sdf_second2.format(Calendar.getInstance().getTime());
        return timeString + RandomNumberGenerator.getRandomNumber(6);
    }

    private static String genOrderNo10() throws CodeException {
        String timeString = sdf_day.format(Calendar.getInstance().getTime());
        return timeString + RandomNumberGenerator.getRandomNumber(4);
    }
}
