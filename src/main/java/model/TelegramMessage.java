package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelegramMessage {
    private SendMessage message;
    private SendPhoto photo;
}
