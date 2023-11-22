package com.wealthpilot.gamescheduler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
public record Team(String name, @JsonProperty("founding_date") String foundingDate) {
}
