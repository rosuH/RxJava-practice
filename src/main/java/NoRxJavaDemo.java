/**
 * @author rosu on 2018/12/4
 */
public class NoRxJavaDemo {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++){
            download(i);
        }
    }

    private static void download(int pos){
        new Thread(() -> {
            try {
                System.out.println("接到工作 ===>>> " + pos);
                Thread.sleep(500);
                System.out.println("完成工作 ===>>> " + pos + "\n 时间：" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
