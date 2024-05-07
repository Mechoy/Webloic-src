package weblogic.upgrade;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInMessageObservation;
import com.bea.plateng.plugin.ia.DefaultChoiceInputAdapter;
import com.bea.plateng.plugin.ia.DefaultCompositeInputAdapter;
import com.bea.plateng.plugin.ia.DefaultTextInputAdapter;
import com.bea.plateng.wizard.plugin.helpers.PlugInWizardInputAdapterHelper;
import com.bea.plateng.wizard.plugin.helpers.SummaryMessageHelper;
import com.oracle.cie.common.util.IObjectStore;
import com.oracle.cie.common.util.ObjectStoreManager;
import com.oracle.cie.common.util.ResourceBundleManager;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import weblogic.common.internal.VersionInfo;
import weblogic.common.internal.VersionInfoFactory;
import weblogic.kernel.KernelLogManager;
import weblogic.logging.ConsoleHandler;
import weblogic.logging.WLLevel;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class UpgradeHelper {
   private static WLLogHandlerForUpgrade wlLogHdlr;

   public static final String i18n(String var0) {
      String var1 = ResourceBundleManager.getString("plugin", var0);
      if (var1 != null) {
         IObjectStore var2 = ObjectStoreManager.getObjectStore("plugin");
         var1 = var2.substitute(var1);
      } else {
         var1 = "";
      }

      return var1;
   }

   public static final String i18n(String var0, Object var1) {
      return MessageFormat.format(i18n(var0), var1);
   }

   public static final String i18n(String var0, Object var1, Object var2) {
      return MessageFormat.format(i18n(var0), var1, var2);
   }

   public static final String i18n(String var0, Object[] var1) {
      return MessageFormat.format(i18n(var0), var1);
   }

   public static List listFiles(File var0, boolean var1, FileFilter var2, List var3) {
      File[] var4 = var0.listFiles();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var2 == null || var2.accept(var4[var5])) {
            if (var1 && var4[var5].isDirectory()) {
               listFiles(var4[var5], var1, var2, var3);
            } else {
               var3.add(var4[var5]);
            }
         }
      }

      return var3;
   }

   public static DefaultTextInputAdapter createTextIA(String var0, String var1) throws Exception {
      return PlugInWizardInputAdapterHelper.createDefaultTextInputAdapter(var0, var1, "");
   }

   public static DefaultCompositeInputAdapter createCompositeIA(String var0, String var1) throws Exception {
      return PlugInWizardInputAdapterHelper.createDefaultCompositeInputAdapter(var0, var1);
   }

   public static DefaultChoiceInputAdapter createChoiceIA(String var0, String var1) throws Exception {
      return PlugInWizardInputAdapterHelper.createDefaultChoiceInputAdapter(var0, var1, "");
   }

   public static void addSummaryMessage(PlugInContext var0, String var1, String var2) {
      SummaryMessageHelper.addSummaryMessage(var0, var1, var2);
   }

   public static void clearSummaryMessages(PlugInContext var0, String var1) {
      Object var2 = (List)var0.get("PLUGIN_SUMMARY_MESSAGES_KEY");
      if (var2 == null) {
         var2 = new ArrayList();
         var0.put("PLUGIN_SUMMARY_MESSAGES_KEY", var2);
      }

      ((List)var2).clear();
   }

   public static String getOutputLocation() {
      String var0 = System.getProperty(Main.REDIRECTED_OUTPUT_FILE_KEY);
      if (var0 == null) {
         var0 = i18n("plugin.std_out_repr");
      }

      return var0;
   }

   public static void addSummaryMessageForOutputLocation(PlugInContext var0, String var1) {
      String var2 = i18n("plugin.comprehensive_log_written", (Object)getOutputLocation());
      addSummaryMessage(var0, var1, var2);
   }

   private static void log(String var0, String var1, AbstractPlugIn var2) {
      PlugInMessageObservation var3 = new PlugInMessageObservation(var0);
      var3.setMessage(var1);
      var2.updateObservers(var3);
   }

   public static void log(AbstractPlugIn var0, String var1) {
      log(var0.getName(), var1, var0);
   }

   public static void setupWLSClientLogger(AbstractPlugIn var0) {
      wlLogHdlr.setCurrentPlugIn(var0);
   }

   public static void close(InputStream var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (Throwable var2) {
      }

   }

   public static void close(OutputStream var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (Throwable var2) {
      }

   }

   public static void resetLocalServerNames(AbstractPlugIn var0, PlugInContext var1) {
      File var2 = (File)var1.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
      DomainMBean var3 = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      ServerMBean[] var4 = var3.getServers();
      HashSet var5 = new HashSet();
      String var6 = System.getProperty("weblogic.Name");
      String var7 = var3.getConfigurationVersion();
      if (var6 != null && var6.trim().length() > 0) {
         var5.add(var6);
      }

      for(int var8 = 0; var8 < var4.length; ++var8) {
         String var9 = var4[var8].getTransactionLogFilePrefix();
         File var10 = null;
         if (var7.startsWith("6.1")) {
            var10 = var2;
         } else {
            var10 = new File(var2, var4[var8].getName());
         }

         if (var9 != null && var9.trim().length() > 0) {
            if ((new File(var9)).isAbsolute()) {
               var10 = new File(var9);
            } else {
               var10 = new File(var10, var9);
            }
         }

         if (var7.startsWith("6.1")) {
            var10 = new File(var10, var4[var8].getName() + ".0000.tlog");
         }

         if (var7.startsWith("6")) {
            var10 = new File(var10, var4[var8].getName() + ".0000.tlog");
         }

         if (var10.exists()) {
            var5.add(var4[var8].getName());
         }
      }

      if (var5.size() > 0) {
         log(var0, i18n("plugin.server_detected_in_directory", (Object)var5));
      } else {
         log(var0, i18n("plugin.no_servers_detected_in_directory"));
      }

      var1.put(DomainPlugInConstants.SERVER_NAMES_KEY, (String[])((String[])var5.toArray(new String[0])));
   }

   public static String getTargetVersion() {
      VersionInfo var0 = VersionInfoFactory.getVersionInfo();
      int var1 = var0.getMajor();
      int var2 = var0.getMinor();
      int var3 = var0.getServicePack();
      int var4 = var0.getRollingPatch();
      return new String(var1 + "." + var2 + "." + var3 + "." + var4);
   }

   static {
      Logger var0 = KernelLogManager.getLogger();
      Handler[] var1 = var0.getHandlers();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] instanceof ConsoleHandler) {
            var1[var2].setLevel(Level.OFF);
         }
      }

      wlLogHdlr = new WLLogHandlerForUpgrade();
      var0.addHandler(wlLogHdlr);
   }

   public static class WLLogHandlerForUpgrade extends Handler {
      private AbstractPlugIn currPlugin;

      public void close() {
      }

      public void flush() {
      }

      public void setCurrentPlugIn(AbstractPlugIn var1) {
         this.currPlugin = var1;
      }

      public void publish(LogRecord var1) {
         if (this.currPlugin != null && var1 != null) {
            Level var2 = var1.getLevel();
            if (var2 == null || (!(var2 instanceof WLLevel) || var2.intValue() < 980) && (!(var2 instanceof Level) || var2.intValue() < Level.SEVERE.intValue())) {
               UpgradeHelper.log(this.currPlugin, var1.getMessage());
            } else {
               UpgradeHelper.log(this.currPlugin, var1.getMessage());
            }

         }
      }
   }
}
