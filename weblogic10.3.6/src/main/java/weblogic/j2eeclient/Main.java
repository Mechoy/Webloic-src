package weblogic.j2eeclient;

import com.oracle.pitchfork.interfaces.inject.ComponentContributor;
import com.oracle.pitchfork.server.Bootstrap;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.xml.registry.ConnectionFactory;
import javax.xml.stream.XMLStreamException;
import org.omg.CORBA.ORB;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.utils.IOUtils;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.deployment.PersistenceUnitInfoImpl;
import weblogic.deployment.ServiceRefProcessor;
import weblogic.deployment.ServiceRefProcessorException;
import weblogic.deployment.ServiceRefProcessorFactory;
import weblogic.ejb20.internal.HandleDelegateImpl;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ApplicationClientBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.InjectionTargetBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.MessageDestinationBean;
import weblogic.j2ee.descriptor.MessageDestinationRefBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.PersistenceUnitRefBean;
import weblogic.j2ee.descriptor.PortComponentRefBean;
import weblogic.j2ee.descriptor.ResourceEnvRefBean;
import weblogic.j2ee.descriptor.ResourceRefBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.EjbReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.MessageDestinationDescriptorBean;
import weblogic.j2ee.descriptor.wl.ResourceDescriptionBean;
import weblogic.j2ee.descriptor.wl.ResourceEnvDescriptionBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationClientBean;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.protocol.ClientEnvironment;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.security.acl.internal.Security;
import weblogic.utils.Debug;
import weblogic.utils.Getopt2;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.classloaders.AugmentableClassLoaderManager;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.JarClassFinder;
import weblogic.utils.classloaders.NullClassFinder;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.workarea.WorkContextHelper;

public final class Main {
   private final File clientJar;
   private final String url;
   private final String[] argv;
   private final File configDir;
   private final DeploymentPlanBean plan;
   private String moduleName = null;
   private final File[] applicationLibraries;
   private final Context rootCtx;
   private static final boolean runningInsideWebStart = Boolean.getBoolean("weblogic.j2ee.client.isWebStart");
   private static final boolean DEBUG = Boolean.getBoolean("weblogic.debug.DebugJ2EEClient");
   private static final Context javaContext = new SimpleContext();
   private ApplicationClientBean stdDD;
   private Class mainClass;
   private static Collection lookupTypes = Arrays.asList((Object[])(new String[]{"ejb-ref", "javax.jms.QueueConnectionFactory", "javax.jms.TopicConnectionFactory", "javax.jms.Queue", "javax.jms.Topic", "javax.sql.DataSource"}));

   public Main(File var1, String var2, String[] var3, String var4, String var5, String var6, File[] var7) throws Exception {
      this.clientJar = var1;
      this.url = var2;
      this.argv = var3;
      if (var4 != null) {
         this.configDir = new File(var4);
      } else {
         this.configDir = null;
      }

      if (var5 != null) {
         this.plan = (new DeploymentPlanDescriptorLoader(new File(var5))).getDeploymentPlanBean();
      } else {
         this.plan = null;
      }

      this.moduleName = var6;
      this.applicationLibraries = var7;
      System.setProperty("java.naming.provider.url", var2);
      System.setProperty("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      System.setProperty("java.naming.factory.url.pkgs", "weblogic.j2eeclient");
      ClientEnvironment.loadEnvironment();
      this.rootCtx = new InitialContext();
   }

   public static Context getJavaContext() {
      return javaContext;
   }

   public void run() throws Exception {
      GenericClassLoader var1 = AugmentableClassLoaderManager.getAugmentableSystemClassLoader();
      Object var2 = NullClassFinder.NULL_FINDER;
      if (DEBUG) {
         Debug.say("Sarting");
      }

      if (DEBUG && runningInsideWebStart) {
         Debug.say("JavaWebStart");
      }

      if (!runningInsideWebStart) {
         var2 = new JarClassFinder(this.clientJar);
      }

      var1.addClassFinder((ClassFinder)var2);
      if (this.applicationLibraries != null) {
         for(int var3 = 0; var3 < this.applicationLibraries.length; ++var3) {
            var1.addClassFinder(new JarClassFinder(this.applicationLibraries[var3]));
         }
      }

      Thread.currentThread().setContextClassLoader(var1);
      String var10 = this.readMainClassName(var1);
      this.mainClass = var1.loadClass(var10);
      AppClientPersistenceUnitRegistry var4 = new AppClientPersistenceUnitRegistry(this.clientJar, var1, this.moduleName, this.configDir, this.plan);
      GenericClassLoader var5 = new GenericClassLoader((ClassFinder)var2);
      this.fillEnvironment(var5, var4);
      PitchforkContext var6 = new PitchforkContext((String)null);
      Bootstrap var7 = (Bootstrap)Class.forName("com.oracle.pitchfork.spi.WLSBootstrap").getConstructor(ClassLoader.class, String.class, String.class, Boolean.TYPE).newInstance(var5, null, null, Boolean.FALSE);
      var7.deploy(this.getComponentContributor(var6));
      this.processLifeCycle(var1, this.sortCallbackClasses(var1, this.stdDD.getPostConstructs()), "PostContruct");

      try {
         this.mainClass.getMethod("main", String[].class).invoke((Object)null, this.argv);
      } catch (NoSuchMethodException var9) {
         throw new IOException("Main-Class " + var10 + " in client-jar " + this.clientJar.getPath() + " did not contain a public static void main(String[]) method");
      }

      this.processLifeCycle(var1, this.sortCallbackClasses(var1, this.stdDD.getPreDestroys()), "PreDestroy");
   }

   private ComponentContributor getComponentContributor(PitchforkContext var1) throws Exception {
      Class var2 = Class.forName("weblogic.j2ee.injection.J2eeClientComponentContributor");
      Constructor var3 = var2.getDeclaredConstructor(this.mainClass.getClass(), ApplicationClientBean.class, PitchforkContext.class);
      return (ComponentContributor)var3.newInstance(this.mainClass, this.stdDD, var1);
   }

   private LifecycleCallbackBean[] sortCallbackClasses(ClassLoader var1, LifecycleCallbackBean[] var2) throws ClassNotFoundException {
      if (var2.length <= 1) {
         return var2;
      } else {
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            String var5 = var2[var4].getLifecycleCallbackClass();
            if (var5 != null && var5.trim().length() != 0) {
               var3.add(var1.loadClass(var5));
            } else {
               var3.add(this.mainClass);
            }
         }

         ArrayList var11 = new ArrayList(Arrays.asList(var2));

         for(int var12 = 1; var12 < var3.size(); ++var12) {
            for(int var6 = 0; var6 < var12; ++var6) {
               Class var7 = (Class)var3.get(var12);
               Class var8 = (Class)var3.get(var6);
               if (var7.equals(Object.class) || var8.equals(Object.class)) {
                  break;
               }

               Class var9;
               LifecycleCallbackBean var10;
               if (var7.isAssignableFrom(var8)) {
                  var9 = (Class)var3.remove(var12);
                  var3.add(var6, var9);
                  var10 = (LifecycleCallbackBean)var11.remove(var12);
                  var11.add(var6, var10);
                  break;
               }

               if (var8.isAssignableFrom(var7)) {
                  while(var6 + 1 < var12 && ((Class)var3.get(var6 + 1)).isAssignableFrom(var7)) {
                     ++var6;
                  }

                  var9 = (Class)var3.remove(var12);
                  var3.add(var6 + 1, var9);
                  var10 = (LifecycleCallbackBean)var11.remove(var12);
                  var11.add(var6 + 1, var10);
                  break;
               }
            }
         }

         return (LifecycleCallbackBean[])((LifecycleCallbackBean[])var11.toArray(var2));
      }
   }

   private void processLifeCycle(ClassLoader var1, LifecycleCallbackBean[] var2, String var3) throws Exception {
      if (var2.length >= 1) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            String var5 = var2[var4].getLifecycleCallbackClass();
            String var6 = var2[var4].getLifecycleCallbackMethod();
            Class var7 = null;
            if (var5 != null && var5.trim().length() != 0) {
               try {
                  var7 = var1.loadClass(var5);
               } catch (ClassNotFoundException var9) {
                  throw new Exception("Unable to load " + var3 + " class " + var5);
               }
            } else {
               var7 = this.mainClass;
            }

            this.callLifeCycleMethod(var7, var6, var3);
         }

      }
   }

   private void callLifeCycleMethod(Class var1, String var2, String var3) throws Exception {
      try {
         Method var4 = var1.getDeclaredMethod(var2);
         var4.setAccessible(true);
         if (Modifier.isStatic(var4.getModifiers())) {
            var4.invoke((Object)null);
         } else {
            Debug.say(" the " + var3 + " method " + var2 + " in class" + var1.getName() + " is not static. According to the common" + " annotations specification the j2ee client " + var3 + " method " + " should be static.");
         }

      } catch (NoSuchMethodException var5) {
         throw new Exception(var3 + "Class " + var1.getName() + " in client-jar " + this.clientJar.getPath() + " did not contain the method " + var2);
      }
   }

   private void fillEnvironment(GenericClassLoader var1, AppClientPersistenceUnitRegistry var2) throws Exception {
      ApplicationClientDescriptor var3 = new ApplicationClientDescriptor(var1, this.configDir, this.plan, this.moduleName);
      this.stdDD = ApplicationClientUtils.getAnnotationProcessedDescriptor(var1, var3, this.mainClass);
      WeblogicApplicationClientBean var4 = var3.getWeblogicApplicationClientBean();
      Context var5 = javaContext.createSubcontext("java:comp");
      var5.bind("UserTransaction", this.rootCtx.lookup("javax.transaction.UserTransaction"));
      var5.bind("HandleDelegate", new HandleDelegateImpl());
      WorkContextHelper.bind(var5);
      DisconnectMonitorListImpl.bindToJNDI(var5);
      var5.bind("ORB", new SimpleContext.SimpleReference() {
         public Object get() throws NamingException {
            return ORB.init(new String[0], (Properties)null);
         }
      });
      this.loadCertificate();
      this.fillEnvContext(var5.createSubcontext("env"), this.stdDD, var4, var2);
   }

   private void loadCertificate() throws Exception {
      String var1 = System.getProperty("javax.net.ssl.keyStore");
      String var2 = System.getProperty("javax.net.ssl.keyStorePassword");
      if (var1 != null && var2 != null) {
         KeyStore var3 = KeyStore.getInstance("JKS");
         FileInputStream var4 = new FileInputStream(var1);
         if (var1 == null) {
            throw new SecurityException("Client authentication keystore not found");
         } else {
            char[] var5 = var2.toCharArray();
            var3.load(new FileInputStream(var1), var5);
            var4.close();
            String var6 = null;
            Enumeration var7 = var3.aliases();
            if (var7.hasMoreElements()) {
               var6 = (String)var7.nextElement();
            }

            if (var6 == null) {
               throw new SecurityException("No alias found in keystore");
            } else {
               Certificate[] var8 = var3.getCertificateChain(var6);
               if (var8 == null) {
                  throw new SecurityException("Certificate chain with useralias " + var6 + " not found");
               } else {
                  Key var9 = var3.getKey(var6, var5);
                  if (var9 == null) {
                     throw new SecurityException("Key with useralias " + var6 + " not found");
                  } else {
                     Security.loadLocalIdentity(var8, (PrivateKey)var9);
                  }
               }
            }
         }
      }
   }

   private void fillEnvContext(Context var1, ApplicationClientBean var2, WeblogicApplicationClientBean var3, AppClientPersistenceUnitRegistry var4) throws NamingException, IOException, Exception {
      this.envEntries(var1, var2.getEnvEntries());
      this.ejbRefs(var1, var2.getEjbRefs(), var3.getEjbReferenceDescriptions());
      this.serviceRefs(var1, var2.getServiceRefs(), var3.getServiceReferenceDescriptions());
      this.resourceRefs(var1, var2.getResourceRefs(), var3.getResourceDescriptions());
      this.resourceEnvRefs(var1, var2.getResourceEnvRefs(), var3.getResourceEnvDescriptions());
      this.persistenceUnitRefs(var1, var2.getPersistenceUnitRefs(), var4);
      this.processMessageDestinationRefs(var1, var2, var3);
   }

   private void processMessageDestinationRefs(Context var1, ApplicationClientBean var2, WeblogicApplicationClientBean var3) throws IOException, NamingException {
      MessageDestinationRefBean[] var4 = var2.getMessageDestinationRefs();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            MessageDestinationRefBean var6 = var4[var5];
            String var7 = null;
            MessageDestinationDescriptorBean var8 = null;
            if (var6.getMessageDestinationLink() != null) {
               MessageDestinationBean var9 = var2.lookupMessageDestination(var6.getMessageDestinationLink());
               if (var9 == null) {
                  var7 = var6.getMessageDestinationRefName();
                  var8 = var3.lookupMessageDestinationDescriptor(var7);
                  if (var8 == null) {
                     throw new IOException("Unable to find corresponding <message-destination> for the <message-destination-ref> with <message-destination-link " + var6.getMessageDestinationLink() + " in application-client.xml");
                  }
               } else {
                  var7 = var9.getMessageDestinationName();
                  var8 = var3.lookupMessageDestinationDescriptor(var7);
                  if (var8 == null) {
                     throw new IOException("Unable to find <message-destinantion-descriptor> with <message-destination-name> " + var7 + " in weblogic-application-client.xml corresponding " + "to <message-destination> in application-client.xml");
                  }
               }
            } else {
               var7 = var6.getMessageDestinationRefName();
               var8 = var3.lookupMessageDestinationDescriptor(var7);
               if (var8 == null) {
                  throw new IOException("Unable to find corresponding <message-destination-descriptor> for the <message-destination-ref> with <message-destination-name> " + var7 + " in weblogic-application-client.xml");
               }
            }

            Object var10 = this.lookupMessageDestination(var8);
            var1.bind(var7, var10);
         }
      }

   }

   private Object lookupMessageDestination(MessageDestinationDescriptorBean var1) throws NamingException {
      Hashtable var2 = new Hashtable();
      if (var1.getInitialContextFactory() != null) {
         var2.put("java.naming.factory.initial", var1.getInitialContextFactory());
      }

      if (var1.getProviderUrl() != null) {
         var2.put("java.naming.provider.url", var1.getProviderUrl());
      }

      InitialContext var3 = new InitialContext(var2);
      return var3.lookup(var1.getDestinationJNDIName());
   }

   private void persistenceUnitRefs(Context var1, PersistenceUnitRefBean[] var2, AppClientPersistenceUnitRegistry var3) throws Exception {
      for(int var4 = 0; var2 != null && var4 < var2.length; ++var4) {
         EntityManagerFactory var5 = this.getEntityManagerFactory(var2[var4], var3);
         var1.bind(var2[var4].getPersistenceUnitRefName(), var5);
      }

   }

   private EntityManagerFactory getEntityManagerFactory(PersistenceUnitRefBean var1, AppClientPersistenceUnitRegistry var2) throws Exception {
      String var3 = this.getPersistenceUnitName(var1.getPersistenceUnitName(), var1.getInjectionTargets());
      PersistenceUnitInfoImpl var4 = var2.getPersistenceUnit(var3);
      if (var4 == null) {
         throw new IllegalArgumentException("No persistence unit named '" + var3 + "' is available in scope ");
      } else {
         return var4.getEntityManagerFactory();
      }
   }

   private String getPersistenceUnitName(String var1, InjectionTargetBean[] var2) throws Exception {
      if (var1 != null && !"".equals(var1)) {
         return var1;
      } else if (var2 != null && var2.length == 1) {
         return var2[0].getInjectionTargetName();
      } else if (var2 != null && var2.length != 0) {
         throw new Exception("PersistenceContext refs defined with multiple injection targets must explicitly name a persistence unit.");
      } else {
         throw new Exception("PersistenceContext refs defined without any injection targets must explicitly name a persistence unit.");
      }
   }

   private Object resolveSimpleType(String var1, String var2) throws IOException {
      if ("java.lang.String".equals(var1)) {
         return var2;
      } else {
         try {
            return Class.forName(var1).getConstructor(String.class).newInstance(var2);
         } catch (NoSuchMethodException var4) {
            throw new AssertionError(var4);
         } catch (IllegalAccessException var5) {
            throw new AssertionError(var5);
         } catch (InstantiationException var6) {
            throw new IOException("Unable to create an environment-entry of  type: " + var1 + " with value: " + var2 + var6);
         } catch (InvocationTargetException var7) {
            throw new IOException("Unable to create an environment-entry of  type: " + var1 + " with value: " + var2 + var7.getTargetException());
         } catch (ClassNotFoundException var8) {
            throw new AssertionError(var8);
         }
      }
   }

   private void envEntries(Context var1, EnvEntryBean[] var2) throws NamingException, IOException {
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            EnvEntryBean var4 = var2[var3];
            var1.bind(var4.getEnvEntryName(), this.resolveSimpleType(var4.getEnvEntryType(), var4.getEnvEntryValue()));
         }
      }

   }

   private String findEJBJNDIName(EjbRefBean var1, EjbReferenceDescriptionBean[] var2) throws IOException {
      String var3 = var1.getEjbRefName();
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var3.equals(var2[var4].getEjbRefName())) {
               return var2[var4].getJNDIName();
            }
         }
      }

      throw new IOException("There is no jndi-name specified in the weblogic-application-client.xml for the ejb-ref " + var3);
   }

   private Object resolveResource(String var1, String var2, ResourceDescriptionBean[] var3) throws NamingException, IOException {
      String var4 = null;
      if (var3 != null) {
         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var1.equals(var3[var5].getResRefName())) {
               var4 = var3[var5].getJNDIName();
               break;
            }
         }
      }

      if (var4 == null) {
         throw new IOException("There is no jndi-name specified in the weblogic-application-client.xml for the resource-ref " + var1);
      } else if (lookupTypes.contains(var2)) {
         return this.rootCtx.lookup(var4);
      } else if (var2.equals("javax.xml.registry.ConnectionFactory")) {
         try {
            return ConnectionFactory.newInstance();
         } catch (Exception var6) {
            throw new IOException(StackTraceUtils.throwable2StackTrace(var6));
         }
      } else if (var2.equals("javax.mail.Session")) {
         try {
            return Session.getDefaultInstance(new Properties());
         } catch (Exception var7) {
            throw new IOException(StackTraceUtils.throwable2StackTrace(var7));
         }
      } else {
         try {
            return Class.forName(var2).getConstructor(String.class).newInstance(var4);
         } catch (Exception var8) {
            var8.printStackTrace();
            throw new IOException(StackTraceUtils.throwable2StackTrace(var8));
         }
      }
   }

   private String findResourceEnvJNDIName(String var1, ResourceEnvDescriptionBean[] var2) throws IOException {
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var1.equals(var2[var3].getResourceEnvRefName())) {
               return var2[var3].getJNDIName();
            }
         }
      }

      throw new IOException("There is no jndi-name specified in the weblogic-application-client.xml for the resource-env-ref " + var1);
   }

   private void ejbRefs(Context var1, EjbRefBean[] var2, EjbReferenceDescriptionBean[] var3) throws NamingException, IOException {
      if (var2 != null && var2.length != 0) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            String var5 = var2[var4].getEjbRefName();
            String var6 = this.findEJBJNDIName(var2[var4], var3);
            if (DEBUG) {
               Debug.say("Adding ejb link: " + var5 + "->" + var6);
            }

            var1.bind(var5, new LinkRef(var6));
         }

      }
   }

   private void serviceRefs(Context var1, ServiceRefBean[] var2, ServiceReferenceDescriptionBean[] var3) throws NamingException, ServiceRefProcessorException {
      if (var3 == null) {
         var3 = new ServiceReferenceDescriptionBean[0];
      }

      HashMap var4 = new HashMap();

      int var5;
      for(var5 = 0; var5 < var3.length; ++var5) {
         var4.put(var3[var5].getServiceRefName(), var3[var5]);
      }

      for(var5 = 0; var5 < var2.length; ++var5) {
         ServiceReferenceDescriptionBean var6 = (ServiceReferenceDescriptionBean)var4.get(var2[var5].getServiceRefName());
         this.removePortComponentLink(var2[var5]);
         ServiceRefProcessor var7 = ServiceRefProcessorFactory.getInstance().getProcessor(var2[var5], var6, (ServletContext)null);
         var7.bindServiceRef(this.rootCtx, var1, this.clientJar.getName());
      }

   }

   private void removePortComponentLink(ServiceRefBean var1) {
      PortComponentRefBean[] var2 = var1.getPortComponentRefs();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         PortComponentRefBean var4 = var2[var3];
         var4.setPortComponentLink((String)null);
      }

   }

   private void resourceRefs(Context var1, ResourceRefBean[] var2, ResourceDescriptionBean[] var3) throws NamingException, IOException {
      if (var2 != null && var2.length != 0) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (!"org.omg.CORBA.ORB".equals(var2[var4].getResType())) {
               String var5 = var2[var4].getResRefName();
               var1.bind(var5, this.resolveResource(var2[var4].getResRefName(), var2[var4].getResType(), var3));
            }
         }

      }
   }

   private void resourceEnvRefs(Context var1, ResourceEnvRefBean[] var2, ResourceEnvDescriptionBean[] var3) throws NamingException, IOException {
      if (var2 != null && var2.length != 0) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            String var5 = var2[var4].getResourceEnvRefName();
            var1.bind(var5, this.rootCtx.lookup(this.findResourceEnvJNDIName(var5, var3)));
         }

      }
   }

   private String readMainClassName(ClassLoader var1) throws IOException {
      String var2 = null;
      if (runningInsideWebStart) {
         Enumeration var3 = var1.getResources("META-INF/MANIFEST.MF");

         while(var3.hasMoreElements()) {
            URL var4 = (URL)var3.nextElement();
            InputStream var5 = var4.openStream();
            Manifest var6 = new Manifest(var5);
            var2 = (String)var6.getMainAttributes().get(Name.MAIN_CLASS);
            if (var2 != null && var2.length() > 0) {
               break;
            }
         }
      } else {
         JarFile var14 = null;

         try {
            var14 = new JarFile(this.clientJar);
            Manifest var15 = var14.getManifest();
            if (var15 == null) {
               throw new IOException("Client jar file " + this.clientJar.getPath() + " did not contain a manifest");
            }

            var2 = (String)var15.getMainAttributes().get(Name.MAIN_CLASS);
         } finally {
            if (var14 != null) {
               try {
                  var14.close();
               } catch (Exception var12) {
               }
            }

         }
      }

      if (var2 == null) {
         throw new IOException("No " + Name.MAIN_CLASS + " was specified in your client-jar file: " + this.clientJar.getPath());
      } else {
         if (DEBUG) {
            Debug.say("Main Class: " + var2);
         }

         return var2;
      }
   }

   public static void main(String[] var0) throws Exception {
      Getopt2 var1 = new Getopt2();
      var1.setUsageArgs("[clientjar|exploded ear containing clientjar] URL [client-args]...");
      var1.setUsageFooter("Example: java weblogic.j2eeclient.Main appclient.jar t3://localhost:7001");
      var1.addOption("clientName", "client-jar-name", "Name of the client-jar to be invoked. This option should be used when passing in an exploded ear.");
      var1.grok(var0);
      String var2 = var1.getOption("clientName");
      var0 = var1.args();
      if (var0.length < 2) {
         System.out.println(var1.fullUsageMessage(Main.class.getName()));
         System.exit(1);
      }

      File var3 = new File(var0[0]);
      ArrayList var4 = new ArrayList();
      String var5 = processExplodedEar(var3, var4);
      if (var2 != null && !"".equals(var2.trim())) {
         var5 = var2;
      }

      File var6 = null;
      if (var5 == null) {
         var6 = var3;
      } else {
         var6 = new File(var3, var5);
      }

      String var7 = var0[1];
      Object var8 = null;
      Object var9 = null;
      Object var10 = null;
      String[] var11 = var0;
      var0 = new String[var0.length - 2];
      System.arraycopy(var11, 2, var0, 0, var0.length);
      Main var12 = new Main(var6, var7, var0, (String)var8, (String)var9, (String)var10, (File[])((File[])var4.toArray(new File[var4.size()])));
      var12.run();
   }

   public static String processExplodedEar(File var0, List var1) throws IOException, XMLStreamException {
      String var2 = null;
      if (var0.isDirectory()) {
         VirtualJarFile var3 = VirtualJarFactory.createVirtualJar(var0);

         try {
            ApplicationDescriptor var4 = new ApplicationDescriptor(var3);
            ApplicationBean var5 = var4.getApplicationDescriptor();
            if (var5 != null) {
               ModuleBean[] var6 = var5.getModules();

               for(int var7 = 0; var7 < var6.length && var2 == null; ++var7) {
                  var2 = var6[var7].getJava();
               }

               if (var2 != null) {
                  String var13 = var5.getLibraryDirectory();
                  if (var13 != null) {
                     File var8 = new File(var0, var13);
                     if (var8.isDirectory()) {
                        File[] var9 = var8.listFiles(new FileFilter() {
                           public boolean accept(File var1) {
                              return !var1.isDirectory() && var1.getName().endsWith(".jar");
                           }
                        });
                        if (var9 != null) {
                           var1.addAll(Arrays.asList(var9));
                        }
                     }
                  }
               }
            }
         } finally {
            IOUtils.forceClose(var3);
         }
      }

      return var2;
   }
}
