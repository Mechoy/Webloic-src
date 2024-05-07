package weblogic.wsee.monitoring;

import com.sun.istack.Nullable;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeAggregatableBaseOperationRuntimeMBean;
import weblogic.management.runtime.WseeBaseOperationRuntimeMBean;
import weblogic.management.runtime.WseeBasePortRuntimeMBean;
import weblogic.management.runtime.WseeClientPortRuntimeMBean;
import weblogic.management.runtime.WseeClusterRoutingRuntimeMBean;
import weblogic.management.runtime.WseeHandlerRuntimeMBean;
import weblogic.management.runtime.WseeMcRuntimeMBean;
import weblogic.management.runtime.WseePortPolicyRuntimeMBean;
import weblogic.management.runtime.WseePortRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.management.runtime.WseeWsrmRuntimeMBean;
import weblogic.t3.srvr.ServerRuntime;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.mc.mbean.WseeMcRuntimeData;
import weblogic.wsee.mc.mbean.WseeMcRuntimeMBeanImpl;

public abstract class WseeBasePortRuntimeMBeanImpl<M extends WseeBasePortRuntimeMBean, D extends WseeBasePortRuntimeData> extends WseeRuntimeMBeanDelegate<M, D> implements WseeBasePortRuntimeMBean {
   private List<WseeBaseOperationRuntimeMBeanImpl> operations = new ArrayList();
   private List<WseeHandlerRuntimeMBeanImpl> handlers = new ArrayList();
   private WseePortPolicyRuntimeMBeanImpl metric = null;
   private WseeClusterRoutingRuntimeMBeanImpl _clusterRouting = null;
   private WseeWsrmRuntimeMBeanImpl _wsrm = null;
   private WseeMcRuntimeMBeanImpl _mc = null;
   private WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl _aggregatedBaseOperations = null;

   @Nullable
   public static WseeBasePortRuntimeMBean getFromPacket(Packet var0) {
      if (var0 == null) {
         return null;
      } else {
         Object var1 = null;
         if (var0.component != null) {
            var1 = (WseeBasePortRuntimeMBean)var0.component.getSPI(WseePortRuntimeMBean.class);
         }

         if (var1 == null) {
            var1 = getFromClientBinding(var0.getBinding());
         }

         return (WseeBasePortRuntimeMBean)var1;
      }
   }

   public static WseePortRuntimeMBean getFromEndpoint(WSEndpoint var0) {
      if (var0 == null) {
         return null;
      } else {
         WseeV2RuntimeMBean var1 = (WseeV2RuntimeMBean)var0.getSPI(WseeV2RuntimeMBean.class);
         if (var1 == null) {
            return null;
         } else {
            QName var2 = var0.getPortName();
            WseePortRuntimeMBean[] var3 = var1.getPorts();
            WseePortRuntimeMBean var4 = null;
            WseePortRuntimeMBean[] var5 = var3;
            int var6 = var3.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               WseePortRuntimeMBean var8 = var5[var7];
               if (var8.getPortName().equals(var2.getLocalPart())) {
                  var4 = var8;
                  break;
               }
            }

            return var4;
         }
      }
   }

   public static WseeClientPortRuntimeMBean getFromClientBinding(WSBinding var0) {
      if (var0 == null) {
         return null;
      } else {
         WseeClientPortRuntimeMBean var1 = null;
         ClientIdentityFeature var2 = (ClientIdentityFeature)var0.getFeature(ClientIdentityFeature.class);
         if (var2 != null) {
            WseeClientRuntimeMBeanImpl var3 = ClientIdentityRegistry.getClientRuntimeMBean(var2.getClientId());
            if (var3 != null) {
               var1 = var3.getPort();
            }
         }

         return var1;
      }
   }

   public WseeBasePortRuntimeMBeanImpl() throws ManagementException {
      super((String)null, (RuntimeMBean)null, (WseeRuntimeMBeanDelegate)null, false);
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   WseeBasePortRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeBasePortRuntimeMBeanImpl var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   WseeBasePortRuntimeMBeanImpl(String var1, String var2) throws ManagementException {
      super(var1, (RuntimeMBean)null, (WseeRuntimeMBeanDelegate)null, false);
   }

   protected void internalInitProxy(WseeBasePortRuntimeMBeanImpl var1) throws ManagementException {
      var1.setData(this.getData());
      var1.setMetric((WseePortPolicyRuntimeMBeanImpl)this.metric.createProxy(this.metric.getName(), var1));
      var1.setClusterRouting((WseeClusterRoutingRuntimeMBeanImpl)this._clusterRouting.createProxy(this._clusterRouting.getName(), var1));
      var1.setWsrm((WseeWsrmRuntimeMBeanImpl)this._wsrm.createProxy(this._wsrm.getName(), var1));
      var1.setMc((WseeMcRuntimeMBeanImpl)this._mc.createProxy(this._mc.getName(), var1));
      var1.setAggregatedBaseOperations((WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl)this._aggregatedBaseOperations.createProxy(this._aggregatedBaseOperations.getName(), var1));
      Iterator var2 = this.operations.iterator();

      while(var2.hasNext()) {
         WseeBaseOperationRuntimeMBeanImpl var3 = (WseeBaseOperationRuntimeMBeanImpl)var2.next();
         WseeBaseOperationRuntimeMBeanImpl var4 = (WseeBaseOperationRuntimeMBeanImpl)var3.createProxy(var3.getName(), var1);
         var1.addOperation(var4);
      }

      var2 = this.handlers.iterator();

      while(var2.hasNext()) {
         WseeHandlerRuntimeMBeanImpl var5 = (WseeHandlerRuntimeMBeanImpl)var2.next();
         WseeHandlerRuntimeMBeanImpl var6 = (WseeHandlerRuntimeMBeanImpl)var5.createProxy(var5.getName(), var1);
         var1.addHandler(var6);
      }

   }

   protected void setMetric(WseePortPolicyRuntimeMBeanImpl var1) {
      this.metric = var1;
      ((WseeBasePortRuntimeData)this.getData()).setPortPolicy((WseePortPolicyRuntimeData)var1.getData());
   }

   protected void setClusterRouting(WseeClusterRoutingRuntimeMBeanImpl var1) {
      this._clusterRouting = var1;
      ((WseeBasePortRuntimeData)this.getData()).setClusterRouting((WseeClusterRoutingRuntimeData)var1.getData());
   }

   protected void setWsrm(WseeWsrmRuntimeMBeanImpl var1) {
      this._wsrm = var1;
      ((WseeBasePortRuntimeData)this.getData()).setWsrm((WseeWsrmRuntimeData)var1.getData());
   }

   protected void setAggregatedBaseOperations(WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl var1) {
      this._aggregatedBaseOperations = var1;
      ((WseeBasePortRuntimeData)this.getData()).setAggregatedBaseOperationsData((WseeBasePortAggregatedBaseOperationsRuntimeData)var1.getData());
   }

   protected void setMc(WseeMcRuntimeMBeanImpl var1) {
      this._mc = var1;
      ((WseeBasePortRuntimeData)this.getData()).setMcData((WseeMcRuntimeData)var1.getData());
   }

   public String getPortName() {
      return this.name;
   }

   public String getTransportProtocolType() {
      return ((WseeBasePortRuntimeData)this.getData()).getTransportProtocolType();
   }

   protected void addOperation(WseeBaseOperationRuntimeMBeanImpl var1) {
      ((WseeBasePortRuntimeData)this.getData()).addOperation((WseeBaseOperationRuntimeData)var1.getData());
      this.operations.add(var1);
      var1.setParent(this);
      if (this.isRegistered()) {
         try {
            var1.register();
         } catch (Exception var3) {
            throw new RuntimeException(var3.toString(), var3);
         }
      }

   }

   public void addHandler(WseeHandlerRuntimeMBeanImpl var1) {
      ((WseeBasePortRuntimeData)this.getData()).addHandler((WseeHandlerRuntimeData)var1.getData());
      this.handlers.add(var1);
      var1.setParent(this);
      if (this.isRegistered()) {
         try {
            var1.register();
         } catch (Exception var3) {
            throw new RuntimeException(var3.toString(), var3);
         }
      }

   }

   public WseeHandlerRuntimeMBean[] getHandlers() {
      return (WseeHandlerRuntimeMBean[])this.handlers.toArray(new WseeHandlerRuntimeMBeanImpl[this.handlers.size()]);
   }

   public void register() throws ManagementException {
      if (!this.isRegistered()) {
         super.register();
         if (!this.isProxy()) {
            ((WseeBasePortRuntimeData)this.getData()).setStartTime(System.currentTimeMillis());
         }

         Iterator var1 = this.operations.iterator();

         while(var1.hasNext()) {
            WseeBaseOperationRuntimeMBeanImpl var2 = (WseeBaseOperationRuntimeMBeanImpl)var1.next();
            var2.register();
         }

         var1 = this.handlers.iterator();

         while(var1.hasNext()) {
            WseeHandlerRuntimeMBeanImpl var3 = (WseeHandlerRuntimeMBeanImpl)var1.next();
            var3.register();
         }

         this.metric.setParent(this);
         this.metric.register();
         this._clusterRouting.register();
         this._wsrm.register();
         this._mc.register();
         this._aggregatedBaseOperations.register();
      }
   }

   public void unregister() throws ManagementException {
      super.unregister();
      if (!this.isProxy()) {
         ((WseeBasePortRuntimeData)this.getData()).setStartTime(0L);
      }

      this.unregisterHandlers();
      this.unregisterOperations();
      this.unregisterWsspMetric();
      this.unregisterClusterRouting();
      this.unregisterWsrm();
      this.unregisterMc();
      this.unregisterAggregatedOps();
   }

   private void unregisterAggregatedOps() {
      if (this._aggregatedBaseOperations != null) {
         try {
            this._aggregatedBaseOperations.unregister();
            ServerRuntime.theOne().removeChild(this._aggregatedBaseOperations);
         } catch (ManagementException var2) {
            var2.printStackTrace();
         }
      }

      this._aggregatedBaseOperations = null;
   }

   private void unregisterWsspMetric() {
      if (this.metric != null) {
         try {
            this.metric.unregister();
            ServerRuntime.theOne().removeChild(this.metric);
         } catch (ManagementException var2) {
            var2.printStackTrace();
         }
      }

      this.metric = null;
   }

   private void unregisterOperations() {
      if (this.operations != null && this.operations.size() > 0) {
         Iterator var1 = this.operations.iterator();

         while(var1.hasNext()) {
            WseeBaseOperationRuntimeMBeanImpl var2 = (WseeBaseOperationRuntimeMBeanImpl)var1.next();

            try {
               var2.unregister();
               ServerRuntime.theOne().removeChild(var2);
            } catch (ManagementException var4) {
               var4.printStackTrace();
            }
         }

         this.operations.clear();
      }

      this.operations = null;
   }

   private void unregisterHandlers() {
      if (this.handlers != null && this.handlers.size() > 0) {
         Iterator var1 = this.handlers.iterator();

         while(var1.hasNext()) {
            WseeHandlerRuntimeMBeanImpl var2 = (WseeHandlerRuntimeMBeanImpl)var1.next();

            try {
               var2.unregister();
               ServerRuntime.theOne().removeChild(var2);
            } catch (ManagementException var4) {
               var4.printStackTrace();
            }
         }

         this.handlers.clear();
      }

      this.handlers = null;
   }

   private void unregisterClusterRouting() {
      if (this._clusterRouting != null) {
         try {
            this._clusterRouting.unregister();
            ServerRuntime.theOne().removeChild(this._clusterRouting);
         } catch (ManagementException var2) {
            WseeCoreLogger.logUnexpectedException(var2.toString(), var2);
         }

         this._clusterRouting = null;
      }

   }

   private void unregisterWsrm() {
      if (this._wsrm != null) {
         try {
            this._wsrm.unregister();
            ServerRuntime.theOne().removeChild(this._wsrm);
         } catch (ManagementException var2) {
            WseeCoreLogger.logUnexpectedException(var2.toString(), var2);
         }

         this._wsrm = null;
      }

   }

   private void unregisterMc() {
      if (this._mc != null) {
         try {
            this._mc.unregister();
            ServerRuntime.theOne().removeChild(this._mc);
         } catch (ManagementException var2) {
            WseeCoreLogger.logUnexpectedException(var2.toString(), var2);
         }

         this._mc = null;
      }

   }

   public int getPolicyFaults() {
      return this.metric.getPolicyFaults();
   }

   public long getStartTime() {
      return ((WseeBasePortRuntimeData)this.getData()).getStartTime();
   }

   /** @deprecated */
   @Deprecated
   public int getTotalFaults() {
      return this.metric.getTotalFaults();
   }

   public int getTotalSecurityFaults() {
      return this.metric.getTotalSecurityFaults();
   }

   public WseePortPolicyRuntimeMBean getPortPolicy() {
      return this.metric;
   }

   public WseeClusterRoutingRuntimeMBean getClusterRouting() {
      return this._clusterRouting;
   }

   public WseeWsrmRuntimeMBean getWsrm() {
      return this._wsrm;
   }

   public WseeMcRuntimeMBean getMc() {
      return this._mc;
   }

   public WseeAggregatableBaseOperationRuntimeMBean getAggregatedBaseOperations() {
      return this._aggregatedBaseOperations;
   }

   public WseeBaseOperationRuntimeMBean[] getBaseOperations() {
      return (WseeBaseOperationRuntimeMBean[])this.operations.toArray(new WseeBaseOperationRuntimeMBean[this.operations.size()]);
   }
}
