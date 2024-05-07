package weblogic.wsee.jaxws;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.Component;
import com.sun.xml.ws.api.PropertySet;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.PropertySet.Property;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundPortType;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.wsee.jaxws.framework.PropertySetUtil;
import weblogic.wsee.jaxws.persistence.StandardPersistentPropertyRegister;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.monitoring.OperationStats;
import weblogic.wsee.monitoring.WseeClientRuntimeMBeanImpl;

public class MonitoringPipe extends AbstractFilterTubeImpl {
   @Nullable
   private WSDLPort port;
   private boolean isClient;
   private MonitoringStatMap _stats = null;
   private Map<String, WSDLBoundOperation> opMap;

   public MonitoringPipe(WSDLPort var1, Tube var2, WSBinding var3, Component var4, boolean var5) {
      super(var2);
      this.port = var1;
      this.isClient = var5;
      if (var5) {
         ClientIdentityFeature var6 = (ClientIdentityFeature)var3.getFeature(ClientIdentityFeature.class);
         if (var6 != null) {
            WseeClientRuntimeMBeanImpl var7 = ClientIdentityRegistry.getClientRuntimeMBean(var6.getClientId());
            if (var7 != null) {
               this._stats = var7.getStatMap();
            }
         }
      }

      if (var1 != null) {
         this.opMap = new HashMap();
         WSDLBoundPortType var9 = var1.getBinding();
         Iterator var11 = var9.getBindingOperations().iterator();

         while(var11.hasNext()) {
            WSDLBoundOperation var8 = (WSDLBoundOperation)var11.next();
            this.opMap.put(var8.getName().getLocalPart(), var8);
         }
      }

      StandardPersistentPropertyRegister var10 = (StandardPersistentPropertyRegister)var4.getSPI(StandardPersistentPropertyRegister.class);
      if (var10 != null) {
         Set var12 = var10.getStandardProperties();
         var12.add("WL_OP_NAME");
         var12.add("WL_DISPATCH_BEGIN");
         var12.add("WL_EXECUTION_BEGIN");
         var12.add("WL_EXECUTION_END");
         var10.getStandardPropertyBagClassNames().add(MonitoringPropertySet.class.getName());
      }

   }

   MonitoringPipe(MonitoringPipe var1, TubeCloner var2) {
      super(var1, var2);
      this.port = var1.port;
      this.isClient = var1.isClient;
      this._stats = var1._stats;
      this.opMap = var1.opMap;
   }

   public final MonitoringPipe copy(TubeCloner var1) {
      return new MonitoringPipe(this, var1);
   }

   private void report(String var1, WSDLBoundOperation var2, Packet var3, Throwable var4, Throwable var5, long var6, long var8, long var10, long var12) {
      MonitoringStatMap var14;
      if (this.isClient) {
         var14 = this._stats;
      } else {
         Component var15 = var3 != null ? var3.component : null;
         var14 = var15 != null ? (MonitoringStatMap)var15.getSPI(MonitoringStatMap.class) : null;
      }

      if (var14 != null && var1 != null) {
         OperationStats var17 = (OperationStats)var14.get(var1);
         if (var17 != null) {
            if (var2 != null && var2.getOperation().isOneWay()) {
               var17.reportOnewayInvocation(var8 - var6, var10 - var8);
            } else {
               if (var10 == var8 && var10 == 0L) {
                  var10 = var8 = (var12 - var6) / 2L + var6;
               }

               var17.reportInvocation(var8 - var6, var10 - var8, var12 - var10);
            }

            if (var4 != null) {
               var17.reportError(var4);
            } else if (var5 != null) {
               var17.reportResponseError(var5);
            } else {
               Message var16 = var3.getMessage();
               if (var16 != null && var16.isFault()) {
                  var17.reportResponseError(new Throwable("Exception in Web Service invocation."));
               }
            }
         }
      }

   }

   @NotNull
   public NextAction processRequest(Packet var1) {
      MonitoringPropertySet var2 = new MonitoringPropertySet();
      var1.addSatellite(var2);
      var2.setDispatchBegin(System.nanoTime());
      Message var3 = var1.getMessage();
      WSDLBoundOperation var4 = null;
      if (var3 != null && this.port != null) {
         var4 = null;

         try {
            var4 = var3.getOperation(this.port);
         } catch (UnsupportedOperationException var6) {
         }
      }

      if (var4 != null) {
         var2.setOpName(var4.getName().getLocalPart());
      } else {
         var2.setOpName("Ws-Protocol");
      }

      return this.doInvoke(this.next, var1);
   }

   @NotNull
   public NextAction processResponse(Packet var1) {
      MonitoringPropertySet var2 = (MonitoringPropertySet)MonitoringPipe.MonitoringPropertySet.propertySetRetriever.getFromPacket(var1);
      if (var2 != null) {
         long var3 = System.nanoTime();
         long var5 = var2.getExecutionBegin();
         long var7 = var2.getExecutionEnd();
         WSDLBoundOperation var9 = this.getWsdlOp(var2);
         this.report(var2.getOpName(), var9, var1, (Throwable)null, (Throwable)null, var2.getDispatchBegin(), var5, var7, var3);
      }

      return this.doReturnWith(var1);
   }

   private WSDLBoundOperation getWsdlOp(MonitoringPropertySet var1) {
      return this.opMap != null ? (WSDLBoundOperation)this.opMap.get(var1.getOpName()) : null;
   }

   @NotNull
   public NextAction processException(Throwable var1) {
      MonitoringPropertySet var2 = (MonitoringPropertySet)MonitoringPipe.MonitoringPropertySet.propertySetRetriever.getFromPacket(Fiber.current().getPacket());
      if (var2 != null) {
         long var3 = System.nanoTime();
         long var5 = var2.getExecutionBegin();
         long var7 = var2.getExecutionEnd();
         WSDLBoundOperation var9 = this.getWsdlOp(var2);
         Throwable var10 = null;
         Throwable var11 = null;
         if (var5 == 0L) {
            var10 = var1;
         } else {
            var11 = var1;
         }

         this.report(var2.getOpName(), var9, Fiber.current().getPacket(), var10, var11, var2.getDispatchBegin(), var5, var7, var3);
      }

      return this.doThrow(var1);
   }

   public static class MonitoringPropertySet extends PropertySet {
      public static PropertySetUtil.PropertySetRetriever<MonitoringPropertySet> propertySetRetriever = PropertySetUtil.getRetriever(MonitoringPropertySet.class);
      static final String OP_NAME = "WL_OP_NAME";
      static final String DISPATCH_BEGIN = "WL_DISPATCH_BEGIN";
      static final String EXECUTION_BEGIN = "WL_EXECUTION_BEGIN";
      static final String EXECUTION_END = "WL_EXECUTION_END";
      private String opName;
      private long dispatchBegin = 0L;
      private long executionBegin = 0L;
      private long executionEnd = 0L;
      private static final PropertySet.PropertyMap model = parse(MonitoringPropertySet.class);

      @Property({"WL_OP_NAME"})
      public String getOpName() {
         return this.opName;
      }

      public void setOpName(String var1) {
         this.opName = var1;
      }

      @Property({"WL_DISPATCH_BEGIN"})
      public long getDispatchBegin() {
         return this.dispatchBegin;
      }

      public void setDispatchBegin(long var1) {
         this.dispatchBegin = var1;
      }

      @Property({"WL_EXECUTION_END"})
      public long getExecutionEnd() {
         return this.executionEnd;
      }

      @Property({"WL_EXECUTION_BEGIN"})
      public long getExecutionBegin() {
         return this.executionBegin;
      }

      public void setExecutionEnd(long var1) {
         this.executionEnd = var1;
      }

      public void setExecutionBegin(long var1) {
         this.executionBegin = var1;
      }

      protected PropertySet.PropertyMap getPropertyMap() {
         return model;
      }
   }
}
