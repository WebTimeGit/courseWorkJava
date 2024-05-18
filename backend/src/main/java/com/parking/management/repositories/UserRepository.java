package com.parking.management.repositories;

import com.parking.management.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Знаходить користувача за електронною адресою.
     * @param email електронна адреса користувача
     * @return Optional, що містить користувача, якщо він знайдений
     */
    Optional<User> findByEmail(String email);

    /**
     * Перевіряє існування користувача за електронною адресою.
     * @param email електронна адреса користувача
     * @return true, якщо користувач існує, false - якщо ні
     */
    boolean existsByEmail(String email);

    /**
     * Знаходить користувача за ідентифікатором.
     * @param id ідентифікатор користувача
     * @return Optional, що містить користувача, якщо він знайдений
     */
    Optional<User> findById(Long id);
}
