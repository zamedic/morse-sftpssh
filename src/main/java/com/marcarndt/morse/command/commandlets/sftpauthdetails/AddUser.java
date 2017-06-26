package com.marcarndt.morse.command.commandlets.sftpauthdetails;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Created by arndt on 2017/05/04.
 */
@Stateless
public class AddUser implements Commandlet {

  public static String key = "Key Based";
  public static String password = "Password Based";

  public static String AddAuthTypeState = "Add Auth Type";

  public boolean canHandleCommand(Message message, String state) {
    return state.equals(AddAuthName.AddAuthUserName);
  }

  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    morseBot.sendReplyKeyboardMessage(message, "Auth type", key, password);
  }

  public String getNewState(Message message, String command) {
    return AddAuthTypeState;
  }

  public List<String> getNewStateParams(Message message, String state, List<String> parameters) {
    parameters.add(message.getText());
    return parameters;
  }
}
