package weblogic.wsee.monitoring;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.server.ContainerResolver;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeBaseRuntimeMBean;
import weblogic.management.runtime.WseePortConfigurationRuntimeMBean;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.handler.HandlerListImpl;
import weblogic.wsee.jaxws.tubeline.standard.ClientContainerUtil;
import weblogic.wsee.runtime.owsm.PolicySubjectUtil;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.util.UniqueNameSet;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsEndpoint;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsPortImpl;
import weblogic.wsee.ws.WsService;

public class WseeRuntimeMBeanManager {
   private static final boolean verbose = Verbose.isVerbose(WseeRuntimeMBeanManager.class);
   private static final Map<String, WseeBaseRuntimeMBean> wseeRuntimeMBeanNames = new HashMap();

   static void remove(String var0) {
      synchronized(wseeRuntimeMBeanNames) {
         wseeRuntimeMBeanNames.remove(var0);
      }
   }

   public static WseeBaseRuntimeMBean createJaxWsMBean(WSEndpoint<?> var0, RuntimeMBean var1, RuntimeMBean var2, String var3, String var4, String var5, String var6, WssPolicyContext var7) throws ManagementException {
      if (var1 == null) {
         throw new IllegalArgumentException("Null app runtime in runtime mbean");
      } else {
         String var8 = var0.getServiceName().getLocalPart();
         String var9 = "1";
         int var10 = var3.indexOf(35);
         if (var10 >= 0) {
            var9 = var3.substring(var10 + 1);
            var3 = var3.substring(0, var10);
         }

         String var11 = getOldStyleName(var3, var9, var8);
         DeployInfo var12 = (DeployInfo)var0.getContainer().getSPI(DeployInfo.class);
         String var13 = var12 == null ? (var2 != null ? var2.getName() : null) : var12.getModuleName();
         String var14 = getNewStyleName(var3, var9, var13, var8);
         synchronized(wseeRuntimeMBeanNames) {
            WseePortRuntimeMBeanImpl var16;
            if (!wseeRuntimeMBeanNames.containsKey(var14)) {
               var16 = createJaxWsPort(var0.getPortName(), var0.getBinding(), var0.getPort(), var13);
               if (var4 == null) {
               }

               WseeV2RuntimeMBeanImpl var21 = new WseeV2RuntimeMBeanImpl(var4, var2 != null ? var2 : var1, var5, var6, var3, var9, WseeRuntimeMBeanManager.Type.JAXWS + " " + WseeRuntimeMBeanManager.JaxWsVersion.latest(), var14);
               var21.addPort(var16);
               var21.setWebserviceDescriptrionName(var4);
               if (var7 != null) {
                  var21.getPolicyRuntime().addPolicies(var7.getPolicyServer().getCachedPolicies());
               }

               if (verbose) {
                  Verbose.log((Object)("WseeRuntimeMbeanImpl[" + var8 + "] with full name " + var14 + " created"));
               }

               wseeRuntimeMBeanNames.put(var14, var21);
               var21.register();
               if (!var11.equals(var14)) {
                  if (!wseeRuntimeMBeanNames.containsKey(var11)) {
                     if (verbose) {
                        Verbose.log((Object)("WseeRuntimeMbeanImpl[" + var8 + "] with full name " + var14 + " and old name " + var11 + " created AS PROXY"));
                     }

                     WseeRuntimeMBeanImpl var18 = (WseeRuntimeMBeanImpl)var21.createProxy(var11, var1);
                     var18.setServiceName(var11);
                     wseeRuntimeMBeanNames.put(var11, var18);
                     var18.register();
                  } else {
                     if (verbose) {
                        Verbose.log((Object)("WseeV2RuntimeMBeanImpl[" + var8 + "] with old name " + var11 + " already registered"));
                     }

                     WseeBaseRuntimeMBean var22 = (WseeBaseRuntimeMBean)wseeRuntimeMBeanNames.get(var11);
                     if (((WseeBaseRuntimeMBeanImpl)var22).isProxy()) {
                        var16 = (WseePortRuntimeMBeanImpl)var16.createProxy(var16.getName(), var22);
                     }

                     var22.addPort(var16);
                  }
               }

               if (verbose) {
                  Verbose.log((Object)("WseeV2RuntimeMbeanImpl[" + var8 + "] with full name " + var14 + " created"));
               }

               return var21;
            } else {
               if (verbose) {
                  Verbose.log((Object)("WseeV2RuntimeMBeanImpl[" + var8 + "] with full name " + var14 + " already registered"));
               }

               var16 = createJaxWsPort(var0.getPortName(), var0.getBinding(), var0.getPort(), var13);
               WseeBaseRuntimeMBean var17 = (WseeBaseRuntimeMBean)wseeRuntimeMBeanNames.get(var14);
               if (((WseeBaseRuntimeMBeanImpl)var17).isProxy()) {
                  var16 = (WseePortRuntimeMBeanImpl)var16.createProxy(var16.getName(), var17);
               }

               var17.addPort(var16);
               return var17;
            }
         }
      }
   }

   private static WseePortRuntimeMBeanImpl createJaxWsPort(QName var0, WSBinding var1, WSDLPort var2, String var3) throws ManagementException {
      if (var0 == null && var2 != null) {
         var0 = var2.getName();
      }

      String var4 = var0 != null ? var0.getLocalPart() : "???";
      String var5 = var2 != null ? var2.getOwner().getName().getLocalPart() : null;
      WseePortRuntimeMBeanImpl var6 = new WseePortRuntimeMBeanImpl(var4, "http");
      ((WseePortRuntimeData)var6.getData()).setPolicySubjectName(var4);
      ((WseePortRuntimeData)var6.getData()).setPolicySubjectType("WLSWSENDPOINT");
      ((WseePortRuntimeData)var6.getData()).setPolicyAttachmentSupport("binding.server.soap.http");
      String var7 = PolicySubjectUtil.formatEndpointPortResourcePattern(var4, var5, var3);
      ((WseePortRuntimeData)var6.getData()).setPolicySubjectResourcePattern(var7);
      List var8 = createJaxWsOperations(var2);
      Iterator var9 = var8.iterator();

      while(var9.hasNext()) {
         WseeOperationRuntimeMBeanImpl var10 = (WseeOperationRuntimeMBeanImpl)var9.next();
         var6.addOperation(var10);
         ((WseeOperationRuntimeData)var10.getData()).setPolicySubjectName(var10.getName());
         ((WseeOperationRuntimeData)var10.getData()).setPolicySubjectResourcePattern(var7 + "/OPERATIONs/" + var10.getName());
         ((WseeOperationRuntimeData)var10.getData()).setPolicySubjectType("WLSWSENDPOINT");
         ((WseeOperationRuntimeData)var10.getData()).setPolicyAttachmentSupport("binding.server.soap.http");
      }

      List var13 = createJaxWsHandlers(var1);
      Iterator var12 = var13.iterator();

      while(var12.hasNext()) {
         WseeHandlerRuntimeMBeanImpl var11 = (WseeHandlerRuntimeMBeanImpl)var12.next();
         var6.addHandler(var11);
      }

      return var6;
   }

   public static WseeClientPortRuntimeMBeanImpl createJaxWsClientPort(QName var0, WSBinding var1, WSDLPort var2, String var3, WseePortConfigurationRuntimeMBean var4) throws ManagementException {
      if (var0 == null && var2 != null) {
         var0 = var2.getName();
      }

      String var5 = var0 != null ? var0.getLocalPart() : "???";
      WseeClientPortRuntimeMBeanImpl var6 = new WseeClientPortRuntimeMBeanImpl(var5, "http");
      Iterator var7 = createJaxWsClientOperations(var2).iterator();

      while(var7.hasNext()) {
         WseeClientOperationRuntimeMBeanImpl var8 = (WseeClientOperationRuntimeMBeanImpl)var7.next();
         var6.addOperation(var8);
      }

      var7 = createJaxWsHandlers(var1).iterator();

      while(var7.hasNext()) {
         WseeHandlerRuntimeMBeanImpl var9 = (WseeHandlerRuntimeMBeanImpl)var7.next();
         var6.addHandler(var9);
      }

      var6.setWseePortConfigurationRuntimeMBean(var4);
      return var6;
   }

   private static List<WseeOperationRuntimeMBeanImpl> createJaxWsOperations(WSDLPort var0) throws ManagementException {
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         Iterable var2 = var0.getBinding().getPortType().getOperations();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            WSDLOperation var4 = (WSDLOperation)var3.next();
            var1.add(new WseeOperationRuntimeMBeanImpl(var4.getName().getLocalPart()));
         }
      }

      var1.add(WseeOperationRuntimeMBeanImpl.createWsProtocolOp());
      return var1;
   }

   private static List<WseeClientOperationRuntimeMBeanImpl> createJaxWsClientOperations(WSDLPort var0) throws ManagementException {
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         Iterable var2 = var0.getBinding().getPortType().getOperations();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            WSDLOperation var4 = (WSDLOperation)var3.next();
            var1.add(new WseeClientOperationRuntimeMBeanImpl(var4.getName().getLocalPart()));
         }
      }

      var1.add(WseeClientOperationRuntimeMBeanImpl.createWsProtocolOp());
      return var1;
   }

   private static List<WseeHandlerRuntimeMBeanImpl> createJaxWsHandlers(WSBinding var0) throws ManagementException {
      UniqueNameSet var1 = new UniqueNameSet();
      ArrayList var2 = new ArrayList();
      List var3 = var0.getHandlerChain();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Handler var5 = (Handler)var4.next();
         QName[] var6 = new QName[0];
         if (var5 instanceof SOAPHandler) {
            Set var7 = ((SOAPHandler)var5).getHeaders();
            if (var7 != null) {
               var6 = (QName[])var7.toArray(new QName[var7.size()]);
            }
         }

         WseeHandlerRuntimeMBeanImpl var8 = new WseeHandlerRuntimeMBeanImpl(var1.add(var5.getClass().getName()), var5.getClass(), var6);
         var2.add(var8);
      }

      return var2;
   }

   public static WseeBaseRuntimeMBean createJaxRpcMBean(ApplicationRuntimeMBean var0, ComponentRuntimeMBean var1, WsService var2, DeployInfo var3, String var4, String var5, String var6, String var7) throws ManagementException {
      if (var0 == null) {
         throw new IllegalArgumentException("Null app runtime in runtime mbean");
      } else {
         int var8 = var6.indexOf(35);
         if (var8 >= 0) {
            var7 = var6.substring(var8 + 1);
            var6 = var6.substring(0, var8);
         }

         String var9 = var2.getWsdlService().getName().getLocalPart();
         String var10 = getOldStyleName(var6, var7, var9);
         String var11 = var3 == null ? var1.getName() : var3.getModuleName();
         String var12 = getNewStyleName(var6, var7, var11, var9);
         synchronized(wseeRuntimeMBeanNames) {
            WseePortRuntimeMBeanImpl[] var14;
            WseePortRuntimeMBeanImpl[] var16;
            int var17;
            int var18;
            WseePortRuntimeMBeanImpl var19;
            if (!wseeRuntimeMBeanNames.containsKey(var12)) {
               var14 = createJaxRpcPorts(var2);
               WseeV2RuntimeMBeanImpl var23 = new WseeV2RuntimeMBeanImpl(var4, var1, (String)null, var5, var6, var7, WseeRuntimeMBeanManager.Type.JAXRPC + " " + WseeRuntimeMBeanManager.JaxRpcVersion.latest(), var12);
               var16 = var14;
               var17 = var14.length;

               for(var18 = 0; var18 < var17; ++var18) {
                  var19 = var16[var18];
                  var23.addPort(var19);
               }

               var23.setWebserviceDescriptrionName(var4);
               var23.getPolicyRuntime().addPolicies(var3.getWssPolicyContext().getPolicyServer().getCachedPolicies());
               if (verbose) {
                  Verbose.log((Object)("WseeRuntimeMbeanImpl[" + var9 + "] with full name " + var12 + " created"));
               }

               wseeRuntimeMBeanNames.put(var12, var23);
               var23.register();
               if (!var10.equals(var12)) {
                  if (!wseeRuntimeMBeanNames.containsKey(var10)) {
                     if (verbose) {
                        Verbose.log((Object)("WseeRuntimeMbeanImpl[" + var9 + "] with full name " + var12 + " and old name " + var10 + " created AS PROXY"));
                     }

                     WseeRuntimeMBeanImpl var24 = (WseeRuntimeMBeanImpl)var23.createProxy(var10, var0);
                     var24.setServiceName(var10);
                     wseeRuntimeMBeanNames.put(var10, var24);
                     var24.register();
                  } else {
                     if (verbose) {
                        Verbose.log((Object)("WseeV2RuntimeMBeanImpl[" + var9 + "] with old name " + var10 + " already registered"));
                     }

                     WseeBaseRuntimeMBean var25 = (WseeBaseRuntimeMBean)wseeRuntimeMBeanNames.get(var10);
                     WseePortRuntimeMBeanImpl[] var26;
                     if (((WseeBaseRuntimeMBeanImpl)var25).isProxy()) {
                        var26 = new WseePortRuntimeMBeanImpl[var14.length];

                        for(var18 = 0; var18 < var14.length; ++var18) {
                           var26[var18] = (WseePortRuntimeMBeanImpl)var14[var18].createProxy(var14[var18].getName(), var25);
                        }

                        var14 = var26;
                     }

                     var26 = var14;
                     var18 = var14.length;

                     for(int var27 = 0; var27 < var18; ++var27) {
                        WseePortRuntimeMBeanImpl var20 = var26[var27];
                        var25.addPort(var20);
                     }
                  }
               }

               if (verbose) {
                  Verbose.log((Object)("WseeV2RuntimeMbeanImpl[" + var9 + "] with full name " + var12 + " created"));
               }

               return var23;
            } else {
               if (verbose) {
                  Verbose.log((Object)("WseeV2RuntimeMBeanImpl[" + var9 + "] with full name " + var12 + " already registered"));
               }

               var14 = createJaxRpcPorts(var2);
               WseeBaseRuntimeMBean var15 = (WseeBaseRuntimeMBean)wseeRuntimeMBeanNames.get(var12);
               if (((WseeBaseRuntimeMBeanImpl)var15).isProxy()) {
                  var16 = new WseePortRuntimeMBeanImpl[var14.length];

                  for(var17 = 0; var17 < var14.length; ++var17) {
                     var16[var17] = (WseePortRuntimeMBeanImpl)var14[var17].createProxy(var14[var17].getName(), var15);
                  }

                  var14 = var16;
               }

               var16 = var14;
               var17 = var14.length;

               for(var18 = 0; var18 < var17; ++var18) {
                  var19 = var16[var18];
                  var15.addPort(var19);
               }

               return var15;
            }
         }
      }
   }

   private static String getOldStyleName(String var0, String var1, String var2) {
      StringBuilder var3 = new StringBuilder(var0);
      if (var1 != null) {
         var3.append('#').append(var1);
      }

      var3.append('!').append(var2);
      return var3.toString();
   }

   private static String getNewStyleName(String var0, String var1, String var2, String var3) {
      StringBuilder var4 = new StringBuilder(var0);
      if (var1 != null) {
         var4.append('#').append(var1);
      }

      if (var2 != null) {
         var4.append('!').append(var2);
      }

      var4.append('!').append(var3);
      return var4.toString();
   }

   private static WseePortRuntimeMBeanImpl[] createJaxRpcPorts(WsService var0) throws ManagementException {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.getPorts();

      while(var2.hasNext()) {
         WsPortImpl var3 = (WsPortImpl)var2.next();
         WseePortRuntimeMBeanImpl var4 = new WseePortRuntimeMBeanImpl(var3.getWsdlPort().getName().getLocalPart(), var3.getWsdlPort().getTransport());
         List var5 = createJaxRpcOperations(var3.getEndpoint());
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            WseeOperationRuntimeMBeanImpl var7 = (WseeOperationRuntimeMBeanImpl)var6.next();
            var4.addOperation(var7);
         }

         List var9 = createJaxRpcHandlers(var3);
         Iterator var10 = var9.iterator();

         while(var10.hasNext()) {
            WseeHandlerRuntimeMBeanImpl var8 = (WseeHandlerRuntimeMBeanImpl)var10.next();
            var4.addHandler(var8);
         }

         var1.add(var4);
         var3.setRuntimeMBean(var4);
         var3.setWsspStats((WsspStats)WsspStats.class.cast(var4.getPortPolicy()));
      }

      return (WseePortRuntimeMBeanImpl[])var1.toArray(new WseePortRuntimeMBeanImpl[var1.size()]);
   }

   private static List<WseeOperationRuntimeMBeanImpl> createJaxRpcOperations(WsEndpoint var0) throws ManagementException {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.getMethods();

      while(var2.hasNext()) {
         WsMethod var3 = (WsMethod)var2.next();
         WseeOperationRuntimeMBeanImpl var4 = new WseeOperationRuntimeMBeanImpl(var3.getOperationName().getLocalPart());
         var1.add(var4);
         var3.setStats(var4);
      }

      return var1;
   }

   private static List<WseeHandlerRuntimeMBeanImpl> createJaxRpcHandlers(WsPort var0) throws ManagementException {
      UniqueNameSet var1 = new UniqueNameSet();
      ArrayList var2 = new ArrayList();
      HandlerListImpl var3 = (HandlerListImpl)var0.getInternalHandlerList();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         HandlerInfo var5 = var3.getInfo(var4);
         WseeHandlerRuntimeMBeanImpl var6 = new WseeHandlerRuntimeMBeanImpl(var1.add(var3.getName(var4)), var5.getHandlerClass(), var5.getHeaders());
         var2.add(var6);
         var3.insert(var4, var6);
      }

      return var2;
   }

   public static WseeClientConfigurationRuntimeMBeanImpl createClientConfigurationMBean(String var0) throws ManagementException {
      ComponentRuntimeMBeanImpl var2 = ClientContainerUtil.getContainingComponentRuntimeByModuleName(ContainerResolver.getInstance().getContainer().getSPI(DeployInfo.class) == null ? null : ((DeployInfo)ContainerResolver.getInstance().getContainer().getSPI(DeployInfo.class)).getModuleName());
      WseeClientConfigurationRuntimeMBeanImpl var1 = new WseeClientConfigurationRuntimeMBeanImpl(var0, var2);
      return var1;
   }

   public static WseePortConfigurationRuntimeMBeanImpl createPortConfigurationMBean(WSDLPort var0, String var1, WseeClientConfigurationRuntimeMBeanImpl var2) throws ManagementException {
      WseePortConfigurationRuntimeMBeanImpl var3 = new WseePortConfigurationRuntimeMBeanImpl(var0.getName().getLocalPart(), var1);
      Iterator var4 = var0.getBinding().getBindingOperations().iterator();

      while(var4.hasNext()) {
         WSDLBoundOperation var5 = (WSDLBoundOperation)var4.next();
         WseeOperationConfigurationRuntimeMBeanImpl var6 = new WseeOperationConfigurationRuntimeMBeanImpl(var5.getName().getLocalPart(), var3);
         var3.addOperation(var6);
      }

      var2.addPort(var3);
      return var3;
   }

   public static enum JaxWsVersion {
      VERSION_20("2.0"),
      VERSION_21("2.1");

      private String _name;

      private JaxWsVersion(String var3) {
         this._name = var3;
      }

      public String toString() {
         return this._name;
      }

      public static JaxWsVersion latest() {
         return values()[values().length - 1];
      }
   }

   public static enum JaxRpcVersion {
      VERSION_11("1.1");

      private String _name;

      private JaxRpcVersion(String var3) {
         this._name = var3;
      }

      public String toString() {
         return this._name;
      }

      public static JaxRpcVersion latest() {
         return values()[values().length - 1];
      }
   }

   public static enum Type {
      JAXRPC("JAX-RPC"),
      JAXWS("JAX-WS");

      private String _name;

      private Type(String var3) {
         this._name = var3;
      }

      public String toString() {
         return this._name;
      }
   }
}
