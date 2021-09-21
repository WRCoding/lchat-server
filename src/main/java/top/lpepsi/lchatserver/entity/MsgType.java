package top.lpepsi.lchatserver.entity;

/**
 * @author 林北
 * @description
 * @date 2021-09-21 09:34
 */
public enum MsgType {
    /** 初始化 */
    INIT("INIT","初始化"),

    TEXT("TEXT","文本"),

    IMAGE("IMAGE","图片"),

    FILE("FILE","文件"),

    ERROR_TYPE("ERROR_TYPE","错误类型");

    private String type;
    private String desc;

    MsgType(String type,String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public static MsgType getByType(String type){
        for (MsgType value : values()) {
            if (value.getType().equalsIgnoreCase(type)){
                return value;
            }
        }
        return ERROR_TYPE;
    }
}
