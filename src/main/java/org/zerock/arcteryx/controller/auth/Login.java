package org.zerock.arcteryx.controller.auth;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.arcteryx.config.Response;
import org.zerock.arcteryx.entity.UserEntity;
import org.zerock.arcteryx.provider.jwtToken.JwtTokenDTO;
import org.zerock.arcteryx.provider.jwtToken.JwtTokenProvider;
import org.zerock.arcteryx.service.auth.AuthService;


@RestController
@Log4j2
@RequiredArgsConstructor
public class Login {
    private final Gson gson;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;


    public record loginDTO(
           String email,
           String password
    ){ }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginDTO loginDto ) {

        // email 검증
        if(!authService.userEmailExist(loginDto.email())){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .massage("email을 확인해주세요.")
                    .build();
        }

        // password 검증
        UserEntity user = authService.matchesPassword(loginDto);
        if(user == null){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .massage("password을 확인해주세요.")
                    .build();
        }

        //토큰 발급
        JwtTokenDTO tokenDTO = jwtTokenProvider.createToken(user);
        HttpHeaders headers = jwtTokenProvider.setTokenToHeader(tokenDTO);

        return Response.builder()
                .status(HttpStatus.OK)
                .massage("로그인 성공")
                .data(tokenDTO)
                .headers(headers)
                .build();

    }
}
