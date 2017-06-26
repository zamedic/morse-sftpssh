package com.marcarndt.morse.command.commandlets.sftpauthdetails;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.Arrays;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Created by arndt on 2017/05/04.
 */
@Stateless
public class AddAuthName implements Commandlet {

  public static String AddAuthUserName = "Add Auth User Name";

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(AddAuth.AddAuthState);
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    morseBot.sendReplyMessage(message, "Username");

  }

  @Override
  public String getNewState(Message message, String command) {
    return AddAuthUserName;
  }

  @Override
  public List<String> getNewStateParams(Message message, String state, List<String> parameters) {
    return Arrays.asList(message.getText());
  }
}
