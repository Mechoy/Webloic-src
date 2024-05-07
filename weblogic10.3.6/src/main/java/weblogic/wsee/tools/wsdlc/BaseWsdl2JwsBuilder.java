package weblogic.wsee.tools.wsdlc;

import java.io.File;
import javax.xml.namespace.QName;
import weblogic.utils.classloaders.ClasspathClassLoader;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.logging.DefaultLogger;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.StringUtil;

public abstract class BaseWsdl2JwsBuilder<T> implements Wsdl2JwsBuilder<T> {
   protected String wsdl;
   protected String wsdlLocation;
   protected String packageName;
   protected QName portName;
   protected File destDir;
   protected File destImplDir;
   protected Logger logger = new DefaultLogger();
   protected ClassLoader classLoader;
   protected String classpath;
   protected File[] bindingFiles;

   public void setClasspath(String[] var1) {
      StringBuilder var2 = new StringBuilder();
      if (var1 != null) {
         String[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var2.length() > 0) {
               var2.append(File.pathSeparatorChar);
            }

            var2.append(var6);
         }
      }

      this.classpath = var2.toString();
      this.classLoader = new ClasspathClassLoader(this.classpath, BaseWsdl2JwsBuilder.class.getClassLoader());
   }

   public void setWsdl(String var1) {
      if (StringUtil.isEmpty(var1)) {
         throw new IllegalArgumentException("No wsdl specified");
      } else {
         this.wsdl = var1;
      }
   }

   public void setWsdlLocation(String var1) {
      this.wsdlLocation = var1;
   }

   public void setPortName(QName var1) {
      this.portName = var1;
   }

   public void setPackageName(String var1) {
      this.packageName = var1;
   }

   public void setDestDir(File var1) {
      if (var1 == null) {
         throw new NullPointerException("destDir");
      } else {
         this.destDir = var1;
      }
   }

   public void setDestImplDir(File var1) {
      this.destImplDir = var1;
   }

   public void setLogger(Logger var1) {
      if (var1 == null) {
         this.logger = new DefaultLogger();
      } else {
         this.logger = var1;
      }

   }

   public void setBindingFiles(File[] var1) {
      this.bindingFiles = var1;
   }

   public final void execute() throws WsBuildException {
      this.validate();
      this.executeImpl();
   }

   private void validate() throws WsBuildException {
      boolean var1 = true;
      if (this.destDir == null) {
         this.logger.log(EventLevel.ERROR, "Dest dir must be specified");
         var1 = false;
      }

      if (StringUtil.isEmpty(this.wsdl)) {
         this.logger.log(EventLevel.ERROR, "Wsdl must be specified");
         var1 = false;
      }

      if (!var1) {
         throw new WsBuildException("Wsdl2JwsBuilder invalid - see log for details");
      }
   }

   protected abstract void executeImpl() throws WsBuildException;
}
