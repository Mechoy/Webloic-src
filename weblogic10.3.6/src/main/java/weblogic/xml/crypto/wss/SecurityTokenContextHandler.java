package weblogic.xml.crypto.wss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Node;
import weblogic.security.service.ContextElement;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.wss.policy.Claims;

/** @deprecated */
public class SecurityTokenContextHandler implements ContextHandler {
   public static final String CLAIMS_MAP = "weblogic.xml.crypto.wss.policy.Claims";
   public static final String TOKEN = "com.bea.contextelement.xml.SecurityToken";
   public static final String SECURITY_INFO = "com.bea.contextelement.xml.SecurityInfo";
   public static final String ISSUER_SERIAL = "com.bea.contextelement.xml.IssuerSerial";
   public static final String KEYID = "com.bea.contextelement.xml.KeyIdentifier";
   public static final String THUMBPRINT = "weblogic.wsee.security.wss11.thumbprint";
   public static final String KEY_NAME = "weblogic.xml.crypto.keyinfo.keyname";
   public static final String ENDPOINT_URL = "com.bea.contextelement.xml.EndpointURL";
   public static final String PKI_INITIATOR = "weblogic.xml.crypto.wss.PKI_Initiator";
   public static final String WSS_SUBJECT_PROPERTY = "weblogic.wsee.wss.subject";
   public static final String DERIVED_FROM_TOKEN = "weblogic.wsee.wsc.derived_from_token";
   public static final String SET_TO_FIRST_TOKEN = "weblogic.wsee.security.move_node_to_top";
   public static final String FRIST_TOKEN_NODE = "weblogic.wsee.security.first_token_node";
   public static final String LAST_TOKEN_NODE = "weblogic.wsee.security.last_token_node";
   public static final String TIMESTAMP_FIRST = "weblogic.wsee.security.timestamp_first";
   public static final String ENCRYPT_THEN_SIGN = "weblogic.wsee.security.encrypt_sign";
   public static final String NEED_TO_MOVE_TIMESTAMP = "weblogic.wsee.security.need_to_move_timestamp";
   public static final String SIGNATURE_NODE = "weblogic.wsee.security.signature_node";
   public static final String ENCRYPTED_ELEMENT_MAP = "weblogic.wsee.security.encrypted_element.map";
   public static final String STRICT_LAYOUT = "weblogic.wsee.security.strict_layout";
   public static final String WST_BOOT_STRAP_POLICY = "weblogic.wsee.security.wst_bootstrap_policy";
   public static final String WST_OUTER_POLICY = "weblogic.wsee.security.wst_outer_policy";
   public static final String ISSUER_ENDPOINT_REF = "weblogic.wsee.security.issuer_endpoint_ref";
   public static final String TRUST_VERSION = "weblogic.wsee.security.trust_version";
   public static final String KEY_TYPE = "weblogic.wsee.security.key_type";
   public static final String SCT_TOKEN_LIFE_TIME = "weblogic.wsee.wssc.sct.lifetime";
   public static String DK_LABEL = "weblogic.wsee.wssc.dk.label";
   public static final String DK_LENGTH = "weblogic.wsee.wssc.dk.length";
   public static final String EK_ENCRYPT_METHOD = "weblogic.wsee.ek.encrypt_method";
   public static final String EK_KEYWRAP_METHOD = "weblogic.wsee.ek.keywrap_method";
   public static final String DK_STR_REFERENCE_TYPE = "weblogic.wsee.dk.referece_type";
   public static final String ENDORSE_SIGNATURE_ENCRYPT_SIGNATURE = "weblogic.wsee.security.endorse_signature_encrypt_signature";
   public static final String DK_BASE_TOKEN_REFERENCE_TYPE = "weblogic.wsee.dk.base_token_referece_type";
   private List names;
   private Map contextElements;

   public SecurityTokenContextHandler() {
      this.names = new ArrayList();
      this.contextElements = new HashMap();
   }

   public SecurityTokenContextHandler(WSSecurityInfo var1) {
      this.names = new ArrayList();
      this.contextElements = new HashMap();
      if (var1 instanceof WSSecurityContext) {
         WSSecurityContext var2 = (WSSecurityContext)var1;
         MessageContext var3 = var2.getMessageContext();
         if (var3 != null) {
            String var4 = (String)var3.getProperty("javax.xml.rpc.service.endpoint.address");
            if (var4 != null) {
               this.addContextElement("com.bea.contextelement.xml.EndpointURL", var4);
            }

            String var5 = (String)var3.getProperty("weblogic.xml.crypto.wss.PKI_Initiator");
            if (var5 != null) {
               this.addContextElement("weblogic.xml.crypto.wss.PKI_Initiator", var5);
            }

            Object var6 = var3.getProperty("com.bea.contextelement.saml.CachingRequested");
            if (var6 != null) {
               this.addContextElement("com.bea.contextelement.saml.CachingRequested", var6);
            }

            Object var7 = var3.getProperty("weblogic.wsee.wssc.sct.lifetime");
            if (var7 != null) {
               this.addContextElement("weblogic.wsee.wssc.sct.lifetime", var7);
            }

            Object var8 = var3.getProperty(DK_LABEL);
            if (var8 != null) {
               this.addContextElement(DK_LABEL, var8);
            }

            Object var9 = var3.getProperty("weblogic.wsee.wssc.dk.length");
            if (var9 != null) {
               this.addContextElement("weblogic.wsee.wssc.dk.length", var9);
            }
         }
      }

      this.addContextElement("com.bea.contextelement.xml.SecurityInfo", var1);
   }

   public SecurityTokenContextHandler(Claims var1) {
      this.names = new ArrayList();
      this.contextElements = new HashMap();
      this.addContextElement("weblogic.xml.crypto.wss.policy.Claims", var1);
   }

   public SecurityTokenContextHandler(Node var1, WSSecurityInfo var2) {
      this(var2);
      this.addContextElement("weblogic.xml.crypto.wss.policy.Claims", var1);
      this.addContextElement("com.bea.contextelement.xml.SecurityInfo", var2);
   }

   public int size() {
      return this.names.size();
   }

   public String[] getNames() {
      return (String[])((String[])this.names.toArray(new String[this.names.size()]));
   }

   public Object getValue(String var1) {
      Object var2 = null;
      ContextElement var3 = (ContextElement)this.contextElements.get(var1);
      if (var3 != null) {
         var2 = var3.getValue();
      }

      return var2;
   }

   public ContextElement[] getValues(String[] var1) {
      if (var1 != null && var1.length != 0) {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3];
            ContextElement var5 = (ContextElement)this.contextElements.get(var4);
            if (var5 != null) {
               var2.add(var5);
            }
         }

         return (ContextElement[])((ContextElement[])var2.toArray(new ContextElement[var2.size()]));
      } else {
         return null;
      }
   }

   public void addContextElement(String var1, Object var2) {
      this.contextElements.put(var1, new ContextElement(var1, var2));
      this.names.add(var1);
   }
}
