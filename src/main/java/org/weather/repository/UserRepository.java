package org.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.weather.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
