package vn.tayjava.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> implements Serializable   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    T id;

//    @Column(name = "created_at")
//    @CreationTimestamp
//    private Date createdAt;
//
//    @Column(name = "updated_at")
//    @UpdateTimestamp
//    private Date updatedAt;
}
