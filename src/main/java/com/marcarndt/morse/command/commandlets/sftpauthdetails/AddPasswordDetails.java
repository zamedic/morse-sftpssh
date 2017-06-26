package com.marcarndt.morse.command.commandlets.sftpauthdetails;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.service.SSHService;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/05/04.
 */
@Stateless
public class AddPasswordDetails implements Commandlet {

  @Inject
  SSHService sshService;

  public boolean canHandleCommand(Message message, String state) {
    return state.equals(AddPassword.AddAuthPasswordState);
  }

  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    String password = message.getText();
    String name = parameters.get(0);
    String username = parameters.get(1);

    sshService.addPassword(name, username, password);
    morseBot
        .sendMessage("Added auth " + name + " with user " + username + " and password " + password,
            message.getChatId().toString());
  }


  public String getNewState(Message message, String command) {
    return null;
  }


  public List<String> getNewStateParams(Message message, String state, List<String> parameters) {
    return null;
  }
}
