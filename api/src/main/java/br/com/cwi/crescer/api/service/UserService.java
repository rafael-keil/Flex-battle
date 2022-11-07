package br.com.cwi.crescer.api.service;

import br.com.cwi.crescer.api.domain.User;
import br.com.cwi.crescer.api.exception.NotFoundException;
import br.com.cwi.crescer.api.mapper.UserMapper;
import br.com.cwi.crescer.api.repository.UserRepository;
import br.com.cwi.crescer.api.representation.request.LoginUserRequest;
import br.com.cwi.crescer.api.representation.response.UserLeaderboardResponse;
import br.com.cwi.crescer.api.representation.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private SecurityApiService securityApiService;

    private ModelMapper modelMapper = new ModelMapper();

    public String loginUser(LoginUserRequest request) {

        try {
            String token = securityApiService.loginUser(request.getUsername(), request.getPassword());

            if (repository.findByUsername(request.getUsername()) == null) {
                User user = UserMapper.toDomain(securityApiService.getAuthenticatedUserByToken(token));

                repository.save(user);
            }

            return token;
        } catch (RuntimeException exception) {
            throw new NotFoundException("Usuário não econtrado");
        }
    }

    public void updateUserRanking(User user, Integer ranking) {
        user.setRanking(Math.max(user.getRanking() + ranking, 0));

        repository.save(user);
    }

    public User getUser() {

        return repository.findByUsername(securityApiService.getAuthenticatedUser().getUsername());
    }

    public UserResponse getUserResponse() {

        User user = getUser();

        return modelMapper.map(user, UserResponse.class);
    }

    public Page<UserLeaderboardResponse> getLeaderboard() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<User> leaderboard = repository.getLeaderboard(pageable);
        
        return leaderboard.map(user -> modelMapper.map(user, UserLeaderboardResponse.class));
    }
}
