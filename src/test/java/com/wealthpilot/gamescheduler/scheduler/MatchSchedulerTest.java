package com.wealthpilot.gamescheduler.scheduler;

import com.wealthpilot.gamescheduler.model.Fixture;
import com.wealthpilot.gamescheduler.model.Team;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MatchSchedulerTest {
    private final List<Team> teams = new ArrayList<>();

    @Autowired
    private MatchScheduler matchScheduler;

    @BeforeEach
    public void setup() {
        Team erzgebirgeAue = new Team("Erzgebirge Aue", null);
        Team volksbank = new Team("Volksbank Kickers", "1998");
        Team kreutherFürth = new Team("Kreuther Fürth", "unknown");
        Team borussiaHelvetia = new Team("Borussia Helvetia", "2001");
        teams.add(erzgebirgeAue);
        teams.add(volksbank);
        teams.add(kreutherFürth);
        teams.add(borussiaHelvetia);
    }

    @DisplayName("Should generate Match schedule")
    @Test
    public void shouldMatchFixtureAndReturnIt() {
        matchScheduler.setTeams(teams);
        int expectedFixtureCount = 12;
        List<Fixture> fixtures = matchScheduler.generateCompleteSchedule();

        assertNotNull(fixtures);
        assertEquals(expectedFixtureCount, fixtures.size());
        Fixture fixture = fixtures.get(0);
        assertEquals( DayOfWeek.SATURDAY, fixture.startDateTime().getDayOfWeek());
    }

    @DisplayName("Should fail to generate fixture when team count is odd")
    @Test
    public void shouldThrowExceptionWhenTeamsCountNotEven() {
        teams.add(new Team("randomTeam", "2300"));
        matchScheduler.setTeams(teams);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            matchScheduler.generateCompleteSchedule();
        });

        String expectedMessage = "Teams must be even and greater than 2";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Should fail to generate fixture when team count is 1")
    @Test
    public void shouldThrowExceptionWhenTeamsCountIsOne() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team("randomTeam", "2300"));
        matchScheduler.setTeams(teams);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            matchScheduler.generateCompleteSchedule();
        });

        String expectedMessage = "Teams must be even and greater than 2";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
