package weblogic.wsee.reliability2.property;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.j2ee.descriptor.wl.ReliabilityConfigBean;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.management.configuration.WebServiceReliabilityMBean;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.buffer.BufferingConfig;
import weblogic.wsee.jaxws.config.BasePropertyAccessor;
import weblogic.wsee.jaxws.config.MapPropertyAccessor;
import weblogic.wsee.jaxws.config.PerClientPropertyAccessor;
import weblogic.wsee.jaxws.config.PerServicePropertyAccessor;
import weblogic.wsee.jaxws.config.Property;
import weblogic.wsee.jaxws.config.PropertyAccessor;
import weblogic.wsee.jaxws.config.PropertyContainer;
import weblogic.wsee.jaxws.config.PropertySource;
import weblogic.wsee.jaxws.config.VmWidePropertyAccessor;
import weblogic.wsee.jaxws.framework.ConfigUtil;
import weblogic.wsee.jaxws.framework.policy.PolicyPropertyBag;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.policy.AcknowledgementInterval;
import weblogic.wsee.reliability.policy.BaseRetransmissionInterval;
import weblogic.wsee.reliability.policy.ExponentialBackoff;
import weblogic.wsee.reliability.policy.InactivityTimeout;
import weblogic.wsee.reliability.policy.RMAssertion;
import weblogic.wsee.reliability.policy.SequenceExpires;
import weblogic.wsee.reliability.policy.WsrmPolicyRuntimeHandler;
import weblogic.wsee.reliability.policy11.RM11Assertion;
import weblogic.wsee.reliability2.api.WsrmClientInitFeature;
import weblogic.wsee.reliability2.policy.WsrmPolicyHelper;
import weblogic.wsee.reliability2.policy.WsrmPolicySelectionHelper;
import weblogic.wsee.reliability2.sequence.DeliveryAssurance;
import weblogic.wsee.reliability2.tube.WsrmTubelineDeploymentListener;
import weblogic.wsee.ws.WsPort;

public class WsrmConfig {
   @Nullable
   public static Source getSource(@NotNull ClientTubeAssemblerContext var0, @Nullable Packet var1, boolean var2) {
      return getSource((Destination)null, var0, var1, var2);
   }

   @Nullable
   public static Source getSource(@Nullable ServerTubeAssemblerContext var0, @Nullable ClientTubeAssemblerContext var1, @Nullable Packet var2, boolean var3) {
      Destination var4 = getDestination(var0, var2, var3);
      return getSource(var4, var1, var2, var3);
   }

   @Nullable
   public static Destination getDestination(@Nullable ServerTubeAssemblerContext var0, @Nullable Packet var1, boolean var2) {
      NormalizedExpression var3 = null;
      if (var0 != null) {
         WsPort var4 = WsrmTubelineDeploymentListener.getWsPort(var0);
         WSDLPort var5 = var0.getWsdlModel();

         try {
            var3 = getEffectivePolicyForPacket(var1, false, var2, var4, var5);
         } catch (Exception var9) {
            throw new RuntimeException(var9.toString(), var9);
         }
      }

      PolicySelectionPreference var10 = var1 != null ? ((PolicyPropertyBag)PolicyPropertyBag.propertySetRetriever.getFromPacket(var1)).getPolicySelectionPreference() : null;
      Map var11 = var1 != null ? var1.invocationProperties : null;
      ReliabilityConfigBean var6 = null;
      if (var0 != null) {
         var6 = getServiceRmConfigBean(var0);
      }

      BufferingConfig.Service var7 = BufferingConfig.getServiceConfig(var0, var1);
      AllProps var8 = new AllProps(var7.getPersistenceConfig(), var3, var10, (WsrmClientInitFeature)null, (Map)null, var11, var6, (AllProps)null);
      return new Destination(var7, var8);
   }

   @Nullable
   public static Destination getDestination(@NotNull ClientTubeAssemblerContext var0, @Nullable Packet var1, boolean var2) {
      Source var3 = getSource(var0, var1, var2);
      var3.getAllProps().setPropertySource(PropertySource.EFFECTIVECLIENT_CONFIG);
      Map var4 = var1 != null ? var1.invocationProperties : null;
      BufferingConfig.Service var5 = BufferingConfig.getServiceConfig(var0, var1);
      AllProps var6 = new AllProps(var5.getPersistenceConfig(), (NormalizedExpression)null, (PolicySelectionPreference)null, (WsrmClientInitFeature)null, (Map)null, var4, (ReliabilityConfigBean)null, var3.getAllProps());
      return new Destination(var5, var6);
   }

   @Nullable
   private static Source getSource(@Nullable Destination var0, @Nullable ClientTubeAssemblerContext var1, @Nullable Packet var2, boolean var3) {
      if (var0 != null) {
         var0.getAllProps().setPropertySource(PropertySource.EFFECTIVESERVICE_CONFIG);
      }

      Map var4 = ConfigUtil.getServiceRefPropsForClient(var1);
      WsrmClientInitFeature var5 = null;
      if (var1 != null) {
         var5 = (WsrmClientInitFeature)var1.getBinding().getFeature(WsrmClientInitFeature.class);
      }

      WsPort var6 = null;
      if (var1 != null) {
         var6 = WsrmTubelineDeploymentListener.getWsPort(var1);
      }

      WSDLPort var7 = null;
      if (var1 != null) {
         var7 = var1.getWsdlModel();
      }

      NormalizedExpression var8;
      try {
         var8 = getEffectivePolicyForPacket(var2, true, var3, var6, var7);
      } catch (Exception var11) {
         throw new RuntimeException(var11.toString(), var11);
      }

      PersistenceConfig.Client var9 = PersistenceConfig.getClientConfig(var1);
      AllProps var10 = new AllProps(var9, var8, (PolicySelectionPreference)null, var5, var4, var2 != null ? var2.invocationProperties : null, (ReliabilityConfigBean)null, var0 != null ? var0.getAllProps() : null);
      return new Source(var10);
   }

   private static ReliabilityConfigBean getServiceRmConfigBean(@Nullable ServerTubeAssemblerContext var0) {
      if (var0 == null) {
         return null;
      } else {
         WsPort var1 = WsrmTubelineDeploymentListener.getWsPort(var0);
         PortComponentBean var2 = var1.getPortComponent();
         ReliabilityConfigBean var3 = var2 != null ? var2.getReliabilityConfig() : null;
         if (var3 != null && !var3.isCustomized()) {
            var3 = null;
         }

         return var3;
      }
   }

   private static NormalizedExpression getEffectivePolicyForPacket(@Nullable Packet var0, boolean var1, boolean var2, @Nullable WsPort var3, @Nullable WSDLPort var4) throws PolicyException {
      if (var3 == null) {
         return null;
      } else {
         WsrmPolicyHelper var5 = new WsrmPolicyHelper(var3);
         if (var0 == null) {
            return var5.getEndpointPolicy();
         } else {
            WsrmPropertyBag var6 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var0);
            WSDLBoundOperation var8 = var4 != null ? var0.getMessage().getOperation(var4) : null;
            String var7;
            if (var8 != null) {
               var7 = var8.getName().getLocalPart();
            } else if (!var1) {
               if (var2) {
                  var7 = var6.getInboundWsdlOperationName();
               } else {
                  var7 = var6.getOutboundWsdlOperationName();
               }
            } else if (var2) {
               var7 = var6.getOutboundWsdlOperationName();
            } else {
               var7 = var6.getInboundWsdlOperationName();
            }

            if (var7 != null) {
               NormalizedExpression var9;
               if (var2) {
                  var9 = var5.getResponseEffectivePolicy(var7);
               } else {
                  var9 = var5.getRequestEffectivePolicy(var7);
               }

               return var9;
            } else {
               return var5.getEndpointPolicy();
            }
         }
      }
   }

   public static class FeaturePropertyAccessor extends BasePropertyAccessor {
      public FeaturePropertyAccessor(Class<? extends Serializable> var1, WsrmClientInitFeature var2) {
         super(PropertySource.FEATURE, var1, var2);
      }

      public FeaturePropertyAccessor(WsrmClientInitFeature var1) {
         super(PropertySource.FEATURE, var1);
      }
   }

   private static class RM10SequenceExpiresAccessor extends RM10PolicyPropertyAccessor {
      public RM10SequenceExpiresAccessor(RMAssertion var1) {
         super(var1, String.class);
      }

      public Object getAssertionValue() {
         SequenceExpires var1 = this._rmAssertion.getSeqExpires();
         return var1 != null ? var1.getExpires() : null;
      }
   }

   private static class RM10InactivityTimeoutAccessor extends RM10PolicyPropertyAccessor {
      public RM10InactivityTimeoutAccessor(RMAssertion var1) {
         super(var1, String.class);
      }

      public Object getAssertionValue() {
         InactivityTimeout var1 = this._rmAssertion.getInactivityTimeout();
         if (var1 != null) {
            long var2 = var1.getTimeout();
            Duration var4 = this.createDurationFromMillis(var2);
            return var4.toString();
         } else {
            return null;
         }
      }
   }

   private static class RM10BaseRetransmissionIntervalAccessor extends RM10PolicyPropertyAccessor {
      public RM10BaseRetransmissionIntervalAccessor(RMAssertion var1) {
         super(var1, String.class);
      }

      public Object getAssertionValue() {
         BaseRetransmissionInterval var1 = this._rmAssertion.getBaseRetransmissionInterval();
         if (var1 != null) {
            long var2 = var1.getInterval();
            Duration var4 = this.createDurationFromMillis(var2);
            return var4.toString();
         } else {
            return null;
         }
      }
   }

   private static class RM10AcknowledgementIntervalAccessor extends RM10PolicyPropertyAccessor {
      public RM10AcknowledgementIntervalAccessor(RMAssertion var1) {
         super(var1, String.class);
      }

      public Object getAssertionValue() {
         AcknowledgementInterval var1 = this._rmAssertion.getAckInterval();
         if (var1 != null) {
            long var2 = var1.getInterval();
            Duration var4 = this.createDurationFromMillis(var2);
            return var4.toString();
         } else {
            return null;
         }
      }
   }

   private static class RM10ExponentialBackoffAccessor extends RM10PolicyPropertyAccessor {
      public RM10ExponentialBackoffAccessor(RMAssertion var1) {
         super(var1, Boolean.class);
      }

      public Class<? extends Serializable> getValueClass() {
         return Boolean.class;
      }

      public Object getAssertionValue() {
         ExponentialBackoff var1 = this._rmAssertion.getExponentialBackoff();
         return var1 != null;
      }
   }

   private abstract static class RM10PolicyPropertyAccessor extends BasePropertyAccessor {
      protected RMAssertion _rmAssertion;

      public RM10PolicyPropertyAccessor(RMAssertion var1, Class<? extends Serializable> var2) {
         super(PropertySource.SERVICE_POLICY, var2, (Object)null);
         this._rmAssertion = var1;
      }

      protected abstract Object getAssertionValue();

      public Object getValue() {
         return this._rmAssertion != null ? this.getAssertionValue() : null;
      }

      protected Duration createDurationFromMillis(long var1) {
         try {
            return DatatypeFactory.newInstance().newDuration(var1);
         } catch (DatatypeConfigurationException var4) {
            throw new RuntimeException(var4.toString(), var4);
         }
      }
   }

   private static class RMAssertionPolicyPropertyAccessor extends BasePropertyAccessor {
      @Nullable
      private NormalizedExpression _policy;
      @Nullable
      private WsrmClientInitFeature _feature;
      @Nullable
      private PolicySelectionPreference _policyPref;

      public RMAssertionPolicyPropertyAccessor(@Nullable NormalizedExpression var1, @Nullable WsrmClientInitFeature var2, @Nullable PolicySelectionPreference var3) {
         super(PropertySource.SERVICE_POLICY, PolicyAssertion.class, (Object)null);
         this._policy = var1;
         this._feature = var2;
         this._policyPref = var3;
      }

      public Object getValue() {
         if (this._policy == null) {
            return null;
         } else {
            WsrmPolicySelectionHelper var1 = new WsrmPolicySelectionHelper(this._policy);
            var1.setForceWsrm10Policy(this._feature != null && this._feature.isForceWsrm10());
            var1.setPolicySelectionPreference(this._policyPref);
            PolicyAssertion[] var2 = var1.getReliabilityPolicyAssertions();
            return var2 != null && var2.length > 0 ? var2[0] : null;
         }
      }
   }

   private static class ContainerDefaultPropertyAccessor extends BasePropertyAccessor {
      public ContainerDefaultPropertyAccessor(AllProps var1) {
         super(var1 != null ? var1.getPropertySource() : PropertySource.DEFAULT_VALUE, var1);
      }
   }

   public static class AllProps extends PropertyContainer {
      @Nullable
      protected PropertySource _propertySource;
      @NotNull
      protected PersistenceConfig.Common _persistConfig;
      protected NormalizedExpression _policy;
      protected PolicySelectionPreference _policyPref;
      protected WsrmClientInitFeature _feature;
      protected Map<String, Object> _serviceRefProps;
      protected Map<String, Object> _packetInvokeProps;
      protected Property<PolicyAssertion> _rmAssertion;
      protected Property<String> _inactivityTimeout;
      protected Property<String> _sequenceExpiration;
      protected Property<String> _baseRetransmissionInterval;
      protected Property<Boolean> _retransmissionExponentialBackoff;
      protected Property<Boolean> _nonBufferedSource;
      protected Property<String> _acknowledgementInterval;
      protected Property<Boolean> _nonBufferedDestination;

      protected List<Property> getPropertyFields() {
         ArrayList var1 = new ArrayList();
         var1.add(this._rmAssertion);
         var1.add(this._inactivityTimeout);
         var1.add(this._sequenceExpiration);
         var1.add(this._baseRetransmissionInterval);
         var1.add(this._retransmissionExponentialBackoff);
         var1.add(this._nonBufferedSource);
         var1.add(this._acknowledgementInterval);
         var1.add(this._nonBufferedDestination);
         return var1;
      }

      public AllProps(@NotNull PersistenceConfig.Common var1, @Nullable NormalizedExpression var2, @Nullable PolicySelectionPreference var3, @Nullable WsrmClientInitFeature var4, @Nullable Map<String, Object> var5, @Nullable Map<String, Object> var6, @Nullable ReliabilityConfigBean var7, @Nullable AllProps var8) {
         if (var4 != null && !var4.isCustomized()) {
            var4 = null;
         }

         if (var7 != null && !var7.isCustomized()) {
            var7 = null;
         }

         this._persistConfig = var1;
         this._policy = var2;
         this._policyPref = var3;
         this._feature = var4;
         this._serviceRefProps = var5;
         this._packetInvokeProps = var6;
         WebServiceMBean var9 = WebServiceMBeanFactory.getInstance();
         WebServiceReliabilityMBean var10 = var9.getWebServiceReliability();
         this._rmAssertion = new Property("RmAssertion", PolicyAssertion.class, (Serializable)null, new PropertyAccessor[]{new RMAssertionPolicyPropertyAccessor(var2, var4, var3), new ContainerDefaultPropertyAccessor(var8)});
         PolicyAssertion var11 = this.getRmAssertion();
         RMAssertion var12 = var11 instanceof RMAssertion ? (RMAssertion)var11 : null;
         this._inactivityTimeout = new Property("InactivityTimeout", String.class, "P0DT600S", new PropertyAccessor[]{new MapPropertyAccessor("weblogic.wsee.wsrm.InactivityTimeout", var6), new FeaturePropertyAccessor(var4), new PerClientPropertyAccessor("weblogic.wsee.wsrm.InactivityTimeout", var5), new PerServicePropertyAccessor(var7), new ContainerDefaultPropertyAccessor(var8), new RM10InactivityTimeoutAccessor(var12), new VmWidePropertyAccessor(var10)});
         this._sequenceExpiration = new Property("SequenceExpiration", String.class, "P1D", new PropertyAccessor[]{new MapPropertyAccessor("weblogic.wsee.wsrm.SequenceExpiration", var6), new FeaturePropertyAccessor(var4), new PerClientPropertyAccessor("weblogic.wsee.wsrm.SequenceExpiration", var5), new PerServicePropertyAccessor(var7), new ContainerDefaultPropertyAccessor(var8), new RM10SequenceExpiresAccessor(var12), new VmWidePropertyAccessor(var10)});
         this._baseRetransmissionInterval = new Property("BaseRetransmissionInterval", String.class, "P0DT8S", new PropertyAccessor[]{new MapPropertyAccessor("weblogic.wsee.wsrm.BaseRetransmissionInterval", var6), new FeaturePropertyAccessor(var4), new PerClientPropertyAccessor("weblogic.wsee.wsrm.BaseRetransmissionInterval", var5), new PerServicePropertyAccessor(var7), new ContainerDefaultPropertyAccessor(var8), new RM10BaseRetransmissionIntervalAccessor(var12), new VmWidePropertyAccessor(var10)});
         this._retransmissionExponentialBackoff = new Property("RetransmissionExponentialBackoff", Boolean.class, false, new PropertyAccessor[]{new MapPropertyAccessor("weblogic.wsee.wsrm.RetransmissionExponentialBackoff", var6), new FeaturePropertyAccessor(var4), new PerClientPropertyAccessor("weblogic.wsee.wsrm.RetransmissionExponentialBackoff", var5), new PerServicePropertyAccessor(var7), new ContainerDefaultPropertyAccessor(var8), new RM10ExponentialBackoffAccessor(var12), new VmWidePropertyAccessor(var10)});
         this._nonBufferedSource = new Property("NonBufferedSource", Boolean.class, false, new PropertyAccessor[]{new MapPropertyAccessor("weblogic.wsee.wsrm.NonBufferedSource", var6), new FeaturePropertyAccessor(Boolean.class, var4), new PerClientPropertyAccessor("weblogic.wsee.wsrm.NonBufferedSource", var5), new PerServicePropertyAccessor(var7), new ContainerDefaultPropertyAccessor(var8), new VmWidePropertyAccessor(var10)});
         this._acknowledgementInterval = new Property("AcknowledgementInterval", String.class, "P0DT3S", new PropertyAccessor[]{new MapPropertyAccessor("weblogic.wsee.wsrm.AcknowledgementInterval", var6), new FeaturePropertyAccessor(var4), new PerClientPropertyAccessor("weblogic.wsee.wsrm.AcknowledgementInterval", var5), new PerServicePropertyAccessor(var7), new ContainerDefaultPropertyAccessor(var8), new RM10AcknowledgementIntervalAccessor(var12), new VmWidePropertyAccessor(var10)});
         this._nonBufferedDestination = new Property("NonBufferedDestination", Boolean.class, false, new PropertyAccessor[]{new MapPropertyAccessor("weblogic.wsee.wsrm.NonBufferedDestination", var6), new FeaturePropertyAccessor(var4), new PerClientPropertyAccessor("weblogic.wsee.wsrm.NonBufferedDestination", var5), new PerServicePropertyAccessor(var7), new ContainerDefaultPropertyAccessor(var8), new VmWidePropertyAccessor(var10)});
      }

      public PropertySource getPropertySource() {
         return this._propertySource;
      }

      public void setPropertySource(PropertySource var1) {
         this._propertySource = var1;
      }

      public PersistenceConfig.Common getPersistenceConfig() {
         return this._persistConfig;
      }

      public PolicyAssertion getRmAssertion() {
         return (PolicyAssertion)this._rmAssertion.getValue();
      }

      public boolean isReliable() {
         return this.getRmAssertion() != null;
      }

      public WsrmConstants.RMVersion getRmVersion() {
         PolicyAssertion var1 = this.getRmAssertion();
         if (var1 == null) {
            return null;
         } else {
            return var1 instanceof RMAssertion ? WsrmConstants.RMVersion.RM_10 : WsrmConstants.RMVersion.latest();
         }
      }

      public DeliveryAssurance getDeliveryAssurance() {
         PolicyAssertion var2 = this.getRmAssertion();
         DeliveryAssurance var1;
         if (var2 instanceof RMAssertion) {
            RMAssertion var3 = (RMAssertion)var2;

            WsrmPolicyRuntimeHandler.RM10QosInfo var4;
            try {
               var4 = WsrmPolicyRuntimeHandler.parseRM10QOS(var3.getSeqQos());
            } catch (Exception var7) {
               throw new RuntimeException(var7.toString(), var7);
            }

            WsrmConstants.DeliveryQOS var5;
            switch (var4.safQoS) {
               case 2:
                  var5 = WsrmConstants.DeliveryQOS.AtLeastOnce;
                  break;
               case 3:
                  var5 = WsrmConstants.DeliveryQOS.AtMostOnce;
                  break;
               default:
                  var5 = WsrmConstants.DeliveryQOS.ExactlyOnce;
            }

            var1 = new DeliveryAssurance(var5, var4.inOrder);
         } else if (var2 instanceof RM11Assertion) {
            RM11Assertion var8 = (RM11Assertion)var2;
            weblogic.wsee.reliability.policy11.DeliveryAssurance var10 = var8.getDeliveryAssurance();
            WsrmConstants.DeliveryQOS var9;
            if (var10 != null) {
               if (var10.getAtLeastOnce() != null) {
                  var9 = WsrmConstants.DeliveryQOS.AtLeastOnce;
               } else if (var10.getAtMostOnce() != null) {
                  var9 = WsrmConstants.DeliveryQOS.AtMostOnce;
               } else {
                  var9 = WsrmConstants.DeliveryQOS.ExactlyOnce;
               }
            } else {
               var9 = WsrmConstants.DeliveryQOS.ExactlyOnce;
            }

            boolean var6 = var8.getDeliveryAssurance() == null || var8.getDeliveryAssurance().getInOrder() != null;
            var1 = new DeliveryAssurance(var9, var6);
         } else {
            var1 = new DeliveryAssurance(WsrmConstants.DeliveryQOS.ExactlyOnce, true);
         }

         return var1;
      }

      public String getInactivityTimeout() {
         return (String)this._inactivityTimeout.getValue();
      }

      public String getSequenceExpiration() {
         return (String)this._sequenceExpiration.getValue();
      }

      public PersistenceConfig.Common getPersistConfig() {
         return this._persistConfig;
      }

      public String getBaseRetransmissionInterval() {
         return (String)this._baseRetransmissionInterval.getValue();
      }

      public Boolean getRetransmissionExponentialBackoff() {
         return (Boolean)this._retransmissionExponentialBackoff.getValue();
      }

      public Boolean getNonBufferedSource() {
         return (Boolean)this._nonBufferedSource.getValue();
      }

      public String getAcknowledgementInterval() {
         return (String)this._acknowledgementInterval.getValue();
      }

      public Boolean isNonBufferedDestination() {
         return (Boolean)this._nonBufferedDestination.getValue();
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString());
         var1.append("\nPersistenceConfig:\n");
         var1.append(this.getPersistenceConfig().toString());
         return var1.toString();
      }
   }

   public static class Destination extends Common {
      private BufferingConfig.Service _bufConfig;

      public Destination(@NotNull BufferingConfig.Service var1, @NotNull AllProps var2) {
         super(var2);
         this._bufConfig = var1;
      }

      public boolean isNonBufferedDestination() {
         return !KernelStatus.isServer() || this._allProps.isNonBufferedDestination();
      }

      public String getAcknowledgementInterval() {
         return this._allProps.getAcknowledgementInterval();
      }

      public BufferingConfig.Service getBufferingConfig() {
         return this._bufConfig;
      }

      public PersistenceConfig.Common getPersistenceConfig() {
         return this._allProps.getPersistenceConfig();
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString());
         var1.append("\nBufferingConfig:\n\n");
         var1.append(this.getBufferingConfig().toString());
         return var1.toString();
      }
   }

   public static class Source extends Common {
      private Source(@NotNull AllProps var1) {
         super(var1);
      }

      public String getBaseRetransmissionInterval() {
         return this._allProps.getBaseRetransmissionInterval();
      }

      public Boolean getRetransmissionExponentialBackoff() {
         return this._allProps.getRetransmissionExponentialBackoff();
      }

      public Boolean isNonBufferedSource() {
         return this._allProps.getNonBufferedSource();
      }

      // $FF: synthetic method
      Source(AllProps var1, Object var2) {
         this(var1);
      }
   }

   public static class Common {
      protected AllProps _allProps;

      public Common(AllProps var1) {
         this._allProps = var1;
      }

      public AllProps getAllProps() {
         return this._allProps;
      }

      public PersistenceConfig.Common getPersistenceConfig() {
         return this._allProps.getPersistenceConfig();
      }

      public PolicyAssertion getRmAssertion() {
         return this._allProps.getRmAssertion();
      }

      public boolean isReliable() {
         return this._allProps.isReliable();
      }

      public WsrmConstants.RMVersion getRmVersion() {
         return this._allProps.getRmVersion();
      }

      public DeliveryAssurance getDeliveryAssurance() {
         return this._allProps.getDeliveryAssurance();
      }

      public String getInactivityTimeout() {
         return this._allProps.getInactivityTimeout();
      }

      public String getSequenceExpiration() {
         return this._allProps.getSequenceExpiration();
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer(this.getClass().getSimpleName());
         var1.append(":\n");
         var1.append(this._allProps.toString());
         return var1.toString();
      }
   }
}
