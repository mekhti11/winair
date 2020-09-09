package com.hititcs.dcs.data.executor;

import com.hititcs.dcs.domain.executor.ThreadExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class JobExecutor implements ThreadExecutor {

  private final int INITIAL_POOL_SIZE = 3;
  private final int MAX_POOL_SIZE = 5;
  private final int KEEP_ALIVE_TIME = 10;
  private final ThreadPoolExecutor threadPoolExecutor;
  private final ThreadFactory threadFactory;
  private final LinkedBlockingQueue<Runnable> linkedBlockingQueue;
  private TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

  @Inject
  public JobExecutor() {
    threadFactory = new JobThreadExecutor();
    linkedBlockingQueue = new LinkedBlockingQueue<>();
    threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
        linkedBlockingQueue, threadFactory);
  }

  @Override
  public void execute(Runnable runnable) {
    if (runnable == null) {
      throw new IllegalArgumentException("Runnable to execute cannot be null");
    }
    threadPoolExecutor.execute(runnable);
  }


  private static class JobThreadExecutor implements ThreadFactory {

    private int counter = 0;

    @Override
    public Thread newThread(Runnable runnable) {
      return new Thread(runnable, "android_" + counter++);
    }
  }
}
