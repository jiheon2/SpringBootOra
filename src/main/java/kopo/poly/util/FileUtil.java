package kopo.poly.util;

import java.io.File;

public class FileUtil {
    /**
     * 현재 날짜를 기준으로 년/월/일 폴더 생성하기
     * @param uploadDir
     * @return 파일이 저장되기 위해 생성된 전체 폴더 경로
     */
    public static String mkdirForDate(String uploadDir) {
        /*
        파일을 저장하기 위한 폴더는 /년/월/일로 폴더를 생성함
        기업들은 하드디스크 용량 관리를 위해 일정기간이 지나면 업로드된 파일을 삭제함
         */
        String path = uploadDir + DateUtil.getDateTime("/yyyy/MM/dd");

        File Folder = new File(path);

        if (!Folder.exists()) { // 저장하기 위한 폴더가 존재하지 않으면 폴더 생성
            Folder.mkdirs();
        }
        return path;
    }
}
