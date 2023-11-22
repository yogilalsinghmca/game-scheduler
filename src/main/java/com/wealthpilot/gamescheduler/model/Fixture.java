package com.wealthpilot.gamescheduler.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Fixture(LocalDateTime startDateTime, Team homeTeam, Team awayTeam) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public String toString() {
        return String.format("%s %s vs %s", DATE_FORMATTER.format(startDateTime), homeTeam.name(), awayTeam.name());
    }
}
