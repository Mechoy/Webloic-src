package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class OperationMBeanImpl extends XMLElementMBeanDelegate implements OperationMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_invocationStyle = false;
   private String invocationStyle;
   private boolean isSet_componentName = false;
   private String componentName;
   private boolean isSet_style = false;
   private String style;
   private boolean isSet_operationName = false;
   private String operationName;
   private boolean isSet_handlerChain = false;
   private HandlerChainMBean handlerChain;
   private boolean isSet_namespace = false;
   private String namespace;
   private boolean isSet_method = false;
   private String method;
   private boolean isSet_component = false;
   private ComponentMBean component;
   private boolean isSet_conversationPhase = false;
   private String conversationPhase;
   private boolean isSet_inSecuritySpec = false;
   private String inSecuritySpec;
   private boolean isSet_outSecuritySpec = false;
   private String outSecuritySpec;
   private boolean isSet_handlerChainName = false;
   private String handlerChainName;
   private boolean isSet_portTypeName = false;
   private String portTypeName;
   private boolean isSet_params = false;
   private ParamsMBean params;
   private boolean isSet_reliableDelivery = false;
   private ReliableDeliveryMBean reliableDelivery;
   private boolean isSet_encoding = false;
   private String encoding;

   public String getInvocationStyle() {
      return this.invocationStyle;
   }

   public void setInvocationStyle(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.invocationStyle;
      this.invocationStyle = var1;
      this.isSet_invocationStyle = var1 != null;
      this.checkChange("invocationStyle", var2, this.invocationStyle);
   }

   public String getComponentName() {
      return this.componentName;
   }

   public void setComponentName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.componentName;
      this.componentName = var1;
      this.isSet_componentName = var1 != null;
      this.checkChange("componentName", var2, this.componentName);
   }

   public String getStyle() {
      return this.style;
   }

   public void setStyle(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.style;
      this.style = var1;
      this.isSet_style = var1 != null;
      this.checkChange("style", var2, this.style);
   }

   public String getOperationName() {
      return this.operationName;
   }

   public void setOperationName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.operationName;
      this.operationName = var1;
      this.isSet_operationName = var1 != null;
      this.checkChange("operationName", var2, this.operationName);
   }

   public HandlerChainMBean getHandlerChain() {
      return this.handlerChain;
   }

   public void setHandlerChain(HandlerChainMBean var1) {
      HandlerChainMBean var2 = this.handlerChain;
      this.handlerChain = var1;
      this.isSet_handlerChain = var1 != null;
      this.checkChange("handlerChain", var2, this.handlerChain);
   }

   public String getNamespace() {
      return this.namespace;
   }

   public void setNamespace(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.namespace;
      this.namespace = var1;
      this.isSet_namespace = var1 != null;
      this.checkChange("namespace", var2, this.namespace);
   }

   public String getMethod() {
      return this.method;
   }

   public void setMethod(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.method;
      this.method = var1;
      this.isSet_method = var1 != null;
      this.checkChange("method", var2, this.method);
   }

   public ComponentMBean getComponent() {
      return this.component;
   }

   public void setComponent(ComponentMBean var1) {
      ComponentMBean var2 = this.component;
      this.component = var1;
      this.isSet_component = var1 != null;
      this.checkChange("component", var2, this.component);
   }

   public String getConversationPhase() {
      return this.conversationPhase;
   }

   public void setConversationPhase(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.conversationPhase;
      this.conversationPhase = var1;
      this.isSet_conversationPhase = var1 != null;
      this.checkChange("conversationPhase", var2, this.conversationPhase);
   }

   public String getInSecuritySpec() {
      return this.inSecuritySpec;
   }

   public void setInSecuritySpec(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.inSecuritySpec;
      this.inSecuritySpec = var1;
      this.isSet_inSecuritySpec = var1 != null;
      this.checkChange("inSecuritySpec", var2, this.inSecuritySpec);
   }

   public String getOutSecuritySpec() {
      return this.outSecuritySpec;
   }

   public void setOutSecuritySpec(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.outSecuritySpec;
      this.outSecuritySpec = var1;
      this.isSet_outSecuritySpec = var1 != null;
      this.checkChange("outSecuritySpec", var2, this.outSecuritySpec);
   }

   public String getHandlerChainName() {
      return this.handlerChainName;
   }

   public void setHandlerChainName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.handlerChainName;
      this.handlerChainName = var1;
      this.isSet_handlerChainName = var1 != null;
      this.checkChange("handlerChainName", var2, this.handlerChainName);
   }

   public String getPortTypeName() {
      return this.portTypeName;
   }

   public void setPortTypeName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.portTypeName;
      this.portTypeName = var1;
      this.isSet_portTypeName = var1 != null;
      this.checkChange("portTypeName", var2, this.portTypeName);
   }

   public ParamsMBean getParams() {
      return this.params;
   }

   public void setParams(ParamsMBean var1) {
      ParamsMBean var2 = this.params;
      this.params = var1;
      this.isSet_params = var1 != null;
      this.checkChange("params", var2, this.params);
   }

   public ReliableDeliveryMBean getReliableDelivery() {
      return this.reliableDelivery;
   }

   public void setReliableDelivery(ReliableDeliveryMBean var1) {
      ReliableDeliveryMBean var2 = this.reliableDelivery;
      this.reliableDelivery = var1;
      this.isSet_reliableDelivery = var1 != null;
      this.checkChange("reliableDelivery", var2, this.reliableDelivery);
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setEncoding(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.encoding;
      this.encoding = var1;
      this.isSet_encoding = var1 != null;
      this.checkChange("encoding", var2, this.encoding);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<operation");
      if (this.isSet_namespace) {
         var2.append(" namespace=\"").append(String.valueOf(this.getNamespace())).append("\"");
      }

      if (this.isSet_invocationStyle) {
         var2.append(" invocation-style=\"").append(String.valueOf(this.getInvocationStyle())).append("\"");
      }

      if (this.isSet_encoding) {
         var2.append(" encoding=\"").append(String.valueOf(this.getEncoding())).append("\"");
      }

      if (this.isSet_handlerChainName) {
         var2.append(" handler-chain=\"").append(String.valueOf(this.getHandlerChainName())).append("\"");
      }

      if (this.isSet_operationName) {
         var2.append(" name=\"").append(String.valueOf(this.getOperationName())).append("\"");
      }

      if (this.isSet_style) {
         var2.append(" style=\"").append(String.valueOf(this.getStyle())).append("\"");
      }

      if (this.isSet_method) {
         var2.append(" method=\"").append(String.valueOf(this.getMethod())).append("\"");
      }

      if (this.isSet_componentName) {
         var2.append(" component=\"").append(String.valueOf(this.getComponentName())).append("\"");
      }

      if (this.isSet_portTypeName) {
         var2.append(" port-type-name=\"").append(String.valueOf(this.getPortTypeName())).append("\"");
      }

      if (this.isSet_conversationPhase) {
         var2.append(" conversation-phase=\"").append(String.valueOf(this.getConversationPhase())).append("\"");
      }

      if (this.isSet_inSecuritySpec) {
         var2.append(" in-security-spec=\"").append(String.valueOf(this.getInSecuritySpec())).append("\"");
      }

      if (this.isSet_outSecuritySpec) {
         var2.append(" out-security-spec=\"").append(String.valueOf(this.getOutSecuritySpec())).append("\"");
      }

      var2.append(">\n");
      if (null != this.getParams()) {
         var2.append(this.getParams().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getReliableDelivery()) {
         var2.append(this.getReliableDelivery().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</operation>\n");
      return var2.toString();
   }
}
