package com.example.fitness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name ="user_id",nullable = false,foreignKey = @ForeignKey(name = "fk_activity_user"))
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING) //Store enum name as text in database.
    private  ActivityType type;
    @JdbcTypeCode(SqlTypes.JSON)  //You are storing a Java Map as a JSON column in the database.
    @Column(columnDefinition = "json") //Databases normally create columns like: VARCHAR. INT ,TIMESTAMP
                                       //   But here, you want to store JSON data (not a normal string).
    private Map<String,Object> additionalMetrics;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "activity",cascade =  CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Recommendation> recommendations = new ArrayList<>();
}
