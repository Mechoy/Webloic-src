package weblogic.wsee.security.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.addressing.SetCookieHeader;
import weblogic.wsee.addressing.TimestampHeader;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.reliability.headers.AckRequestedHeader;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.security.policy.assertions.xbeans.MessagePartsType;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wss.plan.fact.MessagePartsTypeFactory;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.dom.Util;
import weblogic.xml.stax.util.NamespaceContextImpl;
import weblogic.xml.xpath.DOMXPath;
import weblogic.xml.xpath.XPathException;

public class MessagePartsEvaluator {
   public static final String XPATH_DIALECT = "http://www.w3.org/TR/1999/REC-xpath-19991116";
   public static final String WSEE_PART_DIALECT = "http://schemas.xmlsoap.org/2002/12/wsse#part";
   public static final String WLS_EXTENSION_DIALECT = "http://www.bea.com/wls90/security/policy/wsee#part";
   public static final String[] ALL_DIALECTS = new String[]{"http://www.w3.org/TR/1999/REC-xpath-19991116", "http://schemas.xmlsoap.org/2002/12/wsse#part", "http://www.bea.com/wls90/security/policy/wsee#part"};
   public static final QName WSEE_PART_BODY = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "Body");
   public static final QName WSEE_PART_HEADER = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "Header");
   public static final QName WLS_PART_SYSTEM_HEADERS = new QName("http://www.bea.com/wls90/security/policy/wsee#part", "SystemHeaders");
   public static final QName WLS_PART_SECURITY_HEADER = new QName("http://www.bea.com/wls90/security/policy/wsee#part", "SecurityHeader");
   public static final QName XPATH_GET_BODY = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "GetBody");
   public static final QName XPATH_GET_HEADER = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "GetHeader");
   public static final Set WLS_SYSTEM_SOAP_HEADERS;
   private static boolean verbose;
   private MessagePartsType mparts;
   private XPath xpath;
   private SOAPMessage msg;
   private List nodeList;
   private boolean haveEvaluatedExpr = false;
   private Map<String, String> namespaceMap;
   private SOAPMessageContext mctx;

   public MessagePartsEvaluator(MessagePartsType var1, SOAPMessageContext var2, Map var3) {
      this.mparts = var1;
      this.namespaceMap = new HashMap(var3);
      Map var4 = DOMUtils.getNamespaceMapping(var1.newDomNode().getFirstChild());
      this.namespaceMap.putAll(var4);
      this.mctx = var2;
      this.msg = var2.getMessage();
   }

   public MessagePartsEvaluator(XPath var1, SOAPMessageContext var2, Map var3) {
      this.xpath = var1;
      this.mparts = MessagePartsTypeFactory.newInstance(var1.getXPathVersion(), var1.getXPathExpr());
      Map var4 = var1.getXPathNamespaces();
      if (var4.size() > 0) {
         if (verbose) {
            Verbose.log((Object)("Using XPath namespaces (size=" + var4.size() + ") to resolve XPath expression: " + var1.getXPathExpr()));
         }

         this.namespaceMap = this.resolveNamespaceVersions(var4, var3, var1.getXPathExpr());
      } else {
         if (verbose) {
            Verbose.log((Object)("Using SOAP message namespaces (size=" + var3.size() + ") to resolve XPath expression: " + var1.getXPathExpr()));
         }

         this.namespaceMap = var3;
      }

      this.mctx = var2;
      this.msg = var2.getMessage();
   }

   private Map<String, String> resolveNamespaceVersions(Map<String, String> var1, Map<String, String> var2, String var3) {
      HashMap var4 = new HashMap();
      Iterator var5 = var1.entrySet().iterator();

      while(true) {
         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            if (((String)var6.getValue()).equals("http://schemas.xmlsoap.org/soap/envelope/") && !var2.containsValue("http://schemas.xmlsoap.org/soap/envelope/") && var2.containsValue("http://www.w3.org/2003/05/soap-envelope")) {
               if (verbose) {
                  Verbose.log((Object)("Adjusting XPath expression (" + var3 + ") to use SOAP 1.2 namespace (instead of SOAP 1.1), since the message is SOAP 1.2"));
               }

               var4.put(var6.getKey(), "http://www.w3.org/2003/05/soap-envelope");
            } else if (((String)var6.getValue()).equals("http://www.w3.org/2003/05/soap-envelope") && !var2.containsValue("http://www.w3.org/2003/05/soap-envelope") && var2.containsValue("http://schemas.xmlsoap.org/soap/envelope/")) {
               if (verbose) {
                  Verbose.log((Object)("Adjusting XPath expression (" + var3 + ") to use SOAP 1.1 namespace (instead of SOAP 1.2), since the message is SOAP 1.1"));
               }

               var4.put(var6.getKey(), "http://schemas.xmlsoap.org/soap/envelope/");
            } else if (((String)var6.getValue()).equals("http://schemas.xmlsoap.org/ws/2004/08/addressing") && !var2.containsValue("http://schemas.xmlsoap.org/ws/2004/08/addressing") && var2.containsValue("http://www.w3.org/2005/08/addressing")) {
               if (verbose) {
                  Verbose.log((Object)("Adjusting XPath expression (" + var3 + ") to use WS-Addressing 1.0 namespace (instead of WS-A 2004/08), since the SOAP message uses WS-A 1.0"));
               }

               var4.put(var6.getKey(), "http://www.w3.org/2005/08/addressing");
            } else if (((String)var6.getValue()).equals("http://www.w3.org/2005/08/addressing") && !var2.containsValue("http://www.w3.org/2005/08/addressing") && var2.containsValue("http://schemas.xmlsoap.org/ws/2004/08/addressing")) {
               if (verbose) {
                  Verbose.log((Object)("Adjusting XPath expression (" + var3 + ") to use WS-Addressing 2004/08 namespace (instead of WS-A 1.0), since the SOAP message uses WS-A 2004/08"));
               }

               var4.put(var6.getKey(), "http://schemas.xmlsoap.org/ws/2004/08/addressing");
            } else {
               var4.put(var6.getKey(), var6.getValue());
            }
         }

         return var4;
      }
   }

   public List getNodes() throws PolicyException {
      if (!this.haveEvaluatedExpr) {
         this.evalMessagePartsExpr();
      }

      return this.nodeList;
   }

   public List getNodesContent() throws PolicyException {
      if (!this.haveEvaluatedExpr) {
         this.evalMessagePartsExpr();
      }

      if (this.nodeList != null) {
         List var1 = DOMUtils.computeContent(this.nodeList);
         if (var1.size() == 0) {
            throw new PolicyException("No content nodes found, set encryptContentOnly='false' to encrypt the whole node in your policy file");
         } else {
            return var1;
         }
      } else {
         return null;
      }
   }

   private void evalMessagePartsExpr() throws PolicyException {
      String var1 = this.mparts.getDialect();
      if (var1 == null || var1.equals("")) {
         var1 = "http://www.w3.org/TR/1999/REC-xpath-19991116";
      }

      Element var2 = (Element)this.mparts.newDomNode().getFirstChild();
      String var3 = DOMUtils.getTextContent(var2, true);
      if ("http://www.w3.org/TR/1999/REC-xpath-19991116".equals(var1)) {
         if (var3 == null || var3.length() == 0) {
            throw new PolicyException("Missing XPath expression in MessageParts element");
         }

         this.nodeList = this.evalXPath(var3);
         if (this.nodeList != null) {
            Iterator var4 = this.nodeList.iterator();

            while(var4.hasNext()) {
               Node var5 = (Node)var4.next();
               if (var5.getNodeType() != 1) {
                  throw new PolicyException("MessagePart expression '" + var3 + " must evaluate only to Element nodes");
               }
            }
         }
      } else if ("http://schemas.xmlsoap.org/2002/12/wsse#part".equals(var1)) {
         if (var3 == null || var3.length() == 0) {
            throw new PolicyException("Missing WSEE Parts expression in MessageParts element");
         }

         this.nodeList = this.evalWssePartExpr(var3);
      } else {
         if (!"http://www.bea.com/wls90/security/policy/wsee#part".equals(var1)) {
            throw new PolicyException("Unknown dialect in MessageParts: " + var1);
         }

         if (var3 == null || var3.length() == 0) {
            throw new PolicyException("Missing WLS Parts expression in MessageParts element");
         }

         this.nodeList = this.evalWLSPartExpr(var3);
      }

      this.haveEvaluatedExpr = true;
   }

   private List evalXPath(String var1) throws PolicyException {
      if (!var1.startsWith("wsp:")) {
         try {
            return getNodeListFromXPath(this.msg.getSOAPPart().getEnvelope(), var1, this.namespaceMap);
         } catch (SOAPException var5) {
            throw new PolicyException("Could not access SOAP Envelope", var5);
         }
      } else {
         PartsFunction var2 = new PartsFunction(var1, this.mparts, this.namespaceMap);
         if (XPATH_GET_BODY.equals(var2.getFuncQName())) {
            try {
               return getNodeListFromXPath(this.msg.getSOAPPart().getEnvelope().getBody(), var2.getFuncArgs(), this.namespaceMap);
            } catch (SOAPException var6) {
               throw new PolicyException("Could not access SOAP Body", var6);
            }
         } else if (XPATH_GET_HEADER.equals(var2.getFuncQName())) {
            try {
               String var3 = var2.getFuncArgs();
               int var4 = var3.indexOf("wsse:Security");
               if (var4 > -1) {
                  var3 = "." + var3.substring(var4 + 13, var3.length());
                  return getNodeListFromXPath(this.getSecurityHeaderFromContext(), var3, this.namespaceMap);
               } else {
                  return getNodeListFromXPath(this.msg.getSOAPPart().getEnvelope().getHeader(), var3, this.namespaceMap);
               }
            } catch (SOAPException var7) {
               throw new PolicyException("Could not access SOAP Header", var7);
            }
         } else {
            throw new PolicyException("Could not handle xpath: " + var1);
         }
      }
   }

   private static List getNodeListFromXPath(Node var0, String var1, Map var2) throws PolicyException {
      ArrayList var3 = new ArrayList();
      javax.xml.xpath.XPath var4 = XPathFactory.newInstance().newXPath();
      NamespaceContextImpl var5 = new NamespaceContextImpl();
      Iterator var6 = var2.keySet().iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         var5.bindNamespace(var7, (String)var2.get(var7));
      }

      var4.setNamespaceContext(var5);

      try {
         NodeList var10 = (NodeList)var4.evaluate(var1, var0, XPathConstants.NODESET);
         if (var10 != null && var10.getLength() != 0) {
            for(int var8 = 0; var8 < var10.getLength(); ++var8) {
               var3.add(var10.item(var8));
            }

            return var3;
         } else {
            return getNodeList(var0, var1);
         }
      } catch (XPathExpressionException var9) {
         return getNodeList(var0, var1);
      }
   }

   /** @deprecated */
   private static List getNodeList(Node var0, String var1) throws PolicyException {
      ArrayList var2 = new ArrayList();

      try {
         DOMXPath var3 = new DOMXPath(var1);
         Set var4 = var3.evaluateAsNodeset(var0);
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            var2.add((Node)var5.next());
         }
      } catch (XPathException var6) {
         throw new PolicyException("Could not parse XPath expression: " + var1, var6);
      }

      if (var2.size() == 0) {
         throw new PolicyException("Can not resolve Target in MessageParts: " + var1);
      } else {
         return var2;
      }
   }

   private List evalWssePartExpr(String var1) throws PolicyException {
      ArrayList var2 = new ArrayList();
      PartsFunction var3 = new PartsFunction(var1, this.mparts, this.namespaceMap);
      if (WSEE_PART_BODY.equals(var3.getFuncQName())) {
         if (var3.getFuncArgs().length() > 0) {
            throw new PolicyException("Malformed WSSE Parts 'Body' expression: '" + var1 + "'");
         }

         var2.add(this.getSOAPBody());
      } else {
         if (!WSEE_PART_HEADER.equals(var3.getFuncQName())) {
            throw new PolicyException("Unrecognized function name in WSSE Message Parts expression: '" + var3 + "'");
         }

         if (var3.getFuncArgs().length() == 0) {
            throw new PolicyException("Malformed WSSE Parts 'Header' expression: '" + var1 + "'");
         }

         QName var4 = var3.getFuncArgsAsQName();
         var2.addAll(this.getSOAPHeaders(var4));
      }

      if (var2.size() == 0) {
         throw new PolicyException("Can not resolve Target in MessageParts: " + var1);
      } else {
         return var2;
      }
   }

   private List evalWLSPartExpr(String var1) throws PolicyException {
      ArrayList var2 = new ArrayList();
      PartsFunction var3 = new PartsFunction(var1, this.mparts, this.namespaceMap);
      if (WLS_PART_SYSTEM_HEADERS.equals(var3.getFuncQName())) {
         if (var3.getFuncArgs().length() > 0) {
            throw new PolicyException("Malformed WLS Parts '" + WLS_PART_SYSTEM_HEADERS.getLocalPart() + "' expression: '" + var1 + "'");
         }

         var2.addAll(this.getSOAPSystemHeaders());
      } else {
         if (!WLS_PART_SECURITY_HEADER.equals(var3.getFuncQName())) {
            throw new PolicyException("Unrecognized function name in WLS Message Parts expression: '" + var3 + "'");
         }

         var2.addAll(this.getSecurityHeader(var3.getFuncArgsAsQName()));
      }

      return var2;
   }

   private SOAPBody getSOAPBody() throws PolicyException {
      try {
         return this.msg.getSOAPPart().getEnvelope().getBody();
      } catch (SOAPException var2) {
         throw new PolicyException("Could not access SOAP Envelope", var2);
      }
   }

   private List getSOAPHeaders(QName var1) throws PolicyException {
      ArrayList var2 = new ArrayList();
      SOAPHeader var3 = null;

      try {
         var3 = this.msg.getSOAPPart().getEnvelope().getHeader();
      } catch (SOAPException var6) {
         throw new PolicyException("Could not access SOAP Headers");
      }

      Iterator var4 = var3.examineAllHeaderElements();

      while(var4.hasNext()) {
         SOAPHeaderElement var5 = (SOAPHeaderElement)var4.next();
         if (DOMUtils.equalsQName(var5, var1)) {
            var2.add(var5);
         }
      }

      return var2;
   }

   private Element getSecurityHeaderFromContext() throws PolicyException {
      Element var1 = null;
      WSSecurityContext var2 = WSSecurityContext.getSecurityContext(this.mctx);
      if (var2 != null) {
         var1 = var2.getSecurityElement();
      }

      if (var1 == null) {
         SOAPMessage var3 = this.mctx.getMessage();

         try {
            NodeList var4 = var3.getSOAPHeader().getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
            if (var4.getLength() > 0) {
               var1 = (Element)var4.item(0);
            }
         } catch (SOAPException var5) {
         }
      }

      if (var1 == null) {
         if (verbose) {
            String var6 = Util.printNode(this.mctx.getMessage().getSOAPPart());
            throw new PolicyException("Can not retrieve wsee:Security header from the message: " + var6);
         } else {
            throw new PolicyException("Can not retrieve wsee:Security header from the message.");
         }
      } else {
         return var1;
      }
   }

   private Collection getSecurityHeader(QName var1) throws PolicyException {
      ArrayList var2 = new ArrayList();
      Element var3 = this.getSecurityHeaderFromContext();
      boolean var4 = var1.getNamespaceURI() == null || var1.getNamespaceURI().length() == 0;
      NodeList var5 = var3.getChildNodes();

      for(int var6 = 0; var6 < var5.getLength(); ++var6) {
         Node var7 = var5.item(var6);
         if (var7.getNodeType() == 1) {
            if (var4) {
               if (var7.getLocalName().equals(var1.getLocalPart())) {
                  var2.add(var7);
               }
            } else if (DOMUtils.equalsQName(var7, var1)) {
               var2.add(var7);
            }
         }
      }

      if (var2.size() == 0) {
         throw new PolicyException("Can not retrieve header: " + var1);
      } else {
         return var2;
      }
   }

   private Collection getSOAPSystemHeaders() throws PolicyException {
      ArrayList var1 = new ArrayList();
      SOAPHeader var2 = null;

      try {
         var2 = this.msg.getSOAPPart().getEnvelope().getHeader();
      } catch (SOAPException var7) {
         throw new PolicyException("Could not access SOAP Headers");
      }

      for(Node var3 = var2.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (1 == var3.getNodeType()) {
            Element var4 = (Element)var3;
            Iterator var5 = WLS_SYSTEM_SOAP_HEADERS.iterator();

            while(var5.hasNext()) {
               QName var6 = (QName)var5.next();
               if (DOMUtils.equalsQName(var4, var6)) {
                  var1.add(var4);
               }
            }
         }
      }

      return var1;
   }

   static {
      HashSet var0 = new HashSet();
      var0.addAll(WsrmHeader.getQNames(AcknowledgementHeader.class));
      var0.addAll(WsrmHeader.getQNames(AckRequestedHeader.class));
      var0.addAll(WsrmHeader.getQNames(SequenceHeader.class));
      var0.add(WSAddressingConstants.WSA_HEADER_ACTION_10);
      var0.add(WSAddressingConstants.WSA_HEADER_ACTION);
      var0.add(WSAddressingConstants.WSA_HEADER_FAULT_TO_10);
      var0.add(WSAddressingConstants.WSA_HEADER_FAULT_TO);
      var0.add(WSAddressingConstants.WSA_HEADER_SOURCE_10);
      var0.add(WSAddressingConstants.WSA_HEADER_SOURCE);
      var0.add(WSAddressingConstants.WSA_HEADER_MESSAGE_ID_10);
      var0.add(WSAddressingConstants.WSA_HEADER_MESSAGE_ID);
      var0.add(WSAddressingConstants.WSA_HEADER_RELATES_TO_10);
      var0.add(WSAddressingConstants.WSA_HEADER_RELATES_TO);
      var0.add(WSAddressingConstants.WSA_HEADER_REPLY_TO_10);
      var0.add(WSAddressingConstants.WSA_HEADER_REPLY_TO);
      var0.add(SetCookieHeader.NAME);
      var0.add(TimestampHeader.NAME);
      var0.add(WSAddressingConstants.WSA_HEADER_TO_10);
      var0.add(WSAddressingConstants.WSA_HEADER_TO);
      WLS_SYSTEM_SOAP_HEADERS = var0;
      verbose = Verbose.isVerbose(MessagePartsEvaluator.class);
   }

   private static class PartsFunction {
      private QName funcQName;
      private String funcArgs;
      private Node messageParts;
      private String funcExpr;
      private Map namespaceMap = new HashMap();
      private QName funcArgsAsQName = null;

      public PartsFunction(String var1, MessagePartsType var2, Map var3) throws PolicyException {
         this.namespaceMap = var3;
         Element var4 = (Element)var2.newDomNode().getFirstChild();
         String var5 = var1.trim();
         int var6 = var5.indexOf(40);
         if (var6 <= 0) {
            throw new PolicyException("Malformed WSEE Parts expression: '" + var1 + "'");
         } else {
            String var7 = var5.substring(0, var6);
            this.funcQName = DOMUtils.getQNameOf(var7, var4, var3);
            int var8 = var5.indexOf(41, var6);
            if (var8 < 0) {
               throw new PolicyException("Malformed WSEE Parts expression: '" + var1 + "'");
            } else {
               this.funcArgs = var5.substring(var6 + 1, var8);
               if (var8 != var5.length() - 1) {
                  String var9 = var5.substring(var8 + 1);
                  this.funcArgs = this.funcArgs + var9;
               }

               this.messageParts = var4;
               this.funcExpr = var1;
               this.funcArgsAsQName = DOMUtils.getQNameOf(this.funcArgs, var4, var3);
            }
         }
      }

      public QName getFuncQName() {
         return this.funcQName;
      }

      public String getFuncArgs() {
         return this.funcArgs;
      }

      public QName getFuncArgsAsQName() {
         return this.funcArgsAsQName;
      }

      public String toString() {
         return this.funcExpr;
      }
   }
}
