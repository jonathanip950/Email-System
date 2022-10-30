package jebsen.ms.email.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import jebsen.ms.email.models.SimpleEmail;
import jebsen.ms.email.models.ThymeleafEmail;
import jebsen.ms.email.sdk.response.SimpleEmailResponse;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Collections;

@Slf4j
public class EmailSdk {
    private String domain;

    @Builder
    public EmailSdk(String domain) {
        this.domain = domain;
    }

    private HttpHeaders getHttpHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    @SneakyThrows
    private <T> ResponseEntity<T> getRestTemplate(String endpoints, HttpMethod httpMethod, Object body, Class<T> responseType) {
        var httpEntity = new HttpEntity<>(new ObjectMapper().writeValueAsString(body), getHttpHeaders());
        var url = String.format("%s/%s", domain, endpoints);
        log.info("Calling Email Service to send email, URL: {}", url);
        return new RestTemplate().exchange(url, httpMethod, httpEntity, responseType);
    }

    public ResponseEntity<SimpleEmailResponse> simpleEmail(SimpleEmail simpleEmail) {
        return getRestTemplate(String.format("%s/%s", Endpoints.email, Endpoints.emailSimple), HttpMethod.POST, simpleEmail, SimpleEmailResponse.class);
    }

    public ResponseEntity<SimpleEmailResponse> thymeleaf(ThymeleafEmail thymeleafEmail) {
        var resolver = getTemplateResolver(thymeleafEmail);
        var engine = getTemplateEngine(resolver);
        var htmlStr = engine.process(thymeleafEmail.getTemplateName(), thymeleafEmail.getEmailContext());
        thymeleafEmail.setEmailContext(null);
        var input = ThymeleafEmail.EmailInput.builder()
                .encoding(resolver.getCharacterEncoding())
                .htmlStr(htmlStr)
                .thymeleafEmail(thymeleafEmail)
                .isMultiPart(thymeleafEmail.isMultiPart())
                .isHtml(thymeleafEmail.isHtml())
                .build();
        return getRestTemplate(String.format("%s/%s", Endpoints.email, Endpoints.thymeleaf), HttpMethod.POST, input, SimpleEmailResponse.class);
    }

    private ClassLoaderTemplateResolver getTemplateResolver(ThymeleafEmail thymeleafEmail) {
        var templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(1);
        templateResolver.setPrefix(thymeleafEmail.getTemplatePathPrefix());
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    private SpringTemplateEngine getTemplateEngine(ITemplateResolver templateResolver) {
        var templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(templateResolver);
        return templateEngine;
    }
}
