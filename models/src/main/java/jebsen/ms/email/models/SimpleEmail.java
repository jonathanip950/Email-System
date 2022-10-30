package jebsen.ms.email.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class SimpleEmail {
    protected String sentFrom;
    protected List<String> recipients;
    protected String title;
    protected List<String> contentRows;
    protected int lineBreakSize;

    public void pushContentRow(String content, Object details) {
        var detailStr = "";
        if (ObjectUtils.isEmpty(getContentRows())) {
            setContentRows(new ArrayList<>());
        }

        var objectMapper = new ObjectMapper();
        try {
            detailStr = objectMapper.writeValueAsString(details);
        } catch (Exception e1) {
            try {
                detailStr = (String) details;
            } catch (Exception e2) {
                detailStr = "<###>";
            }
        }

        getContentRows().add(content + ": " + detailStr);
    }
}
