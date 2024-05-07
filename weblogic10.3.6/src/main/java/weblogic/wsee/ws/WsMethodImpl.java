package weblogic.wsee.ws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.monitoring.OperationStats;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.util.HolderUtil;
import weblogic.wsee.util.NameValueList;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlOperation;

public final class WsMethodImpl extends WsMethod {
   private WsdlOperation operation;
   private WsEndpoint endpoint;
   private NameValueList parameterList = new NameValueList();
   private WsReturnType returnType;
   private List exceptionList = new ArrayList();
   private String methodName;
   private QName wrapperElement;
   private QName returnWrapperElement;
   private NormalizedExpression cachedEffectiveInboundPolicy;
   private NormalizedExpression cachedEffectiveOutboundPolicy;
   private OperationStats stats;

   WsMethodImpl(WsEndpoint var1, WsdlOperation var2) {
      this.operation = var2;
      this.endpoint = var1;
   }

   public WsEndpoint getEndpoint() {
      return this.endpoint;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMethodName(String var1) {
      this.methodName = var1;
   }

   public WsReturnType getReturnType() {
      return this.returnType;
   }

   public void setReturnType(WsReturnType var1) {
      this.returnType = var1;
   }

   public OperationStats getStats() {
      return this.stats;
   }

   public void setStats(OperationStats var1) {
      this.stats = var1;
   }

   public boolean isWrapped() {
      return this.wrapperElement != null;
   }

   public QName getWrapperElement() {
      return this.wrapperElement;
   }

   public void setWrapperElement(QName var1) {
      this.wrapperElement = var1;
   }

   public QName getReturnWrapperElement() {
      return this.returnWrapperElement;
   }

   public void setReturnWrapperElement(QName var1) {
      this.returnWrapperElement = var1;
   }

   public void setCachedEffectiveInboundPolicy(NormalizedExpression var1) {
      this.cachedEffectiveInboundPolicy = var1;
   }

   public NormalizedExpression getCachedEffectiveInboundPolicy() {
      return this.cachedEffectiveInboundPolicy;
   }

   public void setCachedEffectiveOutboundPolicy(NormalizedExpression var1) {
      this.cachedEffectiveOutboundPolicy = var1;
   }

   public NormalizedExpression getCachedEffectiveOutboundPolicy() {
      return this.cachedEffectiveOutboundPolicy;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("wsdlOperation", this.operation.getName());
      var1.writeField("parameterList", this.parameterList);
      var1.writeField("returnType", this.returnType);
      var1.writeField("faultList", this.exceptionList);
      var1.end();
   }

   public void addException(WsFault var1) {
      this.exceptionList.add(var1);
   }

   void addParameter(String var1, WsParameterType var2) {
      assert var1 != null;

      assert var2 != null;

      this.parameterList.put(var1, var2);
   }

   public Iterator getExceptions() {
      return this.exceptionList.iterator();
   }

   public QName getOperationName() {
      return this.operation.getName();
   }

   public WsParameterType getParameter(int var1) {
      assert var1 < this.parameterList.size();

      return (WsParameterType)this.parameterList.get(var1).value();
   }

   public Iterator getParameters() {
      return this.parameterList.values();
   }

   public int getParameterSize() {
      return this.parameterList.size();
   }

   public Object[] getMethodArgs(Map var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.getParameters();

      while(var3.hasNext()) {
         WsParameterType var4 = (WsParameterType)var3.next();
         switch (var4.getMode()) {
            case 0:
               var2.add(var1.get(var4.getName()));
               break;
            case 1:
            case 2:
               Object var5 = var1.get(var4.getName());
               Object var6 = newInstance(var4.getJavaHolderType());
               if (var5 != null && var4.getMode() == 2) {
                  HolderUtil.setHolderValue(var6, var5);
               }

               var2.add(var6);
               var1.put(var4.getName(), var6);
         }
      }

      return var2.toArray();
   }

   private static Object newInstance(Class var0) {
      try {
         return var0.newInstance();
      } catch (InstantiationException var2) {
         throw new InvocationException("Failed to create holder instance for: " + var0, var2);
      } catch (IllegalAccessException var3) {
         throw new InvocationException("Failed to create holder instance for: " + var0, var3);
      }
   }
}
