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

    void deleteLocationByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);

    void deleteByUserAndLatitudeAndLongitude(User user, BigDecimal latitude, BigDecimal longitude);

    void deleteByUser_IdAndLatitudeAndLongitude(Long userId, BigDecimal latitude, BigDecimal longitude);
}
