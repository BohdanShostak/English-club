package com.inventorsoft.english.feedback.service;

import com.inventorsoft.english.feedback.domain.dto.FeedbackDto;
import com.inventorsoft.english.feedback.domain.mapper.FeedbackMapper;
import com.inventorsoft.english.feedback.repository.FeedbackRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    @Transactional
    public void sendAnonymousFeedback(FeedbackDto feedbackDto) {
        feedbackDto.setCreatedAt(LocalDateTime.now());
        feedbackRepository.save(feedbackMapper.toEntity(feedbackDto));
    }

    @Transactional
    public Page<FeedbackDto> getAllFeedbacks(Pageable pageable) {
        return feedbackRepository.findAll(pageable)
                .map(feedbackMapper::toDto);
    }
}
