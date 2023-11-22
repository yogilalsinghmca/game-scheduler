package com.wealthpilot.gamescheduler.config;

import com.fasterxml.jackson.databind.*;
import com.wealthpilot.gamescheduler.model.LeagueDetail;
import com.wealthpilot.gamescheduler.model.Team;
import java.io.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.io.*;

@Configuration
public class GameConfig {
    private static final Logger logger = LoggerFactory.getLogger(GameConfig.class);
    @Value("classpath:${teamsDetailsFileName}")
    Resource teamsDetailsFileName;

    @Bean
    public List<Team> teams() {
        List<Team> teams;
        LeagueDetail leagueDetails;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File leagueDetailsJsonFile = teamsDetailsFileName.getFile();
            leagueDetails = objectMapper.readValue(leagueDetailsJsonFile, LeagueDetail.class);
            teams = leagueDetails.teams();
        } catch (IOException e) {
            logger.error("Error reading teams details from the file: {} - {}", teamsDetailsFileName, e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return teams;
    }
}
