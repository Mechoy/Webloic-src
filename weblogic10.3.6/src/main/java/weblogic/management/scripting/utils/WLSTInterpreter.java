package weblogic.management.scripting.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.util.InteractiveInterpreter;
import weblogic.Home;
import weblogic.management.scripting.WLScriptContext;
import weblogic.utils.StringUtils;

public class WLSTInterpreter extends InteractiveInterpreter {
   public static final String SKIP_WLS_MODULE_SCANNING = "skipWLSModuleScanning";
   public static final String ENABLE_SCRIPT_MODE = "enableScriptMode";
   private OutputStream stdOstream;
   private Writer stdWriter;
   private OutputStream errOstream;
   private Writer errWriter;
   private String wlstHome;
   private static final String WLST_PROFILE_FILE = "wlstProfile.py";

   public WLSTInterpreter() {
      this((Hashtable)null);
   }

   public WLSTInterpreter(Hashtable h) {
      this.stdOstream = null;
      this.stdWriter = null;
      this.errOstream = null;
      this.errWriter = null;
      this.wlstHome = null;
      PySystemState.initialize();
      super.set("theInterpreter", this);
      super.exec("import sys\n");
      boolean skipPackageScanning = isSkipPackageScanning();
      boolean skipWLSModuleScanning = false;
      Object val;
      if (skipPackageScanning) {
         skipWLSModuleScanning = true;
      } else if (h != null) {
         val = h.get("skipWLSModuleScanning");
         if (val != null && val instanceof Boolean) {
            skipWLSModuleScanning = (Boolean)val;
         }
      }

      if (h != null) {
         val = h.get("enableScriptMode");
         if (val != null && val instanceof Boolean) {
            WLSTUtil.scriptMode = (Boolean)val;
         }
      } else if (WLSTUtil.argsPassedFromMain != null && WLSTUtil.argsPassedFromMain.length > 0) {
         WLSTUtil.scriptMode = true;
      }

      String weblogic90Location = (new File(Home.getFile().getParentFile().getAbsolutePath())).getAbsolutePath();
      String commonLocation = weblogic90Location + File.separator + "common";
      this.wlstHome = commonLocation + File.separator + "wlst";
      this.wlstHome = StringUtils.replaceGlobal(this.wlstHome, File.separator, "/");
      if (!skipPackageScanning) {
         String weblogicJarLocation = weblogic90Location + File.separator + "server" + File.separator + "lib" + File.separator + "weblogic.jar";
         weblogicJarLocation = StringUtils.replaceGlobal(weblogicJarLocation, File.separator, "/");
         debugInit("WebLogic jar is at " + weblogicJarLocation);
         super.exec("sys.path.append(\"" + weblogicJarLocation + "\")\n");
      }

      if (skipPackageScanning) {
         addJavaRuntime();
      }

      WLSTUtil.setupOffline(this);
      WLSTUtil.wlstAsModule = false;
      this.setModules();
      if (!skipWLSModuleScanning) {
         this.addWLSModules();
      }

   }

   public void setClassLoader(ClassLoader cls) {
      this.systemState.setClassLoader(cls);
   }

   private static void debugInit(String msg) {
      WLSTUtil.debugInit(msg);
   }

   private static void debugInit(String msg, Throwable t) {
      WLSTUtil.debugInit(msg, t);
   }

   private static boolean isSkipPackageScanning() {
      String skipCacheDir = System.getProperty("python.cachedir.skip");
      return skipCacheDir != null && skipCacheDir.equalsIgnoreCase("true");
   }

   private void setModules() {
      try {
         String lib = this.wlstHome + "/modules/jython-modules.jar/Lib";
         File f = new File(lib);
         lib = StringUtils.replaceGlobal(f.getAbsolutePath(), File.separator, "/");
         debugInit("The wlst lib is evaluated to " + lib);
         super.exec("sys.path.append('" + lib + "')");
         f = new File(this.wlstHome);
         String s = StringUtils.replaceGlobal(f.getAbsolutePath(), File.separator, "/");
         super.exec("sys.path.append('" + s + "')");
         super.exec("sys.path.append('" + s + "/lib')");
         super.exec("sys.path.append('" + s + "/modules')");
         this.importPyFiles(this.wlstHome + "/lib");
         this.execDefaultUserModules(this.wlstHome);
         this.execProfile();
         super.exec("sys.path.append('.')");
      } catch (Throwable var4) {
         debugInit("The default modules or profile were not executed ... ", var4);
      }

   }

   private void addWLSModules() {
      String cp = System.getProperty("java.class.path");
      if (!StringUtils.isEmptyString(cp)) {
         String jarFileName = null;
         boolean isWeblogicJar = false;
         String[] cpArr = cp.trim().split(System.getProperty("path.separator"));

         for(int i = 0; i < cpArr.length; ++i) {
            String temp = StringUtils.replaceGlobal(cpArr[i], File.separator, "/");
            if (temp.endsWith("/weblogic.jar")) {
               jarFileName = cpArr[i];
               isWeblogicJar = true;
               break;
            }

            if (temp.indexOf("features/weblogic.server.modules_") != -1 && temp.endsWith(".jar")) {
               jarFileName = cpArr[i];
               break;
            }
         }

         if (jarFileName != null) {
            this.addModulesFromManifest(jarFileName, isWeblogicJar);
         }
      }
   }

   private void addModulesFromManifest(String jarFileName, boolean isWeblogicJar) {
      try {
         JarFile jarFile = new JarFile(jarFileName);
         Manifest manifest = jarFile.getManifest();
         if (manifest == null) {
            return;
         }

         Attributes attrs = manifest.getMainAttributes();
         String mcp = attrs.getValue(Name.CLASS_PATH);
         if (StringUtils.isEmptyString(mcp)) {
            return;
         }

         File parent = (new File(jarFileName)).getParentFile();
         String[] mcpArr = mcp.trim().split("\\s+");

         for(int i = 0; i < mcpArr.length; ++i) {
            if (!StringUtils.isEmptyString(mcpArr[i])) {
               if (isWeblogicJar) {
                  String temp = StringUtils.replaceGlobal(mcpArr[i], File.separator, "/");
                  if (temp.indexOf("features/weblogic.server.modules_") != -1 && temp.endsWith(".jar")) {
                     File f = new File(parent, mcpArr[i]);
                     this.addModulesFromManifest(f.getAbsolutePath(), false);
                     break;
                  }
               } else {
                  File f = new File(parent, mcpArr[i]);
                  if (mcpArr[i].endsWith(".jar")) {
                     PySystemState.packageManager.addJar(f.getCanonicalPath(), true);
                  } else {
                     PySystemState.packageManager.addDirectory(f);
                  }
               }
            }
         }
      } catch (IOException var12) {
      }

   }

   private static void addJavaRuntime() {
      Class aClass;
      try {
         aClass = Class.forName("java.lang.Object");
      } catch (ClassNotFoundException var7) {
         return;
      }

      if (aClass != null) {
         String className = aClass.getName().replace('.', '/');
         URL url = aClass.getResource("/" + className + ".class");
         String path = url.getPath();
         if (path.indexOf(".jar!") != -1) {
            String jarPath = path.substring(5, path.indexOf(33));
            if (jarPath.matches("/[a-zA-Z]:/.*")) {
               jarPath = jarPath.substring(1);
            }

            try {
               jarPath = URLDecoder.decode(jarPath, "UTF-8");
            } catch (UnsupportedEncodingException var6) {
               return;
            }

            PySystemState.packageManager.addJar(jarPath, true);
         }
      }
   }

   private void importPyFiles(String lib) {
      File file = new File(lib);
      File[] pyFiles = file.listFiles();
      if (pyFiles != null) {
         for(int i = 0; i < pyFiles.length; ++i) {
            File pyFile = pyFiles[i];
            if (!pyFile.isDirectory() && pyFile.getName().endsWith(".py")) {
               debugInit("Importing module " + pyFile.getAbsolutePath());

               try {
                  super.exec("import " + pyFile.getName().substring(0, pyFile.getName().length() - 3));
               } catch (Exception var8) {
                  debugInit("Exception for file:  " + pyFile.getAbsolutePath(), var8);
                  WLScriptContext ctx = (WLScriptContext)this.get("WLS_ON", WLScriptContext.class);
                  ctx.println(ctx.getWLSTMsgFormatter().getPythonImportError(pyFile.getAbsolutePath(), var8));
               }
            }
         }
      }

   }

   private void execDefaultUserModules(String wlstHome) {
      this.execPyFiles(wlstHome);
      String home = System.getProperty("weblogic.wlstHome", "");
      if (home.length() != 0) {
         String[] homes = home.trim().split(File.pathSeparator);

         for(int i = 0; i < homes.length; ++i) {
            if (!StringUtils.isEmptyString(homes[i])) {
               debugInit("User specified wlst home is " + homes[i]);
               File f = new File(homes[i]);
               String s = StringUtils.replaceGlobal(f.getAbsolutePath(), File.separator, "/");
               super.exec("sys.path.append('" + s + "')");
               super.exec("sys.path.append('" + s + "/lib')");
               super.exec("sys.path.append('" + s + "/modules')");
               this.importPyFiles(homes[i] + "/lib");
               this.execPyFiles(homes[i]);
            }
         }
      }

   }

   private void execPyFiles(String home) {
      File file = new File(home);
      File[] pyFiles = file.listFiles();
      if (pyFiles != null) {
         for(int i = 0; i < pyFiles.length; ++i) {
            File pyFile = pyFiles[i];
            if (!pyFile.isDirectory() && pyFile.getName().endsWith(".py")) {
               debugInit("Execing the py file " + pyFile.getAbsolutePath());

               try {
                  super.execfile(pyFile.getAbsolutePath());
               } catch (Exception var8) {
                  debugInit("Exception for file:  " + pyFile.getAbsolutePath(), var8);
                  WLScriptContext ctx = (WLScriptContext)this.get("WLS_ON", WLScriptContext.class);
                  ctx.println(ctx.getWLSTMsgFormatter().getPythonExecError(pyFile.getAbsolutePath(), var8));
               }
            }
         }
      }

   }

   private void execProfile() {
      File profile = new File("./wlstProfile.py");
      if (profile.exists()) {
         debugInit("WLST Profile found in the current directory, we will execute it " + profile.getAbsolutePath());
         super.execfile(profile.getAbsolutePath());
      } else {
         String userHome = System.getProperty("user.home");
         profile = new File(userHome + "/" + "wlstProfile.py");
         if (profile.exists()) {
            debugInit("WLST Profile found in the user.home, we will execute it " + profile.getAbsolutePath());
            super.execfile(profile.getAbsolutePath());
         } else {
            debugInit("No Profile file found in either current directory or user.home " + userHome);
         }
      }
   }

   public WLSTInterpreter getWLInterpreter() {
      return this;
   }

   public void setOut(OutputStream ostream) {
      this.stdOstream = ostream;
      WLScriptContext ctx = (WLScriptContext)this.get("WLS_ON", WLScriptContext.class);
      ctx.setStdOutputMedium(ostream);
      ctx.setlogToStandardOut(false);
      super.setOut(ostream);
      this.stdWriter = null;
   }

   public void setOut(Writer writer) {
      this.stdWriter = writer;
      WLScriptContext ctx = (WLScriptContext)this.get("WLS_ON", WLScriptContext.class);
      ctx.setStdOutputMedium(writer);
      ctx.setlogToStandardOut(false);
      super.setOut(writer);
      this.stdOstream = null;
   }

   public void setOut(PyObject po) {
      Object obj = po.__tojava__(Object.class);
      if (obj instanceof OutputStream) {
         this.setOut((OutputStream)obj);
      } else if (obj instanceof Writer) {
         this.setOut((Writer)obj);
      }

      super.setOut(po);
   }

   public void setErr(OutputStream ostream) {
      this.errOstream = ostream;
      WLScriptContext ctx = (WLScriptContext)this.get("WLS_ON", WLScriptContext.class);
      ctx.setErrOutputMedium(ostream);
      ctx.setlogToStandardOut(false);
      super.setErr(ostream);
      this.errWriter = null;
   }

   public void setErr(Writer writer) {
      this.errWriter = writer;
      WLScriptContext ctx = (WLScriptContext)this.get("WLS_ON", WLScriptContext.class);
      ctx.setErrOutputMedium(writer);
      ctx.setlogToStandardOut(false);
      super.setErr(writer);
      this.errOstream = null;
   }

   public Object getOut() {
      if (this.stdOstream != null) {
         return this.stdOstream;
      } else {
         return this.stdWriter != null ? this.stdWriter : super.systemState.stdout;
      }
   }

   public Object getErr() {
      if (this.errOstream != null) {
         return this.errOstream;
      } else {
         return this.errWriter != null ? this.errWriter : super.systemState.stderr;
      }
   }

   public void exec(String string) {
      if (WLSTUtil.recordAll) {
         WLScriptContext ctx = (WLScriptContext)this.get("WLS_ON", WLScriptContext.class);
         ctx.getInfoHandler().write(string);
      }

      super.exec(string);
   }

   public void execfile(String string) {
      if (WLSTUtil.recordAll) {
         WLScriptContext ctx = (WLScriptContext)this.get("WLS_ON", WLScriptContext.class);
         ctx.getInfoHandler().write(string);
      }

      super.execfile(string);
   }

   static {
      String cacheDir = System.getProperty("python.cachedir");
      if (cacheDir == null) {
         cacheDir = WLSTUtil.getWLSTTempFile();
         System.setProperty("python.cachedir", cacheDir);
      }

      String var = System.getProperty("python.verbose");
      if (var != null) {
         System.setProperty("python.verbose", var);
      } else {
         System.setProperty("python.verbose", "warning");
      }

   }
}
