package weblogic.management.provider.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.internal.PendingDirectoryManager;
import weblogic.management.internal.ProductionModeHelper;

public final class DescriptorHelper {
   public static final String BINDING_CONFIG = "schema/weblogic-domain-binding.jar.jar";
   private static boolean skipSetProductionMode = false;
   public static final String RECOVERY_EXTENSION = ".recovery";
   public static final String RECOVERY_NEW_EXTENSION = ".recovery_new";

   public static boolean saveDescriptorTree(Descriptor var0, boolean var1, String var2) throws IOException {
      return saveDescriptorTree(var0, var1, var2, (String)null);
   }

   public static boolean saveDescriptorTree(Descriptor var0, boolean var1, String var2, String var3) throws IOException {
      boolean var4 = false;
      File var5 = null;
      File var6 = null;
      boolean var7 = false;
      OutputStream var8 = null;
      PendingDirectoryManager var9 = PendingDirectoryManager.getInstance();

      try {
         if (var0.isModified() || !var1) {
            EditableDescriptorManager var10 = (EditableDescriptorManager)DescriptorManagerHelper.getDescriptorManager(true);
            if (var1) {
               var5 = new File(DomainDir.getPathRelativePendingDir("config.xml"));
               if (var9.configExists()) {
                  var6 = getRecoveryFile(var5);
                  var6.delete();
                  var5.renameTo(var6);
               } else {
                  var6 = getRecoveryFileForNewFile(var5);
                  var7 = true;
               }

               var8 = PendingDirectoryManager.getInstance().getConfigOutputStream();
            } else {
               var8 = getFileOutputStream(var2, "config.xml");
            }

            var10.writeDescriptorAsXML(var0, var8, var3);
            var4 = true;
            var8.flush();
            var8.close();
            var8 = null;
            var5 = null;
            if (var6 != null) {
               var6.delete();
               var6 = null;
            }
         }

         Iterator var19 = DescriptorInfoUtils.getDescriptorInfos(var0);

         while(true) {
            DescriptorInfo var11;
            Descriptor var12;
            do {
               if (var19 == null || !var19.hasNext()) {
                  boolean var20 = var4;
                  return var20;
               }

               var11 = (DescriptorInfo)var19.next();
               var12 = var11.getDescriptor();
            } while(!var12.isModified() && var12 != var0 && var1);

            EditableDescriptorManager var13 = (EditableDescriptorManager)var11.getDescriptorManager();
            DescriptorBean var14 = var11.getDescriptorBean();
            if (var1) {
               String var15 = var11.getConfigurationExtension().getDescriptorFileName();
               var5 = new File(DomainDir.getPathRelativePendingDir(var15));
               if (var9.fileExists(var15)) {
                  var6 = getRecoveryFile(var5);
                  var6.delete();
                  var5.renameTo(var6);
                  var7 = false;
               } else {
                  var6 = getRecoveryFileForNewFile(var5);
                  var7 = true;
               }

               var8 = var9.getFileOutputStream(var15);
            } else {
               var8 = getFileOutputStream(var2, var11.getConfigurationExtension().getDescriptorFileName());
            }

            var13.writeDescriptorAsXML(var14.getDescriptor(), var8, var3);
            var4 = true;
            var8.close();
            var8 = null;
            var5 = null;
            if (var6 != null) {
               var6.delete();
               var6 = null;
            }
         }
      } finally {
         if (var8 != null) {
            var8.close();
            if (var5 != null) {
               var5.delete();
            }
         }

         if (var6 != null) {
            if (var7) {
               var6.delete();
            } else {
               var6.renameTo(var5);
            }
         }

      }
   }

   private static OutputStream getFileOutputStream(String var0, String var1) throws IOException {
      String var2 = null;
      if (var0 != null) {
         var2 = var0 + File.separator + "config" + File.separator + var1;
      } else {
         var2 = DomainDir.getPathRelativeConfigDir(var1);
      }

      File var3 = new File(var2);
      File var4 = var3.getParentFile();
      if (!var4.exists()) {
         var4.mkdirs();
      }

      return new FileOutputStream(var3);
   }

   private static File getRecoveryFile(File var0) throws IOException {
      String var1 = var0.getPath() + ".recovery";
      return new File(var1);
   }

   private static File getRecoveryFileForNewFile(File var0) throws IOException {
      String var1 = var0.getPath() + ".recovery_new";
      File var2 = new File(var1);
      File var3 = var2.getParentFile();
      if (!var3.exists()) {
         var3.mkdirs();
      }

      var2.createNewFile();
      return var2;
   }

   public static void setDescriptorTreeProductionMode(Descriptor var0, boolean var1) {
      if (!skipSetProductionMode) {
         var0.setProductionMode(var1);
         Iterator var2 = DescriptorInfoUtils.getDescriptorInfos(var0);

         while(var2 != null && var2.hasNext()) {
            DescriptorInfo var3 = (DescriptorInfo)var2.next();
            Descriptor var4 = var3.getDescriptor();
            var4.setProductionMode(var1);
         }

      }
   }

   public static void setDescriptorManagerProductionModeIfNeeded(Descriptor var0, boolean var1) {
      if (var1 && !ProductionModeHelper.isProductionModePropertySet()) {
         DescriptorBean var2 = var0.getRootBean();
         if (var2 instanceof DomainMBean && !var0.isEditable()) {
            DescriptorManagerHelper.getDescriptorManager(true).setProductionMode(true);
            DescriptorManagerHelper.getDescriptorManager(false).setProductionMode(true);
         }
      }

   }

   public static boolean recoverPendingDirectory(File[] var0) {
      int var1 = 0;

      for(int var2 = 0; var2 < var0.length; ++var2) {
         String var3 = var0[var2].getPath();
         File var4;
         if (var3.endsWith(".recovery")) {
            var4 = new File(var3.substring(0, var3.lastIndexOf(".recovery")));
            var4.delete();
            var0[var2].renameTo(var4);
         } else if (var3.endsWith(".recovery_new")) {
            var4 = new File(var3.substring(0, var3.lastIndexOf(".recovery_new")));
            var4.delete();
            var0[var2].delete();
            ++var1;
         }
      }

      if (var1 == 0) {
         return true;
      } else {
         File[] var5 = PendingDirectoryManager.getInstance().getAllFiles();
         if (var5 != null && var5.length != 0) {
            return true;
         } else {
            return false;
         }
      }
   }

   public static void setSkipSetProductionMode(boolean var0) {
      skipSetProductionMode = var0;
   }
}
