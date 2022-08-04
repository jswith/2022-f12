package com.woowacourse.f12.exception.unauthorized;

import static com.woowacourse.f12.exception.ErrorCode.TOKEN_EXPIRED;

public class TokenExpiredException extends UnauthorizedException {

    public TokenExpiredException() {
        super(TOKEN_EXPIRED, "토큰이 만료됐습니다.");
    }
}