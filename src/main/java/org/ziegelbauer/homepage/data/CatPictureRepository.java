package org.ziegelbauer.homepage.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.ziegelbauer.homepage.models.CatPicture;

import java.util.List;

public interface CatPictureRepository extends CrudRepository<CatPicture, Integer> {
    @Query("SELECT c.id from CatPicture c")
    List<Integer> findAllId();
}
