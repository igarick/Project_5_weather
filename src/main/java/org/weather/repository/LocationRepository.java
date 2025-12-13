package org.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.weather.model.Location;
import org.weather.model.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findAllByUser_Id(Long userId);

    void deleteByUser_IdAndLatitudeAndLongitude(Long userId, BigDecimal latitude, BigDecimal longitude);


    List<Location> findAllByUser(User user);

    boolean existsByUserAndNameAndLatitudeAndLongitude(User user, String name, BigDecimal latitude, BigDecimal longitude);
//    void deleteByUserAndLatitudeAndLongitude(User user, BigDecimal latitude, BigDecimal longitude);
}
