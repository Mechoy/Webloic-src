package weblogic.management.provider.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import weblogic.deploy.service.Version;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.DomainDir;
import weblogic.management.bootstrap.BootStrap;
import weblogic.security.internal.SerializedSystemIni;

public class ConfigurationVersion implements Version {
   private static final long serialVersionUID = 239777818908345329L;
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");
   private Map versionMap;

   public ConfigurationVersion() {
      this(false);
   }

   public ConfigurationVersion(boolean var1) {
      this.versionMap = new HashMap();
      if (var1) {
         String var2 = SerializedSystemIni.getPath();
         this.addFileToVersionMap(var2, var2);
         File[] var3 = BootStrap.getConfigFiles();

         for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
            if (!var3[var4].getPath().equals(BootStrap.getConfigLockFileName())) {
               String var5 = var3[var4].getAbsolutePath();
               if (var5.indexOf("configCache/crc.ser") == -1 && var5.indexOf("configCache\\crc.ser") == -1) {
                  this.addFileToVersionMap(var3[var4].getPath(), var3[var4].getPath());
               }
            }
         }

         String var6 = DomainDir.getPathRelativeRootDir("fileRealm.properties");
         if ((new File(var6)).exists()) {
            this.addFileToVersionMap(var6, var6);
         }

      }
   }

   public String getIdentity() {
      return "Configuration";
   }

   public Map getVersionComponents() {
      synchronized(this.versionMap) {
         return new HashMap(this.versionMap);
      }
   }

   public void addOrUpdateFile(String var1, String var2) {
      this.addFileToVersionMap(var1, var2);
   }

   public void removeFile(String var1) {
      String var2 = this.removeRootDirectoryFromPath(var1);
      String var3 = var2.replace('\\', '/');
      synchronized(this.versionMap) {
         this.versionMap.remove(var3);
      }
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 instanceof ConfigurationVersion) {
         Map var2 = this.getVersionComponents();
         Map var3 = ((ConfigurationVersion)var1).getVersionComponents();
         boolean var4 = var2.equals(var3);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("ConfigurationVersion equals '" + var2 + "'.equals('" + var3 + "') : " + var4);
         }

         return var4;
      } else {
         return false;
      }
   }

   private void addFileToVersionMap(String var1, String var2) {
      long var3 = 0L;
      CheckedInputStream var5 = null;

      try {
         var5 = new CheckedInputStream(new FileInputStream(var1), new Adler32());
         byte[] var6 = new byte[128];

         while(true) {
            if (var5.read(var6) < 0) {
               var3 = var5.getChecksum().getValue();
               break;
            }
         }
      } catch (IOException var18) {
      } finally {
         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var16) {
            }
         }

      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Create version for path " + var1 + " with version " + var3);
      }

      String var20 = (new Long(var3)).toString();
      var2 = this.removeRootDirectoryFromPath(var2);
      String var7 = var2.replace('\\', '/');
      synchronized(this.versionMap) {
         this.versionMap.put(var7, var20);
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString()).append("(id: ");
      var1.append(this.getIdentity());
      var1.append(", version vector: ");
      synchronized(this.versionMap) {
         Iterator var3 = this.versionMap.entrySet().iterator();

         while(true) {
            if (!var3.hasNext()) {
               break;
            }

            Map.Entry var4 = (Map.Entry)var3.next();
            String var5 = (String)var4.getKey();
            String var6 = (String)var4.getValue();
            var1.append("[component: ");
            var1.append(var5);
            var1.append(":v:");
            var1.append(var6);
            var1.append("]");
         }
      }

      var1.append(")");
      return var1.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      synchronized(this.versionMap) {
         var1.writeObject(this.versionMap);
      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      synchronized(this.versionMap) {
         this.versionMap.putAll((Map)var1.readObject());
      }
   }

   public String removeRootDirectoryFromPath(String var1) {
      String var2 = (new File(DomainDir.getRootDir())).getPath() + File.separator;
      return !var1.startsWith(var2) && !(new File(var1)).getPath().startsWith((new File(var2)).getPath()) ? var1 : var1.substring(var2.length(), var1.length());
   }
}
