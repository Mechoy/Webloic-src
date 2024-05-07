package weblogic.management.scripting.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.util.InteractiveInterpreter;
import weblogic.management.scripting.WLST;
import weblogic.utils.StringUtils;

public class WLSTUtilHelper extends PyObject {
   public static boolean disconnected = false;
   public static boolean recordingInProgress = false;
   public static boolean recordAll = false;
   public static boolean easeSyntax = false;
   public static boolean isExitRequested = false;
   public static PyList sysArgs = null;
   public static boolean scriptFileExecuted = false;
   public static String[] argsPassedFromMain = null;
   public static boolean scriptMode = false;
   static PyObject offline_cmo = null;
   static PyObject offline_myps1 = null;
   static WLSTInterpreter theInterpreter = null;
   static boolean wlstAsModule = true;
   static String wlstHome = "";
   private static boolean debugInitEnabled = Boolean.getBoolean(System.getProperty("wlst.debug.init", "false"));
   private static WLSTMsgTextFormatter txtFmt = new WLSTMsgTextFormatter();
   private static String[] easySyntaxCommandsArray = new String[]{"cd", "ls", "prompt", "reset", "debug", "help", "exit", "get"};
   private static String[] easySyntaxNoArgCommandsArray = new String[]{"config", "runtime", "reset", "dumpVariables", "disconnect", "adminConfig", "custom", "serverConfig", "serverRuntime", "domainRuntime", "domainConfig", "easeSyntax", "dumpStack", "debug", "pwd"};
   private static List easySyntaxCommands;
   private static List easySyntaxNoArgCommands;

   public static WLSTInterpreter getWLSTInterpreter() {
      return theInterpreter;
   }

   public static boolean runningWLSTAsModule() {
      return wlstAsModule;
   }

   private static String getWLSTHome() throws Exception {
      return wlstHome;
   }

   static void debugInit(String msg) {
      if (debugInitEnabled) {
         System.out.println("<wlst-init> " + msg);
      }

   }

   static void debugInit(String msg, Throwable t) {
      if (debugInitEnabled) {
         System.out.println("<wlst-init> " + msg);
         t.printStackTrace();
      }

   }

   public static void setProperties(String propertiesFile, InteractiveInterpreter interp) {
      try {
         File file = new File(propertiesFile);
         Properties props = new Properties();
         FileReader freader = new FileReader(file);
         props.load(freader);
         Enumeration enumProps = props.keys();

         while(enumProps.hasMoreElements()) {
            String propertyName = (String)enumProps.nextElement();
            String value = props.getProperty(propertyName);
            interp.set(propertyName, value);
         }

         WLST.interp = (WLSTInterpreter)interp;
      } catch (FileNotFoundException var8) {
         System.out.println(txtFmt.getPropertiesFileNotFound(propertiesFile));
      } catch (IOException var9) {
         System.out.println(txtFmt.getPropertiesFileNotReadable(propertiesFile));
      }

   }

   public static String convertEasySyntax(String code) {
      String[] cmdArgs = StringUtils.split(code, ' ');
      if (cmdArgs.length > 0) {
         String cmd = cmdArgs[0];
         String args = cmdArgs.length > 1 ? cmdArgs[1] : "";
         if (easySyntaxCommands.contains(cmd)) {
            return convertParamSignature(cmd, args);
         }

         if (easySyntaxNoArgCommands.contains(cmd)) {
            return convertNoParamSignature(cmd);
         }
      }

      return code;
   }

   private static String convertNoParamSignature(String cmd) {
      cmd = cmd + "()";
      return cmd;
   }

   private static String convertParamSignature(String cmd, String args) {
      if (args != null && !"".equals(args)) {
         String[] splitArgs = StringUtils.splitCompletely(args, " ");
         return splitArgs.length == 0 ? convertNoParamSignature(cmd) : cmd + "(" + "\"" + splitArgs[0] + "\")";
      } else {
         return convertNoParamSignature(cmd);
      }
   }

   public static String getWLSTTempFile() {
      return getWLSTTempFile("wlstTemp");
   }

   public static String getWLSTTempFile(String wlstTempDirName) {
      boolean debug = false;
      String debugFlag = System.getProperty("wlst.debug.init", "false");
      if (debugFlag.equals("true")) {
         debug = true;
      }

      String tempDir = System.getProperty("java.io.tmpdir");
      debugInit("Temp Dir evaluated to " + tempDir);
      File f1 = new File(tempDir);
      String user = System.getProperty("user.name");
      if (user == null) {
         user = "nouser";
      }

      if (f1.canWrite()) {
         debugInit("Create a temp dir: " + tempDir + "/" + wlstTempDirName + user);
         f1 = new File(tempDir + "/" + wlstTempDirName + user);
         f1.mkdirs();
         if (f1.canWrite()) {
            return f1.getAbsolutePath();
         } else {
            debugInit("Could not write to " + f1 + ", hence we will create another temp file with no username: " + tempDir + "/" + wlstTempDirName + "nouser");
            f1 = new File(tempDir + "/" + wlstTempDirName + "nouser");

            try {
               f1.mkdirs();
               f1.createNewFile();
            } catch (IOException var8) {
               debugInit("IOException occurred: " + var8);
               return f1.getAbsolutePath();
            }

            f1.deleteOnExit();
            return f1.getAbsolutePath();
         }
      } else {
         debugInit("Could not write to " + tempDir + " hence we will create another temp file");
         File f = new File(tempDir + "WLSTTemp" + user);

         try {
            f.mkdirs();
            f.createNewFile();
         } catch (IOException var9) {
            debugInit("IOException occured: " + var9);
            return f.getAbsolutePath();
         }

         f.deleteOnExit();
         return f.getAbsolutePath();
      }
   }

   public static List getWLSTModules() {
      List modules = new ArrayList();

      try {
         String home = getWLSTHome();
         File wlstHome = new File(home);
         File[] files = wlstHome.listFiles();

         for(int i = 0; i < files.length; ++i) {
            if (!files[i].isDirectory() && files[i].getName().endsWith(".py")) {
               modules.add(files[i]);
            }
         }

         home = System.getProperty("weblogic.wlstHome", "");
         if (home.length() != 0) {
            String[] homes = home.trim().split(File.pathSeparator);

            for(int i = 0; i < homes.length; ++i) {
               if (!StringUtils.isEmptyString(homes[i])) {
                  File _wlstHome = new File(homes[i]);
                  File[] _files = _wlstHome.listFiles();

                  for(int j = 0; j < _files.length; ++j) {
                     if (!_files[j].isDirectory() && _files[j].getName().endsWith(".py")) {
                        modules.add(_files[j]);
                     }
                  }
               }
            }
         }
      } catch (Throwable var9) {
      }

      return modules;
   }

   public static void startProcess(Process process, String processName, boolean drain) {
      WLSTProcess.startIOThreads(process, processName, drain);
   }

   public static void startProcess(Process process, String processName, boolean drain, String serverLog) {
      WLSTProcess.startIOThreads(process, processName, drain, serverLog);
   }

   public static void destroyWLSTProcesses(String processName) {
      WLSTProcess.destroyProcesses(processName);
   }

   public static Process getWLSTProcess(String processName) {
      return WLSTProcess.getProcess(processName);
   }

   static {
      easySyntaxCommands = Arrays.asList(easySyntaxCommandsArray);
      easySyntaxNoArgCommands = Arrays.asList(easySyntaxNoArgCommandsArray);
   }
}
