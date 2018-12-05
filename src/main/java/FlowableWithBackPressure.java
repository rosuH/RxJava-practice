import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

/**
 * @author rosu on 2018/10/27
 * 这个类用于展示 Rxjava 中异步操作中继续调用异步方法的情况
 */
public class FlowableWithBackPressure {
    private static Subscription mSubscription;

    public static void main(String[] args) {
        Flowable
                .fromArray(1, 2, 3, 4, 5)
                .map(integer -> integer + 20)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("doOnNext() =======>>> 调用 download() 方法");
                        download(integer);
                    }
                })
                .subscribe(new FlowableSubscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        mSubscription = s;
                        mSubscription.request(1);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext()2 =======>>>");
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("RxJava =======>>> 事件完成了");
                    }
                });


    }

    private static void download(int pos){
        new Thread(() -> {
            try {
                System.out.println("接到工作 ===>>> " + pos);
                Thread.sleep(3000);
                System.out.println("完成工作 ===>>> " + pos + "\n 时间：" + System.currentTimeMillis() + "\n 准备拉取");
                mSubscription.request(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
