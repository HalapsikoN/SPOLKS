package by.bsuir.spolks.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClientConfiguration {
    String host;
    Integer portTCP;
    Integer portUDP;
}
