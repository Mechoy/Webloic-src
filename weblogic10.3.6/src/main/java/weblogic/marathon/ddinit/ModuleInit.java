package weblogic.marathon.ddinit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import weblogic.marathon.fs.Entry;
import weblogic.marathon.fs.FS;
import weblogic.marathon.fs.FSFinder;
import weblogic.marathon.fs.FSUtils;
import weblogic.tools.ui.progress.ProgressEvent;
import weblogic.tools.ui.progress.ProgressListener;
import weblogic.tools.ui.progress.ProgressProducer;
import weblogic.utils.classfile.ClassFileBean;
import weblogic.utils.classfile.ClassFileLoader;
import weblogic.utils.classfile.MethodBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;

public abstract class ModuleInit implements ProgressProducer {
   protected boolean m_verbose;
   protected boolean m_silentMode;
   protected FS baseFS;
   protected ClassFinder classFinder;
   protected ClassFileLoader cfLoader;
   private GenericClassLoader m_classLoader;
   protected List filesystems;
   protected List m_classes;
   private boolean modulesFound;
   protected ProgressListener listener;
   protected ProgressEvent event;
   protected boolean executionCancelled;
   private Collection m_errors;
   private Collection m_smartCompErrors;

   protected ModuleInit(FS var1) {
      this(var1, true);
   }

   protected ModuleInit(FS var1, boolean var2) {
      this.m_verbose = false;
      this.m_silentMode = false;
      this.filesystems = new ArrayList();
      this.m_classes = new ArrayList();
      this.event = new ProgressEvent();
      this.executionCancelled = false;
      this.m_errors = new ArrayList();
      this.m_smartCompErrors = new ArrayList();
      this.setBaseFS(var1);
      this.classFinder = this.makeFinder();
      this.cfLoader = new ClassFileLoader(this.classFinder);
      this.cfLoader.setParent(ClassFileLoader.getSystem());
      this.m_classLoader = new GenericClassLoader(this.classFinder);
      this.setProgressListener(new CmdLineListener());
      if (var2) {
         this.loadClasses();
      }

   }

   protected abstract void searchForComponents();

   protected abstract void initDescriptors();

   public void execute() throws Exception {
      this.searchForComponents();
      if (this.getFound()) {
         this.initDescriptors();
         this.writeDescriptors();
      }

   }

   public ClassFileBean getModuleClass(String var1) {
      ClassFileBean var2 = null;
      Iterator var3 = this.getClasses().iterator();

      while(var3.hasNext()) {
         ClassFileBean var4 = (ClassFileBean)var3.next();
         if (var4.getName().equals(var1)) {
            var2 = var4;
            break;
         }
      }

      return var2;
   }

   protected void writeDescriptors() throws IOException {
   }

   protected void warn(String var1) {
      if (this.listener != null) {
         this.listener.update(var1, 0);
      }

   }

   public void setVerbose(boolean var1) {
      this.m_verbose = var1;
   }

   public boolean isVerbose() {
      return this.m_verbose;
   }

   public void setSilentMode(boolean var1) {
      this.m_silentMode = var1;
   }

   public boolean isSilentMode() {
      return this.m_silentMode;
   }

   public void setFound(boolean var1) {
      this.modulesFound = var1;
   }

   public boolean getFound() {
      return this.modulesFound;
   }

   public FS getBaseFS() {
      return this.baseFS;
   }

   public void setBaseFS(FS var1) {
      if (this.baseFS != null) {
         try {
            this.baseFS.close();
         } catch (Exception var3) {
         }
      }

      this.baseFS = var1;
   }

   public List getClasses() {
      return this.m_classes;
   }

   public void setClasses(List var1) {
      this.m_classes = var1;
   }

   protected String getClassPrefixPath() {
      return "";
   }

   protected ClassFinder makeFinder() {
      return this.classFinder = new FSFinder(this.baseFS);
   }

   protected void loadClasses() {
      if (this.getClasses().size() <= 0) {
         this.m_classes = new ArrayList();
         String[] var1 = new String[0];

         try {
            var1 = FSUtils.getPaths(this.baseFS, this.getClassPrefixPath(), ".class");
         } catch (IOException var10) {
            this.inform("Error while searching for class files in " + this.baseFS.getPath());
            this.inform(var10.getMessage());
         }

         for(int var2 = 0; var2 < var1.length; ++var2) {
            ClassFileBean var3 = null;

            try {
               Entry var4 = this.baseFS.getEntry(var1[var2]);
               InputStream var5 = var4.getInputStream();

               try {
                  var3 = ClassFileBean.load(var5);
                  if (var3.getName().indexOf("CMP_RDBMS") > -1 || var3.getName().endsWith("Impl") && var3.getName().indexOf("_") > -1) {
                     continue;
                  }
               } finally {
                  var5.close();
               }
            } catch (Exception var12) {
               var12.printStackTrace();
               continue;
            }

            String var13 = var1[var2].substring(0, var1[var2].indexOf("."));
            var13 = var13.replace('/', '.');
            if (var13.equals(var3.getName())) {
               this.m_classes.add(var3);
            }
         }

      }
   }

   private void purgeClasses(List var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(true) {
         Class var4;
         String var5;
         do {
            if (!var3.hasNext()) {
               var3 = var2.iterator();

               while(var3.hasNext()) {
                  var1.remove(var3.next());
               }

               return;
            }

            var4 = (Class)var3.next();
            var5 = var4.getName();
         } while(-1 == var5.indexOf("CMP_RDBMS") && !var5.endsWith("Impl"));

         var2.add(var4);
      }
   }

   private void removeBaseClasses(List var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            Class var6 = (Class)var5.next();
            if (var4 != var6 && var6.isAssignableFrom(var4)) {
               var2.add(var6);
            }
         }
      }

      var3 = var2.iterator();

      while(var3.hasNext()) {
         var1.remove(var3.next());
      }

   }

   private void dumpList(String var1, List var2) {
      ppp("===\n" + var1);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         ppp("  " + var3.next());
      }

      ppp("===");
   }

   public void setProgressListener(ProgressListener var1) {
      this.listener = var1;
   }

   public void cancelExecution() {
      this.executionCancelled = true;
   }

   private static void ppp(String var0) {
      System.out.println("[ModuleInit] " + var0);
   }

   protected void verbose(String var1) {
      if (this.m_verbose) {
         this.inform(var1);
      }

   }

   public Collection getErrors() {
      return this.m_errors;
   }

   protected void addErrors(Collection var1) {
      this.m_errors.addAll(var1);
   }

   protected void inform(String var1) {
      if (!this.m_silentMode) {
         if (this.listener != null) {
            this.listener.update(var1);
         } else {
            System.out.println("[ModuleInit]: " + var1);
         }
      }

   }

   protected boolean hasMethod(ClassFileBean var1, MethodBean var2) {
      MethodBean[] var3 = var1.getPublicMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var2.equals(var3[var4])) {
            return true;
         }
      }

      return false;
   }

   public boolean hasInterface(ClassFileBean var1, String var2) {
      if (var1 == null) {
         return false;
      } else {
         String[] var3 = var1.getInterfaces();

         ClassFileBean var5;
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var2.equals(var3[var4])) {
               return true;
            }

            try {
               var5 = this.cfLoader.loadClass(var3[var4]);
               if (this.hasInterface(var5, var2)) {
                  return true;
               }
            } catch (ClassNotFoundException var7) {
            }
         }

         String var8 = var1.getSuperName();
         if (var8.equals("java.lang.Object")) {
            return false;
         } else {
            try {
               var5 = this.cfLoader.loadClass(var8);
               if (var5 != null) {
                  return this.hasInterface(var5, var2);
               }
            } catch (ClassNotFoundException var6) {
            }

            return false;
         }
      }
   }

   private class CmdLineListener implements ProgressListener {
      private CmdLineListener() {
      }

      public void updateProgress(ProgressEvent var1) {
      }

      public void update(String var1) {
         System.out.println(var1);
      }

      public void update(String var1, int var2) {
      }

      public void setProgressProducer(ProgressProducer var1) {
      }

      // $FF: synthetic method
      CmdLineListener(Object var2) {
         this();
      }
   }
}
