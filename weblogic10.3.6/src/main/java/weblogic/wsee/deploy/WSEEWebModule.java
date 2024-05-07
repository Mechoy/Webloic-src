package weblogic.wsee.deploy;

import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.FieldVisitor;
import com.bea.objectweb.asm.Label;
import com.bea.objectweb.asm.MethodVisitor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.xml.stream.XMLStreamException;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.ListenerBean;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.ServiceImplBeanBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WebserviceDescriptionBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.management.DeploymentException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.ServletRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.internal.WarSource;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.security.Utils;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.policy.deployment.WsPolicyDescriptor;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.wsdl.RelativeResourceResolver;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.xml.schema.binding.util.ClassUtil;

class WSEEWebModule extends WSEEModule {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private WebAppServletContext svltCtx = null;
   private static final Method defineClass;
   private static final Method resolveClass;

   WSEEWebModule(WebAppServletContext var1) {
      super(var1.getApplicationContext());
      this.svltCtx = var1;
   }

   DeployInfo createDeployInfo() {
      ServletDeployInfo var1 = new ServletDeployInfo();
      var1.setServletContext(this.svltCtx);
      return var1;
   }

   String getModuleName() {
      return this.svltCtx.getWebAppModule().getModuleURI();
   }

   WSEEDescriptor loadDescriptor(File var1, DeploymentPlanBean var2) throws IOException, XMLStreamException {
      return new WSEEDescriptor(this.svltCtx, var1, var2, this.getModuleName());
   }

   WsPolicyDescriptor loadWsPolicyDescriptor(File var1, DeploymentPlanBean var2) throws IOException, XMLStreamException {
      return new WsPolicyDescriptor(this.svltCtx, var1, var2, this.getModuleName());
   }

   protected String getLinkName(ServiceImplBeanBean var1) {
      return var1.getServletLink();
   }

   protected void setLinkName(ServiceImplBeanBean var1, String var2) {
      var1.setServletLink(var2);
   }

   protected EnvEntryBean[] getEnvEntries(ServiceImplBeanBean var1) {
      return this.svltCtx.getWebAppModule().getWebAppBean().getEnvEntries();
   }

   protected Map<String, Class> getLinkMap() {
      HashMap var1 = new HashMap();
      ServletBean[] var2 = this.svltCtx.getWebAppModule().getWebAppBean().getServlets();
      if (var2 != null) {
         ServletBean[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ServletBean var6 = var3[var5];

            try {
               String var7 = var6.getServletClass();
               if (!StringUtil.isEmpty(var7)) {
                  var1.put(var6.getServletName(), ClassUtil.loadClass(var7));
               }
            } catch (Exception var8) {
               System.err.println("When processing WebService module '" + this.getModuleName() + "'.  Failed to load servlet Class: " + var6.getServletClass());
               System.err.println("Ignoring: " + var8.getMessage() + " at: " + var8.getStackTrace()[0].toString());
            }
         }
      }

      return var1;
   }

   WsdlDefinitions loadWsdlDefinitions(String var1) throws WsException {
      WarSource var2 = this.svltCtx.getResourceAsSource(var1);
      if (var2 == null && !var1.startsWith("/")) {
         var2 = this.svltCtx.getResourceAsSource("/" + var1);
      }

      if (var2 == null && !var1.toUpperCase(Locale.ENGLISH).startsWith("/WEB-INF")) {
         var2 = this.svltCtx.getResourceAsSource("/WEB-INF" + (var1.startsWith("/") ? "" : "/") + var1);
      }

      URL var3 = var2 == null ? this.svltCtx.getServletClassLoader().getResource(var1) : var2.getURL();
      if (var3 == null && var1.toUpperCase(Locale.ENGLISH).startsWith("/WEB-INF/")) {
         var3 = this.svltCtx.getServletClassLoader().getResource(var1.substring(8));
      }

      if (var3 == null && var1.toUpperCase(Locale.ENGLISH).startsWith("WEB-INF/")) {
         var3 = this.svltCtx.getServletClassLoader().getResource(var1.substring(7));
      }

      if (var3 == null) {
         return null;
      } else {
         try {
            return WsdlFactory.getInstance().parse((String)var3.toString(), (RelativeResourceResolver)(new ServletContextRelativeResourceResolver(this.svltCtx)));
         } catch (WsdlException var5) {
            throw new WsException("While deploying WebService module '" + this.getModuleName() + "'.  Error encountered while attempting to Load WSDL Definitions for WSDL: '" + var3.toString() + "'.  " + var5.getMessage(), var5);
         }
      }
   }

   JavaWsdlMappingBean loadMappingFile(String var1) throws WsException {
      if (StringUtil.isEmpty(var1)) {
         return null;
      } else {
         InputStream var2 = this.svltCtx.getResourceAsStream(var1);
         if (var2 == null) {
            return null;
         } else {
            JavaWsdlMappingBean var5;
            try {
               DescriptorManager var3 = new DescriptorManager();
               JavaWsdlMappingBean var4 = (JavaWsdlMappingBean)var3.createDescriptor(var2).getRootBean();
               var5 = var4;
            } catch (IOException var14) {
               throw new WsException("While deploying WebService module '" + this.getModuleName() + "'.  Error encountered while attemping to load Java-WSDL mapping file: '" + var1 + "'.  " + var14.getMessage(), var14);
            } finally {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Exception var13) {
                     var13.printStackTrace();
                  }
               }

            }

            return var5;
         }
      }
   }

   void wrapContextListeners() {
      boolean var1 = false;
      if (this.deployInfoMap != null) {
         Iterator var2 = this.deployInfoMap.values().iterator();

         while(var2.hasNext()) {
            DeployInfo var3 = (DeployInfo)var2.next();
            if (var3.getWebServicesType() == WebServiceType.JAXWS) {
               var1 = true;
               break;
            }
         }
      }

      if (var1) {
         WebAppBean var8 = this.svltCtx.getWebAppModule().getWebAppBean();
         if (var8 != null) {
            ListenerBean[] var9 = var8.getListeners();
            if (var9 != null) {
               ListenerBean[] var4 = var9;
               int var5 = var9.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  ListenerBean var7 = var4[var6];
                  if ("org.springframework.web.context.ContextLoaderListener".equals(var7.getListenerClass())) {
                     var7.setListenerClass(this.createListenerClassWrapper(var7.getListenerClass()));
                  }
               }
            }
         }
      }

   }

   void registerEndpoint(WebservicesBean var1) throws Exception {
      this.swapServlets();
      this.updateAddress(var1);
      this.setInfoAsAttribute();
   }

   private void setInfoAsAttribute() {
      Iterator var1 = this.deployInfoMap.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         ServletDeployInfo var3 = (ServletDeployInfo)var2.getValue();
         var3.store(this.svltCtx);
      }

   }

   private void swapServlets() throws ServletException {
      Iterator var1 = this.deployInfoMap.values().iterator();

      while(var1.hasNext()) {
         DeployInfo var2 = (DeployInfo)var1.next();
         String var3 = var2.getServlet();
         this.svltCtx.swapServlet(var2.getLinkName(), var3, (Map)null);
         if (verbose) {
            Verbose.log((Object)("Swapping in " + var3 + " for " + var2.getLinkName()));
         }
      }

   }

   private void bindInternalPort(WebAppServletContext var1, PortComponentBean var2, WsdlPort var3) throws WsException {
      String var4 = var1.getWebAppModule().getId();
      String var5 = "wsee/" + var4 + "#" + var2.getPortComponentName();

      try {
         Context var6 = var1.getApplicationContext().getEnvContext();
         var6.bind(var5, var3);
      } catch (NamingException var7) {
         throw new WsException("While deploying WebService module '" + this.getModuleName() + "'.  Failed to bind wsdl port to internal name " + var5 + " " + var7, var7);
      }
   }

   private void updateAddress(WebservicesBean var1) throws WsException {
      ServerRuntimeMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      String var3 = "" + var2.getSSLListenPort();
      WebserviceDescriptionBean[] var4 = var1.getWebserviceDescriptions();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         WebserviceDescriptionBean var7 = var4[var6];
         WsdlDefinitions var8 = null;
         WsdlAddressInfo var9 = new WsdlAddressInfo();
         PortComponentBean[] var10 = var7.getPortComponents();
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            PortComponentBean var13 = var10[var12];
            WsdlAddressInfo.PortAddress var14 = var9.addWsdlPort(var13.getWsdlPort());
            String var15 = var13.getServiceImplBean().getServletLink();
            ServletDeployInfo var16 = (ServletDeployInfo)this.deployInfoMap.get(var15);
            String[] var17 = this.getUrlPattern(this.svltCtx, var15);
            if (var16 != null) {
               var16.setServiceURIs(var17);
               var8 = var16.getWsdlDef();
            }

            if (var8 != null) {
               String var18 = this.svltCtx.getContextPath();
               String var19 = var17[0];
               String var20 = AsyncUtil.calculateServiceTargetURI(var18, var19);
               var14.setServiceuri(var20);
               if (verbose) {
                  Verbose.say("Updated service URI in address of port component " + var13.getWsdlPort() + " to: " + var20);
               }

               WsdlPort var21 = (WsdlPort)var8.getPorts().get(var13.getWsdlPort());
               if (var21.getTransport().startsWith("http")) {
                  if (this.isSSLRequired(this.svltCtx, var16)) {
                     var14.setProtocol("https");
                     var14.setListenPort(var3);
                  } else {
                     var14.setProtocol("http");
                  }
               } else {
                  var14.setProtocol(var21.getTransport());
               }

               var21.setPortAddress(var14);
               this.bindInternalPort(this.svltCtx, var13, var21);
            }
         }

         if (var8 != null) {
            WsdlUtils.updateAddress(var8, var9);
         }
      }

   }

   private String[] getUrlPattern(WebAppServletContext var1, String var2) {
      ServletRuntimeMBean[] var3 = var1.getServletRuntimeMBeans();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].getServletName().equals(var2)) {
            String[] var5 = var3[var4].getURLPatterns();
            if (var5.length == 0) {
               var5 = new String[]{"/"};
            }

            return var5;
         }
      }

      throw new WSEEServletEndpointException("While deploying WebService module '" + this.getModuleName() + "'. Internal error, failed to find " + "ServletRuntimeMBean for servlet " + var2);
   }

   private boolean isSSLRequired(WebAppServletContext var1, ServletDeployInfo var2) {
      String[] var3 = var2.getServiceURIs();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String var5 = var3[var4];
         if (Utils.isSSLRequired(var1, var5, "POST")) {
            return true;
         }
      }

      return false;
   }

   void destroy() throws DeploymentException {
      super.destroy();
      this.unbindInternalPorts(this.svltCtx);
   }

   protected ClassLoader getClassLoader() {
      return this.svltCtx.getWebAppModule().getClassLoader();
   }

   void loadModulePolicies(PolicyServer var1) throws MalformedURLException, PolicyException {
      URL var2 = this.svltCtx.getResource("/WEB-INF/policies");
      PolicyFinder.loadPolicies(var2, var1);
   }

   private void unbindInternalPorts(WebAppServletContext var1) throws DeploymentException {
      String var2 = var1.getWebAppModule().getId();
      Context var3 = var1.getApplicationContext().getEnvContext();
      Iterator var4 = this.serviceAndPorts.values().iterator();

      while(var4.hasNext()) {
         Map var5 = (Map)var4.next();
         Iterator var6 = var5.values().iterator();

         while(var6.hasNext()) {
            PortComponentBean var7 = (PortComponentBean)var6.next();
            String var8 = "wsee/" + var2 + "#" + var7.getPortComponentName();

            try {
               var3.unbind(var8);
            } catch (NamingException var10) {
               throw new DeploymentException("While deploying WebService module '" + this.getModuleName() + "'.  Failed to unbind wsdl port named " + var8 + " " + var10, var10);
            }
         }
      }

   }

   private String createListenerClassWrapper(String var1) {
      try {
         ClassLoader var2 = this.svltCtx.getServletClassLoader();
         Class var3 = var2.loadClass(var1);
         String var4 = "com.oracle.weblogic.wsee.wrapper." + var1;
         ClassWriter var5 = new ClassWriter(0);
         HashSet var6 = new HashSet();
         Class var7 = var3;

         do {
            Class[] var8 = var7.getInterfaces();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               Class var11 = var8[var10];
               var6.add(replaceDotWithSlash(var11.getName()));
            }

            var7 = var7.getSuperclass();
         } while(var7 != null);

         var5.visit(49, 33, replaceDotWithSlash(var4), (String)null, "java/lang/Object", (String[])var6.toArray(new String[var6.size()]));
         FieldVisitor var23 = var5.visitField(2, "target", "L" + replaceDotWithSlash(var1) + ";", (String)null, (Object)null);
         var23.visitEnd();
         var23 = var5.visitField(2, "factory", "Lweblogic/wsee/util/JAXWSClassLoaderFactory;", (String)null, (Object)null);
         var23.visitEnd();
         MethodVisitor var24 = var5.visitMethod(1, "<init>", "()V", (String)null, (String[])null);
         var24.visitCode();
         var24.visitVarInsn(25, 0);
         var24.visitMethodInsn(183, "java/lang/Object", "<init>", "()V");
         var24.visitVarInsn(25, 0);
         var24.visitTypeInsn(187, replaceDotWithSlash(var1));
         var24.visitInsn(89);
         var24.visitMethodInsn(183, replaceDotWithSlash(var1), "<init>", "()V");
         var24.visitFieldInsn(181, replaceDotWithSlash(var4), "target", "L" + replaceDotWithSlash(var1) + ";");
         var24.visitVarInsn(25, 0);
         var24.visitMethodInsn(184, "weblogic/wsee/util/JAXWSClassLoaderFactory", "getInstance", "()Lweblogic/wsee/util/JAXWSClassLoaderFactory;");
         var24.visitFieldInsn(181, replaceDotWithSlash(var4), "factory", "Lweblogic/wsee/util/JAXWSClassLoaderFactory;");
         var24.visitInsn(177);
         var24.visitMaxs(3, 1);
         var24.visitEnd();
         HashSet var25 = new HashSet();
         var7 = var3;

         do {
            Class[] var26 = var7.getInterfaces();
            int var12 = var26.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               Class var14 = var26[var13];
               Method[] var15 = var14.getMethods();
               int var16 = var15.length;

               for(int var17 = 0; var17 < var16; ++var17) {
                  Method var18 = var15[var17];
                  var25.add(var18);
               }
            }

            var7 = var7.getSuperclass();
         } while(var7 != null);

         Iterator var27 = var25.iterator();

         while(var27.hasNext()) {
            Method var29 = (Method)var27.next();
            var24 = var5.visitMethod(1, var29.getName(), "(L" + replaceDotWithSlash(var29.getParameterTypes()[0].getName()) + ";)V", (String)null, (String[])null);
            var24.visitCode();
            Label var31 = new Label();
            Label var32 = new Label();
            Label var33 = new Label();
            var24.visitTryCatchBlock(var31, var32, var33, (String)null);
            Label var34 = new Label();
            var24.visitTryCatchBlock(var33, var34, var33, (String)null);
            var24.visitMethodInsn(184, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;");
            var24.visitMethodInsn(182, "java/lang/Thread", "getContextClassLoader", "()Ljava/lang/ClassLoader;");
            var24.visitVarInsn(58, 2);
            var24.visitVarInsn(25, 0);
            var24.visitFieldInsn(180, replaceDotWithSlash(var4), "factory", "Lweblogic/wsee/util/JAXWSClassLoaderFactory;");
            var24.visitVarInsn(25, 2);
            var24.visitMethodInsn(182, "weblogic/wsee/util/JAXWSClassLoaderFactory", "setContextLoader", "(Ljava/lang/ClassLoader;)V");
            var24.visitLabel(var31);
            var24.visitVarInsn(25, 0);
            var24.visitFieldInsn(180, replaceDotWithSlash(var4), "target", "L" + replaceDotWithSlash(var1) + ";");
            var24.visitVarInsn(25, 1);
            var24.visitMethodInsn(182, replaceDotWithSlash(var1), var29.getName(), "(L" + replaceDotWithSlash(var29.getParameterTypes()[0].getName()) + ";)V");
            var24.visitLabel(var32);
            var24.visitMethodInsn(184, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;");
            var24.visitVarInsn(25, 2);
            var24.visitMethodInsn(182, "java/lang/Thread", "setContextClassLoader", "(Ljava/lang/ClassLoader;)V");
            Label var35 = new Label();
            var24.visitJumpInsn(167, var35);
            var24.visitLabel(var33);
            var24.visitVarInsn(58, 3);
            var24.visitLabel(var34);
            var24.visitMethodInsn(184, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;");
            var24.visitVarInsn(25, 2);
            var24.visitMethodInsn(182, "java/lang/Thread", "setContextClassLoader", "(Ljava/lang/ClassLoader;)V");
            var24.visitVarInsn(25, 3);
            var24.visitInsn(191);
            var24.visitLabel(var35);
            var24.visitInsn(177);
            var24.visitMaxs(2, 4);
            var24.visitEnd();
         }

         var5.visitEnd();
         byte[] var28 = var5.toByteArray();
         Class var30 = (Class)defineClass.invoke(var2, var4, var28, 0, var28.length, var3.getProtectionDomain());
         resolveClass.invoke(var2, var30);
         return var4;
      } catch (ClassNotFoundException var19) {
      } catch (IllegalArgumentException var20) {
      } catch (IllegalAccessException var21) {
      } catch (InvocationTargetException var22) {
      }

      return var1;
   }

   private static String replaceDotWithSlash(String var0) {
      return var0.replace('.', '/');
   }

   static {
      try {
         defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
         resolveClass = ClassLoader.class.getDeclaredMethod("resolveClass", Class.class);
      } catch (NoSuchMethodException var1) {
         throw new NoSuchMethodError(var1.getMessage());
      }

      AccessController.doPrivileged(new PrivilegedAction<Void>() {
         public Void run() {
            WSEEWebModule.defineClass.setAccessible(true);
            WSEEWebModule.resolveClass.setAccessible(true);
            return null;
         }
      });
   }
}
