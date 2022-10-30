import jebsen.ms.email.models.ThymeleafEmail;
import jebsen.ms.email.sdk.EmailSdk;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.context.Context;

import java.util.List;

@Slf4j
public class Test {
    public static void main(String[] args) {
        var sdk = EmailSdk.builder().domain("https://gitappdev.jebsen.com/emailSender").build();

        var email = ThymeleafEmail.builder()
                .title("Email Template Testing")
                .recipients(List.of("ckwong@jebsen.com", "ck.wong.jebsen@gmail.com"))
                .sentFrom("test@t.est")
                .templatePathPrefix("templates/")
                .templateName("test")
                .isMultiPart(true)
                .isHtml(true)
                .build();

        var ctx = new Context();
        ctx.setVariable("greeting", "Hi!");

        email.setEmailContext(ctx);

        var resp = sdk.thymeleaf(email);

        log.info("Success: {}", resp.getBody().getSuccess());
    }
}
