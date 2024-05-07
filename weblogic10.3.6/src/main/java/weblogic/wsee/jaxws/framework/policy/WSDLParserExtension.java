package weblogic.wsee.jaxws.framework.policy;

import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundPortType;
import com.sun.xml.ws.api.model.wsdl.WSDLExtensible;
import com.sun.xml.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.ws.api.model.wsdl.WSDLMessage;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.model.wsdl.WSDLPortType;
import com.sun.xml.ws.api.model.wsdl.WSDLService;
import com.sun.xml.ws.api.wsdl.parser.WSDLParserExtensionContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Locator;
import weblogic.wsee.jaxws.framework.jaxrpc.WsdlExtensibleHolder;
import weblogic.wsee.policy.deployment.PolicyLoader;
import weblogic.wsee.policy.deployment.PolicyReferenceWsdlExtension;
import weblogic.wsee.policy.deployment.PolicyWsdlExtension;
import weblogic.wsee.policy.deployment.ProviderRegistry;
import weblogic.wsee.policy.deployment.UsingPolicy;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyConstants;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.WsdlExtensible;
import weblogic.wsee.wsdl.WsdlExtension;

public class WSDLParserExtension extends com.sun.xml.ws.api.wsdl.parser.WSDLParserExtension {
   private PolicyLoader loader;
   private WSDLParserExtensionContext context;
   private Map<WSDLBoundOperation, WSDLExtensible> opInputs = new HashMap();
   private Map<WSDLBoundOperation, WSDLExtensible> opOutputs = new HashMap();

   public boolean bindingElements(WSDLBoundPortType var1, XMLStreamReader var2) {
      return this.checkPolicyReferenceAndPolicy(var2, var1);
   }

   public void portTypeAttributes(WSDLPortType var1, XMLStreamReader var2) {
      this.checkPolicyReferencedByAttributes(var2, var1);
   }

   public boolean bindingOperationElements(WSDLBoundOperation var1, XMLStreamReader var2) {
      return this.checkPolicyReferenceAndPolicy(var2, var1);
   }

   public void bindingOperationAttributes(WSDLBoundOperation var1, XMLStreamReader var2) {
      WSDLExtension var3 = var1.getExtension(PseudoBoundInputExtensible.class);
      if (var3 == null) {
         PseudoBoundInputExtensible var4 = new PseudoBoundInputExtensible();
         var1.addExtension(var4);
      }

      var3 = var1.getExtension(PseudoBoundOutputExtensible.class);
      if (var3 == null) {
         PseudoBoundOutputExtensible var5 = new PseudoBoundOutputExtensible();
         var1.addExtension(var5);
      }

   }

   public boolean bindingOperationInputElements(WSDLBoundOperation var1, XMLStreamReader var2) {
      WSDLExtension var3 = var1.getExtension(PseudoBoundInputExtensible.class);
      return this.checkPolicyReferenceAndPolicy(var2, (WSDLExtensible)var3);
   }

   public boolean bindingOperationOutputElements(WSDLBoundOperation var1, XMLStreamReader var2) {
      WSDLExtension var3 = var1.getExtension(PseudoBoundOutputExtensible.class);
      return this.checkPolicyReferenceAndPolicy(var2, (WSDLExtensible)var3);
   }

   public void finished(WSDLParserExtensionContext var1) {
      Iterator var2 = this.opInputs.entrySet().iterator();

      Map.Entry var3;
      WsdlExtensible var4;
      WsdlExtensible var5;
      Iterator var6;
      List var7;
      Iterator var8;
      WsdlExtension var9;
      while(var2.hasNext()) {
         var3 = (Map.Entry)var2.next();
         var4 = WsdlExtensibleHolder.get((WSDLExtensible)var3.getValue());
         var5 = WsdlExtensibleHolder.get(((WSDLBoundOperation)var3.getKey()).getOperation().getInput());
         var6 = var4.getExtensions().values().iterator();

         while(var6.hasNext()) {
            var7 = (List)var6.next();
            var8 = var7.iterator();

            while(var8.hasNext()) {
               var9 = (WsdlExtension)var8.next();
               var5.putExtension(var9);
            }
         }
      }

      var2 = this.opOutputs.entrySet().iterator();

      while(var2.hasNext()) {
         var3 = (Map.Entry)var2.next();
         var4 = WsdlExtensibleHolder.get((WSDLExtensible)var3.getValue());
         var5 = WsdlExtensibleHolder.get(((WSDLBoundOperation)var3.getKey()).getOperation().getOutput());
         var6 = var4.getExtensions().values().iterator();

         while(var6.hasNext()) {
            var7 = (List)var6.next();
            var8 = var7.iterator();

            while(var8.hasNext()) {
               var9 = (WsdlExtension)var8.next();
               var5.putExtension(var9);
            }
         }
      }

   }

   private boolean checkUsingPolicy(XMLStreamReader var1, WSDLExtensible var2) {
      if ("UsingPolicy".equals(var1.getLocalName()) && ("http://schemas.xmlsoap.org/ws/2004/09/policy".equals(var1.getNamespaceURI()) || "http://www.w3.org/ns/ws-policy".equals(var1.getNamespaceURI()))) {
         String var3 = var1.getAttributeValue(PolicyConstants.WSDL_NAMESPACE_URI, "Required");
         boolean var4 = "true".equalsIgnoreCase(var3);
         WsdlExtensibleHolder.get(var2).putExtension(new UsingPolicy(var4));
      }

      return false;
   }

   private boolean isPolicy(XMLStreamReader var1) {
      return "Policy".equals(var1.getLocalName()) && ("http://schemas.xmlsoap.org/ws/2004/09/policy".equals(var1.getNamespaceURI()) || "http://www.w3.org/ns/ws-policy".equals(var1.getNamespaceURI()));
   }

   private boolean isPolicyReference(XMLStreamReader var1) {
      return "PolicyReference".equals(var1.getLocalName()) && ("http://schemas.xmlsoap.org/ws/2004/09/policy".equals(var1.getNamespaceURI()) || "http://www.w3.org/ns/ws-policy".equals(var1.getNamespaceURI()));
   }

   private String getPolicyReferenceUri(XMLStreamReader var1) {
      String var2 = var1.getAttributeValue("http://schemas.xmlsoap.org/ws/2004/09/policy", "PolicyURIs");
      if (var2 == null) {
         var2 = var1.getAttributeValue("http://www.w3.org/ns/ws-policy", "PolicyURIs");
      }

      return var2;
   }

   private void checkPolicyReferencedByAttributes(XMLStreamReader var1, WSDLExtensible var2) {
      String var3 = this.getPolicyReferenceUri(var1);
      if (var3 != null && var3.trim().length() > 0) {
         WsdlExtensible var4 = WsdlExtensibleHolder.get(var2);
         PolicyReferencedByAttributeWsdlExtension var5 = null;

         try {
            var5 = new PolicyReferencedByAttributeWsdlExtension(var3);
            var4.putExtension(var5);
         } catch (URISyntaxException var7) {
            throw new WebServiceException(var7);
         }
      }

   }

   private boolean checkPolicy(XMLStreamReader var1, WSDLExtensible var2) {
      if (this.isPolicy(var1)) {
         WsdlExtensible var3 = WsdlExtensibleHolder.get(var2);
         PolicyWsdlExtension var4 = (PolicyWsdlExtension)var3.getExtension("Policy");
         if (var4 == null) {
            var4 = new PolicyWsdlExtension();
            var3.putExtension(var4);
         }

         var4.addPolicy(this.parsePolicy(var1));
         return true;
      } else {
         return false;
      }
   }

   private boolean checkPolicyReferenceAndPolicy(XMLStreamReader var1, WSDLExtensible var2) {
      WsdlExtensible var3;
      if (this.isPolicyReference(var1)) {
         var3 = WsdlExtensibleHolder.get(var2);
         PolicyReferenceWsdlExtension var13 = (PolicyReferenceWsdlExtension)var3.getExtension("PolicyReference");
         if (var13 == null) {
            var13 = new PolicyReferenceWsdlExtension();
            var3.putExtension(var13);
         }

         try {
            var13.addURI(new URI(var1.getAttributeValue((String)null, "URI")));
            return false;
         } catch (URISyntaxException var11) {
            throw new WebServiceException(var11);
         }
      } else if (!this.isPolicy(var1)) {
         return false;
      } else {
         var3 = WsdlExtensibleHolder.get(var2);
         PolicyWsdlExtension var4 = (PolicyWsdlExtension)var3.getExtension("Policy");
         if (var4 == null) {
            var4 = new PolicyWsdlExtension();
            var3.putExtension(var4);
         }

         Node var5 = this.parseNode(var1);
         NodeList var6 = var5.getChildNodes();

         for(int var7 = 0; var7 < var6.getLength(); ++var7) {
            Node var8 = var6.item(var7);
            if (var8.getNodeType() == 1 && (DOMUtils.equalsQName(var8, PolicyConstants.POLICY_INCLUDE) || DOMUtils.equalsQName(var8, PolicyConstants.POLICY_INCLUDE_15))) {
               PolicyReferenceWsdlExtension var9 = (PolicyReferenceWsdlExtension)var3.getExtension("PolicyReference");
               if (var9 == null) {
                  var9 = new PolicyReferenceWsdlExtension();
                  var3.putExtension(var9);
               }

               try {
                  var9.addURI(DOMUtils.getAttributeValueAsURI((Element)var8, PolicyConstants.POLICY_INCLUDE_URI_REF_ATTRIBUTE));
               } catch (URISyntaxException var12) {
                  throw new WebServiceException(var12);
               }
            }
         }

         var4.addPolicy(this.parsePolicy(var5));
         return true;
      }
   }

   public boolean definitionsElements(XMLStreamReader var1) {
      if (this.checkUsingPolicy(var1, this.context.getWSDLModel())) {
         return true;
      } else {
         return this.checkPolicy(var1, this.context.getWSDLModel());
      }
   }

   public boolean messageElements(WSDLMessage var1, XMLStreamReader var2) {
      return this.checkPolicyReferenceAndPolicy(var2, var1);
   }

   public boolean portElements(WSDLPort var1, XMLStreamReader var2) {
      return this.checkPolicyReferenceAndPolicy(var2, var1);
   }

   public boolean serviceElements(WSDLService var1, XMLStreamReader var2) {
      return this.checkPolicyReferenceAndPolicy(var2, var1);
   }

   public void start(WSDLParserExtensionContext var1) {
      this.context = var1;

      try {
         this.loader = new PolicyLoader(ProviderRegistry.getTheRegistry(), (PolicyServer)var1.getContainer().getSPI(PolicyServer.class));
      } catch (PolicyException var3) {
         throw new WebServiceException(var3);
      }
   }

   private PolicyStatement parsePolicy(XMLStreamReader var1) {
      return this.parsePolicy(this.parseNode(var1));
   }

   private PolicyStatement parsePolicy(Node var1) {
      try {
         return this.loader.load(var1);
      } catch (PolicyException var3) {
         throw new WebServiceException(var3);
      }
   }

   private Node parseNode(XMLStreamReader var1) {
      try {
         DocumentBuilderFactory var2 = DocumentBuilderFactory.newInstance();
         var2.setNamespaceAware(true);
         var2.setValidating(false);
         DocumentBuilder var3 = var2.newDocumentBuilder();
         Document var4 = var3.newDocument();
         Element var5 = var4.createElementNS(var1.getNamespaceURI(), var1.getLocalName());
         this.parseNamespaces(var1, var5);
         this.parseAttributes(var1, var5);
         ArrayList var6 = new ArrayList();
         var6.add(var5);
         int var7 = 1;
         StringBuffer var8 = null;

         do {
            var1.next();
            Element var9 = (Element)var6.get(var6.size() - 1);
            switch (var1.getEventType()) {
               case 1:
                  ++var7;
                  Element var10 = var4.createElementNS(var1.getNamespaceURI(), var1.getLocalName());
                  var9.appendChild(var10);
                  var6.add(var10);
                  this.parseNamespaces(var1, var10);
                  this.parseAttributes(var1, var10);
                  break;
               case 2:
                  if (var8 != null) {
                     var9.appendChild(var4.createTextNode(var8.toString()));
                     var8 = null;
                  }

                  --var7;
                  if (var7 == 0) {
                     return var5;
                  }

                  var6.remove(var6.size() - 1);
               case 3:
               case 5:
               case 6:
               case 7:
               case 8:
               case 9:
               case 11:
               case 12:
               default:
                  break;
               case 4:
                  if (var8 == null) {
                     var8 = new StringBuffer();
                  }

                  var8.append(var1.getText());
                  break;
               case 10:
                  this.parseAttributes(var1, var9);
                  break;
               case 13:
                  this.parseNamespaces(var1, var9);
            }
         } while(var1.hasNext());

         return var5;
      } catch (ParserConfigurationException var11) {
         throw new WebServiceException(var11);
      } catch (XMLStreamException var12) {
         throw new WebServiceException(var12);
      }
   }

   private void parseAttributes(XMLStreamReader var1, Element var2) {
      for(int var3 = 0; var3 < var1.getAttributeCount(); ++var3) {
         Attr var4 = var2.getOwnerDocument().createAttributeNS(var1.getAttributeNamespace(var3), var1.getAttributeLocalName(var3));
         var4.setValue(var1.getAttributeValue(var3));
         var2.setAttributeNodeNS(var4);
      }

   }

   private void parseNamespaces(XMLStreamReader var1, Element var2) {
      for(int var3 = 0; var3 < var1.getNamespaceCount(); ++var3) {
         String var4 = var1.getNamespacePrefix(var3);
         String var5 = !StringUtil.isEmpty(var4) ? "xmlns:" + var4 : "xmlns";
         Attr var6 = var2.getOwnerDocument().createAttributeNS("http://www.w3.org/2000/xmlns/", var5);
         var6.setValue(var1.getNamespaceURI(var3));
         var2.setAttributeNodeNS(var6);
      }

   }

   public static class PseudoBoundOutputExtensible extends PseudoExtensible implements WSDLExtension {
      public PseudoBoundOutputExtensible() {
         super(null);
      }

      public QName getName() {
         return null;
      }
   }

   public static class PseudoBoundInputExtensible extends PseudoExtensible implements WSDLExtension {
      public PseudoBoundInputExtensible() {
         super(null);
      }

      public QName getName() {
         return null;
      }
   }

   private static class PseudoExtensible implements WSDLExtensible {
      private final Set<WSDLExtension> extensions;

      private PseudoExtensible() {
         this.extensions = new HashSet();
      }

      public void addExtension(WSDLExtension var1) {
         this.extensions.add(var1);
      }

      public <T extends WSDLExtension> T getExtension(Class<T> var1) {
         Iterator var2 = this.extensions.iterator();

         WSDLExtension var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (WSDLExtension)var2.next();
         } while(!var1.isInstance(var3));

         return (WSDLExtension)var1.cast(var3);
      }

      public Iterable<WSDLExtension> getExtensions() {
         return this.extensions;
      }

      public <T extends WSDLExtension> Iterable<T> getExtensions(Class<T> var1) {
         ArrayList var2 = new ArrayList(this.extensions.size());
         Iterator var3 = this.extensions.iterator();

         while(var3.hasNext()) {
            WSDLExtension var4 = (WSDLExtension)var3.next();
            if (var1.isInstance(var4)) {
               var2.add(var1.cast(var4));
            }
         }

         return var2;
      }

      public Locator getLocation() {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      PseudoExtensible(Object var1) {
         this();
      }
   }
}
