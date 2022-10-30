package jebsen.ms.email.api;

import jebsen.ms.email.models.ThymeleafEmail;
import jebsen.ms.email.sdk.response.SimpleEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ThymeleafService {

    @Autowired
    private JavaMailSender javaMailSender;

    public SimpleEmailResponse sendEmail(ThymeleafEmail.EmailInput emailInput) {
        var result = SimpleEmailResponse.builder().success(false).build();
        try {
            var mimeMsg = javaMailSender.createMimeMessage();
            var msg = new MimeMessageHelper(mimeMsg, emailInput.isMultiPart(), emailInput.getEncoding());
            msg.setFrom(emailInput.getThymeleafEmail().getSentFrom());
            msg.setTo(emailInput.getThymeleafEmail().getRecipients().toArray(String[]::new));
            msg.setSubject(emailInput.getThymeleafEmail().getTitle());
            msg.setText(emailInput.getHtmlStr(), emailInput.isHtml());
            msg.setBcc(emailInput.getThymeleafEmail().getBcc().toArray(String[]::new));
            javaMailSender.send(mimeMsg);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to send email with subject: {} ; FROM: {} ; TO: {}",
                    emailInput.getThymeleafEmail().getTitle(),
                    emailInput.getThymeleafEmail().getSentFrom(),
                    emailInput.getThymeleafEmail().getRecipients(), e);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
