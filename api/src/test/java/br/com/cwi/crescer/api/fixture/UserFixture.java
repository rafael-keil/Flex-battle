package br.com.cwi.crescer.api.fixture;

import br.com.cwi.crescer.api.domain.User;
import br.com.cwi.crescer.api.representation.request.LoginUserRequest;
import br.com.cwi.crescer.api.representation.response.UserLeaderboardResponse;
import br.com.cwi.crescer.api.representation.response.UserResponse;
import br.com.cwi.crescer.api.security.AuthenticatedUser;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.List;

public class UserFixture {

    public static User user() {

        return new User(
                "username",
                "email",
                "name",
                0
        );
    }

    public static User userAlternative() {

        return new User(
                "username2",
                "email2",
                "name2",
                200
        );
    }

    public static AuthenticatedUser authenticatedUser() {

        return new AuthenticatedUser(
                "username",
                "email",
                "name"
        );
    }

    public static LoginUserRequest loginUserRequest() {

        return new LoginUserRequest(
                "username",
                "password"
        );
    }

    public static UserResponse userResponse() {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(user(), UserResponse.class);
    }

    public static Page<User> userPage() {

        List<User> userList = Arrays.asList(userAlternative(), user());
        return new PageImpl<>(userList);
    }

    public static Page<UserLeaderboardResponse> userLeaderboardResponsePage() {

        ModelMapper modelMapper = new ModelMapper();

        return userPage().map(user -> modelMapper.map(user, UserLeaderboardResponse.class));
    }
}
