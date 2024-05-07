package weblogic.servlet.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.xml.ws.WebServiceContext;
import weblogic.deployment.BaseEnvironmentBuilder;
import weblogic.deployment.EnvironmentBuilder;
import weblogic.deployment.EnvironmentException;
import weblogic.deployment.descriptors.xml.EJBReference;
import weblogic.deployment.descriptors.xml.EnvironmentEntry;
import weblogic.deployment.descriptors.xml.ResourceReference;
import weblogic.j2ee.J2EEUtils;
import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.MessageDestinationBean;
import weblogic.j2ee.descriptor.MessageDestinationRefBean;
import weblogic.j2ee.descriptor.PersistenceContextRefBean;
import weblogic.j2ee.descriptor.ResourceEnvRefBean;
import weblogic.j2ee.descriptor.ResourceRefBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.EjbReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.MessageDestinationDescriptorBean;
import weblogic.j2ee.descriptor.wl.ResourceDescriptionBean;
import weblogic.j2ee.descriptor.wl.ResourceEnvDescriptionBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.jndi.internal.ApplicationNamingNode;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.servlet.HTTPLogger;
import weblogic.utils.Debug;
import weblogic.utils.NestedException;
import weblogic.utils.collections.ArrayMap;

public class CompEnv {
   private static final boolean DEBUG = false;
   private static final String INJECTION_BINDING = "WebComponentCreator";
   private final EnvironmentBuilder eBuilder;
   private final WebAppServletContext servletContext;
   private final String subcontextName;
   private final Context webctx;
   private final Context msgDstnCtx;
   private final List envEntriesList = new ArrayList();
   private final List ejbRefsList = new ArrayList();
   private final List resRefsList = new ArrayList();
   private final ArrayMap resEnvRefsMap = new ArrayMap();
   private final List serviceRefsList = new ArrayList();
   private final List wlServiceRefsList = new ArrayList();
   private final Map msgDstnDescriptors = new HashMap();
   private final String linkRefPrefix;

   public CompEnv(WebAppServletContext var1) throws DeploymentException {
      this.servletContext = var1;
      this.subcontextName = this.servletContext.getName() + "/" + this.hashCode();
      Context var2 = this.servletContext.getApplicationContext().getEnvContext();
      this.linkRefPrefix = this.initLinkRefPrefix();
      if (var2 == null) {
         var2 = this.createApplicationContext();
      }

      try {
         this.webctx = (Context)var2.lookup("webapp");
         Context var3 = this.webctx.createSubcontext(J2EEUtils.normalizeJarName(this.subcontextName));
         var3.bind("app", var2);
         this.eBuilder = new EnvironmentBuilder(var3, this.servletContext.getApplicationId(), this.servletContext.getId());
      } catch (NamingException var4) {
         throw new AssertionError(var4);
      }

      this.msgDstnCtx = this.setupMessageDestinationSubContext(var2);
   }

   private String initLinkRefPrefix() {
      return this.servletContext.getWebAppModule() == null ? null : J2EEUtils.normalizeJarName(this.servletContext.getWebAppModule().getId() + "#");
   }

   void prepare() throws DeploymentException {
      WebAppModule var1 = this.servletContext.getWebAppModule();
      if (var1 != null) {
         WebAppBean var2 = var1.getWebAppBean();
         if (var2 != null) {
            WeblogicWebAppBean var3 = var1.getWlWebAppBean();
            this.registerEnvironmentEntries(var2);
            this.registerEjbReferenceDescriptions(var2, var3);
            this.registerEjbLocalReferenceDescriptions(var2, var3);
            this.registerServiceRefs(var2, var3);
            this.registerResourceReferenceDescriptions(var2, var3);
            this.registerResourceEnvDescriptions(var3);
            this.registerResourceEnvWebServices(var2);
            this.registerMessageDestinations(var2, var3);
         }
      }
   }

   void activate() throws DeploymentException {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = var1.getContextClassLoader();
      ClassLoader var3 = this.servletContext.getServletClassLoader();
      var1.setContextClassLoader(var3);
      WebAppBean var4 = this.servletContext.getWebAppModule().getWebAppBean();

      try {
         this.addWebComponentCreator();
         this.eBuilder.addEnvironmentEntries(this.envEntriesList);
         this.eBuilder.addResourceReferences(this.resRefsList);
         this.eBuilder.addServiceReferences(this.serviceRefsList, this.wlServiceRefsList, this.servletContext, this.servletContext.getURI());
         this.initializeMessageDestinationLinks();
         this.initializeMessageDestinationRefs();
         this.eBuilder.addResourceEnvReferences(this.resEnvRefsMap);
         this.eBuilder.addPersistenceContextRefs(var4.getPersistenceContextRefs(), var3, this.servletContext.getWebAppModule());
         this.eBuilder.addPersistenceUnitRefs(var4.getPersistenceUnitRefs(), var3, this.servletContext.getWebAppModule());

         try {
            javaURLContextFactory.pushContext(this.eBuilder.getContext());
            this.eBuilder.addEJBReferences(this.ejbRefsList, this.servletContext.getURI());
         } finally {
            javaURLContextFactory.popContext();
         }
      } catch (NamingException var18) {
         this.cleanup();
         throw new DeploymentException("Could not setup environment", var18);
      } catch (EnvironmentException var19) {
         this.cleanup();
         throw new DeploymentException("Could not setup environment", var19);
      } catch (NestedException var20) {
         this.cleanup();
         throw new DeploymentException(var20);
      } finally {
         var1.setContextClassLoader(var2);
      }

   }

   private Context createApplicationContext() throws DeploymentException {
      Hashtable var2 = new Hashtable(2);
      var2.put("weblogic.jndi.createIntermediateContexts", "true");
      var2.put("weblogic.jndi.replicateBindings", "false");
      Context var1 = (new ApplicationNamingNode("/")).getContext(var2);

      try {
         var1.createSubcontext("webapp");
         return var1;
      } catch (NamingException var4) {
         throw new DeploymentException("Can't create context", var4);
      }
   }

   private void registerEnvironmentEntries(WebAppBean var1) {
      EnvEntryBean[] var2 = var1.getEnvEntries();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            EnvEntryBean var4 = var2[var3];
            EnvironmentEntry var5 = new EnvironmentEntry();
            String[] var6 = var4.getDescriptions();
            if (var6 != null && var6.length > 0) {
               var5.setDescription(var6[0]);
            }

            var5.setName(var4.getEnvEntryName());
            var5.setValue(var4.getEnvEntryValue());
            var5.setType(var4.getEnvEntryType());
            this.envEntriesList.add(var5);
         }

      }
   }

   private void initializeMessageDestinationRefs() throws NamingException, EnvironmentException {
      if (this.servletContext.getWebAppModule() != null && this.servletContext.getWebAppModule().getWebAppBean() != null) {
         MessageDestinationRefBean[] var1 = this.servletContext.getWebAppModule().getWebAppBean().getMessageDestinationRefs();
         if (var1 != null && var1.length >= 1) {
            this.eBuilder.addMessageDestinationReferences(Arrays.asList(var1), this.servletContext.getLogContext());
         }
      }
   }

   private void removeMessageDestinationRefs() {
      if (this.servletContext.getWebAppModule() != null && this.servletContext.getWebAppModule().getWebAppBean() != null) {
         MessageDestinationRefBean[] var1 = this.servletContext.getWebAppModule().getWebAppBean().getMessageDestinationRefs();
         if (var1 != null && var1.length >= 1) {
            this.eBuilder.removeMessageDestinationReferences(Arrays.asList(var1));
         }
      }
   }

   private void initializeMessageDestinationLinks() throws DeploymentException {
      if (!this.msgDstnDescriptors.isEmpty()) {
         try {
            Iterator var1 = this.msgDstnDescriptors.values().iterator();

            while(var1.hasNext()) {
               MessageDestinationDescriptorBean var2 = (MessageDestinationDescriptorBean)var1.next();
               String var3 = J2EEUtils.normalizeJNDIName(var2.getMessageDestinationName());
               this.msgDstnCtx.bind(this.linkRefPrefix + var3, var2);
            }

         } catch (NamingException var4) {
            throw new DeploymentException(this.servletContext.getLogContext(), var4);
         }
      }
   }

   private void removeMessageDestinationLinks() {
      if (!this.msgDstnDescriptors.isEmpty()) {
         Iterator var1 = this.msgDstnDescriptors.values().iterator();

         while(var1.hasNext()) {
            MessageDestinationDescriptorBean var2 = (MessageDestinationDescriptorBean)var1.next();
            String var3 = J2EEUtils.normalizeJNDIName(var2.getMessageDestinationName());

            try {
               this.msgDstnCtx.unbind(this.linkRefPrefix + var3);
            } catch (NamingException var5) {
            }
         }
      }

   }

   private Context setupMessageDestinationSubContext(Context var1) {
      try {
         return (Context)var1.lookup("messageDestination");
      } catch (NameNotFoundException var5) {
         try {
            return var1.createSubcontext("messageDestination");
         } catch (NamingException var4) {
            throw new AssertionError(var4);
         }
      } catch (NamingException var6) {
         throw new AssertionError(var6);
      }
   }

   private void registerMessageDestinations(WebAppBean var1, WeblogicWebAppBean var2) throws DeploymentException {
      int var4;
      if (var2 != null) {
         MessageDestinationDescriptorBean[] var3 = var2.getMessageDestinationDescriptors();
         if (var3 != null) {
            for(var4 = 0; var4 < var3.length; ++var4) {
               this.msgDstnDescriptors.put(var3[var4].getMessageDestinationName(), var3[var4]);
            }
         }
      }

      MessageDestinationBean[] var6 = var1.getMessageDestinations();
      if (var6 != null && var6.length >= 1) {
         for(var4 = 0; var4 < var6.length; ++var4) {
            if (!this.msgDstnDescriptors.containsKey(var6[var4].getMessageDestinationName())) {
               Loggable var5 = HTTPLogger.logMissingMessageDestinationDescriptorLoggable(var6[var4].getMessageDestinationName(), this.servletContext.getLogContext());
               throw new DeploymentException(var5.getMessage());
            }
         }

      }
   }

   private void registerEjbReferenceDescriptions(WebAppBean var1, WeblogicWebAppBean var2) {
      EjbRefBean[] var3 = var1.getEjbRefs();
      if (var3 != null && var3.length != 0) {
         EjbReferenceDescriptionBean[] var4 = null;
         if (var2 != null) {
            var4 = var2.getEjbReferenceDescriptions();
         }

         for(int var5 = 0; var5 < var3.length; ++var5) {
            EjbRefBean var6 = var3[var5];
            EJBReference var7 = new EJBReference();
            String[] var8 = var6.getDescriptions();
            if (var8 != null && var8.length > 0) {
               var7.setDescription(var8[0]);
            }

            var7.setName(var6.getEjbRefName());
            var7.setRefType(var6.getEjbRefType());
            var7.setHomeInterfaceName(var6.getHome());
            var7.setRemoteInterfaceName(var6.getRemote());
            var7.setLinkedEjbName(var6.getEjbLink());
            var7.setMappedName(var6.getMappedName());
            if (var4 != null) {
               for(int var9 = 0; var9 < var4.length; ++var9) {
                  if (var6.getEjbRefName().equals(var4[var9].getEjbRefName())) {
                     var7.setJNDIName(this.transformJNDIName(var4[var9].getJNDIName()));
                     break;
                  }
               }
            }

            var7.setLocalLink(false);
            this.ejbRefsList.add(var7);
         }

      }
   }

   private void registerEjbLocalReferenceDescriptions(WebAppBean var1, WeblogicWebAppBean var2) {
      EjbLocalRefBean[] var3 = var1.getEjbLocalRefs();
      if (var3 != null && var3.length != 0) {
         EjbReferenceDescriptionBean[] var4 = null;
         if (var2 != null) {
            var4 = var2.getEjbReferenceDescriptions();
         }

         for(int var5 = 0; var5 < var3.length; ++var5) {
            EjbLocalRefBean var6 = var3[var5];
            EJBReference var7 = new EJBReference();
            String[] var8 = var6.getDescriptions();
            if (var8 != null && var8.length > 0) {
               var7.setDescription(var8[0]);
            }

            var7.setName(var6.getEjbRefName());
            var7.setRefType(var6.getEjbRefType());
            var7.setHomeInterfaceName(var6.getLocalHome());
            var7.setRemoteInterfaceName(var6.getLocal());
            var7.setLinkedEjbName(var6.getEjbLink());
            var7.setMappedName(var6.getMappedName());
            if (var4 != null) {
               for(int var9 = 0; var9 < var4.length; ++var9) {
                  if (var6.getEjbRefName().equals(var4[var9].getEjbRefName())) {
                     var7.setJNDIName(this.transformJNDIName(var4[var9].getJNDIName()));
                     break;
                  }
               }
            }

            var7.setLocalLink(true);
            this.ejbRefsList.add(var7);
         }

      }
   }

   private void registerResourceReferenceDescriptions(WebAppBean var1, WeblogicWebAppBean var2) {
      ResourceRefBean[] var3 = var1.getResourceRefs();
      if (var3 != null) {
         ResourceDescriptionBean[] var4 = null;
         if (var2 != null) {
            var4 = var2.getResourceDescriptions();
         }

         for(int var5 = 0; var5 < var3.length; ++var5) {
            ResourceRefBean var6 = var3[var5];
            ResourceReference var7 = new ResourceReference();
            var7.setName(var6.getResRefName());
            var7.setResourceType(var6.getResType());
            var7.setResourceAuthMode(var6.getResAuth());
            String[] var8 = var6.getDescriptions();
            if (var8 != null && var8.length > 0) {
               var7.setDescription(var8[0]);
            }

            var7.setResourceSharingScope(var6.getResSharingScope());
            var7.setMappedName(var6.getMappedName());
            if (var4 != null) {
               for(int var9 = 0; var9 < var4.length; ++var9) {
                  if (var6.getResRefName().equals(var4[var9].getResRefName())) {
                     String var10 = var4[var9].getJNDIName();
                     if (var10 == null || var10.length() < 1) {
                        String var11 = this.servletContext.getApplicationContext().getApplicationId();
                        String var12 = var4[var5].getResourceLink();
                        if (var12 != null && var12.length() > 0) {
                           var10 = constructJNDIName(var11, var12);
                        }
                     }

                     var7.setJNDIName(this.transformJNDIName(var10));
                     if (HTTPDebugLogger.isEnabled()) {
                        Loggable var13 = HTTPLogger.logBindingResourceReferenceLoggable(this.servletContext.getAppDisplayName(), this.servletContext.getId(), var6.getResRefName(), var10);
                        HTTPDebugLogger.debug(var13.getMessage());
                     }
                     break;
                  }
               }
            }

            this.resRefsList.add(var7);
         }

      }
   }

   private void registerResourceEnvWebServices(WebAppBean var1) {
      ResourceEnvRefBean[] var2 = var1.getResourceEnvRefs();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ResourceEnvRefBean var4 = var2[var3];
         if (WebServiceContext.class.getName().equals(var4.getResourceEnvRefType())) {
            this.resEnvRefsMap.put(var4.getResourceEnvRefName(), "java:comp/WebServiceContext");
         }
      }

   }

   private void registerResourceEnvDescriptions(WeblogicWebAppBean var1) {
      if (var1 != null) {
         ResourceEnvDescriptionBean[] var2 = var1.getResourceEnvDescriptions();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               String var4 = this.servletContext.getApplicationContext().getApplicationId();
               String var5 = var2[var3].getJNDIName();
               if (var5 == null || var5.length() < 1) {
                  String var6 = var2[var3].getResourceLink();
                  if (var6 != null && var6.length() > 0) {
                     var5 = constructJNDIName(var4, var6);
                  }
               }

               this.resEnvRefsMap.put(var2[var3].getResourceEnvRefName(), this.transformJNDIName(var5));
            }

         }
      }
   }

   private void registerServiceRefs(WebAppBean var1, WeblogicWebAppBean var2) {
      ServiceReferenceDescriptionBean[] var3 = null;
      if (var2 != null) {
         var3 = var2.getServiceReferenceDescriptions();
      }

      ServiceRefBean[] var4 = var1.getServiceRefs();
      int var5;
      if (var4 != null) {
         for(var5 = 0; var5 < var4.length; ++var5) {
            this.serviceRefsList.add(var4[var5]);
         }
      }

      if (var3 != null) {
         for(var5 = 0; var5 < var3.length; ++var5) {
            this.wlServiceRefsList.add(var3[var5]);
         }
      }

   }

   private String transformJNDIName(String var1) {
      return BaseEnvironmentBuilder.transformJNDIName(var1, this.servletContext.getApplicationName());
   }

   private static String constructJNDIName(String var0, String var1) {
      return "weblogic." + var0 + "." + var1;
   }

   private void destroyContext() {
      try {
         Context var1 = (Context)this.webctx.lookup(this.subcontextName);
         var1.unbind("app");
         this.webctx.destroySubcontext(this.subcontextName);
      } catch (NamingException var2) {
      }

   }

   void cleanup() {
      if (!this.resRefsList.isEmpty()) {
         this.eBuilder.removeResourceReferences(this.resRefsList);
      }

      this.eBuilder.removePersistenceContextRefs((PersistenceContextRefBean[])null);
      if (!this.resEnvRefsMap.isEmpty()) {
         this.eBuilder.removeResourceEnvReferences(this.resEnvRefsMap);
      }

      if (!this.ejbRefsList.isEmpty()) {
         this.eBuilder.removeEJBReferences(this.ejbRefsList);
      }

      this.removeMessageDestinationRefs();
      this.removeMessageDestinationLinks();
      if (!this.serviceRefsList.isEmpty()) {
         this.eBuilder.removeServiceReferences(this.serviceRefsList);
      }

   }

   void destroy() {
      this.removeWebComponentCreator();
      if (!this.envEntriesList.isEmpty()) {
         this.eBuilder.removeEnvironmentEntries(this.envEntriesList);
      }

      WebAppBean var1 = this.servletContext.getWebAppModule().getWebAppBean();
      this.eBuilder.removePersistenceContextRefs(var1.getPersistenceContextRefs());
      this.eBuilder.removePersistenceUnitRefs(var1.getPersistenceUnitRefs());
      this.eBuilder.removeStandardEntries();
      this.envEntriesList.clear();
      this.resRefsList.clear();
      this.resEnvRefsMap.clear();
      this.ejbRefsList.clear();
      this.msgDstnDescriptors.clear();
      this.serviceRefsList.clear();
      this.destroyContext();
   }

   Context getEnvironmentContext() {
      return this.eBuilder.getContext();
   }

   private void say(String var1) {
      Debug.say(this.subcontextName + ": " + var1);
   }

   public void dump() {
      Debug.say("DUMPING COMP ENV FOR WEBAPP: " + this.subcontextName);
      this.dumpContext(this.eBuilder.getContext(), "  ", this.servletContext.getApplicationContext().getEnvContext());
   }

   private void dumpContext(Context var1, String var2, Context var3) {
      if (var1 != null) {
         try {
            NamingEnumeration var4 = var1.listBindings("");

            while(var4.hasMoreElements()) {
               Object var5 = var4.next();
               Binding var6 = (Binding)var5;
               String var7 = var6.getName();
               var5 = var6.getObject();
               this.say(var2 + var7 + "=" + var5);
               if (var5 instanceof Context && var5 != var3) {
                  this.dumpContext((Context)var5, "  " + var2, var3);
               }
            }
         } catch (NamingException var8) {
            HTTPLogger.logErrorWithThrowable(this.servletContext.getLogContext(), "Naming exception while dumping context", var8);
         }

      }
   }

   void bindResourceRef(String var1, String var2, String var3, String var4, boolean var5, String var6) throws DeploymentException {
      ResourceReference var7 = new ResourceReference();
      var7.setName(var1);
      var7.setResourceType(var2);
      if (!ResourceReference.VALID_AUTH_MODES.contains(var3)) {
         throw new DeploymentException("invalid value for res-auth: " + var3 + ", valid values are: \"Application\" and \"Container\"");
      } else {
         var7.setResourceAuthMode(var3);
         var7.setJNDIName(var4);
         if (var5) {
            var7.setResourceSharingScope("Shareable");
         } else {
            var7.setResourceSharingScope("Unshareable");
         }

         var7.setDescription(var6);

         try {
            this.eBuilder.addResourceReference(var7);
         } catch (NamingException var9) {
            throw new DeploymentException(var9.getMessage(), var9);
         } catch (EnvironmentException var10) {
            throw new DeploymentException(var10.getMessage(), var10);
         }

         this.resRefsList.add(var7);
      }
   }

   void bindEjbRef(String var1, String var2, String var3, String var4, String var5, String var6, String var7, boolean var8) throws DeploymentException {
      EJBReference var9 = new EJBReference();
      var9.setName(var1);
      if (!EJBReference.VALID_EJB_REF_TYPES.contains(var2)) {
         throw new DeploymentException("invalid value for ejb-ref-type: " + var2 + ", valid values are: \"Entity\" and \"Session\"");
      } else {
         var9.setRefType(var2);
         var9.setHomeInterfaceName(var3);
         var9.setRemoteInterfaceName(var4);
         var9.setLinkedEjbName(var5);
         var9.setJNDIName(this.transformJNDIName(var6));
         var9.setLocalLink(var8);
         var9.setDescription(var7);
         this.ejbRefsList.add(var9);
      }
   }

   boolean isResourceBound(String var1) {
      boolean var2;
      Iterator var3;
      do {
         try {
            if (!this.resRefsList.isEmpty()) {
               var3 = this.resRefsList.iterator();

               while(var3.hasNext()) {
                  ResourceReference var4 = (ResourceReference)var3.next();
                  if (var4.getName().equals(var1)) {
                     return true;
                  }
               }
            }

            var2 = false;
         } catch (ConcurrentModificationException var6) {
            var6.printStackTrace();
            var2 = true;
         }
      } while(var2);

      do {
         try {
            if (!this.ejbRefsList.isEmpty()) {
               var3 = this.ejbRefsList.iterator();

               while(var3.hasNext()) {
                  EJBReference var7 = (EJBReference)var3.next();
                  if (var7.getName().equals(var1)) {
                     return true;
                  }
               }
            }

            var2 = false;
         } catch (ConcurrentModificationException var5) {
            var5.printStackTrace();
            var2 = true;
         }
      } while(var2);

      if (this.resEnvRefsMap.keySet().contains(var1)) {
         return true;
      } else {
         return false;
      }
   }

   private void addWebComponentCreator() throws NamingException {
      if (this.servletContext.isJsfApplication()) {
         Context var1 = this.eBuilder.getContext();
         Context var2 = (Context)var1.lookup("bea");
         if (var2 != null) {
            var2.bind("WebComponentCreator", this.servletContext.getComponentCreator());
         } else {
            throw new NamingException("Can't bind WebComponentCreator because of context bea not found. ");
         }
      }
   }

   private void removeWebComponentCreator() {
      if (this.servletContext.isJsfApplication()) {
         Context var1 = this.eBuilder.getContext();

         try {
            Context var2 = (Context)var1.lookup("bea");
            if (var2 != null) {
               var2.unbind("WebComponentCreator");
            }
         } catch (NamingException var3) {
         }

      }
   }

   EnvironmentBuilder getEnvironmentBuilder() {
      return this.eBuilder;
   }
}
