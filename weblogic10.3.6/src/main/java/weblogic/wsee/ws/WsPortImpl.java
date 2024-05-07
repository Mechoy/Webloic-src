package weblogic.wsee.ws;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.monitoring.WsspStats;
import weblogic.wsee.policy.deployment.PolicyRef;
import weblogic.wsee.wsdl.WsdlPort;

public final class WsPortImpl extends WsPort {
   private WsdlPort wsdlPort;
   private WsEndpoint endpoint;
   private HandlerList internalHandlerList;
   private Set inboundPolicyRefs = new LinkedHashSet();
   private Set outboundPolicyRefs = new LinkedHashSet();
   private RuntimeMBean mbean;
   private PortComponentBean portComp;
   private Map<Name, QName> soapDispatchMap;
   private Map<String, Map<Name, QName>> actionDispatchMap;
   private WsspStats stats;

   WsPortImpl(WsdlPort var1, WsEndpoint var2) {
      this.wsdlPort = var1;
      this.endpoint = var2;
   }

   public WsEndpoint getEndpoint() {
      return this.endpoint;
   }

   public Set getInboundPolicyRefs() {
      return this.inboundPolicyRefs;
   }

   public HandlerList getInternalHandlerList() {
      assert this.internalHandlerList != null;

      return this.internalHandlerList;
   }

   public void setInternalHandlerList(HandlerList var1) {
      this.internalHandlerList = var1;
   }

   public Map<Name, QName> getSoapDispatchMap() {
      return this.soapDispatchMap;
   }

   public Map<Name, QName> getActionDispatchMap(String var1) {
      return (Map)this.actionDispatchMap.get(var1);
   }

   public void setSoapDispatchMap(Map<Name, QName> var1) {
      this.soapDispatchMap = var1;
   }

   public void setActionDispatchMap(Map<String, Map<Name, QName>> var1) {
      this.actionDispatchMap = var1;
   }

   public Set getOutboundPolicyRefs() {
      return this.outboundPolicyRefs;
   }

   public WsdlPort getWsdlPort() {
      return this.wsdlPort;
   }

   public PortComponentBean getPortComponent() {
      return this.portComp;
   }

   public void setPortComponent(PortComponentBean var1) {
      this.portComp = var1;
   }

   public String toString() {
      return "WsPort{wsdlPort=" + this.wsdlPort + ", endpoint=" + this.endpoint + ", internalHandlerList=" + this.internalHandlerList + ", inboundPolicyRefs=" + this.inboundPolicyRefs + ", outboundPolicyRefs=" + this.outboundPolicyRefs + ", mbean=" + this.mbean + "}";
   }

   public void addPolicyRef(PolicyRef var1, boolean var2, boolean var3) {
      assert var1 != null;

      assert var2 || var3;

      if (var2) {
         this.inboundPolicyRefs.add(var1);
      }

      if (var3) {
         this.outboundPolicyRefs.add(var1);
      }

   }

   public RuntimeMBean getRuntimeMBean() {
      return this.mbean;
   }

   public void removePolicyRef(PolicyRef var1) {
      assert var1 != null;

      this.inboundPolicyRefs.remove(var1);
      this.outboundPolicyRefs.remove(var1);
   }

   public void resetPolicyRefs() {
      this.inboundPolicyRefs.clear();
      this.outboundPolicyRefs.clear();
   }

   public void setRuntimeMBean(RuntimeMBean var1) {
      this.mbean = var1;
   }

   public WsspStats getWsspStats() {
      return this.stats;
   }

   public void setWsspStats(WsspStats var1) {
      this.stats = var1;
   }
}
