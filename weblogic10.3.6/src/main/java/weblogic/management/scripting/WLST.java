package weblogic.management.scripting;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyInstance;
import org.python.core.PyInteger;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.__builtin__;
import org.python.util.InteractiveInterpreter;
import weblogic.management.scripting.plugin.WLSTPluginsList;
import weblogic.management.scripting.utils.WLSTInterpreter;
import weblogic.management.scripting.utils.WLSTMsgTextFormatter;
import weblogic.management.scripting.utils.WLSTUtil;
import weblogic.utils.StringUtils;

public class WLST extends WLSTUtil {
   private static String PYTHON_VERBOSE = "python.verbose";
   private static String PYTHON_WARNING = "warning";
   private static String PYTHON_CACHEDIR_SKIP = "python.cachedir.skip";
   private static String EMPTY_STRING = "";
   private static String COMMA = ",";
   private static String MYPS1 = "myps1";
   private static String SYS_ARGV_EQUALS = "sys.argv=";
   public static WLSTInterpreter interp = null;
   private static WLSTMsgTextFormatter txtFmt = new WLSTMsgTextFormatter();
   private static List wlstOptions = null;

   public static void main(String[] args) throws Throwable {
      ifHelpExit(args);

      try {
         wlstOptions = getWLSTOptions();
         print(txtFmt.getInitializingWLST());
         String var = System.getProperty(PYTHON_VERBOSE);
         if (var != null) {
            System.setProperty(PYTHON_VERBOSE, var);
         } else {
            System.setProperty(PYTHON_VERBOSE, PYTHON_WARNING);
         }

         boolean skipWLSModuleScanning = ifSkipWLSModuleScanning(args);
         if (!skipWLSModuleScanning) {
            String cacheDir = System.getProperty("python.cachedir");
            if (cacheDir == null) {
               cacheDir = WLSTUtil.getWLSTTempFile();
               System.setProperty("python.cachedir", cacheDir);
            }

            File packagesDir = new File(cacheDir, "packages");
            if (!packagesDir.exists()) {
               print(txtFmt.getScanningPackage());
            } else if (!packagesDir.canWrite()) {
               cacheDir = WLSTUtil.getWLSTTempFile("WlstTemp");
               System.setProperty("python.cachedir", cacheDir);
               packagesDir = new File(cacheDir, "packages");
               packagesDir.mkdir();
               packagesDir.createNewFile();
               if (!packagesDir.exists()) {
                  print(txtFmt.getScanningPackage());
               }
            }
         }

         WLSTUtil.argsPassedFromMain = args;
         Hashtable h = new Hashtable();
         if (skipWLSModuleScanning) {
            h.put("skipWLSModuleScanning", new Boolean(true));
         }

         if (WLSTUtil.argsPassedFromMain != null && WLSTUtil.argsPassedFromMain.length > 0) {
            h.put("enableScriptMode", new Boolean(true));
         }

         interp = (new WLSTInterpreter(h)).getWLInterpreter();
         initializeScriptPlugins(WLSTPluginsList.SUBSYSTEM_LIST);
         print(txtFmt.getWelcome());
         print(txtFmt.getHelpInfo());
         boolean scriptFilesPassed = false;
         if (WLSTUtil.sysArgs == null) {
            WLSTUtil.sysArgs = new PyList();
         }

         args = checkOptions(args, interp);
         String codeString;
         if (args != null && args.length > 0 && !WLSTUtil.scriptFileExecuted) {
            codeString = EMPTY_STRING;

            for(int k = 0; k < args.length; ++k) {
               if (k == 0) {
                  codeString = args[k];
                  WLSTUtil.sysArgs.append(new PyString(codeString));
                  scriptFilesPassed = true;
               } else {
                  WLSTUtil.sysArgs.append(new PyString(args[k]));
               }
            }

            interp.exec(SYS_ARGV_EQUALS + WLSTUtil.sysArgs);
            File f = new File(codeString);
            FileInputStream is = new FileInputStream(f.getAbsoluteFile());
            interp.set("WLST_FILE_NAME_LAST_EXECUTED", codeString);
            interp.execfile(is, f.getAbsolutePath());
         }

         codeString = EMPTY_STRING;
         String ps1 = interp.get(MYPS1).toString();
         PyObject promt = new PyString(ps1);
         PyObject dotPromt = new PyString("...");
         StringBuilder sb = new StringBuilder();

         while(true) {
            while(!scriptFilesPassed) {
               if (WLSTUtil.disconnected) {
                  interp.exec("init()");
                  interp.exec("evaluatePrompt()");
               } else {
                  interp.exec("restoreDisplay()");
               }

               if (!ps1.equals(interp.get(MYPS1).toString())) {
                  ps1 = interp.get(MYPS1).toString();
                  promt = new PyString(ps1);
               }

               try {
                  codeString = __builtin__.raw_input(promt);
               } catch (PyException var12) {
                  if (var12.toString().indexOf("EOFError: raw_input()") != -1 && var12.traceback.tb_lineno == 0) {
                     return;
                  }

                  throw var12;
               }

               WLScriptContext ctx;
               if (codeString != null) {
                  if (WLSTUtil.easeSyntax) {
                     codeString = WLSTUtil.convertEasySyntax(codeString);
                  }

                  if (WLSTUtil.recordAll) {
                     ctx = (WLScriptContext)interp.get("WLS_ON", WLScriptContext.class);
                     ctx.getInfoHandler().write(codeString);
                  }

                  sb.delete(0, sb.length());
                  sb.trimToSize();
                  sb.append(codeString);
                  interp.exec("originalErr=sys.stderr");

                  String s;
                  for(; interp.runsource(sb.toString(), "<console>"); sb.append("\n").append(s)) {
                     s = __builtin__.raw_input(dotPromt);
                     if (WLSTUtil.recordAll) {
                        WLScriptContext ctx = (WLScriptContext)interp.get("WLS_ON", WLScriptContext.class);
                        if (!ctx.commandType.equals("stopRecording")) {
                           ctx.getInfoHandler().write("  " + s);
                        }
                     }
                  }

                  interp.exec("sys.stderr=originalErr");
               } else {
                  if (WLSTUtil.recordingInProgress) {
                     interp.exec("stopRecording()");
                  }

                  ctx = (WLScriptContext)interp.get("WLS_ON", WLScriptContext.class);
                  if (!ctx.isEditSessionInProgress) {
                     System.out.println(txtFmt.getExitingWLST());
                     return;
                  }

                  ctx.println(txtFmt.getExitEdit());
                  interp.exec("stopEdit('y')");
               }
            }

            return;
         }
      } catch (PyException var13) {
         exitIfSystemExit(var13);
         throw var13;
      }
   }

   private static void print(String s) {
      System.out.println(s);
   }

   private static void ifHelpExit(String[] args) {
      if (args != null && args.length == 1 && args[0].equals("-help")) {
         System.out.println(txtFmt.getCommandLineHelp());
         System.exit(0);
      }

   }

   private static boolean ifSkipWLSModuleScanning(String[] args) {
      String skipCacheDir = System.getProperty(PYTHON_CACHEDIR_SKIP);
      if (skipCacheDir != null && skipCacheDir.equalsIgnoreCase("true")) {
         return true;
      } else if (args != null && args.length != 0) {
         for(int i = 0; i < args.length; ++i) {
            if (args[i].equals("-skipWLSModuleScanning")) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static void exitIfSystemExit(PyException exc) {
      if (Py.matchException(exc, Py.SystemExit)) {
         PyObject value = exc.value;
         if (value instanceof PyInstance) {
            PyObject tmp = value.__findattr__("code");
            if (tmp != null) {
               value = tmp;
            }
         }

         Py.getSystemState().callExitFunc();
         if (value instanceof PyInteger) {
            System.exit(((PyInteger)value).getValue());
         } else {
            if (value != Py.None) {
               try {
                  Py.println(value);
                  System.exit(1);
               } catch (Throwable var3) {
               }
            }

            System.exit(0);
         }
      }

   }

   private static void initializeScriptPlugins(String[] classList) {
      StringBuffer subSystemBuffer = new StringBuffer();

      for(int count = 0; count < classList.length; ++count) {
         subSystemBuffer.append("import " + classList[count] + "\n");
         subSystemBuffer.append("from " + classList[count] + " import *\n");
         subSystemBuffer.append(classList[count] + ".setInterpreter(theInterpreter)\n");
         subSystemBuffer.append(classList[count] + ".initialize()\n");
      }

      interp.exec(subSystemBuffer.toString());
   }

   private static List getWLSTOptions() {
      wlstOptions = new ArrayList();
      wlstOptions.add("-easeSyntax");
      wlstOptions.add("-loadProperties");
      wlstOptions.add("-online");
      wlstOptions.add("-offline");
      wlstOptions.add("-i");
      wlstOptions.add("-skipWLSModuleScanning");
      return wlstOptions;
   }

   private static String[] checkOptions(String[] args, InteractiveInterpreter interp) {
      if (args == null || args.length == 0 || args.length == 1 && args[0] != null && args[0].equals("")) {
         return null;
      } else {
         List options = getWLSTOptions();
         String newArgs = null;
         List all = new ArrayList();

         for(int k = 0; k < args.length; ++k) {
            all.add(args[k]);
         }

         boolean changedArgs = false;

         for(int i = 0; i < args.length; ++i) {
            if (options.contains(args[i])) {
               if (args[i].equals("-easeSyntax")) {
                  easeSyntax = true;
                  System.out.println(txtFmt.getEaseSyntaxEnabled());
                  all.remove(args[i]);
               } else {
                  String fileName;
                  if (args[i].equals("-loadProperties")) {
                     try {
                        fileName = args[i + 1];
                        setProperties(fileName, interp);
                        all.remove(args[i]);
                        all.remove(args[i + 1]);
                     } catch (ArrayIndexOutOfBoundsException var13) {
                        System.out.println(txtFmt.getSpecifyPropertiesLocation());
                        all.remove(args[i]);
                     }
                  } else if (args[i].equals("-skipWLSModuleScanning")) {
                     all.remove(args[i]);
                  } else if (args[i].equals("-i")) {
                     try {
                        fileName = args[i + 1];
                        List all_ = new ArrayList();

                        int k;
                        for(k = 0; k < args.length; ++k) {
                           all_.add(args[k]);
                        }

                        for(k = 0; k < args.length; ++k) {
                           if (options.contains(args[k])) {
                              int ind;
                              if (args[k].equals("-skipWLSModuleScanning")) {
                                 ind = all_.indexOf("-skipWLSModuleScanning");
                                 all_.remove(ind);
                              }

                              if (args[k].equals("-easeSyntax")) {
                                 ind = all_.indexOf("-easeSyntax");
                                 all_.remove(ind);
                              } else if (args[k].equals("-loadProperties")) {
                                 ind = all_.indexOf("-loadProperties");
                                 all_.remove(ind);
                                 all_.remove(ind);
                              } else if (args[k].equals("-i")) {
                                 ind = all_.indexOf("-i");
                                 all_.remove(ind);
                                 all_.remove(ind);
                              }
                           }
                        }

                        String[] args_ = new String[all_.size()];
                        Iterator iter_ = all_.iterator();
                        int h = false;
                        sysArgs = new PyList();
                        sysArgs.append(new PyString(fileName));

                        while(iter_.hasNext()) {
                           PyString s = new PyString((String)iter_.next());
                           sysArgs.append(s);
                        }

                        interp.exec(SYS_ARGV_EQUALS + sysArgs);
                        interp.execfile(fileName);
                        scriptFileExecuted = true;
                        all.remove(args[i]);
                        all.remove(args[i + 1]);
                     } catch (ArrayIndexOutOfBoundsException var14) {
                        System.out.println(txtFmt.getNoFileNameSpecified());
                        all.remove(args[i]);
                     }
                  }
               }

               changedArgs = true;
            }
         }

         if (!changedArgs) {
            return args;
         } else {
            Iterator iter = all.iterator();

            while(iter.hasNext()) {
               if (newArgs != null) {
                  newArgs = newArgs + (String)iter.next() + COMMA;
               } else {
                  newArgs = (String)iter.next() + COMMA;
               }
            }

            String[] news = null;
            if (newArgs != null) {
               news = StringUtils.splitCompletely(newArgs, COMMA);
               if (news.length == 0) {
                  news = new String[]{newArgs};
               }
            }

            return news;
         }
      }
   }
}
