import jebsen.ms.email.models.SimpleEmail;
import jebsen.ms.email.sdk.EmailSdk;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ApiTest {

    public static void main(String[] args) {
        var sdk = EmailSdk.builder().domain("http://localhost:8080").build();

        var result = sdk.simpleEmail(SimpleEmail.builder()
                .sentFrom("test@test.com")
                .title("Testing Title")
                .recipients(List.of("ckwong@jebsen.com", "justinyuen@jebsen.com"))
                .contentRows(List.of("row1: content1", "row2: content2"))
                .build());

        log.info("Result: {}", result);
    }
}
