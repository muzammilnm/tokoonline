package com.tokoonline.demo.User;

import java.io.Serializable;
import java.time.OffsetTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode.Exclude;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="_user")
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @CreationTimestamp
    @Exclude
    @Column(updatable = false)
    private OffsetTime createdAt;

    @UpdateTimestamp
    @Exclude
    private OffsetTime updateAt;
}
