import java.nio.channels.SelectionKey;

/**
 * Created by Aizhanglin on 2017/11/27.
 */
public class Test {
//    @org.junit.Test
//    public void test() throws InterruptedException {
//        Date startTime=new Date();
//        System.out.println("开始时间"+startTime);
//        CountDownLatch countDownLatch = new CountDownLatch(1000000);
//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3000);
//        for (int i = 0; i < 1000000; i++) {
//            fixedThreadPool.execute(() -> t2(countDownLatch));
//        }
//        countDownLatch.await();
//        Date endTime = new Date();
//        System.out.println("结束时间"+endTime);
//        System.out.println("花费时间"+(endTime.getTime()-startTime.getTime()));
//    }
//
//    private void t2(CountDownLatch countDownLatch){
//        try {
//            RestTemplate template=new RestTemplate();
//            HttpHeaders requestHeaders = new HttpHeaders();
//            requestHeaders.add("Cookie", "JSESSIONID=6AC208F78AC1A6C6C4169444A98E5898");
//            HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
//            String url="http://192.168.249.199:8000/microservice-business/t2";
//            ResponseEntity<String> response =  template.exchange(url, HttpMethod.GET, requestEntity, String.class);
//            System.out.println("结果："+response);
//            System.out.println("进度："+countDownLatch.getCount());
//        }finally {
//            countDownLatch.countDown();
//        }
//    }
}
