package com.hititcs.dcs.domain.interactor.login;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.SingleWithParamUseCase;
import com.hititcs.dcs.domain.model.AuthModel;
import com.hititcs.dcs.domain.model.LoginRequest;
import com.hititcs.dcs.domain.repository.LoginRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class LoginUseCase extends SingleWithParamUseCase<AuthModel, LoginRequest> {

  LoginRepository loginRepository;

  @Inject
  public LoginUseCase(LoginRepository loginRepository, PostExecutionThread postExecutionThread,
      ThreadExecutor threadExecutor) {
    super(postExecutionThread, threadExecutor);
    this.loginRepository = loginRepository;
  }

  @Override
  protected Single<AuthModel> buildUseCaseObservable(LoginRequest param) {
    if (param == null) {
      return Single.error(new NullPointerException("LoginModel request null"));
    }
    return loginRepository.login(param);
  }
}
