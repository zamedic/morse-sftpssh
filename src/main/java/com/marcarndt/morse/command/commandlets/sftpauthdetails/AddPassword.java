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
public class AddPassword implements Commandlet {

  public static String AddAuthPasswordState = "Add Auth Password";

  public boolean canHandleCommand(Message message, String state) {
    return state.equals(AddUser.AddAuthTypeState) && message.getText().equals(AddUser.password);
  }

  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    morseBot.sendReplyMessage(message, "Password");
  }

  public String getNewState(Message message, String command) {
    return AddAuthPasswordState;
  }

  public List<String> getNewStateParams(Message message, String state, List<String> parameters) {
    return parameters;
  }
}
