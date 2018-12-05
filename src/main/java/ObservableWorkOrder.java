import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author rosu on 2018/10/27
 * 这个类用于弄懂 Observable 的工作顺序，以及上下游的关系
 * 输出结果为：
 * onSubscribe =======>>>
 * Create1 ===>>>
 * map =========>>>> 1
 * flatmap =======>>> 11
 * onNext ======>>> 111
 * Create2 ===>>>
 * map =========>>>> 2
 * flatmap =======>>> 12
 * onNext ======>>> 112
 * Create3 ===>>>
 * onComplete ========>>>
 * Create4 ===>>>
 * 表明，我们应该通过「订阅者」和「被订阅者」来理解上下游。
 * 调用了 onNext() 方法之后，只有走了订阅者的 onNext() 之后，才会继续下一个事件的发送
 */
public class ObservableWorkOrder {
    public static void main(String[] args) {
        Observable
                .create((ObservableOnSubscribe<Integer>) emitter -> {
                    System.out.println("Create1 ===>>> ");
                    emitter.onNext(1);
                    System.out.println("Create2 ===>>> ");
                    emitter.onNext(2);
                    System.out.println("Create3 ===>>> ");
                    emitter.onComplete();
                    System.out.println("Create4 ===>>> ");
                })
                .map(integer -> {
                    System.out.println("map =========>>>> " + integer);
                    return integer + 10;
                })
                .flatMap((Function<Integer, ObservableSource<Integer>>) integer -> {
                    System.out.println("flatmap =======>>> " + integer);
                    integer += 100;
                    return Observable.just(integer);
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe =======>>> ");
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
                        System.out.println("onComplete ========>>> ");
                    }
                });
    }
}
