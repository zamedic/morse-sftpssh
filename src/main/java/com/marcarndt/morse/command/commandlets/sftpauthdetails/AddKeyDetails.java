package com.marcarndt.morse.command.commandlets.sftpauthdetails;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.MorseBotException;
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
public class AddKeyDetails implements Commandlet {

  @Inject
  SSHService sshService;

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(AddKey.AddAuthKeyState);
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    String keyName = message.getText();
    String name = parameters.get(0);
    String username = parameters.get(1);

    try {
      sshService.addKeyAuth(name, username, keyName);
      morseBot.sendMessage("Added key auth " + name + " user " + username + " keyfile " + keyName,
          message.getChatId().toString());
    } catch (MorseBotException e) {
      morseBot.sendMessage(e.getMessage(), message.getChatId().toString());
    }

  }

  @Override
  public String getNewState(Message message, String command) {
    return null;
  }

  @Override
  public List<String> getNewStateParams(Message message, String state, List<String> parameters) {
    return null;
  }
}
