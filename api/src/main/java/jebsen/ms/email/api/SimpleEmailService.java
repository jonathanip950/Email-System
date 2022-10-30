package jebsen.ms.email.api;

import jebsen.ms.email.models.SimpleEmail;
import jebsen.ms.email.sdk.response.SimpleEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SimpleEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private String emailType = "simple";

    public <Email extends SimpleEmail> SimpleEmailResponse sendEmail(Email email) {
        var result = SimpleEmailResponse.builder().success(false).build();

        var sentFrom = email.getSentFrom();
        var recipients = email.getRecipients();
        var title = email.getTitle();
        var contentRows = email.getContentRows();

        if (StringUtils.isBlank(sentFrom) || StringUtils.isBlank(title) || ObjectUtils.isEmpty(recipients) || ObjectUtils.isEmpty(contentRows)) {
            log.warn("E-mail ({}) with title \"{}\" has no either < sentFrom | title | recipients | content >, not sending!", emailType, title);
            result.setMsg(String.format("E-mail (%s) with title %s has no either < sentFrom | title | recipients | content >, not sending", emailType, title));
            return result;
        }

        try {
            log.info("Sending E-mail ({}), title: {}", emailType, title);
            var msg = new SimpleMailMessage();
            msg.setTo(recipients.toArray(String[]::new));
            msg.setFrom(sentFrom);
            msg.setSubject(title);

            var content = "";
            for (var row : contentRows) {
                if (StringUtils.isNotBlank(row)) {
                    content = String.format("%s%s%s", content, StringUtils.isNotBlank(content) ? StringUtils.repeat("\n", email.getLineBreakSize()) : "", row);
                }
            }
            msg.setText(content);

            javaMailSender.send(msg);

            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to send E-mail ({}) with title \"{}\"", emailType, title, e);
            result.setMsg(String.format("Failed to send E-mail (%s) with title \"%s\" - %s", emailType, title, e));
        }

        return result;
    }
}
