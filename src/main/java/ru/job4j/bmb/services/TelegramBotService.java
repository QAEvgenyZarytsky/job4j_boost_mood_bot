package ru.job4j.bmb.services;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.condition.RealModeCondition;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

@Service
@Conditional(RealModeCondition.class)
public class TelegramBotService extends TelegramLongPollingBot implements BeanNameAware, ApplicationContextAware,
        SentContent {

    private ApplicationContext applicationContext;
    private final String botName;
    private final String botToken;
    private final UserRepository userRepository;
    private final KeyboardBuilderComponent keyboardBuilder;
    private final TelegramSenderComponent telegramSender;

    public TelegramBotService(@Value("${telegram.bot.name}") String botName,
                              @Value("${telegram.bot.token}") String botToken,
                              UserRepository userRepository,
                              KeyboardBuilderComponent keyboardBuilder,
                              TelegramSenderComponent telegramSender) {
        this.botName = botName;
        this.botToken = botToken;
        this.userRepository = userRepository;
        this.keyboardBuilder = keyboardBuilder;
        this.telegramSender = telegramSender;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            if ("/start".equals(message.getText())) {
                long chatId = message.getChatId();
                var user = new User();
                user.setClientId(message.getFrom().getId());
                user.setChatId(chatId);
                userRepository.save(user);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Как настроение сегодня?");
                sendMessage.setReplyMarkup(keyboardBuilder.buildMoodKeyboard());

                telegramSender.send(sendMessage);
            }
        }
    }

    public void receive(Content content) {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Имя бина: " + name);
    }

    public void displayAllBeanNames() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beaName : beanNames) {
            System.out.println(beaName);
        }
    }

    @Override
    public void sent(Content content) {
        try {
            if (content.getAudio() != null) {
                SendAudio sendAudio = new SendAudio();
                sendAudio.setChatId(content.getChatId().toString());
                sendAudio.setAudio(content.getAudio());
                if (content.getText() != null && !content.getText().isEmpty()) {
                    sendAudio.setCaption(content.getText());
                }
                execute(sendAudio);
            }

            if (content.getPhoto() != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(content.getChatId().toString());
                sendPhoto.setPhoto(content.getPhoto());
                if (content.getText() != null && !content.getText().isEmpty()) {
                    sendPhoto.setCaption(content.getText());
                }
                execute(sendPhoto);
            }

            if (content.getText() != null && !content.getText().isEmpty()) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(content.getChatId().toString());
                sendMessage.setText(content.getText());

                if (content.getMarkup() != null) {
                    sendMessage.setReplyMarkup(content.getMarkup());
                }

                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            throw new SentContentException("Failed to send content", e);
        }
    }
}
