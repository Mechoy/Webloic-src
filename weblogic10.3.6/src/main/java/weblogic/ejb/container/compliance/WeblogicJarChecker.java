package weblogic.ejb.container.compliance;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.naming.Name;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;

public final class WeblogicJarChecker extends BaseComplianceChecker {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.compliance.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.compliance.verbose") != null;
   private DeploymentInfo di;

   public WeblogicJarChecker(DeploymentInfo var1) {
      this.di = var1;
      if (debug) {
         Debug.assertion(var1 != null);
      }

   }

   public void checkWeblogicJar() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Iterator var2 = this.di.getBeanInfos().iterator();

      do {
         if (!var2.hasNext()) {
            return;
         }

         BeanInfo var3 = (BeanInfo)var2.next();
         this.doCheckNoDuplicateJNDINames(var3, var1);
         this.doCheckForCorrectJndiNames(var3, var1);
         this.doCheckEJBReferenceDescriptions(var3, var1);
      } while(var1.isEmpty());

      throw var1;
   }

   public void doCheckNoDuplicateJNDINames(BeanInfo var1, ErrorCollectionException var2) throws ErrorCollectionException {
      if (var1 instanceof ClientDrivenBeanInfo) {
         ClientDrivenBeanInfo var3 = (ClientDrivenBeanInfo)var1;
         HashSet var4 = new HashSet();
         Name var5 = var3.getJNDIName();
         Name var6 = var3.getLocalJNDIName();
         String var7;
         if (var5 != null) {
            if (var4.contains(var5)) {
               var2.add(new ComplianceException(this.fmt.DUPLICATE_JNDI_NAME(var3.getEJBName(), var3.getJNDINameAsString()), new DescriptorErrorInfo("<jndi-name>", var3.getEJBName(), var3.getJNDINameAsString())));
            } else if (!var3.hasRemoteClientView()) {
               log.logWarning(this.fmt.JNDI_NAME_MUST_HAVE_REMOTE_INTERFACE(var3.getDisplayName()));
            } else {
               var7 = var3.getJNDINameAsString();
               if (var7.indexOf("java:comp/env") != -1) {
                  var2.add(new ComplianceException(this.fmt.INCORRECT_JNDI_NAME(var3.getEJBName(), var7)));
               }

               var4.add(var5);
            }
         }

         if (var6 != null) {
            if (var4.contains(var6)) {
               var2.add(new ComplianceException(this.fmt.DUPLICATE_JNDI_NAME(var3.getEJBName(), var3.getLocalJNDINameAsString()), new DescriptorErrorInfo("<local-jndi-name>", var3.getEJBName(), var3.getJNDINameAsString())));
            } else if (!var3.hasLocalClientView()) {
               log.logWarning(this.fmt.LOCAL_JNDI_NAME_MUST_HAVE_LOCAL_INTERFACE(var3.getDisplayName()));
            } else {
               var7 = var3.getLocalJNDINameAsString();
               if (var7.indexOf("java:comp/env") != -1) {
                  var2.add(new ComplianceException(this.fmt.INCORRECT_JNDI_NAME(var3.getEJBName(), var7)));
               }

               var4.add(var6);
            }
         }
      }

   }

   public void doCheckForCorrectJndiNames(BeanInfo var1, ErrorCollectionException var2) throws ErrorCollectionException {
      if (var1 instanceof ClientDrivenBeanInfo) {
         ClientDrivenBeanInfo var3 = (ClientDrivenBeanInfo)var1;
         Name var4 = var3.getJNDIName();
         Name var5 = var3.getLocalJNDIName();
         String var6 = var3.getEJBName();
         if (var3.hasRemoteClientView() && !var3.isEJB30() && var4 == null) {
            log.logWarning(this.fmt.NO_JNDI_NAME_DEFINED_FOR_REMOTE_VIEW(var6));
         }
      }

   }

   public void doCheckEJBReferenceDescriptions(BeanInfo var1, ErrorCollectionException var2) throws ErrorCollectionException {
      Map var3 = var1.getAllEJBReferenceJNDINames();
      if (var3.size() > 0) {
         HashSet var4 = new HashSet();
         Collection var5 = var1.getAllEJBReferences();
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            EjbRefBean var7 = (EjbRefBean)var6.next();
            var4.add(var7.getEjbRefName());
         }

         Set var14 = var3.keySet();
         Iterator var8 = var14.iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            if (!var4.contains(var9)) {
               var2.add(new ComplianceException(this.fmt.noEJBRefForReferenceDescription(var1.getEJBName(), var9)));
            }
         }
      }

      Map var11 = var1.getAllEJBLocalReferenceJNDINames();
      if (var11.size() > 0) {
         HashSet var12 = new HashSet();
         Collection var13 = var1.getAllEJBLocalReferences();
         Iterator var15 = var13.iterator();

         while(var15.hasNext()) {
            EjbLocalRefBean var16 = (EjbLocalRefBean)var15.next();
            var12.add(var16.getEjbRefName());
         }

         Set var17 = var11.keySet();
         Iterator var18 = var17.iterator();

         while(var18.hasNext()) {
            String var10 = (String)var18.next();
            if (!var12.contains(var10)) {
               var2.add(new ComplianceException(this.fmt.noEJBLocalRefForReferenceDescription(var1.getEJBName(), var10)));
            }
         }
      }

   }

   public static void validateEnterpriseBeansMinimalConfiguration(EjbJarBean var0, String var1) throws ComplianceException {
      if (var0 != null) {
         EnterpriseBeansBean var2 = var0.getEnterpriseBeans();
         if (var2 != null && var2.getEntities().length + var2.getMessageDrivens().length + var2.getSessions().length >= 1) {
            SessionBeanBean[] var3 = var2.getSessions();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var3[var4].getEjbClass() == null) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().NO_SESSION_BEAN_CLASS_FOUND_FOR_EJB(var3[var4].getEjbName()));
               }

               if (!"stateless".equalsIgnoreCase(var3[var4].getSessionType()) && !"stateful".equalsIgnoreCase(var3[var4].getSessionType())) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().SESSION_BEAN_NO_SESSION_TYPE(var3[var4].getEjbName()));
               }
            }

            MessageDrivenBeanBean[] var6 = var2.getMessageDrivens();

            for(int var5 = 0; var5 < var6.length; ++var5) {
               if (var6[var5].getEjbClass() == null) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().NO_MDB_CLASS_FOUND_FOR_EJB(var6[var5].getEjbName()));
               }
            }

         } else {
            throw new ComplianceException(EJBComplianceTextFormatter.getInstance().NO_EJBS_FOUND_IN_DD(var1.toString()));
         }
      }
   }
}
