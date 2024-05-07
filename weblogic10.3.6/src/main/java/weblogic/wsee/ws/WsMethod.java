package weblogic.wsee.ws;

import java.util.Iterator;
import javax.xml.namespace.QName;
import weblogic.wsee.monitoring.OperationStats;
import weblogic.wsee.policy.framework.NormalizedExpression;

public abstract class WsMethod {
   public abstract WsEndpoint getEndpoint();

   public abstract String getMethodName();

   public abstract WsReturnType getReturnType();

   public abstract OperationStats getStats();

   public abstract void setStats(OperationStats var1);

   public abstract boolean isWrapped();

   public abstract QName getWrapperElement();

   public abstract QName getReturnWrapperElement();

   public abstract void setCachedEffectiveInboundPolicy(NormalizedExpression var1);

   public abstract NormalizedExpression getCachedEffectiveInboundPolicy();

   public abstract void setCachedEffectiveOutboundPolicy(NormalizedExpression var1);

   public abstract NormalizedExpression getCachedEffectiveOutboundPolicy();

   public abstract Iterator getExceptions();

   public abstract QName getOperationName();

   public abstract WsParameterType getParameter(int var1);

   public abstract Iterator getParameters();

   public abstract int getParameterSize();
}
