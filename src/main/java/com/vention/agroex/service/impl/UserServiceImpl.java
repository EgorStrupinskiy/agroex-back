package com.vention.agroex.service.impl;

import com.vention.agroex.entity.User;
import com.vention.agroex.repository.UserRepository;
import com.vention.agroex.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with this id not found!"));
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setEmailVerified(false);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        User user = getById(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }

}
