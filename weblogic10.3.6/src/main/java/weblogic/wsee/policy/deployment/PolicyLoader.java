package weblogic.wsee.policy.deployment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.OperatorType;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyAssertionFactory;
import weblogic.wsee.policy.framework.PolicyConstants;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyExpression;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.provider.PolicyProvider;
import weblogic.wsee.policy.provider.PolicyValidationHandler;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.policy.util.PolicyHelper;

public class PolicyLoader {
   private static final boolean debug = false;
   private ProviderRegistry providerRegistry;
   private Map embeddedStatements;
   private boolean haveForwardReferences;
   private PolicyServer ps;

   public PolicyLoader(ProviderRegistry var1) {
      this(var1, (PolicyServer)null);
   }

   public PolicyLoader(ProviderRegistry var1, PolicyServer var2) {
      this.embeddedStatements = new HashMap();
      this.haveForwardReferences = false;

      assert var1 != null;

      this.providerRegistry = var1;
      this.ps = var2;
   }

   public PolicyStatement load(Document var1) throws PolicyException {
      Element var2 = var1.getDocumentElement();
      return this.load((Node)var2);
   }

   public PolicyStatement load(Node var1) throws PolicyException {
      PolicyStatement var2 = this.readPolicyStatement(var1);
      if (this.haveForwardReferences) {
         this.resolveForwardRefs(var2);
      }

      return var2;
   }

   public boolean validate(String var1, PolicyStatement var2) throws PolicyException {
      Iterator var3 = this.providerRegistry.iterateProviders();

      while(var3.hasNext()) {
         PolicyProvider var4 = (PolicyProvider)var3.next();
         PolicyValidationHandler var5 = var4.getValidationHandler();
         if (var5 != null) {
            var5.validate(var1, var2);
         }
      }

      return true;
   }

   private PolicyStatement readPolicyStatement(Node var1) throws PolicyException {
      assert var1.getNodeType() == 1;

      Element var2 = (Element)var1;
      String var3 = var2.getNamespaceURI();
      boolean var4 = DOMUtils.equalsQName(var2, PolicyConstants.POLICY_INCLUDE) || DOMUtils.equalsQName(var2, PolicyConstants.POLICY_INCLUDE_15);
      if (var4) {
         PolicyStatement var11 = PolicyStatement.createPolicyStatement((String)null);
         var11.setPolicyNamespaceUri(var3);
         PolicyExpression var12 = this.readExpression(var2);
         var11.addExpression(var12);
         return var11;
      } else {
         String var5 = DOMUtils.getAttributeValueAsString(var2, PolicyConstants.POLICY_STATEMENT_ID_ATTRIBUTE);
         PolicyStatement var6 = PolicyStatement.createPolicyStatement(var5);
         var6.setPolicyNamespaceUri(var3);
         if (var5 != null && var5.length() > 0 && this.embeddedStatements.get(var5) == null) {
            this.embeddedStatements.put(var5, var6);
         }

         NodeList var7 = var2.getChildNodes();

         for(int var8 = 0; var8 < var7.getLength(); ++var8) {
            Node var9 = var7.item(var8);
            if (var9.getNodeType() == 1) {
               PolicyExpression var10 = this.readExpression((Element)var9);
               var6.addExpression(var10);
            }
         }

         return var6;
      }
   }

   private PolicyExpression readExpression(Element var1) throws PolicyException {
      if (var1 == null) {
         return null;
      } else {
         Object var2 = null;
         boolean var3 = PolicyHelper.hasWsp15NamespaceUri(var1);
         if (PolicyConstants.ALL.getLocalPart().equals(var1.getLocalName())) {
            var2 = this.readAllExpression(var1);
         } else if (PolicyConstants.ONE_OR_MORE.getLocalPart().equals(var1.getLocalName())) {
            var2 = this.readOneOrMoreExpression(var1);
         } else if (PolicyConstants.EXACTLY_ONE.getLocalPart().equals(var1.getLocalName())) {
            var2 = this.readExactlyOneExpression(var1);
         } else if (PolicyConstants.POLICY_INCLUDE.getLocalPart().equals(var1.getLocalName())) {
            var2 = this.readPolicyIncludeExpression(var1);
         } else if (PolicyConstants.POLICY_STATEMENT_ELEMENT.getLocalPart().equals(var1.getLocalName())) {
            var2 = this.readPolicyStatement(var1);
         } else {
            var2 = this.readAssertionExpression(var1);
         }

         if (var2 == null) {
            throw new PolicyException("Unrecognized element in policy expression: " + var1);
         } else {
            if (var3) {
               ((PolicyExpression)var2).setPolicyNamespaceUri("http://www.w3.org/ns/ws-policy");
            } else {
               ((PolicyExpression)var2).setPolicyNamespaceUri("http://schemas.xmlsoap.org/ws/2004/09/policy");
            }

            return (PolicyExpression)var2;
         }
      }
   }

   private PolicyExpression readAllExpression(Element var1) throws PolicyException {
      PolicyExpression var2 = PolicyExpression.createExpression(OperatorType.ALL);
      NodeList var3 = var1.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Node var5 = var3.item(var4);
         if (var5.getNodeType() == 1) {
            var2.addExpression(this.readExpression((Element)var5));
         }
      }

      return var2;
   }

   private PolicyExpression readOneOrMoreExpression(Element var1) throws PolicyException {
      PolicyExpression var2 = PolicyExpression.createExpression(OperatorType.ONE_OR_MORE);
      NodeList var3 = var1.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Node var5 = var3.item(var4);
         if (var5.getNodeType() == 1) {
            var2.addExpression(this.readExpression((Element)var5));
         }
      }

      return var2;
   }

   private PolicyExpression readExactlyOneExpression(Element var1) throws PolicyException {
      PolicyExpression var2 = PolicyExpression.createExpression(OperatorType.EXACTLY_ONE);
      NodeList var3 = var1.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Node var5 = var3.item(var4);
         if (var5.getNodeType() == 1) {
            var2.addExpression(this.readExpression((Element)var5));
         }
      }

      return var2;
   }

   private PolicyExpression readPolicyIncludeExpression(Element var1) throws PolicyException {
      QName var2 = DOMUtils.getAttributeValueAsQName(var1, PolicyConstants.POLICY_INCLUDE_QNAME_REF_ATTRIBUTE);
      if (var2 != null) {
      }

      URI var3 = null;

      try {
         var3 = DOMUtils.getAttributeValueAsURI(var1, PolicyConstants.POLICY_INCLUDE_URI_REF_ATTRIBUTE);
      } catch (URISyntaxException var8) {
         throw new PolicyException(var8);
      }

      if (var3 == null) {
         throw new PolicyException("PolicyReference element must contain a URI attribute");
      } else {
         byte[] var4 = DOMUtils.getAttributeValueAsByteArray(var1, PolicyConstants.POLICY_INCLUDE_DIGEST_ATTRIBUTE);
         QName var5 = DOMUtils.getAttributeValueAsQName(var1, PolicyConstants.POLICY_INCLUDE_DIGEST_ALGORITHM_ATTRIBUTE);
         if (isInternalRef(var3)) {
            String var9 = var3.getRawFragment();

            assert var9 != null;

            Object var7 = (PolicyStatement)this.embeddedStatements.get(var9);
            if (var7 == null) {
               var7 = new ForwardRef(var9);
               this.haveForwardReferences = true;
            }

            return (PolicyExpression)var7;
         } else {
            PolicyRef var6 = new PolicyRef((String)null, var3, var4, var5);
            return var6.getPolicy(this.ps, false);
         }
      }
   }

   private static boolean isInternalRef(URI var0) {
      return var0.toString().trim().startsWith("#");
   }

   private PolicyExpression readAssertionExpression(Node var1) throws PolicyException {
      PolicyExpression var2 = null;
      PolicyAssertion var3 = this.readAssertionElement(var1);
      var2 = PolicyExpression.createTerminal(var3);
      PolicyHelper.setPolicyExpressionNs(var2, var1);
      return var2;
   }

   private PolicyAssertion readAssertionElement(Node var1) throws PolicyException {
      Iterator var2 = this.providerRegistry.iterateProviders();
      PolicyAssertion var3 = null;

      while(var2.hasNext()) {
         PolicyProvider var4 = (PolicyProvider)var2.next();
         PolicyAssertionFactory var5 = var4.getAssertionFactory();
         var3 = var5.createAssertion(var1);
         if (var3 != null) {
            Boolean var6 = PolicyHelper.getOptionalBoolean((Element)var1);
            if (null != var6) {
               var3.setOptional(var6);
               String var7 = PolicyHelper.getOptionalPolicyNamespaceUri((Element)var1);
               if (null != var7) {
                  var3.setPolicyNamespaceUri(var7);
               }
            }
            break;
         }
      }

      return var3;
   }

   private void resolveForwardRefs(PolicyExpression var1) throws PolicyException {
      List var2 = var1.getExpressions();
      if (var2 != null) {
         ListIterator var3 = var2.listIterator();

         while(var3.hasNext()) {
            PolicyExpression var4 = (PolicyExpression)var3.next();
            if (var4 instanceof ForwardRef) {
               ForwardRef var5 = (ForwardRef)var4;
               PolicyStatement var6 = (PolicyStatement)this.embeddedStatements.get(var5.getId());
               if (var6 == null) {
                  throw new PolicyException("Could not resolve reference to policy statement '" + var5.getId() + "'");
               }

               var3.set(var6);
            } else {
               this.resolveForwardRefs(var4);
            }
         }

      }
   }

   public static DocumentBuilder getParser() {
      try {
         DocumentBuilderFactory var0 = DocumentBuilderFactory.newInstance();
         var0.setNamespaceAware(true);
         return var0.newDocumentBuilder();
      } catch (ParserConfigurationException var1) {
         throw new AssertionError(var1);
      } catch (FactoryConfigurationError var2) {
         throw new AssertionError(var2);
      }
   }

   private static class ForwardRef extends PolicyStatement {
      public ForwardRef(String var1) {
         super(var1);
      }
   }
}
