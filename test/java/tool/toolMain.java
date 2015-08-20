package tool;

/**
 * Created by xlo on 2015/8/20.
 * it's the main class to test tool
 */
public class toolMain {
    public static void main(String[] args) {
        ResourceManager resourceManager = ResourceManager.getResourceManager();
        new Thread(){
            @Override
            public void run() {
                System.out.println(new String(resourceManager.getResource("/ForbiddenPage.html")));
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                System.out.println(new String(resourceManager.getResource("/ForbiddenPage.html")));
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                System.out.println(new String(resourceManager.getResource("/ForbiddenPage.html")));
            }
        }.start();
    }
}
