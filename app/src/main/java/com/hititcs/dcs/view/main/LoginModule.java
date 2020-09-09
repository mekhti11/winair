package com.hititcs.dcs.view.main;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.login.LoginUseCase;
import com.hititcs.dcs.domain.repository.LoginRepository;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class LoginModule {

  @Provides
  static LoginUseCase provideLoginUseCase(LoginRepository loginRepository, PostExecutionThread postExecutionThread,
      ThreadExecutor threadExecutor) {
    return new LoginUseCase(loginRepository, postExecutionThread, threadExecutor);
  }

}
