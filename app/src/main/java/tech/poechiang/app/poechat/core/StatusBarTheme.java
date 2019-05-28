package tech.poechiang.app.poechat.core;


public enum StatusBarTheme {
    DARK("深色主题",1),LIGHT("浅色主题",2);

    private String value;
    private int key;

    private StatusBarTheme(String value, int key) {
        this.value = value;
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}