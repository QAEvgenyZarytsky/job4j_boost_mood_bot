package ru.job4j.bmb.services.fakesv;

import org.springframework.stereotype.Component;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.services.SentContent;

import java.util.ArrayList;
import java.util.List;

@Component("sentContentFake")
public class SentContentFake implements SentContent {
    private final List<Content> sentMessages = new ArrayList<>();

    @Override
    public void sent(Content content) {
        sentMessages.add(content);
    }

    public List<Content> getSentMessages() {
        return sentMessages;
    }
}