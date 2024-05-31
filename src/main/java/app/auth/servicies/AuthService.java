package app.auth.servicies;

import app.auth.dto.UserOutData;
import app.auth.dto.request.SignInRequest;
import app.auth.dto.request.SignUpRequest;


public interface AuthService {

    UserOutData signup(SignUpRequest request);

    UserOutData signin(SignInRequest request);
}
