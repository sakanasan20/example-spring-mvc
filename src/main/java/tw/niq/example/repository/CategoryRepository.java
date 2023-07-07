package tw.niq.example.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.niq.example.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
