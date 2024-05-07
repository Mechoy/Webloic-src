package weblogic.wsee.security.wssc.sct;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.security.Key;
import java.util.Calendar;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.security.configuration.TimestampConfiguration;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.TrustCredential;
import weblogic.wsee.server.StateExpiration;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.KeyIdentifierImpl;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class SCCredential implements TrustCredential, Externalizable, StateExpiration {
   private static final long serialVersionUID = -2509719402171717073L;
   protected static final boolean verbose = Verbose.isVerbose(SCCredential.class);
   private static final String ELEMENT_FLAG = "<![[]]>";
   public static final int SCT_RETENTION_PRIOR = 600;
   private static final int ELEMENT_FLAG_SIZE = "<![[]]>".length();
   protected String identifier;
   protected Key secretKey;
   protected Subject authenticatedSubject;
   protected String appliesTo;
   protected Element appliesToElement;
   protected Calendar created;
   protected Calendar expires;
   protected String tokenId;
   protected String securityContextTokenIDAttribute;
   protected String NETCookieValue;
   protected QName NETCookieQName;
   protected String NETCookiePrefix;
   protected SecurityTokenReferenceInfo unattachedSecurityTokenReferenceInfo;
   protected SecurityTokenReferenceInfo attachedSecurityTokenReferenceInfo;
   protected String scNamespace = "";

   public Subject getSubject() {
      return this.authenticatedSubject;
   }

   public void setSubject(Subject var1) {
      this.authenticatedSubject = var1;
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public void setIdentifier(String var1) {
      this.identifier = var1;
   }

   public String getSecurityContextTokenIDAttribute() {
      return this.securityContextTokenIDAttribute;
   }

   public void setSecurityContextTokenIDAttribute(String var1) {
      this.securityContextTokenIDAttribute = var1;
   }

   public Key getSecret() {
      return this.secretKey;
   }

   public void setSecret(Key var1) {
      this.secretKey = var1;
   }

   public String getAppliesTo() {
      return this.appliesTo;
   }

   public void setAppliesTo(String var1) {
      this.appliesTo = var1;
   }

   public Element getAppliesToElement() {
      return this.appliesToElement;
   }

   public void setAppliesToElement(Element var1) {
      this.appliesToElement = var1;
   }

   public void setCreated(Calendar var1) {
      this.created = var1;
   }

   public Calendar getCreated() {
      return this.created;
   }

   public void setExpires(Calendar var1) {
      this.expires = var1;
   }

   public Calendar getExpires() {
      return this.expires;
   }

   public void setScNamespace(String var1) {
      this.scNamespace = var1;
   }

   public String getScNamespace() {
      return this.scNamespace;
   }

   public int hashCode() {
      return this.identifier.hashCode();
   }

   public String getTokenId() {
      return this.tokenId;
   }

   public void setTokenId(String var1) {
      this.tokenId = var1;
   }

   public void setNETCookieValue(String var1) {
      this.NETCookieValue = var1;
   }

   public String getNETCookieValue() {
      return this.NETCookieValue;
   }

   public void setNETCookieQName(QName var1) {
      this.NETCookieQName = var1;
   }

   public QName getNETCookieQName() {
      return this.NETCookieQName;
   }

   public SecurityTokenReferenceInfo newUnattachedSecurityTokenReferenceInfo() {
      this.unattachedSecurityTokenReferenceInfo = new SecurityTokenReferenceInfo();
      return this.unattachedSecurityTokenReferenceInfo;
   }

   public SecurityTokenReferenceInfo newAttachedSecurityTokenReferenceInfo() {
      this.attachedSecurityTokenReferenceInfo = new SecurityTokenReferenceInfo();
      return this.attachedSecurityTokenReferenceInfo;
   }

   public SecurityTokenReferenceInfo getUnattachedSecurityTokenReferenceInfo() {
      return this.unattachedSecurityTokenReferenceInfo;
   }

   public SecurityTokenReferenceInfo getAttachedSecurityTokenReferenceInfo() {
      return this.attachedSecurityTokenReferenceInfo;
   }

   public void setUnattachedSecurityTokenReferenceInfo(SecurityTokenReferenceInfo var1) {
      this.unattachedSecurityTokenReferenceInfo = var1;
   }

   public void setAttachedSecurityTokenReferenceInfo(SecurityTokenReferenceInfo var1) {
      this.attachedSecurityTokenReferenceInfo = var1;
   }

   public boolean hasUnattachedSecurityTokenReference() {
      return this.unattachedSecurityTokenReferenceInfo != null;
   }

   public boolean hasAttachedSecurityTokenReference() {
      return this.attachedSecurityTokenReferenceInfo != null;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof SCCredential) {
         SCCredential var2 = (SCCredential)var1;
         if (var2.getIdentifier() != null && this.getIdentifier() != null) {
            return var2.getIdentifier().equals(this.getIdentifier());
         }
      }

      return false;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF(this.identifier);
      SCTExternalizationUtil.writeKey(this.secretKey, var1);
      SCTExternalizationUtil.writeSubject(this.authenticatedSubject, var1);
      if (this.appliesToElement != null) {
         var1.writeUTF("<![[]]>" + DOMUtils.toXMLString(this.appliesToElement));
      } else {
         var1.writeUTF(this.appliesTo);
      }

      SCTExternalizationUtil.writeCalendar(this.created, var1);
      SCTExternalizationUtil.writeCalendar(this.expires, var1);
      var1.writeUTF(this.scNamespace);
      SCTExternalizationUtil.writeConditionalString(this.NETCookieValue, var1);
      SCTExternalizationUtil.writeConditionalQName(this.NETCookieQName, var1);
      SCTExternalizationUtil.writeConditionalString(this.securityContextTokenIDAttribute, var1);
      SCTExternalizationUtil.writeConditionalSecurityTokenReferenceInfo(this.unattachedSecurityTokenReferenceInfo, var1);
      SCTExternalizationUtil.writeConditionalSecurityTokenReferenceInfo(this.attachedSecurityTokenReferenceInfo, var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.identifier = var1.readUTF();
      this.secretKey = SCTExternalizationUtil.readKey(var1);
      this.authenticatedSubject = SCTExternalizationUtil.readSubject(var1);
      this.appliesTo = var1.readUTF();
      if (this.appliesTo != null && this.appliesTo.startsWith("<![[]]>")) {
         this.appliesToElement = (Element)DOMUtils.toXMLNode(this.appliesTo.substring(ELEMENT_FLAG_SIZE)).getFirstChild();
         this.appliesTo = null;
      }

      this.created = SCTExternalizationUtil.readCalendar(var1);
      this.expires = SCTExternalizationUtil.readCalendar(var1);
      this.scNamespace = var1.readUTF();
      this.NETCookieValue = SCTExternalizationUtil.readConditionalString(var1);
      this.NETCookieQName = SCTExternalizationUtil.readConditionalQName(var1);
      this.securityContextTokenIDAttribute = SCTExternalizationUtil.readConditionalString(var1);
      this.unattachedSecurityTokenReferenceInfo = SCTExternalizationUtil.readConditionalSecurityTokenReferenceInfo(var1);
      this.attachedSecurityTokenReferenceInfo = SCTExternalizationUtil.readConditionalSecurityTokenReferenceInfo(var1);
   }

   public boolean isExpired() {
      try {
         if (this.expires == null) {
            return false;
         } else {
            Calendar var1 = (Calendar)this.expires.clone();
            var1.add(13, 600);
            TimestampConfiguration var2 = new TimestampConfiguration();
            var2.checkExpiration(this.created, var1);
            return false;
         }
      } catch (SOAPFaultException var3) {
         if (verbose) {
            Verbose.log((Object)("SC credential is expired. Msg = " + var3.getMessage()));
         }

         return true;
      }
   }

   public static SCCredential copy(SCCredential var0, SCCredential var1) {
      var1.setAppliesTo(var0.getAppliesTo());
      var1.setAppliesToElement(var0.getAppliesToElement());
      var1.setCreated(var0.getCreated());
      var1.setExpires(var0.getExpires());
      var1.setIdentifier(var0.getIdentifier());
      var1.setNETCookieQName(var0.getNETCookieQName());
      var1.setNETCookieValue(var0.getNETCookieValue());
      var1.setScNamespace(var0.getScNamespace());
      var1.setSecret(var0.getSecret());
      var1.setSecurityContextTokenIDAttribute(var0.getSecurityContextTokenIDAttribute());
      var1.setSubject(var0.getSubject());
      var1.setTokenId(var0.getTokenId());
      var1.setAttachedSecurityTokenReferenceInfo(var0.getAttachedSecurityTokenReferenceInfo());
      var1.setUnattachedSecurityTokenReferenceInfo(var0.getUnattachedSecurityTokenReferenceInfo());
      return var1;
   }

   public static void copyFromSTRToInfo(SecurityTokenReference var0, SecurityTokenReferenceInfo var1) throws WSTFaultException {
      QName var2 = var0.getSTRType();
      if (var2.equals("Embedded")) {
         throw new WSTFaultException("Cannot process <RequestedUnattachedReference> or <RequestedAttachedReference> that contains a <SecurityTokenReference> with an Embedded Security Token");
      } else {
         var1.setId(var0.getId());
         var1.setReferenceURI(var0.getReferenceURI());
         var1.setValueType(var0.getValueType());
         var1.setSTRType(var2);
         KeyIdentifier var3 = var0.getKeyIdentifier();
         if (var3 != null) {
            var1.setKeyIdentifier_identifier(var3.getIdentifier());
            var1.setKeyIdentifier_encoding(var3.getEncodingType());
         }

      }
   }

   public static void copyFromInfoToSTR(SecurityTokenReferenceInfo var0, SecurityTokenReference var1) {
      var1.setId(var0.getId());
      var1.setReferenceURI(var0.getReferenceURI());
      var1.setValueType(var0.getValueType());
      var1.setSTRType(var0.getSTRType());
      byte[] var2 = var0.getKeyIdentifier_identifier();
      if (var2 != null) {
         String var3 = var0.getKeyIdentifier_encoding();
         KeyIdentifierImpl var4 = new KeyIdentifierImpl(var2, var3);
         var1.setKeyIdentifier(var4);
      }

   }

   public class SecurityTokenReferenceInfo implements Serializable {
      QName strType;
      String id;
      String referenceURI;
      String valueType;
      byte[] keyIdentifier_identifier;
      String keyIdentifier_encoding;

      public void setSTRType(QName var1) {
         this.strType = var1;
      }

      public QName getSTRType() {
         return this.strType;
      }

      public void setId(String var1) {
         this.id = var1;
      }

      public String getId() {
         return this.id;
      }

      public void setReferenceURI(String var1) {
         this.referenceURI = var1;
      }

      public String getReferenceURI() {
         return this.referenceURI;
      }

      public void setValueType(String var1) {
         this.valueType = var1;
      }

      public String getValueType() {
         return this.valueType;
      }

      public void setKeyIdentifier_identifier(byte[] var1) {
         this.keyIdentifier_identifier = var1;
      }

      public byte[] getKeyIdentifier_identifier() {
         return this.keyIdentifier_identifier;
      }

      public void setKeyIdentifier_encoding(String var1) {
         this.keyIdentifier_encoding = var1;
      }

      public String getKeyIdentifier_encoding() {
         return this.keyIdentifier_encoding;
      }
   }
}
