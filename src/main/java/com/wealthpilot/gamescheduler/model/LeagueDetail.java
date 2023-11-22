package com.wealthpilot.gamescheduler.model;

import java.util.List;

public record LeagueDetail(String league, String country, List<Team> teams) {
}
