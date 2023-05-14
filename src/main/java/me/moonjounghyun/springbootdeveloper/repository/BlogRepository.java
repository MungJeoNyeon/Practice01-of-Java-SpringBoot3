package me.moonjounghyun.springbootdeveloper.repository;

import me.moonjounghyun.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}

