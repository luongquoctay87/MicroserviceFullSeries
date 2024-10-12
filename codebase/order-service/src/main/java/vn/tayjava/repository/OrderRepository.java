package vn.tayjava.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.tayjava.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
