package ru.yandex.practicum.mymarket.service.user;

import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{
    @Override
    public Long getCurrentUserId() {
        return 1L;
    }
}
