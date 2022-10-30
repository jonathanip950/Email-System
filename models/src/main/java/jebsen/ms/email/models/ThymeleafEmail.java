package jebsen.ms.email.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.thymeleaf.context.Context;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class ThymeleafEmail {
    protected String sentFrom;
    protected List<String> recipients;
    protected String title;
    protected String templatePathPrefix;
    protected String templateName;
    protected Context emailContext;
    protected boolean isMultiPart;
    protected boolean isHtml;
    protected List<String> bcc;

    @Builder
    @Data
    public static class EmailInput {
        protected boolean isHtml;
        ThymeleafEmail thymeleafEmail;
        String htmlStr;
        String encoding;
        boolean isMultiPart;
    }
}
