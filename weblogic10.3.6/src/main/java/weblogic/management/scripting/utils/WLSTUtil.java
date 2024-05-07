package weblogic.management.scripting.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.python.core.ArgParser;
import org.python.core.Py;
import org.python.core.PyObject;
import org.python.util.InteractiveInterpreter;
import org.python.util.PythonInterpreter;
import weblogic.Home;
import weblogic.management.scripting.WLScriptContext;
import weblogic.management.utils.PDevHelper;
import weblogic.utils.FileUtils;
import weblogic.utils.StringUtils;

public class WLSTUtil extends WLSTUtilHelper {
   private static ClassLoader pdevCls = null;

   private static InputStream getWLSTScript() {
      return WLSTUtil.class.getResourceAsStream("wlst.py");
   }

   private static InputStream getWLSTCommonScript() {
      return WLSTUtil.class.getResourceAsStream("wlst_common.py");
   }

   public static void writeWLSTAsModule(String path) throws Throwable {
      InputStream is = WLSTUtil.class.getResourceAsStream("wlstModule.py");
      File file = new File(path);
      FileUtils.writeToFile(is, file);
   }

   public static String getWLSTScriptPath() throws IOException {
      InputStream is = WLSTUtil.class.getResourceAsStream("wlst.py");
      File file = File.createTempFile("wlst_module1", ".py");
      FileUtils.writeToFile(is, file);
      return file.getAbsolutePath();
   }

   public static String getWLSTCommonModulePath() throws IOException {
      InputStream is = WLSTUtil.class.getResourceAsStream("wlstCommonModule.py");
      File file = File.createTempFile("wlst_module2", ".py");
      FileUtils.writeToFile(is, file);
      return file.getAbsolutePath();
   }

   public static String getOfflineWLSTScriptPath() {
      ClassLoader prevCls = Thread.currentThread().getContextClassLoader();

      String var1;
      try {
         if (pdevCls == null) {
            pdevCls = PDevHelper.getPDevClassLoader(WLSTUtil.class.getClassLoader());
         }

         Thread.currentThread().setContextClassLoader(pdevCls);
         var1 = getOfflineWLSTScriptPathInternal();
      } finally {
         Thread.currentThread().setContextClassLoader(prevCls);
      }

      return var1;
   }

   public static String getOfflineWLSTScriptPathInternal() {
      String path = null;
      String debugFlag = System.getProperty("wlst.debug.init", "false");
      boolean debug = false;
      if (debugFlag.equals("true")) {
         debug = true;
      }

      try {
         Class clz = Class.forName("com.oracle.cie.domain.script.jython.WLST_offline", true, Thread.currentThread().getContextClassLoader());
         Method mthd = clz.getMethod("getWLSTOfflineInitFilePath", (Class[])null);
         path = (String)mthd.invoke(clz, (Object[])null);
         debugInit("The WLST Offline script path has been evaluated to " + path);
         return path;
      } catch (ClassNotFoundException var5) {
         throw new RuntimeException("Could not find the OffLine WLST class ", var5);
      } catch (NoSuchMethodException var6) {
         throw new RuntimeException("Could not find the method while getting the WLSTOffLineScript path", var6);
      } catch (IllegalAccessException var7) {
         throw new RuntimeException("Illegal Access while getting the WLSTOffLineScript path", var7);
      } catch (InvocationTargetException var8) {
         throw new RuntimeException("Invocation Target exception while getting the WLSTOffLineScript path", var8);
      }
   }

   public static String writeKshFile() {
      String weblogic90Location = (new File(Home.getFile().getParentFile().getAbsolutePath())).getAbsolutePath();
      String wlscontrol = weblogic90Location + File.separator + "common" + File.separator + "bin" + File.separator + "wlscontrol.sh";
      wlscontrol = StringUtils.replaceGlobal(wlscontrol, File.separator, "/");
      return wlscontrol;
   }

   public static String getOfflineWLSTScriptForModule() {
      String s = "";
      s = s + "\n" + "WLS.setup(1)";
      s = s + "\n";
      return s;
   }

   private static InputStream getNonSupportedOnlineWLSTScript() {
      return WLSTUtil.class.getResourceAsStream("online_nonsupported.py");
   }

   private static InputStream getNonSupportedOfflineWLSTScript() {
      return WLSTUtil.class.getResourceAsStream("offline_nonsupported.py");
   }

   private static InputStream getOnlineAppendedWLSTScript() {
      return WLSTUtil.class.getResourceAsStream("online_append.py");
   }

   public static void initializeOnlineWLST(PyObject[] args, String[] kw) throws Throwable {
      ArgParser ap = new ArgParser("connect", args, kw, "username", "password", "url");
      WLSTInterpreter interp = (WLSTInterpreter)Py.tojava(ap.getPyObject(3), "weblogic.management.scripting.utils.WLSTInterpreter");
      setupOnline(interp);
      WLScriptContext ctx = (WLScriptContext)interp.get("WLS_ON");
      ctx.connect(args, kw);
      interp.exec("updateGlobals()");
      interp.exec("print ''");
   }

   public static WLSTInterpreter ensureInterpreter() {
      if (theInterpreter == null) {
         theInterpreter = (new WLSTInterpreter()).getWLInterpreter();
      }

      return theInterpreter;
   }

   private static void execWLSTScript(WLSTInterpreter interp, boolean online) {
      WLScriptContext ctx = ensureWLCtx(interp);
      PyObject offlineCtx = interp.get("WLS");
      offline_cmo = interp.get("cmo");
      offline_myps1 = interp.get("myps1");
      if (online) {
         interp.execfile(getWLSTScript());
      } else {
         interp.execfile(getWLSTCommonScript());
      }

      if (ctx != null) {
         interp.set("WLS_ON", ctx);
      }

      if (offlineCtx != null) {
         interp.set("WLS", offlineCtx);
      }

      interp.set("home", ctx.getHome());
      interp.set("adminHome", ctx.getAdminHome());
      interp.set("CMO", ctx.getCmo());
      interp.set("cmo", ctx.getCmo());
      interp.set("mbs", ctx.getMBeanServer());
      interp.set("cmgr", ctx.getConfigManager());
      interp.set("domainRuntimeService", ctx.getDomainRuntimeServiceMBean());
      interp.set("runtimeService", ctx.getRuntimeServiceMBean());
      interp.set("editService", ctx.getEditServiceMBean());
      interp.set("typeService", ctx.getMBeanTypeService());
      interp.set("_editService", ctx.getEditService());
      interp.set("nmService", ctx.getNodeManagerService());
      interp.set("scriptMode", ctx.getScriptMode());
   }

   public static WLScriptContext ensureWLCtx(WLSTInterpreter interp) {
      Object o = interp.get("WLS_ON");
      if (o instanceof WLScriptContext) {
         return (WLScriptContext)o;
      } else {
         WLScriptContext ctx = new WLScriptContext();
         interp.set("WLS_ON", ctx);
         return ctx;
      }
   }

   public static void setupOnline(WLSTInterpreter interp) {
      execWLSTScript(interp, true);
      interp.execfile(getNonSupportedOfflineWLSTScript());
   }

   public static void setupOffline(WLSTInterpreter interp) {
      ClassLoader prevCls = Thread.currentThread().getContextClassLoader();

      try {
         if (pdevCls == null) {
            pdevCls = PDevHelper.getPDevClassLoader(WLSTUtil.class.getClassLoader());
         }

         Thread.currentThread().setContextClassLoader(pdevCls);
         if (interp != null) {
            interp.setClassLoader(pdevCls);
         }

         setupOfflineInternal(interp);
      } finally {
         Thread.currentThread().setContextClassLoader(prevCls);
      }

   }

   private static void setupOfflineInternal(WLSTInterpreter interp) {
      theInterpreter = interp;
      PyObject ctx = interp.get("WLS_ON");
      PyObject ns = interp.get("nmService");
      PyObject es = interp.get("_editService");
      PyObject offlineCtx = interp.get("WLS");
      execWLSTScript(interp, false);
      if (ctx != null) {
         interp.set("WLS_ON", ctx);
      }

      if (ns != null) {
         interp.set("nmService", ns);
      }

      if (es != null) {
         interp.set("_editService", es);
      }

      interp.execfile(getOfflineWLSTScriptPathInternal());
      interp.execfile(getOnlineAppendedWLSTScript());
      if (offlineCtx != null) {
         interp.set("WLS", offlineCtx);
      }

      if (offline_cmo != null) {
         interp.set("cmo", offline_cmo);
      }

      if (offline_myps1 != null) {
         interp.set("myps1", offline_myps1);
      }

      interp.execfile(getNonSupportedOnlineWLSTScript());
      initOfflineContext(interp);
   }

   private static void initOfflineContext(WLSTInterpreter interp) {
      try {
         Class clz = Class.forName("com.oracle.cie.domain.script.jython.WLST_offline", true, Thread.currentThread().getContextClassLoader());
         Class[] params = new Class[]{PythonInterpreter.class, Boolean.TYPE};
         Method mthd = clz.getMethod("setupContext", params);
         Object[] args = new Object[]{interp, null};
         if (scriptMode) {
            args[1] = new Boolean(true);
         } else {
            args[1] = new Boolean(false);
         }

         mthd.invoke(clz, args);
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   private static void unsupportPyMethod(InteractiveInterpreter interp, String sig) {
      String msg = "This command is not supported while connected to a running server";
      String action = "def " + sig + ":\n" + "  print ''\n" + "  print '" + msg + "'\n" + "  print ''\n" + "  return\n";
      interp.exec(action);
   }
}
