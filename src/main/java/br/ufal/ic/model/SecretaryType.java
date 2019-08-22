package br.ufal.ic.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SecretaryType {
    @JsonProperty("Graduation")
    Graduation,
    @JsonProperty("PostGraduation")
    PostGraduation
}
