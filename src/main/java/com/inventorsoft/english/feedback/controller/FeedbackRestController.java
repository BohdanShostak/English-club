package com.inventorsoft.english.feedback.controller;

import com.inventorsoft.english.feedback.domain.dto.FeedbackDto;
import com.inventorsoft.english.feedback.service.FeedbackService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackRestController {

    private final FeedbackService feedbackService;

    @PostMapping
    public void sendAnonymousFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        feedbackService.sendAnonymousFeedback(feedbackDto);
    }

    @GetMapping
    public Page<FeedbackDto> getAllFeedBacks(Pageable pageable) {
        return feedbackService.getAllFeedbacks(pageable);
    }
}
