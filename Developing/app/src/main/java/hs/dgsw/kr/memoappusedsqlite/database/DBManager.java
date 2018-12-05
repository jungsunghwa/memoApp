package hs.dgsw.kr.memoappusedsqlite.database;

public class DBManager {
    //메모 테이블을 생성하는 코드입니다.
    public String getCreateMemoTable() {
        return "CREATE TABLE memo ( "
                + "idx INTEGER PRIMARY KEY, "
                + "content STRING, "
                + "date STRING "
                + ")";
    }

    //사진이 2개이상 들어갈경우 데이터베이스 오류를 일으킬수있어서 분리했습니다. (아마 데이터베이스 이론을 배우셨다면 제1정규형에 어긋나는 문법이라 분리했습니다.)
    public String getCreateImagesTable() {
        return "CREATE TABLE image ( "
                + "idx INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
                + "image STRING, "
                + "memo_idx STRING, "
                + "FOREIGN KEY(memo_idx) REFERENCES memo(idx) "
                + ")";
    }
}
