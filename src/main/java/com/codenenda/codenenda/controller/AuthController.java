package com.codenenda.codenenda.controller;

import java.util.UUID;
import com.codenenda.codenenda.domain.User;
import com.codenenda.codenenda.dto.AddUserRequest;
import com.codenenda.codenenda.dto.ErrorCode;
import com.codenenda.codenenda.dto.RestResult;
import com.codenenda.codenenda.dto.RestStatus;
import com.codenenda.codenenda.service.UserService;
import com.codenenda.codenenda.util.PasswordChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserService userService;

    @PostMapping("signup")
    public Object signup(AddUserRequest req, HttpSession session) throws Exception {

        if (req.getUsername().length() <= 50 &&
                req.getEmail().contains("@") &&
                PasswordChecker.isValidPassword(req.getPassword())) {

            req.setVerified(false); // 회원가입 시 기본값

            userService.save(req);  // 이메일 인증 발송은 UserService에서 처리

            return new RestResult()
                    .setStatus(RestStatus.SUCCESS);
        }

        return new RestResult()
                .setErrorCode(ErrorCode.rest.CONTROLLER_EXCEPTION)
                .setStatus(RestStatus.FAILURE);
    }

    @GetMapping("verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        User user = userService.updateByVerifyToken(token);
        if (user != null) {
            return ResponseEntity.ok("인증이 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
        }
    }
}

