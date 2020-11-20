package by.bsuir.spolks.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ServerConfiguration {

    String host;
    Integer portTCP;
    Integer portUDP;
    @Builder.Default
    Integer maxClientNumber = 5;
}
