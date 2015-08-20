package config;

/**
 * Created by xlo on 2015/8/20.
 * this main class use to debug
 */
public class ConfigMain {
    public static void main(String[] args) {
        ShowFileTypeConfig showFileTypeConfig = new ShowFileTypeConfig();
        System.out.println(showFileTypeConfig.getClass().getName());
        showFileTypeConfig.init();
        System.out.println(showFileTypeConfig.isShow("abc.html"));
    }
}
