package com.hititcs.dcs.domain.interactor;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class SingleUseCase<T> {

  private final PostExecutionThread postExecutionThread;
  private final ThreadExecutor threadExecutor;
  private CompositeDisposable disposables;

  public SingleUseCase(PostExecutionThread postExecutionThread, ThreadExecutor threadExecutor) {
    this.postExecutionThread = postExecutionThread;
    this.threadExecutor = threadExecutor;
    disposables = new CompositeDisposable();
  }

  public void execute(SingleObserver<T> useCaseSubscriber) {
    Single<T> single = buildUseCaseObservable()
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler());
    addDisposable(single.subscribe(useCaseSubscriber::onSuccess, useCaseSubscriber::onError));
  }

  protected abstract Single<T> buildUseCaseObservable();

  private final void addDisposable(Disposable disposable) {
    if (disposables == null || disposables.isDisposed()) {
      disposables = new CompositeDisposable();
    }
    disposables.add(disposable);
  }

  public void dispose() {
    if (!disposables.isDisposed()) {
      disposables.dispose();
    }
  }
}