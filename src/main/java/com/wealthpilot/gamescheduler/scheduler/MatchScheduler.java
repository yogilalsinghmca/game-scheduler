package com.wealthpilot.gamescheduler.scheduler;

import com.wealthpilot.gamescheduler.model.Team;
import com.wealthpilot.gamescheduler.model.Fixture;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class MatchScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MatchScheduler.class);
    private List<Team> teams;
    @Value("${leagueStartDate}")
    private LocalDateTime leagueStartDate;


    public MatchScheduler(List<Team> teams) {
        this.teams = teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Fixture> generateCompleteSchedule() {
        // First leg
        boolean secondLeg = false;
        List<Fixture> fixtureList = generateLegSchedule(leagueStartDate, secondLeg);
        // Add a 3-week break
        int breakInWeeks = 3;
        LocalDateTime secondLeagueStartDate = leagueStartDate.plusWeeks(breakInWeeks);
        // Second leg with swapped teams
        secondLeg = true;
        List<Fixture> secondLegFixtures = generateLegSchedule(secondLeagueStartDate, secondLeg);
        fixtureList.addAll(secondLegFixtures);

        return fixtureList;
    }
private List<Fixture> generateLegSchedule(LocalDateTime startDate, boolean secondLeg) {
    int teamSize = teams.size();
    if (teamSize < 2 || teamSize % 2 != 0) {
        logger.error("Teams must be even and greater than 2");
        throw new RuntimeException("Teams must be even and greater than 2");
    }
    List<Fixture> legSchedule = new ArrayList<>();
    List<Team> rotatingTeams = teams.subList(1, teamSize);
    LocalDateTime fixtureDateTime =  findNextMatchDay(startDate);
    Team homeTeam;
    Team awayTeam;
    for (int round = 1; round < teamSize; round++) {
        List<Team> fixedTeam = new ArrayList<>(teams.subList(0, 1));
        fixedTeam.addAll(rotatingTeams);

        for (int i = 0; i < teamSize / 2; i++) {
            if(secondLeg) {
                homeTeam = fixedTeam.get(teamSize - 1 - i);
                awayTeam = fixedTeam.get(i);
            } else {
                homeTeam = fixedTeam.get(i);
                awayTeam = fixedTeam.get(teamSize - 1 - i);
            }

            Fixture fixture = new Fixture(fixtureDateTime, homeTeam, awayTeam);
            legSchedule.add(fixture);
            fixtureDateTime = LocalDateTime.from(fixtureDateTime.plusDays(7));
        }

        Collections.rotate(rotatingTeams, +1);
    }

    return legSchedule;
}
    private  LocalDateTime findNextMatchDay(LocalDateTime startDate) {
        LocalDateTime currentDateTime = startDate;

        while (currentDateTime.getDayOfWeek() != DayOfWeek.SATURDAY) {
            currentDateTime = currentDateTime.plusDays(1);
        }
        // Set match start time to 5 PM
        currentDateTime = currentDateTime.withHour(17).withMinute(0).withSecond(0);
        return currentDateTime;
    }
}

