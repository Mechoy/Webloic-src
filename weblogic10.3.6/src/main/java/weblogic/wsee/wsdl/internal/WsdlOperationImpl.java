package weblogic.wsee.wsdl.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlExtensionParser;
import weblogic.wsee.wsdl.WsdlExtensionRegistry;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlMethodBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPartBuilder;

public class WsdlOperationImpl extends WsdlExtensibleImpl implements WsdlOperationBuilder {
   private MessageElement input;
   private MessageElement output;
   private Map<String, MessageElement> faultList;
   private PolicyURIs policyUris;
   private QName name;
   private String parameterOrder;
   private WsdlMethodBuilder wsdlMethod;
   public static final int REQUEST_RESPONSE = 0;
   public static final int ONE_WAY = 1;
   public static final int SOLICIT_RESPONSE = 2;
   public static final int NOTIFICATION = 3;
   private int type;
   private WsdlDefinitionsBuilder definitions;

   WsdlOperationImpl(WsdlDefinitionsBuilder var1) {
      this.faultList = new LinkedHashMap();
      this.wsdlMethod = null;
      this.type = 0;
      this.definitions = var1;
   }

   WsdlOperationImpl(QName var1, WsdlDefinitionsBuilder var2) {
      this(var2);
      this.name = var1;
   }

   public String getParameterOrder() {
      return this.parameterOrder;
   }

   public void setParameterOrder(String var1) {
      this.parameterOrder = var1;
   }

   public QName getName() {
      return this.name;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public WsdlMessageBuilder getInput() {
      return this.input == null ? null : (WsdlMessageBuilder)this.definitions.getMessages().get(this.input.message);
   }

   public String getInputAction() {
      return this.input != null && this.input.action != null ? this.input.action.getActionURI() : null;
   }

   public String getInputName() {
      if (this.input == null) {
         return null;
      } else {
         String var1 = this.input.getName();
         if (StringUtil.isEmpty(var1)) {
            if (this.type != 1 && this.type != 3) {
               if (this.type == 0) {
                  var1 = this.name.getLocalPart() + "Request";
               } else if (this.type == 2) {
                  var1 = this.name.getLocalPart() + "Solicit";
               }
            } else {
               var1 = this.name.getLocalPart();
            }
         }

         return var1;
      }
   }

   public PolicyURIs getInputPolicyUris() {
      return this.input == null ? null : this.input.getPolicyUris();
   }

   public void setInputPolicyUris(PolicyURIs var1) {
      if (this.input != null) {
         this.input.setPolicyUris(var1);
      }

   }

   public WsdlMessageBuilder getOutput() {
      return this.output == null ? null : (WsdlMessageBuilder)this.definitions.getMessages().get(this.output.message);
   }

   public String getOutputAction() {
      return this.output != null && this.output.action != null ? this.output.action.getActionURI() : null;
   }

   public String getOutputName() {
      if (this.output == null) {
         return null;
      } else {
         String var1 = this.output.getName();
         if (StringUtil.isEmpty(var1)) {
            if (this.type != 1 && this.type != 3) {
               var1 = this.name.getLocalPart() + "Response";
            } else {
               var1 = this.name.getLocalPart();
            }
         }

         return var1;
      }
   }

   public PolicyURIs getOutputPolicyUris() {
      return this.output == null ? null : this.output.getPolicyUris();
   }

   public void setOutputPolicyUris(PolicyURIs var1) {
      if (this.output != null) {
         this.output.setPolicyUris(var1);
      }

   }

   public Map<String, WsdlMessageBuilder> getFaults() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.faultList.values().iterator();

      while(var2.hasNext()) {
         MessageElement var3 = (MessageElement)var2.next();
         WsdlMessageBuilder var4 = (WsdlMessageBuilder)this.definitions.getMessages().get(var3.message);
         var1.put(var3.name, var4);
      }

      return var1;
   }

   public WsdlMethodBuilder getWsdlMethod() {
      return this.getWsdlMethod(false);
   }

   public WsdlMethodBuilder getWsdlMethod(boolean var1) {
      if (this.wsdlMethod == null) {
         this.wsdlMethod = new WsdlMethodImpl();
         WsdlMessageBuilder var2;
         WsdlMessageBuilder var3;
         if (this.isWLW81CallbackOperation()) {
            var2 = this.getOutput();
            var3 = this.getInput();
         } else {
            var2 = this.getInput();
            var3 = this.getOutput();
         }

         WsdlPartBuilder var4 = null;
         String var5 = WsdlUtils.findReturnPart(this);
         if (!StringUtil.isEmpty(var5)) {
            var4 = (WsdlPartBuilder)var3.getParts().get(var5);
            this.wsdlMethod.setResultPart(var4);
         }

         String var6 = this.getParameterOrder();
         if (StringUtil.isEmpty(var6)) {
            this.fillWithOutParameterOrder(var2, var3, var4);
         } else {
            this.fillWithParameterOrder(var2, var3, var4, var6, var1);
         }
      }

      return this.wsdlMethod;
   }

   public void flipCallbackInputAndOutputParts() {
      if (this.isWLW81CallbackOperation()) {
         MessageElement var1 = this.input;
         this.input = this.output;
         this.output = var1;
         if (this.type == 2) {
            this.type = 0;
         }

         if (this.type == 3) {
            this.type = 1;
         }
      }

   }

   private void fillWithOutParameterOrder(WsdlMessageBuilder var1, WsdlMessageBuilder var2, WsdlPartBuilder var3) {
      HashSet var4 = new HashSet();
      Iterator var5;
      WsdlPartBuilder var6;
      WsdlPartBuilder var7;
      if (var1 != null) {
         for(var5 = var1.getParts().values().iterator(); var5.hasNext(); this.wsdlMethod.addWsdlParameter(new WsdlParameterImpl(var6, var7))) {
            var6 = (WsdlPartBuilder)var5.next();
            var7 = null;
            if (var2 != null) {
               var7 = (WsdlPartBuilder)var2.getParts().get(var6.getName());
               if (var7 != null && var7.equals(var6)) {
                  var4.add(var7);
               } else {
                  var7 = null;
               }
            }
         }
      }

      if (var2 != null) {
         var5 = var2.getParts().values().iterator();

         while(var5.hasNext()) {
            var6 = (WsdlPartBuilder)var5.next();
            if (var6 != var3 && !var4.contains(var6)) {
               this.wsdlMethod.addWsdlParameter(new WsdlParameterImpl((WsdlPartBuilder)null, var6));
            }
         }
      }

   }

   private void fillWithParameterOrder(WsdlMessageBuilder var1, WsdlMessageBuilder var2, WsdlPartBuilder var3, String var4, boolean var5) {
      ArrayList var6 = new ArrayList();
      if (var4 != null) {
         StringTokenizer var7 = new StringTokenizer(var4, " ");

         while(var7.hasMoreTokens()) {
            var6.add(var7.nextToken());
         }
      }

      HashSet var13 = new HashSet();
      HashSet var8 = new HashSet();
      Iterator var9 = var6.iterator();

      WsdlPartBuilder var11;
      while(var9.hasNext()) {
         String var10 = (String)var9.next();
         var11 = (WsdlPartBuilder)var1.getParts().get(var10);
         if (var11 != null) {
            var8.add(var11);
         }

         WsdlPartBuilder var12 = null;
         if (var2 != null) {
            var12 = (WsdlPartBuilder)var2.getParts().get(var10);
            if ((var12 == null || var11 != null) && (var11 == null || !var11.equals(var12))) {
               var12 = null;
            } else {
               var13.add(var12);
            }
         }

         if (var11 == null && var12 == null) {
            throw new IllegalArgumentException("No In part or out part found for: " + var10 + " with Parameter Order = " + var4);
         }

         this.wsdlMethod.addWsdlParameter(new WsdlParameterImpl(var11, var12));
      }

      WsdlPartBuilder var14;
      if (var5 && var1 != null) {
         var9 = var1.getParts().values().iterator();

         label70:
         while(true) {
            do {
               if (!var9.hasNext()) {
                  break label70;
               }

               var14 = (WsdlPartBuilder)var9.next();
            } while(var8.contains(var14));

            var11 = null;
            if (var2 != null) {
               var11 = (WsdlPartBuilder)var2.getParts().get(var14.getName());
               if (var11 != null && var11.equals(var14)) {
                  var13.add(var11);
               } else {
                  var11 = null;
               }
            }

            this.wsdlMethod.addWsdlParameter(new WsdlParameterImpl(var14, var11));
         }
      }

      if (var2 != null) {
         var9 = var2.getParts().values().iterator();

         while(var9.hasNext()) {
            var14 = (WsdlPartBuilder)var9.next();
            if (var14 != var3 && !var13.contains(var14)) {
               this.wsdlMethod.addWsdlParameter(new WsdlParameterImpl((WsdlPartBuilder)null, var14));
            }
         }
      }

   }

   public WsdlMessageBuilder getFault(String var1) throws WsdlException {
      Iterator var2 = this.faultList.values().iterator();

      MessageElement var3;
      do {
         if (!var2.hasNext()) {
            throw new WsdlException("Unable to find fault with name: " + var1);
         }

         var3 = (MessageElement)var2.next();
      } while(!var1.equals(var3.name));

      return (WsdlMessageBuilder)this.definitions.getMessages().get(var3.message);
   }

   public PolicyURIs getFaultPolicyUris(String var1) {
      Iterator var2 = this.faultList.values().iterator();

      MessageElement var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MessageElement)var2.next();
      } while(!var3.getName().equals(var1));

      return var3.getPolicyUris();
   }

   public void setFaultPolicyUris(String var1, PolicyURIs var2) {
      Iterator var3 = this.faultList.values().iterator();

      while(var3.hasNext()) {
         MessageElement var4 = (MessageElement)var3.next();
         if (var4.getName().equals(var1)) {
            var4.setPolicyUris(var2);
         }
      }

   }

   public PolicyURIs getPolicyUris() {
      return this.policyUris;
   }

   public void setPolicyUris(PolicyURIs var1) {
      this.policyUris = var1;
   }

   public void parse(Element var1, String var2) throws WsdlException {
      this.addDocumentation(var1);
      this.parseAttributes(var1, var2);
      NodeList var3 = var1.getChildNodes();
      boolean var4 = false;
      boolean var5 = false;

      for(int var6 = 0; var6 < var3.getLength(); ++var6) {
         Node var7 = var3.item(var6);
         if (!WsdlReader.isWhiteSpace(var7)) {
            WsdlReader.checkDomElement(var7);
            if ("input".equals(var7.getLocalName())) {
               if (!var4) {
                  var4 = true;
                  var5 = true;
               }

               this.input = new MessageElement((Element)var7, false);
            } else if ("output".equals(var7.getLocalName())) {
               if (!var4) {
                  var4 = true;
                  var5 = false;
               }

               this.output = new MessageElement((Element)var7, false);
            } else if ("fault".equals(var7.getLocalName())) {
               if (!var4) {
                  throw new WsdlException("Fount a fault message before input or output. for operation " + this);
               }

               MessageElement var8 = new MessageElement((Element)var7, true);
               this.faultList.put(var8.name, var8);
            } else if (!"documentation".equals(var7.getLocalName())) {
               WsdlExtension var9 = this.parseChild((Element)var7, var2);
               this.putExtension(var9);
            }
         }
      }

      if (var5) {
         this.type = this.output == null ? 1 : 0;
      } else {
         this.type = this.input == null ? 3 : 2;
      }

   }

   protected WsdlExtension parseChild(Element var1, String var2) throws WsdlException {
      WsdlExtensionParser var3 = WsdlExtensionRegistry.getParser();
      WsdlExtension var4 = var3.parseOperation(this, var1);
      return var4;
   }

   protected void parseAttributes(Element var1, String var2) throws WsdlException {
      String var3 = WsdlReader.getMustAttribute(var1, (String)null, "name");
      this.name = new QName(var2, var3);
      this.parameterOrder = WsdlReader.getAttribute(var1, (String)null, "parameterOrder");
      PolicyURIs var4 = this.getPolicyUri(var1);
      if (null != var4) {
         this.policyUris = var4;
      }

   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "operation", WsdlConstants.wsdlNS);
      var2.setAttribute(var3, "name", WsdlConstants.wsdlNS, this.name.getLocalPart());
      if (this.parameterOrder != null) {
         var2.setAttribute(var3, "parameterOrder", WsdlConstants.wsdlNS, this.parameterOrder);
      }

      this.writeDocumentation(var3, var2);
      if (this.type != 0 && this.type != 1) {
         this.writeOutputElement(var2, var3);
         this.writeInputElement(var2, var3);
      } else {
         this.writeInputElement(var2, var3);
         this.writeOutputElement(var2, var3);
      }

      this.writeFault(var2, var3);
   }

   private void writeFault(WsdlWriter var1, Element var2) {
      Iterator var3 = this.faultList.values().iterator();

      while(var3.hasNext()) {
         MessageElement var4 = (MessageElement)var3.next();
         Element var5 = var1.addChild(var2, "fault", WsdlConstants.wsdlNS);
         var5.setAttribute("name", var4.name);
         var1.setAttribute(var5, "message", WsdlConstants.wsdlNS, var4.message);
         var4.writeDocumentation(var5, var1);
      }

   }

   private void writeInputElement(WsdlWriter var1, Element var2) {
      if (this.input != null) {
         Element var3 = var1.addChild(var2, "input", WsdlConstants.wsdlNS);
         if (this.input.name != null && this.input.name.length() > 0) {
            var1.setAttribute(var3, "name", WsdlConstants.wsdlNS, this.input.name);
         }

         var1.setAttribute(var3, "message", WsdlConstants.wsdlNS, this.input.message);
         if (this.input.action != null) {
            var1.setAttribute(var3, "Action", this.input.action.getActionNamespace(), this.input.action.getActionURI());
         }

         if (this.input.getPolicyUris() != null) {
            this.input.getPolicyUris().write(var3, var1);
         }

         this.input.writeDocumentation(var3, var1);
      }

   }

   private void writeOutputElement(WsdlWriter var1, Element var2) {
      if (this.output != null) {
         Element var3 = var1.addChild(var2, "output", WsdlConstants.wsdlNS);
         if (this.output.name != null && this.output.name.length() > 0) {
            var1.setAttribute(var3, "name", WsdlConstants.wsdlNS, this.output.name);
         }

         var1.setAttribute(var3, "message", WsdlConstants.wsdlNS, this.output.message);
         if (this.output.action != null) {
            var1.setAttribute(var3, "Action", this.output.action.getActionNamespace(), this.output.action.getActionURI());
         }

         if (this.output.getPolicyUris() != null) {
            this.output.getPolicyUris().write(var3, var1);
         }

         this.output.writeDocumentation(var3, var1);
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.name);
      var1.writeField("parameterOrder", this.parameterOrder);
      var1.writeField("type", this.type);
      var1.writeField("input", this.input);
      var1.writeField("output", this.output);
      var1.writeArray("faultList", this.faultList.values().iterator());
      var1.end();
   }

   public void setInput(WsdlMessageBuilder var1) {
      MessageElement var2 = new MessageElement(var1.getName());
      this.input = var2;
   }

   public void setOutput(WsdlMessageBuilder var1) {
      MessageElement var2 = new MessageElement(var1.getName());
      this.output = var2;
   }

   public void addFault(WsdlMessageBuilder var1, String var2) {
      MessageElement var3 = new MessageElement(var1.getName());
      var3.name = var2;
      this.faultList.put(var2, var3);
   }

   public boolean isWrapped() {
      return this.isWrappedNormal() || this.isWrappedWLW81Callback();
   }

   public boolean isWrappedNormal() {
      return this.isWrapped(this.getInput(), this.getOutput());
   }

   public boolean isWrappedWLW81Callback() {
      return this.type != 2 && this.type != 3 ? false : this.isWrapped(this.getOutput(), this.getInput());
   }

   protected boolean isWrapped(WsdlMessageBuilder var1, WsdlMessageBuilder var2) {
      if (var1 == null) {
         return false;
      } else {
         Iterator var3 = var1.getParts().values().iterator();
         if (!var3.hasNext()) {
            return false;
         } else {
            WsdlPart var4 = (WsdlPart)var3.next();
            if (var3.hasNext()) {
               return false;
            } else if (var4.getElement() == null) {
               return false;
            } else {
               String var5 = this.getName().getLocalPart();
               if (!var5.equals(var4.getElement().getLocalPart())) {
                  return false;
               } else {
                  if (var2 != null) {
                     Iterator var6 = var2.getParts().values().iterator();
                     if (!var6.hasNext()) {
                        return false;
                     }

                     WsdlPart var7 = (WsdlPart)var6.next();
                     if (var6.hasNext()) {
                        return false;
                     }

                     if (var7.getElement() == null) {
                        return false;
                     }

                     if (!(var5 + "Response").equals(var7.getElement().getLocalPart())) {
                        return false;
                     }

                     if (!var4.getElement().getNamespaceURI().equals(var7.getElement().getNamespaceURI())) {
                        return false;
                     }
                  }

                  return true;
               }
            }
         }
      }
   }

   public boolean isWLW81CallbackOperation() {
      if (this.type != 2 && this.type != 3) {
         return false;
      } else {
         WsdlMessageBuilder var1 = this.getInput();
         WsdlMessageBuilder var2 = this.getOutput();
         if (var2 != null) {
            if (this.isWrapped()) {
               return this.isWrappedWLW81Callback();
            } else {
               Iterator var3 = var2.getParts().values().iterator();
               if (!var3.hasNext()) {
                  return false;
               } else {
                  WsdlPart var4 = (WsdlPart)var3.next();
                  if (var4.getName().equals(this.getName().getLocalPart() + "Result")) {
                     return false;
                  } else if (var1 == null) {
                     return true;
                  } else {
                     Iterator var5 = var1.getParts().values().iterator();
                     if (!var5.hasNext()) {
                        return true;
                     } else {
                        WsdlPart var6 = (WsdlPart)var5.next();
                        return var5.hasNext() ? false : var6.getName().equals(this.getName().getLocalPart() + "Result");
                     }
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   private static class WSAWAction {
      private String actionValue;
      private String actionNamespace;

      public WSAWAction(String var1, String var2) {
         this.actionValue = var1;
         this.actionNamespace = var2;
      }

      public String getActionURI() {
         return this.actionValue;
      }

      public void setActionURI(String var1) {
         this.actionValue = var1;
      }

      public String getActionNamespace() {
         return this.actionNamespace;
      }

      public void setActionNamespace(String var1) {
         this.actionNamespace = var1;
      }
   }

   private static class MessageElement extends WsdlBase {
      String name;
      QName message;
      PolicyURIs policyUris;
      WSAWAction action;

      public MessageElement(QName var1) {
         this.message = var1;
      }

      public MessageElement(Element var1, boolean var2) throws WsdlException {
         this.addDocumentation(var1);
         String var3 = WsdlReader.getMustAttribute(var1, (String)null, "message");
         this.message = WsdlReader.createQName(var1, var3);
         this.name = var2 ? WsdlReader.getMustAttribute(var1, (String)null, "name") : var1.getAttribute("name");
         PolicyURIs var4 = this.getPolicyUri(var1);
         if (null != var4) {
            this.policyUris = var4;
         }

         String var5 = WsdlReader.getAttribute(var1, "http://schemas.xmlsoap.org/ws/2004/08/addressing", "Action");
         if (var5 != null) {
            this.action = new WSAWAction(var5, "http://schemas.xmlsoap.org/ws/2004/08/addressing");
         } else {
            if (var5 == null) {
               var5 = WsdlReader.getAttribute(var1, "http://www.w3.org/2006/05/addressing/wsdl", "Action");
               if (var5 != null) {
                  this.action = new WSAWAction(var5, "http://www.w3.org/2006/05/addressing/wsdl");
               }
            }

         }
      }

      public PolicyURIs getPolicyUris() {
         return this.policyUris;
      }

      void setPolicyUris(PolicyURIs var1) {
         this.policyUris = var1;
      }

      public String getName() {
         return this.name;
      }

      public String toString() {
         return "[name=" + this.name + " message=" + this.message + "]";
      }
   }
}
