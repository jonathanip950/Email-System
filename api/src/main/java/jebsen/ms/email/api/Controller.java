package jebsen.ms.email.api;

import jebsen.ms.email.models.SimpleEmail;
import jebsen.ms.email.models.ThymeleafEmail;
import jebsen.ms.email.sdk.Endpoints;
import jebsen.ms.email.sdk.response.SimpleEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoints.email)
public class Controller {
    @Autowired
    private SimpleEmailService simpleEmailService;

    @Autowired
    private ThymeleafService thymeleafService;

    @CrossOrigin
    @PostMapping(Endpoints.emailSimple)
    public ResponseEntity<SimpleEmailResponse> simpleEmail(@RequestBody SimpleEmail simpleEmail) {
        return ResponseEntity.ok(simpleEmailService.sendEmail(simpleEmail));
    }

    @CrossOrigin
    @PostMapping(Endpoints.thymeleaf)
    public ResponseEntity<SimpleEmailResponse> thymeleaf(@RequestBody ThymeleafEmail.EmailInput emailInput) {
        return ResponseEntity.ok(thymeleafService.sendEmail(emailInput));
    }
}
