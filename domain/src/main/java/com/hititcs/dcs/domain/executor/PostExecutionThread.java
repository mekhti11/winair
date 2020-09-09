package com.hititcs.dcs.domain.executor;

import io.reactivex.Scheduler;

public interface PostExecutionThread {

  Scheduler getScheduler();
}
