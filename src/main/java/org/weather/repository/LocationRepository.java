package org.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.weather.model.Location;
import org.weather.model.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    void deleteByUserAndLatitudeAndLongitude(User user, BigDecimal latitude, BigDecimal longitude);

    List<Location> findAllByUser(User user);

    boolean existsByUserAndNameAndLatitudeAndLongitude(User user, String name, BigDecimal latitude, BigDecimal longitude);
}
