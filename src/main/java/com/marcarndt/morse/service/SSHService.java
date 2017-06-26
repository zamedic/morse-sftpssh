package com.marcarndt.morse.service;

import com.marcarndt.morse.MorseBotException;
import com.marcarndt.morse.data.AuthDetails;
import com.marcarndt.morse.data.AuthDetails.AuthTypes;
import com.marcarndt.morse.data.Command;
import com.marcarndt.morse.data.SftpFile;
import com.marcarndt.morse.dto.SCPResponse;
import com.marcarndt.morse.dto.SSHResponse;
import com.marcarndt.morse.utils.ExceptionUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/10.
 */
@Stateless
public class SSHService {

  @Inject
  MongoService mongoService;

  private String sshNoKey = "-oStrictHostKeyChecking=no";
  private String noKnownHostFile = "-oUserKnownHostsFile=/dev/null";
  private static Logger LOG = Logger.getLogger(SSHService.class.getName());

  public void addKeyAuth(String name, String user, String keyfile) throws MorseBotException {
    File file = new File(keyfile);
    if (!file.exists()) {
      throw new MorseBotException("Could not find key: " + keyfile);
    }

    AuthDetails authDetails = new AuthDetails(name, user, AuthTypes.KEY, keyfile);
    mongoService.getDatastore().save(authDetails);

  }

  public void addPassword(String name, String user, String password) {
    AuthDetails authDetails = new AuthDetails(name, user, AuthTypes.PASSWORD, password);
    mongoService.getDatastore().save(authDetails);
  }

  public void addCommand(String name, String command) {
    Command commandOnbject = new Command(name, command);
    mongoService.getDatastore().save(commandOnbject);
  }

  public List<String> getCommandNames() {
    return mongoService.getDatastore().createQuery(Command.class).asList().stream()
        .map(command -> command.getDescription()).collect(
            Collectors.toList());
  }

  public void addFile(String description, String filePath) {
    SftpFile chefSftpFile = new SftpFile(description, filePath);
    mongoService.getDatastore().save(chefSftpFile);
  }

  public List<String> getFileDescriptions() {
    return mongoService.getDatastore().createQuery(SftpFile.class).asList().stream()
        .map(chefFile -> chefFile.getDescription()).collect(
            Collectors.toList());
  }

  public SftpFile getFile(String fileDescription) {
    return mongoService.getDatastore().createQuery(SftpFile.class).field("description")
        .equal(fileDescription).get();
  }

  public void deleteFile(String file) {
    mongoService.getDatastore().delete(getFile(file));
  }


  public SCPResponse fetchFile(String address, String sourceFilename) {
    StringBuilder stringBuilder = new StringBuilder();
    LOG.info("Fetching" + sourceFilename + "for " + address);
    stringBuilder.append("Fetching").append(sourceFilename).append("for ").append(address)
        .append("\n");
    String filename = "/tmp/" + address + ".log";
    String sudoCommand = "chmod a+r " + sourceFilename;
      SSHResponse response = performSSH(sudoCommand, address, stringBuilder);

    if (!response.isSuccessful()) {
      return new SCPResponse(false, stringBuilder.toString(), null);
    }

    SCPResponse scpResponse = downloadFile(address, filename, sourceFilename);
    scpResponse.setLog(stringBuilder.append(scpResponse.getLog()).toString());
    return scpResponse;
  }

  public SSHResponse runSSHCommand(String address, String command) {
    Command commandObject = mongoService.getDatastore().createQuery(Command.class)
        .field("description").equal(command).get();

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Executing command ").append(command).append(" on dto ").append(address)
        .append("\n");

    SSHResponse response = performSSH(commandObject.getCommand(), address,
        stringBuilder);
    if (response.isSuccessful()) {
      return response;
    }
    return new SSHResponse(false, stringBuilder.toString());
  }

  private SSHResponse performSSH(String command, String ipaddress, StringBuilder stringBuilder) {
    List<AuthDetails> authDetailss = mongoService.getDatastore().createQuery(AuthDetails.class)
        .asList();
    for (AuthDetails authDetails : authDetailss) {
      SSHResponse sshResponse = null;
      switch (authDetails.getAuthTypes()) {
        case KEY:
          sshResponse = keySshCommand(ipaddress, command,
              stringBuilder, authDetails.getDetails(),
              authDetails.getUsername());
          break;
        case PASSWORD:
          sshResponse = passwordSSH(ipaddress, command, stringBuilder,
              authDetails.getDetails(),
              authDetails.getUsername());
          break;
      }
      stringBuilder.append(sshResponse.getLog()).append("\n");
      if (sshResponse.isSuccessful()) {
        return sshResponse;
      }
    }
    return new SSHResponse(false, "");
  }

  private SSHResponse keySshCommand(String nodeName, String command, StringBuilder stringBuilder,
      String sshkeyfile, String sshUser) {
    String[] keyCommand =
        {"ssh", "-i", sshkeyfile, "-t", sshNoKey, noKnownHostFile, sshUser + "@"
            + nodeName, "sudo " + command};
    stringBuilder.append("Executing ssh command using key ").append(Arrays.toString(keyCommand));
    SSHResponse sshResponse = exec(keyCommand, stringBuilder);
    stringBuilder.append(sshResponse.getLog());
    return sshResponse;
  }

  private SSHResponse passwordSSH(String nodeName, String command, StringBuilder stringBuilder,
      String password, String user) {
    String[] commandNoKey = {"sshpass", "-p", password, "ssh", "-t", sshNoKey, noKnownHostFile,
        user + "@" + nodeName, command};
    stringBuilder.append("Executing ssh command using password: ").append(
        "ssh " + "-t" + " " + sshNoKey + " " + noKnownHostFile + " " + user + "@" + nodeName + " "
            + command).append("\n");
    return exec(commandNoKey, stringBuilder);
  }


  private SCPResponse downloadFile(String nodeName, String filename, String source) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Downloading ").append(source).append(" from ").append(nodeName)
        .append(" to ").append(filename).append("\n");
    File tmpFile = new File(filename);
    if (tmpFile.exists()) {
      stringBuilder.append("Found file ").append(filename).append(" on local dto. Deleting it");
      tmpFile.delete();
    }

    List<AuthDetails> authDetailss = mongoService.getDatastore().createQuery(AuthDetails.class)
        .asList();
    for (AuthDetails authDetails : authDetailss) {
      SSHResponse sshResponse = null;
      switch (authDetails.getAuthTypes()) {
        case KEY:
          sshResponse = keySCP(nodeName, source, filename, authDetails.getDetails(),
              authDetails.getUsername());
          break;
        case PASSWORD:
          sshResponse = passwordSCP(nodeName, source, filename, authDetails.getDetails(),
              authDetails.getUsername());
          break;
      }
      stringBuilder.append(sshResponse.getLog()).append("\n");
      if (sshResponse.isSuccessful()) {
        return new SCPResponse(true, stringBuilder.toString(), filename);
      }
    }
    return new SCPResponse(false, stringBuilder.toString(), null);

  }

  private SSHResponse keySCP(String nodeName, String source, String target, String sshKeyFile,
      String sshKeyUser) {
    StringBuilder stringBuilder = new StringBuilder();
    String command = "scp -i " + sshKeyFile
        + " " + sshNoKey + " " + noKnownHostFile + " " + sshKeyUser + "@" + nodeName
        + ":" + source + " " + target;
    stringBuilder.append(command).append("\n");
    return exec(command, stringBuilder);
  }


  private SSHResponse passwordSCP(String nodeName, String source, String target, String sshPassword,
      String sshUserName) {
    StringBuilder stringBuilder = new StringBuilder();
    String[] command = {"sshpass", "-p", sshPassword, "scp",
        sshNoKey, noKnownHostFile, sshUserName + "@" + nodeName
        + ":" + source, target};

    stringBuilder
        .append("scp" + sshNoKey + " " + noKnownHostFile + " " + sshUserName + "@" + nodeName
            + ":" + source + " " + target).append("\n");

    return exec(command, stringBuilder);
  }

  private SSHResponse exec(String[] command, StringBuilder stringBuilder) {
    try {
      LOG.info("Executing: " + Arrays.toString(command));
      Process process = Runtime.getRuntime().exec(command);
      SSHResponse response = handleProcess(process);
      response.setLog(stringBuilder.append(response.getLog().toString()).toString());
      return response;
    } catch (IOException e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
      stringBuilder.append("Exception caught attempting to execute. ").append("\n");
      stringBuilder.append(ExceptionUtils.exceptionStacktraceToString(e));
      return new SSHResponse(false, stringBuilder.toString());
    }
  }

  private SSHResponse exec(String command, StringBuilder stringBuilder) {
    stringBuilder.append("Executing ssh command ").append(command).append("\n");
    try {
      LOG.info("Executing: " + command);
      Process process = Runtime.getRuntime().exec(command);
      SSHResponse response = handleProcess(process);
      if (response.isSuccessful()) {
        return response;
      }
      response.setLog(stringBuilder.append(response.getLog().toString()).toString());
      return response;
    } catch (IOException e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
      stringBuilder.append("Exception caught executing command").append("\n")
          .append(ExceptionUtils.exceptionStacktraceToString(e));
      return new SSHResponse(false, stringBuilder.toString());
    }
  }


  private SSHResponse handleProcess(Process process) {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      process.waitFor();
    } catch (InterruptedException e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
      stringBuilder.append("Caught Exception when executing process. \n");
      stringBuilder.append(ExceptionUtils.exceptionStacktraceToString(e)).append("\n");
      return new SSHResponse(false, stringBuilder.toString());
    }
    String error = processStream(process.getErrorStream());
    String info = processStream(process.getInputStream());
    LOG.info("Error: " + error);
    LOG.info("Output: " + info);
    if (process.exitValue() == 0) {
      return new SSHResponse(true, info + " " + error);
    } else {
      stringBuilder.append("Error Output").append("\n").append(error).append("\n");
      return new SSHResponse(false, stringBuilder.toString());
    }


  }

  private String processStream(InputStream inputStream) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder stringBuilder = new StringBuilder();
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stringBuilder.toString();

  }

  private boolean verifyHost(String nodeName) {
    return true;
  }


}
