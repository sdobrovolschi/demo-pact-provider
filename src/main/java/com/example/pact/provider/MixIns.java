package com.example.pact.provider;

import com.fasterxml.jackson.annotation.JsonValue;

public interface MixIns {

    interface CustomerIdMixIn {

        @JsonValue
        String stringValue();
    }
}
