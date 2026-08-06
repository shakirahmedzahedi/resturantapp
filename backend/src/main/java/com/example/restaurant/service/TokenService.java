package com.example.restaurant.service;

import com.example.restaurant.domain.DailyTokenSequence;
import com.example.restaurant.repository.DailyTokenSequenceRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class TokenService {
    private final DailyTokenSequenceRepository sequences;

    public TokenService(DailyTokenSequenceRepository sequences) {
        this.sequences = sequences;
    }

    public int nextToken(LocalDate date) {
        return sequences.findForUpdate(date)
                .map(DailyTokenSequence::next)
                .orElseGet(() -> createFirstToken(date));
    }

    private int createFirstToken(LocalDate date) {
        try {
            DailyTokenSequence sequence = new DailyTokenSequence(date, 100);
            sequences.saveAndFlush(sequence);
            return 100;
        } catch (DataIntegrityViolationException race) {
            DailyTokenSequence existing = sequences.findForUpdate(date)
                    .orElseThrow(() -> race);
            return existing.next();
        }
    }
}
