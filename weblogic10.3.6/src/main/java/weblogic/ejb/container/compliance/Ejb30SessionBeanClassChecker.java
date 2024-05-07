package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.SessionBean;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.utils.ErrorCollectionException;

public final class Ejb30SessionBeanClassChecker extends SessionBeanClassChecker {
   private Ejb3SessionBeanInfo sbi;
   private DeploymentInfo di;
   private EjbJarBean ejbJarBean;
   private boolean isStateful;

   public Ejb30SessionBeanClassChecker(ClientDrivenBeanInfo var1) throws ClassNotFoundException {
      super(var1);
      this.sbi = (Ejb3SessionBeanInfo)var1;
      this.di = this.sbi.getDeploymentInfo();
      EjbDescriptorBean var2 = this.di.getEjbDescriptorBean();
      this.ejbJarBean = var2.getEjbJarBean();
      this.isStateful = this.sbi.isStateful();
   }

   public void checkSBInterfaceConstraints() throws ComplianceException {
      String var1 = this.sbi.getBeanClassName();
      String var2 = this.sbi.getEJBName();
      LifecycleCallbackBean[] var3 = null;
      LifecycleCallbackBean[] var4 = null;
      LifecycleCallbackBean[] var5 = null;
      LifecycleCallbackBean[] var6 = null;
      SessionBeanBean[] var7 = this.ejbJarBean.getEnterpriseBeans().getSessions();

      String var11;
      for(int var8 = 0; var8 < var7.length; ++var8) {
         BeanInfo var9 = this.di.getBeanInfo(var7[var8].getEjbName());
         if (var9.isEJB30()) {
            SessionBeanBean var10 = (SessionBeanBean)var7[var8];
            var11 = var10.getEjbClass();
            if (var11.equals(var1)) {
               var6 = var10.getPostConstructs();
               var3 = var10.getPreDestroys();
               if (this.isStateful) {
                  var4 = var10.getPostActivates();
                  var5 = var10.getPrePassivates();
               }
               break;
            }
         }
      }

      Class var14 = this.sbi.getBeanClass();
      if (!this.sbi.isStateful()) {
         try {
            Method var15 = var14.getMethod("ejbCreate", (Class[])null);
            if (var6 != null && var6.length > 0) {
               for(int var17 = 0; var17 < var6.length; ++var17) {
                  var11 = var6[var17].getLifecycleCallbackMethod();
                  String var12 = var6[var17].getLifecycleCallbackClass();
                  if (var12.equals(var14.getName()) && !var11.equals("ejbCreate")) {
                     throw new ComplianceException(EJBComplianceTextFormatter.getInstance().SLSB_POSTCONSTRUCT_NOT_APPLY_EJBCREATE(var2));
                  }
               }
            }
         } catch (NoSuchMethodException var13) {
         }
      }

      if (SessionBean.class.isAssignableFrom(var14)) {
         int var16;
         String var18;
         if (var3 != null && var3.length > 0) {
            for(var16 = 0; var16 < var3.length; ++var16) {
               var18 = var3[var16].getLifecycleCallbackMethod();
               var11 = var3[var16].getLifecycleCallbackClass();
               if (var11.equals(var14.getName()) && !var18.equals("ejbRemove")) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().SESSION_BEAN_PREDESTROY_NOT_APPLY_EJBREMOVE(var2));
               }
            }
         }

         if (var4 != null && var4.length > 0) {
            for(var16 = 0; var16 < var4.length; ++var16) {
               var18 = var4[var16].getLifecycleCallbackMethod();
               var11 = var4[var16].getLifecycleCallbackClass();
               if (var11.equals(var14.getName()) && !var18.equals("ejbActivate")) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().SESSION_BEAN_POSTACTIVATE_NOT_APPLY_EJBACTIVE(var2));
               }
            }
         }

         if (var5 != null && var5.length > 0) {
            for(var16 = 0; var16 < var5.length; ++var16) {
               var18 = var5[var16].getLifecycleCallbackMethod();
               var11 = var5[var16].getLifecycleCallbackClass();
               if (var11.equals(var14.getName()) && !var18.equals("ejbPassivate")) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().SESSION_BEAN_PREPASSIVATE_NOT_APPLY_EJBPASSIVATE(var2));
               }
            }
         }
      }

   }

   public void checkBusinessMethods() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Set var2 = this.getAllBusinessMethods();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Method var4 = (Method)var3.next();

         try {
            this.validateBusinessMethod(var4);
         } catch (ErrorCollectionException var6) {
            var1.add(var6);
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void validateBusinessMethod(Method var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException();
      int var3 = var1.getModifiers();
      if (var1.getName().startsWith("ejb")) {
         var2.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().METHOD_CANNOT_START_WITH_EJB(var1.getName(), var1.getDeclaringClass().getName())));
      }

      if (!Modifier.isPublic(var3)) {
         var2.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().BUSINESS_METHOD_MUST_BE_PUBLIC(var1.getName(), var1.getDeclaringClass().getName())));
      }

      if (Modifier.isFinal(var3)) {
         var2.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().BUSINESS_METHOD_MUST_NOT_BE_FINAL(var1.getName(), var1.getDeclaringClass().getName())));
      }

      if (Modifier.isStatic(var3)) {
         var2.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().BUSINESS_METHOD_MUST_NOT_BE_STATIC(var1.getName(), var1.getDeclaringClass().getName())));
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   private Set getAllBusinessMethods() {
      HashSet var1 = new HashSet();
      HashMap var2 = new HashMap();
      Method[] var3 = this.sbi.getBeanClass().getMethods();
      String[] var4 = new String[var3.length];

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4[var5] = DDUtils.getMethodSignature(var3[var5]);
         var2.put(var4[var5], var3[var5]);
      }

      Set var13 = this.sbi.getBusinessLocals();
      Iterator var6 = var13.iterator();

      while(var6.hasNext()) {
         Class var7 = (Class)var6.next();
         Method[] var8 = var7.getMethods();

         for(int var9 = 0; var9 < var8.length; ++var9) {
            String var10 = DDUtils.getMethodSignature(var8[var9]);
            Method var11 = (Method)var2.get(var10);
            if (var11 != null) {
               var1.add(var11);
            }
         }
      }

      Set var14 = this.sbi.getBusinessRemotes();
      Iterator var15 = var14.iterator();

      while(var15.hasNext()) {
         Class var16 = (Class)var15.next();
         Method[] var17 = var16.getMethods();

         for(int var18 = 0; var18 < var17.length; ++var18) {
            String var19 = DDUtils.getMethodSignature(var17[var18]);
            Method var12 = (Method)var2.get(var19);
            if (var12 != null) {
               var1.add(var12);
            }
         }
      }

      return var1;
   }

   public static void validateRemoveMethodToBeBusinessMethod(SessionBeanBean var0, MethodInfo var1, String var2) throws ComplianceException {
      if (var1 == null) {
         throw new ComplianceException(EJBComplianceTextFormatter.getInstance().REMOVE_METHOD_NOT_BE_BUSINESS_METHOD(var2.toString(), var0.getEjbName()));
      }
   }
}
