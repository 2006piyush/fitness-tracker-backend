package com.example.fitness.service;

import com.example.fitness.Repository.ActivityRepository;
import com.example.fitness.Repository.RecommendationRepository;
import com.example.fitness.Repository.UserRepository;
import com.example.fitness.dto.RecommendationRequest;

import com.example.fitness.model.Activity;
import com.example.fitness.model.Recommendation;
import com.example.fitness.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final RecommendationRepository recommendationRepository;
    public Recommendation generateRecommendation(RecommendationRequest recommendationRequest){
        User user = userRepository.findById(recommendationRequest.getUserId())
                .orElseThrow(()-> new RuntimeException("user not found"+recommendationRequest));
        Activity activity = activityRepository.findById(recommendationRequest.getActivityId())
                .orElseThrow(()-> new RuntimeException("Activity not found"+recommendationRequest));
        Recommendation recommendation = Recommendation.builder()
                .user(user)
                .activity(activity)
                .improvements(recommendationRequest.getImprovements())
                .suggestions(recommendationRequest.getSuggestions())
                .safety(recommendationRequest.getSafety())
                .build();
        Recommendation savedRecommendation = recommendationRepository.save(recommendation);
        return savedRecommendation;
    }

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public List<Recommendation> getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId);
    }
}
