package weblogic.servlet.internal.dd.compliance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.j2ee.descriptor.FilterBean;
import weblogic.j2ee.descriptor.FilterMappingBean;
import weblogic.j2ee.descriptor.SecurityConstraintBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.ServletMappingBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;
import weblogic.tools.ui.progress.ProgressListener;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.classloaders.GenericClassLoader;

public abstract class BaseComplianceChecker implements ComplianceChecker {
   private static final boolean debug = false;
   protected ProgressListener listener;
   protected boolean executionCancelled;
   protected ArrayList errorList;
   protected WebAppComplianceTextFormatter fmt;
   protected boolean verbose;

   public BaseComplianceChecker() {
      this.executionCancelled = false;
      this.fmt = new WebAppComplianceTextFormatter();
      this.verbose = false;
   }

   public BaseComplianceChecker(ProgressListener var1) {
      this();
      this.listener = var1;
   }

   public void setProgressListener(ProgressListener var1) {
      this.listener = var1;
   }

   public void cancelExecution() {
      this.executionCancelled = true;
   }

   public void update(String var1) {
      if (this.listener != null) {
         this.listener.update(var1);
      } else if (this.verbose) {
         HTTPLogger.logInfo("ComplianceChecker", var1);
      }

   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public void addDescriptorError(ComplianceException var1) {
      if (this.errorList == null) {
         this.errorList = new ArrayList();
      }

      if (var1 != null) {
         this.errorList.add(var1);
      }

   }

   public void addDescriptorError(String var1, DescriptorErrorInfo var2) {
      if (this.errorList == null) {
         this.errorList = new ArrayList();
      }

      if (var2 == null) {
         this.errorList.add(new ComplianceException(var1));
      } else {
         this.errorList.add(new ComplianceException(var1, var2));
      }

   }

   public void addDescriptorError(String var1) {
      this.addDescriptorError(var1, (DescriptorErrorInfo)null);
   }

   public List getDescriptorErrorsAsList() {
      return this.errorList;
   }

   public boolean hasErrors() {
      return this.errorList != null && !this.errorList.isEmpty();
   }

   public void checkForExceptions() throws ErrorCollectionException {
      if (this.hasErrors()) {
         ErrorCollectionException var1 = new ErrorCollectionException();
         List var2 = this.getDescriptorErrorsAsList();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            var1.add((ComplianceException)var3.next());
         }

         var2.clear();
         throw var1;
      }
   }

   public abstract void check(DeploymentInfo var1) throws ErrorCollectionException;

   protected boolean isClassAssignable(ClassLoader var1, String var2, String var3, String var4) {
      Class var5 = null;
      Class var6 = null;

      try {
         var5 = var1.loadClass(var3);
      } catch (ClassNotFoundException var8) {
         this.update(this.fmt.warning() + this.fmt.CLASS_NOT_FOUND(var2, var3));
      } catch (NoClassDefFoundError var9) {
         this.update(this.fmt.warning() + " Error while loading class : " + var3 + var9.getMessage());
      } catch (Exception var10) {
         this.update(this.fmt.warning() + this.fmt.CLASS_NOT_FOUND(var2, var3) + StackTraceUtils.throwable2StackTrace(var10));
      }

      if (var5 == null) {
         return false;
      } else {
         try {
            var6 = var1.loadClass(var4);
         } catch (ClassNotFoundException var11) {
            this.update(this.fmt.warning() + " Unable to load class '" + var4 + "' " + (var1 instanceof GenericClassLoader ? " from the following classpath :" + ((GenericClassLoader)var1).getClassPath() : ""));
            return false;
         } catch (Exception var12) {
            return false;
         }

         if (!var6.isAssignableFrom(var5)) {
            this.addDescriptorError(this.fmt.CLASS_NOT_ASSIGNABLE_FROM(var4, var2, var3));
            return false;
         } else {
            return true;
         }
      }
   }

   public static ComplianceChecker[] makeComplianceCheckers(DeploymentInfo var0) {
      ArrayList var1 = new ArrayList();
      WebAppBean var2 = var0.getWebAppBean();
      if (var2 != null) {
         var1.add(new WebAppDescriptorComplianceChecker());
         ServletMappingBean[] var3 = var2.getServletMappings();
         ServletBean[] var4 = var2.getServlets();
         if (var3 != null || var4 != null) {
            var1.add(new ServletComplianceChecker());
         }

         FilterBean[] var5 = var2.getFilters();
         FilterMappingBean[] var6 = var2.getFilterMappings();
         if (var5 != null || var6 != null) {
            var1.add(new FilterComplianceChecker());
         }

         SecurityConstraintBean[] var7 = var2.getSecurityConstraints();
         if (var7 != null) {
            var1.add(new SecurityConstraintComplianceChecker());
         }

         if (var2.getEjbRefs() != null || var2.getEjbLocalRefs() != null) {
            var1.add(new EJBRefsComplianceChecker());
         }

         if (var2.getEnvEntries() != null && var2.getEnvEntries().length > 0) {
            var1.add(new EnvEntryComplianceChecker());
         }
      }

      WeblogicWebAppBean var8 = var0.getWeblogicWebAppBean();
      if (var8 != null) {
         var1.add(new WebLogicWebAppComplianceChecker());
      }

      if (var1.isEmpty()) {
         HTTPLogger.logInfo("ComplianceChecker", "Could not find elements to be checked for compliance, skipping compliance check");
      }

      return (ComplianceChecker[])((ComplianceChecker[])var1.toArray(new ComplianceChecker[var1.size()]));
   }
}
