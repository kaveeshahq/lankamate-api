package lk.lankamate.api.user;

import lk.lankamate.api.auth.CurrentUser;
import lk.lankamate.api.auth.dto.UserSummary;
import lk.lankamate.api.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public ApiResponse<UserSummary> me(@CurrentUser User user) {
        return ApiResponse.ok(UserSummary.from(user));
    }
}