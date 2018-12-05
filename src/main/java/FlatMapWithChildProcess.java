import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author rosu on 2018/10/27
 * 这个类用于展示 Rxjava 中异步操作中继续调用异步方法的情况
 */
public class FlatMapWithChildProcess {
    public static void main(String[] args) {
        Observable
                .fromArray(1, 2, 3, 4, 5, 6)
                .flatMap((Function<Integer, ObservableSource<Integer>>) integer -> {
                    download(integer);
                    return Observable.just(integer);
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe =======>>> 开始订阅事件");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext ======>>> " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete ========>>> RxJava 事件完成了");
                    }
                });
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
