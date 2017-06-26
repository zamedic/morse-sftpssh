package com.marcarndt.morse.command.commandlets.sshcommands;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.command.SSHCommand;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Created by arndt on 2017/05/04.
 */
@Stateless
public class AddCommand implements Commandlet {

  public static String AddSshFileCOmmandName = "Add SSH Command Name";

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(SSHCommand.SSHCommandConfigureStage) && message.getText()
        .equals(SSHCommand.add);
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    morseBot.sendReplyMessage(message, "Command Name");
  }

  @Override
  public String getNewState(Message message, String command) {
    return AddSshFileCOmmandName;
  }

  @Override
  public List<String> getNewStateParams(Message message, String state, List<String> parameters) {
    return null;
  }
}
