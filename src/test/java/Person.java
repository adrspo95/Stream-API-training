import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Person {

    private String name;
    private final String identifier = UUID.randomUUID().toString();
}
