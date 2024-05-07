package weblogic.wsee.ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.jws.WLDeployment;
import weblogic.wsee.deploy.ClientDeployInfo;
import weblogic.wsee.deploy.EJBDeployInfo;
import weblogic.wsee.deploy.ServletDeployInfo;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.init.WsDeploymentChain;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;
import weblogic.wsee.ws.init.WsDeploymentListenerConfig;

public final class WsFactory {
   private static final boolean verbose = Verbose.isVerbose(WsFactory.class);
   private static final WsFactory instance = new WsFactory();

   private WsFactory() {
   }

   public static WsFactory instance() {
      return instance;
   }

   public WsService createClientService(ClientDeployInfo var1) throws WsException {
      WsService var2 = (new WsBuilder()).buildService(var1);
      this.callClientListeners(this.buildDeploymentContext(var2));
      return var2;
   }

   public WsService createServerService(ServletDeployInfo var1) throws WsException {
      PortComponentBean var2 = var1.getWlPortComp();
      WsService var3 = (new WsBuilder()).buildService(var1);
      WsDeploymentContext var4 = this.buildDeploymentContext(var3, var1.getContextPath(), var1.getServiceURIs(), var1.getServiceName());

      try {
         this.callServerListeners(var4, var2);
      } finally {
         var1.setDynamicEjbs(var4.getDynamicEjbs());
         var1.setBufferTargetURIs(var4.getBufferTargetURIs());
      }

      return var3;
   }

   public WsService createServerService(EJBDeployInfo var1) throws WsException {
      WsServiceImpl var2 = (new WsBuilder()).buildService(var1);
      WsDeploymentContext var3 = this.buildDeploymentContext(var2, var1.getContextPath(), var1.getServiceURIs(), var1.getServiceName());

      try {
         this.callServerListeners(var3, var1.getWlPortComp());
      } finally {
         var1.setDynamicEjbs(var3.getDynamicEjbs());
         var1.setBufferTargetURIs(var3.getBufferTargetURIs());
      }

      return var2;
   }

   public void loadPolicy(WsService var1) throws WsException {
      this.callClientListeners(this.buildDeploymentContext(var1));
   }

   private void callServerListeners(WsDeploymentContext var1, PortComponentBean var2) throws WsException {
      Iterator var3 = var1.getWsService().getEndpoints();

      while(var3.hasNext()) {
         WsEndpoint var4 = (WsEndpoint)var3.next();
         List var5 = this.getCustomListeners(var4, var2);
         if (verbose) {
            Verbose.log((Object)("Custom Deployment listener = " + var5));
         }

         WsDeploymentListener var6 = WsDeploymentChain.newServerChain(var5);

         try {
            var6.process(var1);
         } catch (WsDeploymentException var8) {
            throw new WsException("Failed to call deployment processor", var8);
         }
      }

   }

   private void callClientListeners(WsDeploymentContext var1) throws WsException {
      WsDeploymentListener var2 = WsDeploymentChain.newClientChain(Collections.emptyList());

      try {
         var2.process(var1);
      } catch (WsDeploymentException var4) {
         throw new WsException("Failed to call deployment processor", var4);
      }
   }

   private WsDeploymentContext buildDeploymentContext(WsService var1) {
      return this.buildDeploymentContext(var1, (String)null, (String[])null, (String)null);
   }

   private WsDeploymentContext buildDeploymentContext(WsService var1, String var2, String[] var3, String var4) {
      WsDeploymentContext var5 = new WsDeploymentContext();
      var5.setWsService(var1);
      var5.setServiceURIs(var3);
      String var6 = ApplicationVersionUtils.getCurrentVersionId();
      var5.setVersion(var6);
      var5.setContextPath(var2);
      var5.setServiceName(var4);
      if (verbose) {
         Verbose.log((Object)var5);
      }

      return var5;
   }

   private List<WsDeploymentListenerConfig> getCustomListeners(WsEndpoint var1, PortComponentBean var2) {
      ArrayList var3 = new ArrayList();
      this.addDeploymentListeners((PortComponentBean)var2, var3);
      Class var4 = var1.getJwsClass();
      if (var4 != null) {
         this.addDeploymentListeners((Class)var4, var3);
      }

      return var3;
   }

   private void addDeploymentListeners(Class var1, List<WsDeploymentListenerConfig> var2) {
      WLDeployment var3 = (WLDeployment)var1.getAnnotation(WLDeployment.class);
      if (var3 != null) {
         String[] var4 = var3.deploymentListener();
         if (var4 != null) {
            this.addDeploymentListeners(var4, var2);
         }
      }

   }

   private void addDeploymentListeners(PortComponentBean var1, List<WsDeploymentListenerConfig> var2) {
      if (var1 != null && var1.getDeploymentListenerList() != null) {
         String[] var3 = var1.getDeploymentListenerList().getDeploymentListeners();
         this.addDeploymentListeners(var3, var2);
      }

   }

   private void addDeploymentListeners(String[] var1, List<WsDeploymentListenerConfig> var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         try {
            var2.add(new WsDeploymentListenerConfig(ClassUtil.loadClass(var1[var3])));
         } catch (ClassNotFoundException var5) {
            throw new IllegalArgumentException("unable to load " + var1[var3], var5);
         }
      }

   }
}
