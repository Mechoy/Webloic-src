package weblogic.upgrade.domain.directorybackup;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;
import com.bea.plateng.plugin.PlugInProgressObservation;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.DefaultChoiceInputAdapter;
import com.bea.plateng.plugin.ia.DefaultCompositeInputAdapter;
import com.bea.plateng.plugin.ia.DefaultFileSelectionInputAdapter;
import com.bea.plateng.plugin.ia.DefaultTextInputAdapter;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import weblogic.logging.WeblogicLogfileFilter;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LogMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;
import weblogic.utils.FileUtils;

public class DomainDirectoryBackupPlugIn extends AbstractPlugIn {
   public DomainDirectoryBackupPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      new PlugInMessageObservation(this.getName());
      this.updateObservers(new PlugInMessageObservation(this.getName(), UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.prepare.prepare_to_backup")));
      File var3 = (File)this._context.get(DomainPlugInConstants.DOMAIN_BACKUP_DIRECTORY_KEY);
      if (var3 == null) {
         var3 = (File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
      }

      if (var3 == null) {
         var3 = new File(".");
      }

      DefaultCompositeInputAdapter var4 = (DefaultCompositeInputAdapter)this._adapter;
      DefaultFileSelectionInputAdapter var5 = (DefaultFileSelectionInputAdapter)var4.getInputAdapter("FileSelectionIA");
      var5.setSelectionMode(1);
      var5.setMultipleSelection(false);
      var5.setSelectedFileNames(new String[]{var3.getPath()});
      String var6 = (String)this._context.get(DomainPlugInConstants.DOMAIN_BACKUP_FILE_KEY);
      if (var6 == null) {
         DomainMBean var7 = (DomainMBean)this._context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
         String var8 = var7.getName();
         if (var8 == null) {
            var8 = "";
         } else {
            var8 = "-" + var8;
         }

         File var9 = new File((File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY), "config.xml");
         File var10 = null;

         for(int var11 = 0; var11 < Integer.MAX_VALUE; ++var11) {
            if (var11 == 0) {
               var10 = new File("weblogic-domain-backup" + var8 + ".zip");
            } else {
               var10 = new File("weblogic-domain-backup" + var8 + "-" + var11 + ".zip");
            }

            if (!var10.exists() || var10.lastModified() > var9.lastModified()) {
               var6 = var10.getName();
               break;
            }
         }
      }

      DefaultTextInputAdapter var12 = (DefaultTextInputAdapter)var4.getInputAdapter("TextIA");
      var12.setValue(var6);
      var12.setPrompt(UpgradeHelper.i18n(this.getName() + "." + var12.getName() + ".input.prompt.text"));
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      ValidationStatus var2 = super.validateInputAdapter(var1);
      DefaultCompositeInputAdapter var3 = (DefaultCompositeInputAdapter)this._adapter;
      DefaultTextInputAdapter var4 = (DefaultTextInputAdapter)var3.getInputAdapter("TextIA");
      DefaultFileSelectionInputAdapter var5 = (DefaultFileSelectionInputAdapter)var3.getInputAdapter("FileSelectionIA");
      DefaultChoiceInputAdapter var6 = (DefaultChoiceInputAdapter)var3.getInputAdapter("ChoiceIA");
      if (var2.isValid()) {
         if (var4.getValue() != null && !"".equals(var4.getValue())) {
            File var7 = new File((File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY), "config.xml");
            File var8 = new File(var5.getSelectedFile(), var4.getValue());
            if (var8.exists() && var8.lastModified() < var7.lastModified()) {
               var2.setErrorMessage(UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.TextIA.input.fileexists.text"));
               var2.setValid(false);
            }
         } else {
            var2.setErrorMessage(UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.TextIA.input.filemissing.text"));
            var2.setValid(false);
         }

         if (var2.isValid()) {
            this._context.put(DomainPlugInConstants.DOMAIN_BACKUP_DIRECTORY_KEY, var5.getSelectedFile());
            this._context.put(DomainPlugInConstants.DOMAIN_BACKUP_FILE_KEY, var4.getValue());
         }
      }

      return var2;
   }

   public void execute() throws PlugInException {
      PlugInProgressObservation var1 = new PlugInProgressObservation(this.getName());
      this.updateObservers(var1.setProgress(0));
      new PlugInMessageObservation(this.getName());
      this.updateObservers(new PlugInMessageObservation(this.getName(), UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.execute.message.start.text") + ""));
      ZipOutputStream var3 = null;
      File var4 = new File((File)this._context.get(DomainPlugInConstants.DOMAIN_BACKUP_DIRECTORY_KEY), (String)this._context.get(DomainPlugInConstants.DOMAIN_BACKUP_FILE_KEY));

      try {
         File var5 = new File((File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY), "config.xml");
         if (!var4.exists() || var4.lastModified() <= var5.lastModified()) {
            var4.createNewFile();
            var4 = var4.getAbsoluteFile().getCanonicalFile();
            if (this.skipLogFiles()) {
               this.updateObservers(new PlugInMessageObservation(this.getName(), UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.execute.including_log_files.false.text")));
            } else {
               this.updateObservers(new PlugInMessageObservation(this.getName(), UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.execute.including_log_files.true.text")));
            }

            this.updateObservers(new PlugInMessageObservation(this.getName(), UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.execute.now_adding_message.text", (Object)var4)));
            File var6 = (File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
            var6 = var6.getCanonicalFile();
            String var7 = var6.getPath().replace(File.separatorChar, '/');
            if (!var7.endsWith("/")) {
               var7 = var7 + "/";
            }

            this.replaceMigratedConfigWithOriginal(var6);
            int var8 = var7.length();
            FileFilter var9 = this.getFileFilter();
            ArrayList var10 = new ArrayList();
            UpgradeHelper.listFiles(var6, true, var9, var10);
            this.updateObservers(var1.setProgress(5));
            var3 = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(var4)));
            File var11 = null;
            byte[] var12 = new byte[1024];
            int var13 = var10.size();
            int var14 = 0;

            for(int var15 = 0; var15 < var13; ++var15) {
               var11 = (File)var10.get(var15);
               FileInputStream var16 = null;
               String var17 = var11.getPath().replace(File.separatorChar, '/').substring(var8);

               try {
                  var16 = new FileInputStream(var11);
                  ZipEntry var18 = new ZipEntry(var17);
                  var3.putNextEntry(var18);
                  boolean var19 = false;

                  int var35;
                  while((var35 = var16.read(var12)) != -1) {
                     var3.write(var12, 0, var35);
                  }

                  var3.closeEntry();
                  var16.close();
                  int var20 = Math.round(100.0F * (float)(var15 + 1) / (float)var13);
                  if (var20 > var14) {
                     this.updateObservers(var1.setProgress(var20));
                     var14 = var20;
                  }
               } catch (IOException var31) {
                  this.updateObservers(new PlugInMessageObservation(this.getName(), UpgradeHelper.i18n("plugin.exception_adding_file_to_zip", var31, var17)));
               } finally {
                  this.close((InputStream)var16);
               }
            }

            this.updateObservers(new PlugInMessageObservation(this.getName(), UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.execute.message.end.text", (File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY), (String)this._context.get(DomainPlugInConstants.DOMAIN_BACKUP_FILE_KEY))));
            UpgradeHelper.addSummaryMessage(this._context, this.getName(), UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.execute.domain_dir_backed_up_to", (Object)var4.getAbsoluteFile().getCanonicalFile().getPath()));
            return;
         }

         this.updateObservers(new PlugInMessageObservation(this.getName(), UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.execute.backup_file_already_created.text", (Object)var4.getAbsoluteFile().getCanonicalFile().getPath())));
         this.updateObservers(var1.setProgress(100));
      } catch (IOException var33) {
         throw new PlugInException(this.getName(), UpgradeHelper.i18n("DomainDirectoryBackupPlugIn.execute.failure.text"), var33);
      } finally {
         this.close((OutputStream)var3);
      }

   }

   private void replaceMigratedConfigWithOriginal(File var1) {
      try {
         File var2 = new File(var1.getAbsolutePath(), "config.xml.beforeMigration");
         if (var2.exists()) {
            File var3 = new File(var1.getAbsolutePath(), "config.xml");
            FileUtils.copy(var2, var3);
            FileUtils.remove(var2);
         }
      } catch (Exception var4) {
      }

   }

   private String makeAddMessage(StringBuffer var1, ZipEntry var2) {
      var1.append(var2.getName());
      if (var2.getMethod() == 8) {
         long var3 = var2.getSize();
         if (var3 > 0L) {
            long var5 = var2.getCompressedSize();
            long var7 = (var3 - var5) * 100L / var3;
            var1.append(" (deflated " + var7 + "%)");
         } else {
            var1.append(" (deflated 0%)");
         }
      } else {
         var1.append(" (stored 0%)");
      }

      return var1.toString();
   }

   private void close(InputStream var1) {
      try {
         if (var1 != null) {
            var1.close();
         }
      } catch (Throwable var3) {
      }

   }

   private void close(OutputStream var1) {
      try {
         if (var1 != null) {
            var1.close();
         }
      } catch (Throwable var3) {
      }

   }

   private FileFilter getFileFilter() {
      DDBPFileFilter var1 = null;

      try {
         var1 = new DDBPFileFilter();
      } catch (Exception var3) {
      }

      return var1;
   }

   private boolean skipLogFiles() {
      boolean var1 = false;
      String[] var2 = (String[])((String[])this._context.get(DomainPlugInConstants.OPTIONAL_GROUPS_KEY));
      var1 = !Arrays.asList(var2).contains("DOMAIN_DIRECTORY_BACKUP_LOG_FILES_INCLUDED_SELECTED_VALUE");
      return var1;
   }

   private class DDBPFileFilter implements FileFilter {
      private File dir;
      private long domainDirSelectionTime;
      private Set logfiles = new HashSet();

      public DDBPFileFilter() {
         this.dir = (File)DomainDirectoryBackupPlugIn.this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
         this.domainDirSelectionTime = ((Date)DomainDirectoryBackupPlugIn.this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_SELECTION_DATE_KEY)).getTime();
         if (DomainDirectoryBackupPlugIn.this.skipLogFiles()) {
            String[] var2 = (String[])((String[])DomainDirectoryBackupPlugIn.this._context.get(DomainPlugInConstants.SERVER_NAMES_KEY));
            DomainMBean var3 = (DomainMBean)DomainDirectoryBackupPlugIn.this._context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
            String var4 = var3.getConfigurationVersion();
            LogMBean var5 = var3.getLog();
            File var6 = new File(var5.getFileName());
            if (!var6.isAbsolute()) {
               if (!var4.startsWith("6") && !var4.startsWith("7")) {
                  var6 = new File(this.dir, var6.getName());
               } else {
                  String var7 = var5.getFileName();
                  if (var7.startsWith("./")) {
                     var7 = var7.substring(2);
                  }

                  var6 = new File(this.dir, var7);
               }
            }

            if (var6.exists()) {
               this.logfiles.add(var6);
            }

            if (var6.getParentFile().exists()) {
               this.logfiles.addAll(Arrays.asList(var6.getParentFile().listFiles(new WeblogicLogfileFilter(new File(var6.getName())))));
            }

            for(int var14 = 0; var14 < var2.length; ++var14) {
               ServerMBean var8 = null;

               try {
                  var8 = var3.lookupServer(var2[var14]);
               } catch (Exception var13) {
               }

               if (var8 != null) {
                  LogMBean var9 = var8.getLog();
                  File var10 = new File(var9.getFileName());
                  File var15;
                  if (!var10.isAbsolute()) {
                     if (!var4.startsWith("6") && !var4.startsWith("7")) {
                        var15 = new File(this.dir, var2[var14]);
                        var10 = new File(var15, var10.getName());
                     } else {
                        String var11 = var9.getFileName();
                        if (var11.startsWith("./")) {
                           var11 = var11.substring(2);
                        }

                        var10 = new File(this.dir, var11);
                     }
                  }

                  var15 = new File(var10.getParentFile(), "access.log");
                  if (var10.exists()) {
                     this.logfiles.add(var10);
                  }

                  if (var15.exists()) {
                     this.logfiles.add(var15);
                  }

                  WeblogicLogfileFilter var12 = new WeblogicLogfileFilter(new File(var10.getName()));
                  if (var10.getParentFile().exists()) {
                     this.logfiles.addAll(Arrays.asList(var10.getParentFile().listFiles(var12)));
                  }
               }
            }

            DomainDirectoryBackupPlugIn.this.updateObservers(new PlugInMessageObservation(DomainDirectoryBackupPlugIn.this.getName(), "Will skip adding " + this.logfiles.size() + " log files to zip archive"));
         }

      }

      public boolean accept(File var1) {
         if (!var1.isDirectory() && var1.lastModified() > this.domainDirSelectionTime) {
            return false;
         } else if ((!var1.getName().startsWith("weblogic-domain-backup") || !var1.getParentFile().equals(this.dir)) && !var1.getName().equals(".wlnotdelete")) {
            if (DomainDirectoryBackupPlugIn.this.skipLogFiles()) {
               return !this.logfiles.contains(var1);
            } else {
               return true;
            }
         } else {
            return false;
         }
      }
   }
}
