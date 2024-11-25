package me.byeongyeonkim.springbootdeveloper.repository;

import me.byeongyeonkim.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
