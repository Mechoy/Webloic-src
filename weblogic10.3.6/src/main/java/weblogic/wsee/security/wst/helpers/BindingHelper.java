package weblogic.wsee.security.wst.helpers;

import java.security.Key;
import java.security.SecureRandom;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.security.wst.binding.BinarySecret;
import weblogic.wsee.security.wst.binding.Entropy;
import weblogic.wsee.security.wst.binding.RSTBase;
import weblogic.wsee.security.wst.binding.RequestSecurityToken;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponse;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponseCollection;
import weblogic.wsee.security.wst.binding.TokenType;
import weblogic.wsee.security.wst.faults.InvalidRequestException;
import weblogic.wsee.security.wst.faults.RequestFailedException;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.dom.marshal.MarshalException;

public class BindingHelper {
   private static final SecureRandom rand = new SecureRandom();
   private static final String DUMMY_NODE = "DummyROOT";

   public static RequestSecurityTokenResponse unmarshalRSTRNode(Node var0, SecurityTokenHandler var1) throws InvalidRequestException {
      if ("RequestSecurityTokenResponse".equals(var0.getLocalName())) {
         return unmarshalRSTRNode(var0, var1, (String)null);
      } else {
         if ("RequestSecurityTokenResponseCollection".equals(var0.getLocalName())) {
            RequestSecurityTokenResponseCollection var2 = unmarshalRSTRCNode(var0, var1, (String)null);
            if (var2 != null) {
               List var3 = var2.getRequestSecurityTokenResponseCollection();
               if (!var3.isEmpty()) {
                  return (RequestSecurityTokenResponse)var3.get(0);
               }
            }
         }

         return null;
      }
   }

   public static RequestSecurityTokenResponse unmarshalFromRSTRorRSTRCNode(Node var0, SecurityTokenHandler var1, String var2) throws InvalidRequestException {
      if ("RequestSecurityTokenResponse".equals(var0.getLocalName())) {
         return unmarshalRSTRNode(var0, var1, var2);
      } else {
         if ("RequestSecurityTokenResponseCollection".equals(var0.getLocalName())) {
            RequestSecurityTokenResponseCollection var3 = unmarshalRSTRCNode(var0, var1, var2);
            if (var3 != null) {
               List var4 = var3.getRequestSecurityTokenResponseCollection();
               if (!var4.isEmpty()) {
                  return (RequestSecurityTokenResponse)var4.get(0);
               }
            }
         }

         return null;
      }
   }

   public static RequestSecurityTokenResponse unmarshalRSTRNode(Node var0, SecurityTokenHandler var1, String var2) throws InvalidRequestException {
      RequestSecurityTokenResponse var3 = new RequestSecurityTokenResponse();
      var3.setTokenHandler(var1);
      if (var2 != null) {
         var3.setTokenType(new TokenType(var2));
      }

      try {
         var3.unmarshal(var0);
         return var3;
      } catch (MarshalException var5) {
         throw new InvalidRequestException(var5.getMessage());
      }
   }

   public static RequestSecurityTokenResponseCollection unmarshalRSTRCNode(Node var0, SecurityTokenHandler var1) throws InvalidRequestException {
      return unmarshalRSTRCNode(var0, var1, (String)null);
   }

   public static RequestSecurityTokenResponseCollection unmarshalRSTRCNode(Node var0, SecurityTokenHandler var1, String var2) throws InvalidRequestException {
      RequestSecurityTokenResponseCollection var3 = new RequestSecurityTokenResponseCollection();
      var3.setTokenHandler(var1);
      if (var2 != null) {
         var3.setTokenType(new TokenType(var2));
      }

      try {
         var3.unmarshal(var0);
         return var3;
      } catch (MarshalException var5) {
         throw new InvalidRequestException(var5.getMessage());
      }
   }

   public static RequestSecurityToken unmarshalRSTNode(Node var0, SecurityTokenHandler var1) throws InvalidRequestException {
      RequestSecurityToken var2 = new RequestSecurityToken();
      var2.setTokenHandler(var1);

      try {
         var2.unmarshal(var0);
         return var2;
      } catch (MarshalException var4) {
         throw new InvalidRequestException(var4.getMessage());
      }
   }

   public static RequestSecurityToken unmarshalRSTNode(Node var0) throws InvalidRequestException {
      return unmarshalRSTNode(var0, (SecurityTokenHandler)null);
   }

   public static Node marshalRST(RSTBase var0, WSTContext var1) throws RequestFailedException {
      Element var2 = createDummyRootNode();

      try {
         var0.marshal(var2, (Node)null, var1.getNamespaces());
      } catch (MarshalException var4) {
         throw new RequestFailedException("unable to marshal RSTR: " + var4.getMessage());
      }

      return DOMUtils.getFirstElement(var2);
   }

   public static RequestSecurityTokenResponseCollection createEmptyRSTRC(WSTContext var0) {
      String var1 = var0.getWstNamespaceURI();
      RequestSecurityTokenResponseCollection var2 = new RequestSecurityTokenResponseCollection(var1);
      return var2;
   }

   public static BinarySecret createBinarySecret(String var0, String var1, byte[] var2) {
      BinarySecret var3 = new BinarySecret(var0);
      var3.setType(var1);
      var3.setValue(KeyUtils.createNonce());
      return var3;
   }

   public static BinarySecret createBinarySecret(String var0, Key var1, String var2) {
      BinarySecret var3 = new BinarySecret(var0);
      var3.setType(var2);
      var3.setValue(var1.getEncoded());
      return var3;
   }

   public static final Entropy createNewEntropy(String var0, String var1) {
      Entropy var2 = new Entropy(var0);
      BinarySecret var3 = createBinarySecret(var0, var1, KeyUtils.createNonce());
      var2.setBinarySecret(var3);
      return var2;
   }

   public static final Entropy createNewEntropy(String var0, Key var1, String var2) {
      Entropy var3 = new Entropy(var0);
      BinarySecret var4 = createBinarySecret(var0, var1, var2);
      var3.setBinarySecret(var4);
      return var3;
   }

   private static Element createDummyRootNode() {
      return getParser().newDocument().createElement("DummyROOT");
   }

   private static DocumentBuilder getParser() {
      try {
         DocumentBuilderFactory var0 = DocumentBuilderFactory.newInstance();
         var0.setNamespaceAware(true);
         return var0.newDocumentBuilder();
      } catch (FactoryConfigurationError var1) {
         throw new AssertionError(var1);
      } catch (ParserConfigurationException var2) {
         throw new AssertionError(var2);
      }
   }
}
