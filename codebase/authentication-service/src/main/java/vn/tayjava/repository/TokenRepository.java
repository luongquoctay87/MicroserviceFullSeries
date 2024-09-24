package vn.tayjava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.tayjava.model.RedisToken;

@Repository
public interface TokenRepository extends CrudRepository<RedisToken, String> {
}
