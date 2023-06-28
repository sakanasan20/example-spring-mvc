package tw.niq.example.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.niq.example.entity.Beer;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {

}
