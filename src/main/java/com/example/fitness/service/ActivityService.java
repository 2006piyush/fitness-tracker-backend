package com.example.fitness.service;

import com.example.fitness.Repository.ActivityRepository;
import com.example.fitness.Repository.UserRepository;
import com.example.fitness.dto.ActivityRequest;
import com.example.fitness.dto.ActivityResponse;
import com.example.fitness.model.Activity;
import com.example.fitness.model.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public @Nullable ActivityResponse trackActivity(ActivityRequest activityRequest) {
        User user =userRepository.findById(activityRequest.getUserId())
                .orElseThrow(()-> new RuntimeException("Invalid User"+activityRequest.getUserId()));
        Activity activity = Activity.builder()
                .user(user)
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .build();
       Activity savedActivity =  activityRepository.save(activity);
       return mapToResponse(savedActivity);
    }

    private @Nullable ActivityResponse mapToResponse(Activity savedActivity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(savedActivity.getId());
        response.setUserId(savedActivity.getUser().getId());
        response.setType(savedActivity.getType());
        response.setDuration(savedActivity.getDuration());
        response.setCaloriesBurned(savedActivity.getCaloriesBurned());
        response.setStartTime(savedActivity.getStartTime());
        response.setAdditionalMetrics(savedActivity.getAdditionalMetrics());
        response.setCreatedAt(savedActivity.getCreatedAt());
        response.setUpdatedAt(savedActivity.getUpdatedAt());
        return response;


    }

    public @Nullable List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activityList = activityRepository.findByUserId(userId);
        return activityList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
