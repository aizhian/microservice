import com.genesis.microservice.common.idGenerate.IdWorker;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Aizhanglin on 2017/12/6.
 */
public class Test {
    private final AtomicReference<SecureRandom> secureRandom = new AtomicReference((Object)null);
    Set set =new HashSet();
    CountDownLatch countDownLatch=new CountDownLatch(2);
    @org.junit.Test
    public void test() throws InterruptedException {
        Runnable t1=new Runnable() {
            @Override
            public void run() {
                IdWorker idworker1=new IdWorker(0,0);
                for (int i = 0; i < 500; i++) {
                    long l = idworker1.nextId();
                    System.out.println("机器1-"+i+" "+l);
                    set.add(l);
                }
                countDownLatch.countDown();
            }
        };
        Runnable t2=new Runnable() {
            @Override
            public void run() {
                IdWorker idworker1=new IdWorker(1,0);
                for (int i = 0; i < 500; i++) {
                    long l = idworker1.nextId();

                    System.out.println("机器2-"+i+" "+l );
                    set.add(l);

                }
                countDownLatch.countDown();
            }
        };
        Thread thread1=new Thread(t1);
        Thread thread2=new Thread(t2);
        thread1.start();
        thread2.start();
        countDownLatch.await();
        System.out.println(set.size()==1000);
    }

}
