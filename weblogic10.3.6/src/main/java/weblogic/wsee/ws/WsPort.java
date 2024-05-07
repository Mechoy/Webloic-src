package weblogic.wsee.ws;

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

public abstract class WsPort {
   public abstract WsEndpoint getEndpoint();

   public abstract Set getInboundPolicyRefs();

   public abstract HandlerList getInternalHandlerList();

   public abstract void setInternalHandlerList(HandlerList var1);

   public abstract Map<Name, QName> getSoapDispatchMap();

   public abstract Map<Name, QName> getActionDispatchMap(String var1);

   public abstract Set getOutboundPolicyRefs();

   public abstract WsdlPort getWsdlPort();

   public abstract void addPolicyRef(PolicyRef var1, boolean var2, boolean var3);

   public abstract RuntimeMBean getRuntimeMBean();

   public abstract void removePolicyRef(PolicyRef var1);

   public abstract void resetPolicyRefs();

   public abstract PortComponentBean getPortComponent();

   public abstract void setPortComponent(PortComponentBean var1);

   public abstract WsspStats getWsspStats();

   public abstract void setWsspStats(WsspStats var1);
}
