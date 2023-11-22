package com.wealthpilot.gamescheduler;

import com.wealthpilot.gamescheduler.model.Fixture;
import com.wealthpilot.gamescheduler.scheduler.MatchScheduler;
import java.util.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext app = SpringApplication.run(Application.class, args);
		MatchScheduler matchScheduler = app.getBean(MatchScheduler.class);
		List<Fixture> fixtures = matchScheduler.generateCompleteSchedule();
		fixtures.forEach(System.out::println);
	}
}
