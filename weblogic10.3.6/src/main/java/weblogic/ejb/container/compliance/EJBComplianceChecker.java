package weblogic.ejb.container.compliance;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.jar.JarFile;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationContextInternal;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.deployer.DeploymentDescriptorException;
import weblogic.ejb.container.deployer.MBeanDeploymentInfoImpl;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.EjbDescriptorFactory;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;
import weblogic.utils.AssertionError;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.PlatformConstants;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public final class EJBComplianceChecker extends BaseComplianceChecker implements ComplianceChecker, PlatformConstants {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   public static final boolean isNeedCheck = Boolean.getBoolean("ignoreEJBChecker");

   EJBComplianceChecker() {
   }

   private Object[] getDeploymentInfoCheckers(DeploymentInfo var1) {
      return new Object[]{new CmpJarChecker(var1), new WeblogicJarChecker(var1), new ClientJarChecker(var1), new SecurityRoleChecker(var1), new EnvironmentValuesChecker(var1), new ContainerTransactionChecker(var1)};
   }

   private Object[] getInterceptorCheckers(DeploymentInfo var1) {
      Object var2;
      try {
         Class var3 = Class.forName("weblogic.ejb.container.compliance.InterceptorChecker");
         Constructor var4 = var3.getConstructor(DeploymentInfo.class);
         var2 = var4.newInstance(var1);
      } catch (Exception var5) {
         throw new AssertionError("Couldn't load InterceptorChecker: " + var5);
      }

      return new Object[]{var2};
   }

   private Object[] getGlobalRelationsCheckers(DeploymentInfo var1) throws ClassNotFoundException {
      return new Object[]{new GlobalRelationsChecker(var1)};
   }

   private Object[] getRelationCheckers(EjbRelation var1, DeploymentInfo var2) throws ClassNotFoundException {
      return new Object[]{new RelationChecker(var1, var2)};
   }

   private Object[] getDependentCheckers(DeploymentInfo var1) throws ClassNotFoundException {
      return new Object[]{new DependentChecker(var1)};
   }

   private Object[] getEjb30SessionCheckers(BeanInfo var1) throws ClassNotFoundException {
      return this.getSessionCheckers(var1);
   }

   private Object[] getEjb30MessageDrivenCheckers(BeanInfo var1) throws ClassNotFoundException {
      Object var2;
      try {
         Class var3 = Class.forName("weblogic.ejb.container.compliance.Ejb30MessageDrivenBeanClassChecker");
         Constructor var4 = var3.getConstructor(BeanInfo.class);
         var2 = var4.newInstance(var1);
      } catch (Exception var5) {
         throw new AssertionError("Couldn't load Ejb30MessageDrivenBeanClassChecker: " + var5);
      }

      return new Object[]{var2};
   }

   private Object[] getSessionCheckers(BeanInfo var1) throws ClassNotFoundException {
      ClientDrivenBeanInfo var2 = (ClientDrivenBeanInfo)var1;
      ArrayList var3 = new ArrayList();
      if (var2 instanceof Ejb3SessionBeanInfo) {
         Object var4;
         try {
            Class var5 = Class.forName("weblogic.ejb.container.compliance.Ejb30SessionBeanClassChecker");
            Constructor var6 = var5.getConstructor(ClientDrivenBeanInfo.class);
            var4 = var6.newInstance(var2);
         } catch (Exception var7) {
            throw new AssertionError("Couldn't load Ejb30SessionBeanClassChecker: " + var7);
         }

         var3.add(var4);
      } else {
         var3.add(new SessionBeanClassChecker(var2));
      }

      if (var2.hasDeclaredRemoteHome()) {
         var3.add(new SessionHomeInterfaceChecker(var2.getHomeInterfaceClass(), var2.getRemoteInterfaceClass(), var2.getBeanClass(), var2, EJBHome.class));
         var3.add(new EJBObjectClassChecker(var2.getRemoteInterfaceClass(), var2, EJBObject.class));
      }

      if (var2.hasDeclaredLocalHome()) {
         var3.add(new SessionHomeInterfaceChecker(var2.getLocalHomeInterfaceClass(), var2.getLocalInterfaceClass(), var2.getBeanClass(), var2, EJBLocalHome.class));
         var3.add(new EJBObjectClassChecker(var2.getLocalInterfaceClass(), var2, EJBLocalObject.class));
      }

      if (var2 instanceof Ejb3SessionBeanInfo) {
         Ejb3SessionBeanInfo var8 = (Ejb3SessionBeanInfo)var2;
         if (var8.hasBusinessRemotes()) {
            var3.add(new BusinessRemoteInterfaceChecker(var8));
         }

         if (var8.hasBusinessLocals()) {
            var3.add(new BusinessLocalInterfaceChecker(var8));
         }
      }

      return var3.toArray();
   }

   private Object[] getMessageDrivenCheckers(BeanInfo var1) throws ClassNotFoundException {
      return new Object[]{new MessageDrivenBeanClassChecker(var1)};
   }

   private Object[] getEntityCheckers(BeanInfo var1) throws ClassNotFoundException {
      ClientDrivenBeanInfo var2 = (ClientDrivenBeanInfo)var1;
      EntityBeanInfo var3 = (EntityBeanInfo)var1;
      ArrayList var4 = new ArrayList();
      if (var2.hasRemoteClientView()) {
         var4.add(new EntityHomeInterfaceChecker(var2.getHomeInterfaceClass(), var2.getRemoteInterfaceClass(), var2.getBeanClass(), var2, EJBHome.class));
         var4.add(new EJBObjectClassChecker(var2.getRemoteInterfaceClass(), var2, EJBObject.class));
      }

      if (var2.hasLocalClientView()) {
         var4.add(new EntityHomeInterfaceChecker(var2.getLocalHomeInterfaceClass(), var2.getLocalInterfaceClass(), var2.getBeanClass(), var2, EJBLocalHome.class));
         var4.add(new EJBObjectClassChecker(var2.getLocalInterfaceClass(), var2, EJBLocalObject.class));
      }

      var4.add(new PKClassChecker((EntityBeanInfo)var1));
      if (!var3.getIsBeanManagedPersistence() && !var3.getCMPInfo().uses20CMP()) {
         var4.add(new EJB11EntityBeanClassChecker(var3));
      } else {
         var4.add(new EJB20EntityBeanClassChecker(var3));
      }

      return var4.toArray();
   }

   public void checkDeploymentInfo(DeploymentInfo var1) throws ErrorCollectionException, ClassNotFoundException {
      if (!isNeedCheck) {
         Object[] var2 = this.getDeploymentInfoCheckers(var1);
         this.check(var2);
         Collection var3 = var1.getBeanInfos();

         Iterator var4;
         for(var4 = var3.iterator(); var4.hasNext(); this.check(var2)) {
            BeanInfo var5 = (BeanInfo)var4.next();
            if (var1.getEjbDescriptorBean().isEjb30()) {
               if (var5 instanceof SessionBeanInfo) {
                  var2 = this.getEjb30SessionCheckers(var5);
               } else if (var5 instanceof EntityBeanInfo) {
                  var2 = this.getEntityCheckers(var5);
               } else {
                  if (!(var5 instanceof MessageDrivenBeanInfo)) {
                     throw new AssertionError("Unexpected BeanInfo type: " + var5);
                  }

                  var2 = this.getEjb30MessageDrivenCheckers(var5);
               }
            } else if (var5 instanceof SessionBeanInfo) {
               var2 = this.getSessionCheckers(var5);
            } else if (var5 instanceof EntityBeanInfo) {
               var2 = this.getEntityCheckers(var5);
            } else {
               if (!(var5 instanceof MessageDrivenBeanInfo)) {
                  throw new AssertionError("Unexpected BeanInfo type: " + var5);
               }

               var2 = this.getMessageDrivenCheckers(var5);
            }
         }

         if (var1.getEjbDescriptorBean().isEjb30()) {
            var2 = this.getInterceptorCheckers(var1);
            this.check(var2);
         }

         Relationships var7 = var1.getRelationships();
         if (var7 != null) {
            var2 = this.getGlobalRelationsCheckers(var1);
            this.check(var2);
            var4 = var7.getAllEjbRelations().values().iterator();

            while(var4.hasNext()) {
               EjbRelation var6 = (EjbRelation)var4.next();
               var2 = this.getRelationCheckers(var6, var1);
               this.check(var2);
            }
         }

         if (var1.getDependents() != null) {
            var2 = this.getDependentCheckers(var1);
            this.check(var2);
         }

      }
   }

   private void check(Object[] var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Object var4 = var1[var3];
         Method[] var5 = var4.getClass().getMethods();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].getName().startsWith("check")) {
               try {
                  var5[var6].invoke(var4, (Object[])null);
               } catch (IllegalAccessException var9) {
                  throw new AssertionError(var9);
               } catch (InvocationTargetException var10) {
                  Object var8 = var10.getTargetException();
                  if (var8 instanceof NoClassDefFoundError) {
                     var8 = new NoClassDefFoundError("Class not found: " + ((Throwable)var8).getMessage());
                  }

                  var2.add((Throwable)var8);
               }
            }
         }

         if (!var2.isEmpty()) {
            throw var2;
         }
      }

   }

   private static boolean checkFile(File var0) {
      EJBComplianceTextFormatter var1 = new EJBComplianceTextFormatter();
      if (!var0.exists()) {
         System.err.println(var1.jarFileMissing(var0.getAbsolutePath()));
         return false;
      } else if (var0.isDirectory()) {
         System.err.println(var1.jarFileIsDirectory(var0.getAbsolutePath()));
         return false;
      } else {
         return true;
      }
   }

   public static void complianceCheckEJB(DeploymentInfo var0, ClassLoader var1) throws ErrorCollectionException {
      try {
         ComplianceChecker var2 = ComplianceCheckerFactory.getComplianceChecker();
         var2.checkDeploymentInfo(var0);
      } catch (ErrorCollectionException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new ErrorCollectionException(var4);
      }
   }

   private static void initParser() {
   }

   private static void complianceCheckEJBJar(File var0) throws IOException, XMLParsingException, XMLProcessingException, ErrorCollectionException {
      ClasspathClassFinder2 var1 = new ClasspathClassFinder2(var0.getPath());
      GenericClassLoader var2 = new GenericClassLoader(var1);

      try {
         JarFile var3 = new JarFile(var0);

         try {
            EjbDescriptorBean var4 = EjbDescriptorFactory.createDescriptorFromJarFile(var3);
            MBeanDeploymentInfoImpl var5 = new MBeanDeploymentInfoImpl(var4, var2, "", "", VirtualJarFactory.createVirtualJar(var0), (ApplicationContextInternal)null);
            complianceCheckEJB(var5, var2);
            var1.close();
         } catch (ClassNotFoundException var36) {
            EJBLogger.logStackTrace(var36);
         } catch (DeploymentDescriptorException var37) {
            EJBLogger.logStackTrace(var37);
         } catch (WLDeploymentException var38) {
            EJBLogger.logStackTrace(var38);
         } catch (XMLStreamException var39) {
            EJBLogger.logStackTrace(var39);
         } finally {
            try {
               var3.close();
            } catch (IOException var35) {
            }

         }
      } finally {
         var2.close();
      }

   }

   public static void checkJar(File var0) throws IOException, XMLParsingException, XMLProcessingException, ErrorCollectionException {
      initParser();
      complianceCheckEJBJar(var0);
   }

   public static void main(String[] var0) {
      EJBComplianceTextFormatter var1 = new EJBComplianceTextFormatter();
      initParser();
      if (var0.length == 0) {
         Localizer var12 = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.ejb.container.EJBComplianceTextLocalizer");
         System.err.println(var12.get("usage"));
      } else {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            String var3 = var0[var2];
            System.out.println(var1.checkingJarFile(var3));
            System.out.println("");
            File var4 = new File(var3);
            if (checkFile(var4)) {
               try {
                  complianceCheckEJBJar(var4);
                  System.out.println(var1.compliant(var3));
                  System.out.println("");
               } catch (IOException var8) {
                  System.err.println(var1.notValid(var4.getName(), var8));
               } catch (XMLParsingException var9) {
                  System.err.println(var1.failedToParse(var4.getName(), var9));
               } catch (XMLProcessingException var10) {
                  System.err.println(var1.failedToLoad(var4.getName(), var10));
               } catch (ErrorCollectionException var11) {
                  Iterator var6 = var11.getExceptions().iterator();

                  while(var6.hasNext()) {
                     Throwable var7 = (Throwable)var6.next();
                     if (var7 instanceof ComplianceException) {
                        System.err.println(var1.complianceError(var7.getMessage()));
                     } else if (var7 instanceof ClassNotFoundException) {
                        System.err.println(var1.loadFailure(var7.getMessage()));
                     } else {
                        System.err.println(var1.complianceError(StackTraceUtils.throwable2StackTrace(var7)));
                     }
                  }
               }
            }
         }

      }
   }
}
