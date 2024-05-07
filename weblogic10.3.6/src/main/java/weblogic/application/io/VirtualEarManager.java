package weblogic.application.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.WebBean;

public final class VirtualEarManager {
   public static final String PAT_DIRECT_DESC = "*";
   public static final String PAT_DIRECT_DESC_OR_SELF = ".*";
   public static final String PAT_ALL_DESC = "**";
   public static final String PAT_ALL_DESC_OR_SELF = ".**";
   private static final String FILE_NAME = ".beabuild.txt";
   private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Win");
   private static final String LINE_SEPARATOR = System.getProperty("line.separator");
   private static final String LINKING_MSG = "Linking {0} to {1}.";
   private static final String NO_MODULES = "Application contains no deployable modules.";
   private final String earRootPath;
   private final ArrayList links;
   private boolean linksDirty;
   private ApplicationBean appBean;
   private boolean appBeanDirty;

   public VirtualEarManager(String var1) throws VirtualEarException {
      this.earRootPath = normalize(var1);
      this.links = new ArrayList();
      this.linksDirty = false;
      this.appBean = null;
      this.appBeanDirty = false;
      File var2 = new File(var1, ".beabuild.txt");
      if (var2.exists()) {
         BufferedReader var3 = null;

         try {
            var3 = new BufferedReader(new FileReader(var2));
            byte var4 = 1;
            StringBuffer var8 = null;
            StringBuffer var9 = null;
            int var10 = var3.read();

            while(true) {
               if (var10 == -1) {
                  if (var4 == 3) {
                     this._link(unescape(var8.toString().trim()), unescape(var9.toString().trim()));
                  } else if (var4 == 2) {
                     throw new VirtualEarException(1);
                  }
                  break;
               }

               if (var10 != 10 && var10 != 13) {
                  if (var10 == 61) {
                     if (var4 != 2) {
                        throw new VirtualEarException(1);
                     }

                     var9 = new StringBuffer();
                     var4 = 3;
                  } else if (var4 == 1) {
                     var8 = new StringBuffer();
                     var8.append((char)var10);
                     var4 = 2;
                  } else if (var4 == 2) {
                     var8.append((char)var10);
                  } else {
                     var9.append((char)var10);
                  }
               } else if (var4 == 3) {
                  this._link(unescape(var8.toString().trim()), unescape(var9.toString().trim()));
                  var4 = 1;
               } else if (var4 == 2) {
                  throw new VirtualEarException(1);
               }

               var10 = var3.read();
            }
         } catch (IOException var19) {
            throw new VirtualEarException(var19);
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (IOException var18) {
               }
            }

         }

      }
   }

   public void link(String var1, String var2) throws VirtualEarException {
      File var3 = new File(var1);
      if (var3.exists()) {
         String[] var4 = this.resolve(var2);
         StringBuffer var5 = new StringBuffer();

         for(int var6 = 0; var6 < var4.length; ++var6) {
            File var7 = new File(var4[var6]);
            if (!var3.equals(var7) && (var3.isFile() && var7.isDirectory() || var3.isDirectory() && var7.isFile() || var3.isFile() && var7.isFile())) {
               if (var5.length() > 0) {
                  var5.append("\n                ");
               }

               var5.append(var4[var6]);
            }
         }

         if (var5.length() > 0) {
            String[] var8 = new String[]{toPlatformFormat(normalize(var1)), toPlatformFormat(normalize(var2)), toPlatformFormat(var5.toString())};
            throw new VirtualEarException(0, var8);
         }
      }

      if (this._link(var1, var2)) {
         this.linksDirty = true;
         this.write();
      }

   }

   private boolean _link(String var1, String var2) {
      Link var3;
      if (var1.equals("bea.srcdir")) {
         var3 = new Link(var2, "");
      } else {
         var3 = new Link(var1, var2);
      }

      if (!this.links.contains(var3)) {
         this.links.add(var3);
         return true;
      } else {
         return false;
      }
   }

   public void unlink(String var1, String var2) throws VirtualEarException {
      Link[] var3 = this.search(var1, var2);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         this.links.remove(var3[var4]);
         this.linksDirty = true;
      }

      this.write();
   }

   public Link[] search(String var1, String var2) {
      ArrayList var3 = new ArrayList();
      String[] var4 = split(var1);
      String[] var5 = split(var2);
      int var6 = 0;

      for(int var7 = this.links.size(); var6 < var7; ++var6) {
         Link var8 = (Link)this.links.get(var6);
         String[] var9 = split(var8.getSourcePath());
         String[] var10 = split(var8.getTargetPath());
         if (match(var9, var4) && match(var10, var5)) {
            var3.add(var8);
         }
      }

      return (Link[])((Link[])var3.toArray(new Link[var3.size()]));
   }

   private static boolean match(String[] var0, String[] var1) {
      if (var1 == null) {
         return true;
      } else {
         int var2 = 0;

         while(true) {
            String var3;
            if (var2 < var0.length && var2 < var1.length) {
               var3 = var0[var2];
               String var4 = var1[var2];
               if ((!IS_WINDOWS || var3.equalsIgnoreCase(var4)) && (IS_WINDOWS || var3.equals(var4))) {
                  ++var2;
                  continue;
               }

               boolean var5 = var2 + 1 == var0.length;
               if (!var4.equals("**") && !var4.equals(".**") && (!var4.equals("*") || !var5) && (!var4.equals(".*") || !var5)) {
                  return false;
               }

               return true;
            }

            if (var2 < var0.length) {
               return false;
            }

            if (var2 >= var1.length) {
               return true;
            }

            var3 = var1[var2];
            if (var2 + 1 != var1.length || !var3.equals(".*") && !var3.equals(".**")) {
               return false;
            }

            return true;
         }
      }
   }

   public String[] list(String var1) {
      HashSet var2 = new HashSet();
      String var3 = normalize(var1);
      String[] var4 = this.resolve(var1);

      for(int var5 = 0; var5 < var4.length; ++var5) {
         String var10000 = var4[var5];
         File var7 = new File(var1);
         if (!var7.isDirectory()) {
            throw new IllegalArgumentException("not a directory");
         }

         String[] var8 = var7.list();

         for(int var9 = 0; var9 < var8.length; ++var9) {
            var2.add(var3 + "/" + var8[var9]);
         }
      }

      return (String[])((String[])var2.toArray(new String[var2.size()]));
   }

   public String[] resolve(String var1) {
      String[] var2 = split(var1);
      ArrayList var3 = new ArrayList();
      var3.add(this.earRootPath + "/" + var1);
      int var4 = 0;

      for(int var5 = this.links.size(); var4 < var5; ++var4) {
         Link var6 = (Link)this.links.get(var4);
         String[] var7 = split(var6.getTargetPath());

         int var8;
         for(var8 = 0; var8 < var2.length && var8 < var7.length; ++var8) {
            String var9 = var2[var8];
            String var10 = var7[var8];
            if (IS_WINDOWS && !var9.equalsIgnoreCase(var10) || !IS_WINDOWS && !var9.equals(var10)) {
               break;
            }
         }

         if (var8 == var7.length) {
            if (var8 == var2.length) {
               var3.add(var6.getSourcePath());
            } else {
               var3.add(var6.getSourcePath() + merge(var2, var8, var2.length - var8));
            }
         }
      }

      Iterator var11 = var3.iterator();

      while(var11.hasNext()) {
         if (!(new File((String)var11.next())).exists()) {
            var11.remove();
         }
      }

      return (String[])((String[])var3.toArray(new String[var3.size()]));
   }

   public void addEjbModule(String var1) throws VirtualEarException {
      ApplicationBean var2 = this.getApplicationBean(true);
      ModuleBean[] var3 = var2.getModules();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var1.equals(var3[var4].getEjb())) {
            return;
         }
      }

      ModuleBean var5 = var2.createModule();
      var5.setEjb(var1);
      this.appBeanDirty = true;
      this.write();
   }

   public void addWebModule(String var1, String var2) throws VirtualEarException {
      ApplicationBean var3 = this.getApplicationBean(true);
      ModuleBean[] var4 = var3.getModules();
      boolean var5 = false;

      WebBean var7;
      for(int var6 = 0; var6 < var4.length && !var5; ++var6) {
         var7 = var4[var6].getWeb();
         if (var7 != null && var7.getWebUri().equals(var1)) {
            var5 = true;
            if (!var7.getContextRoot().equals(var2)) {
               var7.setContextRoot(var2);
               this.appBeanDirty = true;
            }
         }
      }

      if (!var5) {
         ModuleBean var8 = var3.createModule();
         var7 = var8.createWeb();
         var7.setWebUri(var1);
         var7.setContextRoot(var2);
         this.appBeanDirty = true;
      }

      this.write();
   }

   public void removeEjbModule(String var1) throws VirtualEarException {
      boolean var2 = var1.endsWith("*");
      String var3 = var2 ? var1.substring(0, var1.length() - 1) : var1;
      ApplicationBean var4 = this.getApplicationBean(true);
      ModuleBean[] var5 = var4.getModules();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         ModuleBean var7 = var5[var6];
         String var8 = var7.getEjb();
         if (var8 != null && (var2 && var8.startsWith(var3) || !var2 && var8.equals(var3))) {
            var4.destroyModule(var7);
            this.appBeanDirty = true;
         }
      }

      this.write();
   }

   public void removeWebModule(String var1) throws VirtualEarException {
      ApplicationBean var2 = this.getApplicationBean(true);
      ModuleBean[] var3 = var2.getModules();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         ModuleBean var5 = var3[var4];
         WebBean var6 = var5.getWeb();
         if (var6 != null && var6.getWebUri().equals(var1)) {
            var2.destroyModule(var5);
            this.appBeanDirty = true;
            break;
         }
      }

      this.write();
   }

   public ApplicationBean getApplicationBean(boolean var1) throws VirtualEarException {
      this.appBean = null;
      File var2 = new File(this.earRootPath, "META-INF/application.xml");
      if (this.appBean != null && !var2.exists()) {
         this.appBean = null;
      }

      if (this.appBean == null) {
         if (var2.exists()) {
            FileInputStream var3;
            try {
               var3 = new FileInputStream(var2);
            } catch (IOException var14) {
               throw new VirtualEarException(var14);
            }

            try {
               try {
                  this.appBean = (ApplicationBean)(new EditableDescriptorManager()).createDescriptor(var3).getRootBean();
               } finally {
                  try {
                     var3.close();
                  } catch (IOException var12) {
                  }

               }
            } catch (Exception var15) {
               if (!var2.delete()) {
                  throw new VirtualEarException(2);
               }
            }
         }

         if (!var2.exists() && var1) {
            this.appBean = (ApplicationBean)(new EditableDescriptorManager()).createDescriptorRoot(ApplicationBean.class).getRootBean();
            int var16 = this.earRootPath.lastIndexOf("/");
            if (var16 == -1) {
               var16 = this.earRootPath.lastIndexOf("\\");
            }

            String var4 = this.earRootPath.substring(var16 + 1);
            this.appBean.setDisplayNames(new String[]{var4});
         }
      }

      return this.appBean;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;

      for(int var3 = this.links.size(); var2 < var3; ++var2) {
         Link var4 = (Link)this.links.get(var2);
         var4.toString(var1);
         var1.append(LINE_SEPARATOR);
      }

      return var1.toString();
   }

   private void write() throws VirtualEarException {
      File var2;
      if (this.linksDirty) {
         FileWriter var1 = null;

         try {
            var2 = new File(this.earRootPath, ".beabuild.txt");
            var1 = new FileWriter(var2);
            var1.write(this.toString());
         } catch (IOException var25) {
            throw new VirtualEarException(var25);
         } finally {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (IOException var23) {
               }
            }

         }
      }

      if (this.appBeanDirty) {
         FileOutputStream var28 = null;

         try {
            var2 = new File(this.earRootPath, "META-INF");
            var2.mkdirs();
            File var3 = new File(var2, "application.xml");
            var28 = new FileOutputStream(var3);
            (new EditableDescriptorManager()).writeDescriptorAsXML(((DescriptorBean)this.appBean).getDescriptor(), var28);
         } catch (IOException var24) {
            throw new VirtualEarException(var24);
         } finally {
            if (var28 != null) {
               try {
                  var28.close();
               } catch (IOException var22) {
               }
            }

         }
      }

   }

   private static String[] split(String var0) {
      if (var0 == null) {
         return null;
      } else {
         StringTokenizer var1 = new StringTokenizer(var0, "/\\");
         String[] var2 = new String[var1.countTokens()];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = var1.nextToken();
         }

         return var2;
      }
   }

   private static String merge(String[] var0, int var1, int var2) {
      StringBuffer var3 = new StringBuffer();
      int var4 = var1;

      for(int var5 = var1 + var2; var4 < var5; ++var4) {
         if (var4 != var1) {
            var3.append('/');
         }

         var3.append(var0[var4]);
      }

      return var3.toString();
   }

   private static String escape(String var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;

      for(int var3 = var0.length(); var2 < var3; ++var2) {
         char var4 = var0.charAt(var2);
         if (var4 == ':') {
            var1.append("\\:");
         } else if (var4 == ' ') {
            var1.append("\\ ");
         } else if (var4 == '\\') {
            var1.append("\\\\");
         } else {
            var1.append(var4);
         }
      }

      return var1.toString();
   }

   private static String unescape(String var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;

      for(int var3 = var0.length(); var2 < var3; ++var2) {
         char var4 = var0.charAt(var2);
         if (var4 == '\\' && var2 + 1 < var3) {
            var1.append(var0.charAt(var2 + 1));
            ++var2;
         } else {
            var1.append(var4);
         }
      }

      return var1.toString();
   }

   private static String normalize(String var0) {
      File var1 = new File(var0);
      if (var1.isAbsolute()) {
         try {
            var0 = var1.getCanonicalPath();
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      }

      var0 = var0.replace(File.separatorChar, '/');
      if (var0.endsWith("/")) {
         var0 = var0.substring(0, var0.length() - 1);
      }

      return var0;
   }

   private static String toPlatformFormat(String var0) {
      return File.separatorChar == '/' ? var0 : var0.replace('/', File.separatorChar);
   }

   public abstract static class AbstractModuleTask extends AbstractTask {
      protected String uri = null;

      public void setUri(String var1) {
         this.uri = var1;
      }
   }

   public abstract static class AbstractLinkUnlinkTask extends AbstractTask {
      protected String sourcePath = null;
      protected String targetPath = null;

      public void setSource(String var1) {
         this.sourcePath = var1;
      }

      public void setTarget(String var1) {
         this.targetPath = var1;
      }
   }

   public abstract static class AbstractTask extends Task {
      protected String earRootPath = null;

      public void setEar(String var1) {
         this.earRootPath = var1;
      }

      public void execute() throws BuildException {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();
         var1.setContextClassLoader(this.getClass().getClassLoader());
         if (System.getProperty("weblogic.home") == null) {
            String var3 = this.getProject().getProperty("wl.home");
            if (var3 != null) {
               System.setProperty("weblogic.home", var3);
            }
         }

         try {
            this._execute(VirtualEarManager.Cache.get(this.earRootPath));
         } catch (VirtualEarException var7) {
            this.error(var7.getMessage());
            throw new BuildException();
         } finally {
            var1.setContextClassLoader(var2);
         }

      }

      protected abstract void _execute(VirtualEarManager var1) throws VirtualEarException;

      protected void info(String var1) {
         this.log(var1, 2);
      }

      protected void warning(String var1) {
         this.log(var1, 1);
      }

      protected void error(String var1) {
         this.log(var1, 0);
      }

      public void log(String var1, int var2) {
         StringBuffer var3 = new StringBuffer();

         for(int var4 = 0; var4 < var1.length(); ++var4) {
            char var5 = var1.charAt(var4);
            if (var5 == '\n') {
               if (var3.length() == 0) {
                  var3.append(' ');
               }

               super.log(var3.toString(), var2);
               var3 = new StringBuffer();
            } else {
               var3.append(var5);
            }
         }

         if (var3.length() > 0) {
            super.log(var3.toString(), var2);
         }

      }
   }

   public static final class Validate extends AbstractTask {
      protected void _execute(VirtualEarManager var1) throws VirtualEarException {
         ApplicationBean var2 = var1.getApplicationBean(true);
         if (var2.getModules().length == 0) {
            this.warning("Application contains no deployable modules.");
         }

      }
   }

   public static final class RemoveWebModuleTask extends AbstractModuleTask {
      protected void _execute(VirtualEarManager var1) throws VirtualEarException {
         var1.removeWebModule(this.uri);
      }
   }

   public static final class RemoveEjbModuleTask extends AbstractModuleTask {
      protected void _execute(VirtualEarManager var1) throws VirtualEarException {
         var1.removeEjbModule(this.uri);
      }
   }

   public static final class AddWebModuleTask extends AbstractModuleTask {
      private String contextPath;

      public void setContextPath(String var1) {
         this.contextPath = var1;
      }

      protected void _execute(VirtualEarManager var1) throws VirtualEarException {
         var1.addWebModule(this.uri, this.contextPath);
      }
   }

   public static final class AddEjbModuleTask extends AbstractModuleTask {
      protected void _execute(VirtualEarManager var1) throws VirtualEarException {
         var1.addEjbModule(this.uri);
      }
   }

   public static final class UnlinkTask extends AbstractLinkUnlinkTask {
      protected void _execute(VirtualEarManager var1) throws VirtualEarException {
         var1.unlink(this.sourcePath, this.targetPath);
      }
   }

   public static final class LinkTask extends AbstractLinkUnlinkTask {
      protected void _execute(VirtualEarManager var1) throws VirtualEarException {
         Object[] var2 = new Object[]{VirtualEarManager.toPlatformFormat(VirtualEarManager.normalize(this.sourcePath)), VirtualEarManager.toPlatformFormat(VirtualEarManager.normalize(this.targetPath))};
         String var3 = MessageFormat.format("Linking {0} to {1}.", var2);
         this.info(var3);
         var1.link(this.sourcePath, this.targetPath);
      }
   }

   public static final class Link {
      private String source;
      private String target;

      private Link(String var1, String var2) {
         File var3 = new File(var1);
         File var4 = new File(var2);
         if (!var3.isAbsolute()) {
            throw new IllegalArgumentException("Source should be absolute.");
         } else if (var4.isAbsolute()) {
            throw new IllegalArgumentException("Target should be relative.");
         } else {
            this.source = VirtualEarManager.normalize(var1);
            this.target = VirtualEarManager.normalize(var2);
         }
      }

      public boolean equals(Object var1) {
         Link var2 = (Link)var1;
         return this.getSourceFile().equals(var2.getSourceFile()) && this.getTargetFile().equals(var2.getTargetFile());
      }

      public int hashCode() {
         return this.getSourceFile().hashCode() ^ this.getTargetFile().hashCode();
      }

      public String getSourcePath() {
         return this.source;
      }

      public File getSourceFile() {
         return new File(this.source);
      }

      public String getTargetPath() {
         return this.target;
      }

      public File getTargetFile() {
         return new File(this.target);
      }

      public String toString() {
         return this.toString(new StringBuffer()).toString();
      }

      private StringBuffer toString(StringBuffer var1) {
         var1.append(VirtualEarManager.escape(this.source));
         var1.append(" = ");
         var1.append(VirtualEarManager.escape(this.target));
         return var1;
      }

      // $FF: synthetic method
      Link(String var1, String var2, Object var3) {
         this(var1, var2);
      }
   }

   public static final class Cache {
      private static final HashMap cache = new HashMap();

      public static VirtualEarManager get(String var0) throws VirtualEarException {
         VirtualEarManager var1 = (VirtualEarManager)cache.get(var0);
         if (var1 == null) {
            var1 = new VirtualEarManager(var0);
         }

         return var1;
      }

      public static void remove(String var0) {
         cache.remove(var0);
      }
   }
}
