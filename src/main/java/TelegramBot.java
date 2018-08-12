import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.bots.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class TelegramBot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegaBot = new TelegramBotsApi();
        try{
            telegaBot.registerBot(new TelegramBot());
        }
        catch(TelegramApiRequestException ex){
            ex.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage){

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyBoardFirstRow = new KeyboardRow();

        keyBoardFirstRow.add(new KeyboardButton("/help"));
        keyBoardFirstRow.add(new KeyboardButton("/settings"));

        keyboardRowList.add(keyBoardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try{
            setButtons(sendMessage);
            execute(sendMessage);
        }
        catch (TelegramApiException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        //585461266:AAEW66lI3peGrVjY5DLQ7zhXncBH5cuLZVQ  - WeatherBot
        return "630089224:AAE_WnlABNJlkcwXDyE2W7iXfTxINZrGEOM";
    }

    public void onUpdateReceived(Update update) {

        Model model = new Model();

        Message message = update.getMessage();
        if(message != null && message.hasText()){
            String s = message.getText();
            if ("/start".equals(s)) {
                sendMsg(message, "Hello!");

            } else if ("/settings".equals(s)) {
                sendMsg(message, "What should I set?");
            }
            else if ("/help".equals(s)) {
                sendMsg(message, "How can I help you?");
            }
            else {
                try{
                    sendMsg(message, Weather.getWeather(message.getText(), model));
                }
                catch (IOException ex){
                    sendMsg(message, "Такой город не найден.");
                }
            }
        }
    }

    public String getBotUsername() {
        return "GrandMasterBot_bot";
    }
}