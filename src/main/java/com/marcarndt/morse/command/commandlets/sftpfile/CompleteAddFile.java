package com.marcarndt.morse.command.commandlets.sftpfile;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.service.SSHService;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/05/05.
 */
@Stateless
public class CompleteAddFile implements Commandlet {

  @Inject
  SSHService sshService;

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(AddFileDetails.AddSftpFileDetailsState);
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    String description = parameters.get(0);
    String path = message.getText();
    sshService.addFile(description, path);
    morseBot.sendMessage("Added file " + description + " with path " + path,
        message.getChatId().toString());
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
