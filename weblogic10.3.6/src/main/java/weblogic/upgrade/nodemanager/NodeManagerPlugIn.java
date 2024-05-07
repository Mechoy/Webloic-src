package weblogic.upgrade.nodemanager;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.DefaultFileSelectionInputAdapter;
import com.bea.plateng.plugin.ia.InputAdapter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import weblogic.nodemanager.server.Upgrader;
import weblogic.upgrade.UpgradeHelper;
import weblogic.utils.FileUtils;

public class NodeManagerPlugIn extends AbstractPlugIn {
   private static final String NODE_MANAGER_HOME_KEY = "NodeManagerPlugin.NODE_MANAGER_HOME";

   public NodeManagerPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      File var2 = (File)this._context.get("NodeManagerPlugin.NODE_MANAGER_HOME");
      if (var2 == null) {
         var2 = new File(".");
      }

      DefaultFileSelectionInputAdapter var3 = (DefaultFileSelectionInputAdapter)this._adapter;
      var3.setSelectionMode(1);
      var3.setMultipleSelection(false);
      var3.setSelectedFileNames(new String[]{var2.getPath()});
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      DefaultFileSelectionInputAdapter var2 = (DefaultFileSelectionInputAdapter)this._adapter;
      ValidationStatus var3 = super.validateInputAdapter(var1);
      if (var3.isValid()) {
         File var4 = var2.getSelectedFiles()[0];
         if (!var4.isDirectory()) {
            var3.setErrorMessage(UpgradeHelper.i18n("NodeManagerPlugIn.IA.input.invalid.text"));
            var3.setValid(false);
         }

         this._context.put("NodeManagerPlugin.NODE_MANAGER_HOME", var4);
      }

      return var3;
   }

   public void execute() throws PlugInException {
      File var1 = (File)this._context.get("NodeManagerPlugin.NODE_MANAGER_HOME");
      ZipOutputStream var2 = null;
      File var3 = null;

      try {
         UpgradeHelper.setupWLSClientLogger(this);
         UpgradeHelper.addSummaryMessageForOutputLocation(this._context, this.getName());
         var1 = var1.getAbsoluteFile().getCanonicalFile();
         this.log(UpgradeHelper.i18n("NodeManagerPlugIn.execute.upgrading_node_manager_dir", (Object)var1));
         Upgrader.upgrade(var1, true);
         this.log(UpgradeHelper.i18n("NodeManagerPlugIn.execute.archiving_old_state_files"));
         var3 = new File(var1, "weblogic-nodemanager-backup.zip");
         String var4 = var1.getPath().replace(File.separatorChar, '/');
         if (!var4.endsWith("/")) {
            var4 = var4 + "/";
         }

         int var5 = var4.length();
         var3.createNewFile();
         var3 = var3.getAbsoluteFile().getCanonicalFile();
         var2 = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(var3)));
         DDBPFileFilter var6 = new DDBPFileFilter();
         ArrayList var7 = new ArrayList();
         UpgradeHelper.listFiles(var1, true, var6, var7);
         File var8 = null;
         byte[] var9 = new byte[1024];
         int var10 = var7.size();

         for(int var11 = 0; var11 < var10; ++var11) {
            var8 = (File)var7.get(var11);
            FileInputStream var12 = null;
            String var13 = var8.getPath().replace(File.separatorChar, '/').substring(var5);

            try {
               var12 = new FileInputStream(var8);
               ZipEntry var14 = new ZipEntry(var13);
               var2.putNextEntry(var14);
               boolean var15 = false;

               int var30;
               while((var30 = var12.read(var9)) != -1) {
                  var2.write(var9, 0, var30);
               }

               var2.closeEntry();
            } catch (IOException var26) {
               this.log(UpgradeHelper.i18n("plugin.exception_adding_file_to_zip", var26, var13));
            } finally {
               UpgradeHelper.close((InputStream)var12);
            }
         }

         this.log(UpgradeHelper.i18n("NodeManagerPlugIn.execute.deleting_old_state_files"));
         this.remove(new File(var1, "nodemanager.out"));
         this.remove(new File(var1, "NodeManagerClientLogs"));
         this.remove(new File(var1, "NodeManagerLogs"));
         this.log(UpgradeHelper.i18n("NodeManagerPlugIn.execute.completed_msg"));
         UpgradeHelper.addSummaryMessage(this._context, this.getName(), UpgradeHelper.i18n("NodeManagerPlugIn.execute.archived_old_state_files", (Object)var3));
      } catch (Throwable var28) {
         throw new PlugInException(this.getName(), UpgradeHelper.i18n("NodeManagerPlugIn.execute.exception_msg", (Object)var28), var28);
      } finally {
         UpgradeHelper.close((OutputStream)var2);
         UpgradeHelper.setupWLSClientLogger((AbstractPlugIn)null);
      }

   }

   private void remove(File var1) {
      if (var1.exists()) {
         FileUtils.remove(var1);
      }

   }

   private void log(String var1) {
      PlugInMessageObservation var2 = new PlugInMessageObservation(this.getName());
      var2.setMessage(var1);
      this.updateObservers(var2);
   }

   private class DDBPFileFilter implements FileFilter {
      private DDBPFileFilter() {
      }

      public boolean accept(File var1) {
         File var2 = (File)NodeManagerPlugIn.this._context.get("NodeManagerPlugin.NODE_MANAGER_HOME");
         if (!var2.equals(var1.getParentFile())) {
            return true;
         } else {
            return var1.isFile() && var1.getName().equals("nodemanager.out") || var1.isDirectory() && var1.getName().equals("NodeManagerClientLogs") || var1.isDirectory() && var1.getName().equals("NodeManagerLogs");
         }
      }

      // $FF: synthetic method
      DDBPFileFilter(Object var2) {
         this();
      }
   }
}
