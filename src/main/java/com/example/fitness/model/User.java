package com.example.fitness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "my_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    @Column(unique = true)
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    //This side is NOT the owner , The user field exists in Activity entity:
    //Foreign key is stored in Activity table, not User
    //Cascade = “do the same operation on the child automatically”
    //If you perform an operation on User, Hibernate will cascade that operation to its child entities (Activity, Recommendation).
    @JsonIgnore // Don’t look at this field while making JSON.
    private List<Activity> activities = new ArrayList<>();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Recommendation> recommendations= new ArrayList<>();

}
