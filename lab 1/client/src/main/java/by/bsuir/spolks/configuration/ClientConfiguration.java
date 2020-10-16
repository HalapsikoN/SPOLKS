package by.bsuir.spolks.configuration;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClientConfiguration {
    String host;
    Integer port;
}
