package com.marcarndt.morse.command.commandlets.sftpauthdetails;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.command.ConfigureAuthCommand;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Created by arndt on 2017/05/04.
 */
@Stateless
public class AddAuth implements Commandlet {

  public static String AddAuthState = "Add Auth State";

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(ConfigureAuthCommand.ConfigAuthState) && message.getText()
        .equals(ConfigureAuthCommand.addAuth);
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {

    morseBot.sendReplyMessage(message, "Please enter a name to describe the auth method");

  }

  @Override
  public String getNewState(Message message, String state) {
    return AddAuthState;
  }

  @Override
  public List<String> getNewStateParams(Message message, String state, List<String> parameters) {
    return null;
  }
}
