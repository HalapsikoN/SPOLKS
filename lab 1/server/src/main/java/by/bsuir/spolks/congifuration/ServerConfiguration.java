package by.bsuir.spolks.congifuration;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ServerConfiguration {

    String host;
    Integer port;
    @Builder.Default
    Integer maxClientNumber = 5;
}
