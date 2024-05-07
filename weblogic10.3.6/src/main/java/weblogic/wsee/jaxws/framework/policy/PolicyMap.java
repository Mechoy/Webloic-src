package weblogic.wsee.jaxws.framework.policy;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.model.JavaMethod;
import com.sun.xml.ws.api.model.SEIModel;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundPortType;
import com.sun.xml.ws.api.model.wsdl.WSDLExtensible;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.server.Container;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.WebServiceProvider;
import org.w3c.dom.Element;
import weblogic.j2ee.descriptor.wl.OperationPolicyBean;
import weblogic.j2ee.descriptor.wl.PortPolicyBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.j2ee.descriptor.wl.WsPolicyBean;
import weblogic.jws.Policy;
import weblogic.jws.Policy.Direction;
import weblogic.jws.jaxws.AggregatePolicyFeature;
import weblogic.jws.jaxws.BasePolicyFeature;
import weblogic.jws.jaxws.PoliciesFeature;
import weblogic.jws.jaxws.PolicyFeature;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.jaxws.framework.jaxrpc.WsdlExtensibleHolder;
import weblogic.wsee.jaxws.util.WriterUtil;
import weblogic.wsee.policy.deployment.PolicyWsdlExtension;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlExtensible;
import weblogic.wsee.wstx.wsat.TransactionalFeature;
import weblogic.wsee.wstx.wsat.policy.ATAssertionBuilder;

public class PolicyMap {
   private static boolean verbose = Verbose.isVerbose(PolicyMap.class);
   private Map<String, PolicyStatement> policies = new HashMap();
   private Map<QName, List<PolicyFeature>> targets = new HashMap();
   private PolicyServer policyServer = null;

   public PolicyMap(Container var1, SEIModel var2, WSBinding var3, QName var4, Class var5) {
      this.policies.clear();
      this.targets.clear();
      this.policyServer = var1 != null ? (PolicyServer)var1.getSPI(PolicyServer.class) : null;
      if (this.policyServer == null) {
         this.policyServer = new PolicyServer();
      }

      AggregatePolicyFeature var6 = (AggregatePolicyFeature)var3.getFeature(AggregatePolicyFeature.class);
      DeployInfo var7 = var1 != null ? (DeployInfo)var1.getSPI(DeployInfo.class) : null;
      WebservicePolicyRefBean var8 = var7 != null ? var7.getPolicyRef() : null;
      TransactionalFeature var9 = (TransactionalFeature)var3.getFeature(TransactionalFeature.class);
      if (var6 != null || var8 != null || var9 != null) {
         HashMap var10 = new HashMap();
         boolean var11 = var5.getAnnotation(WebServiceProvider.class) != null;
         if (var6 != null && (var2 != null || var11)) {
            Map var12 = var6.getMap();
            Iterator var13 = var12.entrySet().iterator();

            label97:
            while(true) {
               while(true) {
                  if (!var13.hasNext()) {
                     break label97;
                  }

                  Map.Entry var14 = (Map.Entry)var13.next();
                  if (var14.getKey() instanceof Method && !var11) {
                     Method var15 = (Method)var14.getKey();
                     if (var15.getDeclaringClass() != var5 && var15.getDeclaringClass().isAssignableFrom(var5)) {
                        try {
                           var15 = var5.getMethod(var15.getName(), var15.getParameterTypes());
                        } catch (Exception var21) {
                        }
                     }

                     JavaMethod var16 = var2.getJavaMethod(var15);
                     if (var16 != null) {
                        var10.put(new QName(var4.getNamespaceURI(), var16.getOperationName()), var14.getValue());
                     }
                  } else {
                     var10.put(var4, var14.getValue());
                  }
               }
            }
         }

         if (var8 != null) {
            this.updatePolicyMap(var4, var8, var10, var7.getLinkName());
         }

         HashMap var22 = new HashMap();
         if (var9 != null) {
            ATAssertionBuilder.buildPolicyMap(var4.getNamespaceURI(), var22, var10, var9);
         }

         this.policies.putAll(var22);
         this.policyServer.addPolicies(var22);
         Set var23 = var10.entrySet();
         Iterator var24 = var23.iterator();

         while(true) {
            while(var24.hasNext()) {
               Map.Entry var25 = (Map.Entry)var24.next();
               QName var26 = (QName)var25.getKey();
               WebServiceFeature var17 = (WebServiceFeature)var25.getValue();
               if (var17 instanceof PolicyFeature) {
                  this.addPolicyToMap(this.policies, this.targets, var26, (PolicyFeature)var17);
               } else {
                  if (!(var17 instanceof PoliciesFeature)) {
                     throw new IllegalStateException("Unexpected feature type: " + var17.getClass().getName());
                  }

                  List var18 = ((PoliciesFeature)var17).getPolicies();
                  Iterator var19 = var18.iterator();

                  while(var19.hasNext()) {
                     PolicyFeature var20 = (PolicyFeature)var19.next();
                     this.addPolicyToMap(this.policies, this.targets, var26, var20);
                  }
               }
            }

            return;
         }
      }
   }

   private void updatePolicyMap(QName var1, WebservicePolicyRefBean var2, Map<QName, BasePolicyFeature> var3, String var4) {
      PortPolicyBean[] var5 = var2.getPortPolicy();
      int var8;
      if (var5 != null) {
         PortPolicyBean[] var6 = var5;
         int var7 = var5.length;

         for(var8 = 0; var8 < var7; ++var8) {
            PortPolicyBean var9 = var6[var8];
            if (var1.getLocalPart().equals(var9.getPortName())) {
               WsPolicyBean[] var10 = var9.getWsPolicy();
               if (var10 != null) {
                  WsPolicyBean[] var11 = var10;
                  int var12 = var10.length;

                  for(int var13 = 0; var13 < var12; ++var13) {
                     WsPolicyBean var14 = var11[var13];
                     if (var14.getStatus().equals("enabled")) {
                        Policy.Direction var15 = var14.getDirection() != null ? Direction.valueOf(var14.getDirection()) : Direction.both;
                        PolicyFeature var16 = new PolicyFeature(var14.getUri(), var15);
                        this.addPolicyFeature(var3, var16, var1);
                     }
                  }

                  this.removePolicyFeature(var3, var1, var10, Direction.both);
                  this.removePolicyFeature(var3, var1, var10, Direction.inbound);
                  this.removePolicyFeature(var3, var1, var10, Direction.outbound);
               }
            }
         }
      }

      OperationPolicyBean[] var19 = var2.getOperationPolicy();
      if (var19 != null) {
         OperationPolicyBean[] var20 = var19;
         var8 = var19.length;

         for(int var21 = 0; var21 < var8; ++var21) {
            OperationPolicyBean var22 = var20[var21];
            QName var23 = new QName(var1.getNamespaceURI(), var22.getOperationName());
            WsPolicyBean[] var24 = var22.getWsPolicy();
            if (var24 != null) {
               WsPolicyBean[] var25 = var24;
               int var26 = var24.length;

               for(int var27 = 0; var27 < var26; ++var27) {
                  WsPolicyBean var28 = var25[var27];
                  String var17 = var22.getServiceLink();
                  if (var28.getStatus().equals("enabled") && (StringUtil.isEmpty(var17) || !StringUtil.isEmpty(var17) && var17.equals(var4))) {
                     PolicyFeature var18 = new PolicyFeature(var28.getUri(), Direction.valueOf(var28.getDirection()));
                     this.addPolicyFeature(var3, var18, var23);
                  }
               }

               this.removePolicyFeature(var3, var23, var24, Direction.both);
               this.removePolicyFeature(var3, var23, var24, Direction.inbound);
               this.removePolicyFeature(var3, var23, var24, Direction.outbound);
            }
         }
      }

   }

   private void removePolicyFeature(Map<QName, BasePolicyFeature> var1, QName var2, WsPolicyBean[] var3, Policy.Direction var4) {
      List var5 = this.getPolicies(var1, var2, var4);
      if (var5 != null) {
         ArrayList var6 = new ArrayList(var5);
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();

            for(int var9 = 0; var9 < var3.length; ++var9) {
               WsPolicyBean var10 = var3[var9];
               if (urisEquivalent(var10.getUri(), var8.toString())) {
                  if (var4.equals(Direction.both)) {
                     if (!var10.getStatus().equals("enabled")) {
                        removePolicy(var1, var2, var8);
                     }
                  } else if (!var10.getStatus().equals("enabled")) {
                     if (var4.equals(Direction.valueOf(var10.getDirection()))) {
                        removePolicy(var1, var2, var8, var4);
                     }
                  } else if (!var4.equals(Direction.valueOf(var10.getDirection()))) {
                     removePolicy(var1, var2, var8, var4);
                  }
               }
            }
         }
      }

   }

   private static void removePolicy(Map<QName, BasePolicyFeature> var0, QName var1, String var2) {
      removePolicy(var0, var1, var2, (Policy.Direction)null);
   }

   private static void removePolicy(Map<QName, BasePolicyFeature> var0, QName var1, String var2, Policy.Direction var3) {
      BasePolicyFeature var4 = (BasePolicyFeature)var0.get(var1);
      if (var4 != null) {
         if (var4 instanceof PolicyFeature) {
            if (((PolicyFeature)var4).getUri().equals(var2) && (var3 == null || var3.equals(((PolicyFeature)var4).getDirection()))) {
               var0.remove(var1);
            }
         } else if (var4 instanceof PoliciesFeature) {
            List var5 = ((PoliciesFeature)var4).getPolicies();
            ArrayList var6 = new ArrayList(var5);
            Iterator var7 = var6.iterator();

            while(true) {
               PolicyFeature var8;
               do {
                  do {
                     if (!var7.hasNext()) {
                        return;
                     }

                     var8 = (PolicyFeature)var7.next();
                  } while(!var8.getUri().equals(var2));
               } while(var3 != null && !var3.equals(var8.getDirection()));

               var5.remove(var8);
            }
         }
      }

   }

   private List<String> getPolicies(Map<QName, BasePolicyFeature> var1, QName var2, Policy.Direction var3) {
      ArrayList var4 = new ArrayList();
      BasePolicyFeature var5 = (BasePolicyFeature)var1.get(var2);
      if (var5 != null) {
         if (var5 instanceof PolicyFeature) {
            if (((PolicyFeature)var5).getDirection().equals(var3)) {
               var4.add(((PolicyFeature)var5).getUri());
            }
         } else if (var5 instanceof PoliciesFeature) {
            List var6 = ((PoliciesFeature)var5).getPolicies();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               PolicyFeature var8 = (PolicyFeature)var7.next();
               if (var8.getDirection().equals(var3)) {
                  var4.add(var8.getUri());
               }
            }
         }
      }

      return var4;
   }

   private static boolean urisEquivalent(String var0, String var1) {
      if (var0.equals(var1)) {
         return true;
      } else {
         if (var0.endsWith(".xml")) {
            String var2 = var0.substring(0, var0.length() - 4);
            if (var2.equals(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   private void addPolicyFeature(Map<QName, BasePolicyFeature> var1, PolicyFeature var2, QName var3) {
      Object var4 = (BasePolicyFeature)var1.get(var3);
      if (var4 == null) {
         var4 = new PoliciesFeature(new PolicyFeature[0]);
      } else if (var4 instanceof PolicyFeature) {
         var4 = new PoliciesFeature(new PolicyFeature[]{(PolicyFeature)var4});
      }

      PoliciesFeature var5 = (PoliciesFeature)var4;
      if (var5.getPolicies() != null && var5.getPolicies().size() == 0) {
         var5.getPolicies().add(var2);
      } else {
         boolean var6 = false;
         Iterator var7 = var5.getPolicies().iterator();

         while(var7.hasNext()) {
            PolicyFeature var8 = (PolicyFeature)var7.next();
            if (var8.getUri().equals(var2.getUri())) {
               var6 = true;
               break;
            }
         }

         if (!var6) {
            var5.getPolicies().add(var2);
         }
      }

      var1.put(var3, var4);
   }

   private void addPolicyToMap(Map<String, PolicyStatement> var1, Map<QName, List<PolicyFeature>> var2, QName var3, PolicyFeature var4) {
      if (verbose) {
         Verbose.log((Object)"PROCESSING POLICY FEATURE");
         Verbose.log((Object)("AnnotatedElement: " + var3.getClass().getCanonicalName()));
         Verbose.log((Object)("Feature ID: " + var4.getID()));
         Verbose.log((Object)("Feature Enabled: " + var4.isEnabled()));
         Verbose.log((Object)("Policy URI: " + var4.getUri()));
         Verbose.log((Object)("Policy Direction: " + var4.getDirection()));
         Verbose.log((Object)("Policy Attach WSDL: " + var4.isAttachToWsdl()));
      }

      String var5 = var4.getUri();
      PolicyStatement var6 = this.policyServer.lookupPolicy(var5);
      if (var6 == null && verbose) {
         Verbose.log((Object)("Couldn't find policy for '" + var5 + "'"));
      }

      var1.put(var5, var6);
      Object var7 = (List)var2.get(var3);
      if (var7 == null) {
         var7 = new LinkedList();
         var2.put(var3, var7);
      }

      ((List)var7).add(var4);
   }

   public void addApplicablePolicyReferences(QName var1, Policy.Direction var2, TypedXmlWriter var3) {
      this.addApplicablePolicyReferences(var1, var2, (Object)var3);
   }

   public void addApplicablePolicyReferences(QName var1, Policy.Direction var2, XMLStreamWriter var3) {
      this.addApplicablePolicyReferences(var1, var2, (Object)var3);
   }

   private void addApplicablePolicyReferences(QName var1, Policy.Direction var2, Object var3) {
      if (var1 != null) {
         List var4 = (List)this.targets.get(var1);
         if (var4 != null) {
            Iterator var5 = var4.iterator();

            while(true) {
               PolicyFeature var6;
               String var7;
               do {
                  if (!var5.hasNext()) {
                     return;
                  }

                  var6 = (PolicyFeature)var5.next();
                  var7 = var6.getUri();
                  PolicyStatement var8 = (PolicyStatement)this.policies.get(var7);
                  if (var8 != null) {
                     var7 = "#" + var8.getId();
                  }
               } while(var2 != null && !var6.getDirection().equals(var2));

               this.writePolicyReference(var3, var7);
            }
         }
      }
   }

   public void updateWSDLModel(WSDLPort var1) {
      if (var1 != null) {
         WSDLBoundPortType var2 = var1.getBinding();
         List var3 = (List)this.targets.get(var1.getName());
         ArrayList var4 = new ArrayList();
         Iterator var5;
         if (var3 != null) {
            var5 = var3.iterator();

            label61:
            while(true) {
               while(true) {
                  PolicyFeature var6;
                  PolicyStatement var8;
                  do {
                     if (!var5.hasNext()) {
                        break label61;
                     }

                     var6 = (PolicyFeature)var5.next();
                     String var7 = var6.getUri();
                     var8 = (PolicyStatement)this.policies.get(var7);
                  } while(var8 == null);

                  if (var6.getDirection() != Direction.both && var6.getDirection() != null) {
                     var4.add(var6);
                  } else {
                     this.addPolicyReference(var2, var8);
                  }
               }
            }
         }

         var5 = var2.getBindingOperations().iterator();

         while(var5.hasNext()) {
            WSDLBoundOperation var15 = (WSDLBoundOperation)var5.next();
            ArrayList var16 = new ArrayList(var4);
            QName var17 = var15.getName();
            List var9 = (List)this.targets.get(var17);
            if (var9 != null) {
               var16.addAll(var9);
            }

            Iterator var10 = var16.iterator();

            while(var10.hasNext()) {
               PolicyFeature var11 = (PolicyFeature)var10.next();
               String var12 = var11.getUri();
               PolicyStatement var13 = (PolicyStatement)this.policies.get(var12);
               if (var13 != null) {
                  Object var14 = var15;
                  if (Direction.inbound.equals(var11.getDirection())) {
                     var14 = (WSDLExtensible)var15.getExtension(WSDLParserExtension.PseudoBoundInputExtensible.class);
                  } else if (Direction.outbound.equals(var11.getDirection())) {
                     var14 = (WSDLExtensible)var15.getExtension(WSDLParserExtension.PseudoBoundOutputExtensible.class);
                  }

                  if (var14 != null) {
                     this.addPolicyReference((WSDLExtensible)var14, var13);
                  }
               }
            }
         }

      }
   }

   private void addPolicyReference(WSDLExtensible var1, PolicyStatement var2) {
      WsdlExtensible var3 = WsdlExtensibleHolder.get(var1);
      PolicyWsdlExtension var4 = (PolicyWsdlExtension)var3.getExtension("Policy");
      if (var4 == null) {
         var4 = new PolicyWsdlExtension();
         var3.putExtension(var4);
      }

      var4.addPolicy(var2);
   }

   private void writePolicyReference(Object var1, String var2) {
      if (var1 instanceof TypedXmlWriter) {
         PolicyReferenceElement var3 = (PolicyReferenceElement)((TypedXmlWriter)var1)._element(PolicyReferenceElement.class);
         var3.URI(var2);
      } else {
         try {
            XMLStreamWriter var5 = (XMLStreamWriter)var1;
            var5.writeStartElement("http://schemas.xmlsoap.org/ws/2004/09/policy", "PolicyReference");
            var5.writeAttribute("URI", var2);
            var5.writeEndElement();
         } catch (XMLStreamException var4) {
            throw new WebServiceException(var4);
         }
      }

   }

   public void addDefinitionsExtension(TypedXmlWriter var1) {
      this.addDefinitionsExtension((Object)var1);
   }

   protected void addDefinitionsExtension(XMLStreamWriter var1, boolean var2, boolean var3) {
      this.addDefinitionsExtension((Object)var1, var2, var3);
   }

   private void addDefinitionsExtension(Object var1) {
      this.addDefinitionsExtension(var1, false, false);
   }

   private void addDefinitionsExtension(Object var1, boolean var2, boolean var3) {
      if (!this.policies.isEmpty()) {
         if (!var3) {
            WriterUtil.writeNamespace(var1, "http://schemas.xmlsoap.org/ws/2004/09/policy", "wsp");
         }

         if (!var2) {
            WriterUtil.writeNamespace(var1, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wssutil");
         }

         if (var1 instanceof TypedXmlWriter) {
            UsingPolicyElement var4 = (UsingPolicyElement)((TypedXmlWriter)var1)._element(UsingPolicyElement.class);
            var4.Required(true);
         } else {
            try {
               XMLStreamWriter var7 = (XMLStreamWriter)var1;
               var7.writeStartElement("http://schemas.xmlsoap.org/ws/2004/09/policy", "UsingPolicy");
               var7.writeAttribute("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Required", "true");
               var7.writeEndElement();
            } catch (XMLStreamException var6) {
               throw new WebServiceException(var6);
            }
         }
      }

      Iterator var8 = this.policies.values().iterator();

      while(var8.hasNext()) {
         PolicyStatement var5 = (PolicyStatement)var8.next();
         this.writePolicy(var5, var1);
      }

   }

   public boolean isEmpty() {
      return this.policies.isEmpty();
   }

   private void writePolicy(PolicyStatement var1, Object var2) {
      if (var1 != null) {
         try {
            Element var3 = var1.toXML();
            String var4 = var1.getId();
            WriterUtil.writeElement(var3, var4, var2);
         } catch (PolicyException var5) {
            if (verbose) {
               Verbose.log((Object)"Couldn't convert policy to XML!");
            }
         }
      }

   }

   @XmlElement(
      ns = "http://schemas.xmlsoap.org/ws/2004/09/policy",
      value = "PolicyReference"
   )
   private interface PolicyReferenceElement extends TypedXmlWriter {
      @XmlAttribute
      void URI(String var1);
   }

   @XmlElement(
      ns = "http://schemas.xmlsoap.org/ws/2004/09/policy",
      value = "UsingPolicy"
   )
   private interface UsingPolicyElement extends TypedXmlWriter {
      @XmlAttribute(
         ns = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
      )
      void Required(boolean var1);
   }
}
