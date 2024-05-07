package weblogic.wsee.tools.jws.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weblogic.utils.classloaders.ClasspathClassLoader;
import weblogic.wsee.tools.logging.CompositeLogger;
import weblogic.wsee.tools.logging.CountLogger;
import weblogic.wsee.tools.logging.Logger;

public class JwsBuildContextImpl implements JwsBuildContext {
   public static final String DEFAULT_DEST_ENCODING = "UTF-8";
   public static final String DEFAULT_WSDL_ENCODING = "UTF-8";
   private JwsBuildContext.Task task;
   private CompositeLogger loggers;
   private CountLogger countLogger;
   private String srcInputEncoding;
   private String destEncoding;
   private Map properties;
   private ClassLoader classLoader;
   private String[] classpath;
   private String[] sourcepath;
   private Collection<File> clientGenOutputDirs;

   public JwsBuildContextImpl() {
      this.task = JwsBuildContext.Task.JWSC;
      this.loggers = new CompositeLogger();
      this.countLogger = new CountLogger();
      this.srcInputEncoding = null;
      this.destEncoding = "UTF-8";
      this.properties = new HashMap();
      this.classLoader = null;
      this.classpath = null;
      this.sourcepath = null;
      this.clientGenOutputDirs = new ArrayList();
      this.loggers.addLogger(this.countLogger);
   }

   public Logger getLogger() {
      return this.loggers;
   }

   public boolean isInError() {
      return this.countLogger.getErrorCount() > 0;
   }

   public List<String> getErrorMsgs() {
      return this.countLogger.getErrorMsgs();
   }

   public JwsBuildContext.Task getTask() {
      return this.task;
   }

   public String getSrcEncoding() {
      return this.srcInputEncoding;
   }

   public String getDestEncoding() {
      return this.destEncoding;
   }

   public String getCodegenOutputEncoding() {
      return this.srcInputEncoding;
   }

   public void setSrcEncoding(String var1) {
      this.srcInputEncoding = var1;
   }

   public void setDestEncoding(String var1) {
      this.destEncoding = var1 == null ? "UTF-8" : var1;
   }

   public Collection<File> getClientGenOutputDirs() {
      return this.clientGenOutputDirs;
   }

   public void addLogger(Logger var1) {
      this.loggers.addLogger(var1);
   }

   public Map getProperties() {
      return this.properties;
   }

   public void setTask(JwsBuildContext.Task var1) {
      this.task = var1;
   }

   public ClassLoader getClassLoader() {
      if (this.classLoader == null) {
         this.classLoader = new ClasspathClassLoader();
      }

      return this.classLoader;
   }

   public String[] getClasspath() {
      return this.classpath;
   }

   public void setClasspath(String[] var1, ClassLoaderFactory var2) {
      this.classpath = var1;
      this.classLoader = var2.newInstance(var1);
   }

   public void setClasspath(String[] var1) {
      this.setClasspath(var1, new ClasspathClassLoaderFactory());
   }

   public String[] getSourcepath() {
      return this.sourcepath;
   }

   public void setSourcepath(String[] var1) {
      this.sourcepath = var1;
   }

   public static class ClasspathClassLoaderFactory implements ClassLoaderFactory {
      public ClassLoader newInstance(String[] var1) {
         StringBuilder var2 = new StringBuilder();
         String[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var2.length() > 0) {
               var2.append(File.pathSeparatorChar);
            }

            var2.append(var6);
         }

         return new ClasspathClassLoader(var2.toString(), JwsBuildContextImpl.class.getClassLoader());
      }
   }

   public interface ClassLoaderFactory {
      ClassLoader newInstance(String[] var1);
   }
}
