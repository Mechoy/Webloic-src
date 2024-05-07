package weblogic.xml.security.specs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class SecurityDD implements SpecConstants {
   private SignatureKey signingKey = null;
   private EncryptionKey encryptionKey = null;
   private User user = null;
   private final Map specs = new HashMap();
   private TimestampConfig timestampConfig = null;
   private static final SecurityDD DEFAULT_DD = createDefaultSecurityDD();

   public SecurityDD(XMLInputStream var1) throws XMLStreamException {
      this.fromXMLInternal(var1);
      if (this.timestampConfig == null) {
         this.timestampConfig = new TimestampConfig();
      }

   }

   public User getUser() {
      return this.user;
   }

   public void setUser(User var1) {
      this.user = var1;
   }

   public SecurityDD(SecuritySpec var1) {
      String var2 = var1.getId();
      if (var2 == null) {
         var2 = "default-spec";
         var1.setId(var2);
      }

      this.addSecuritySpec(var1);
   }

   public void addSecuritySpec(SecuritySpec var1) {
      String var2 = var1.getId();
      if (var2 == null) {
         var2 = "default-spec";
         var1.setId(var2);
      }

      SecuritySpec var3 = (SecuritySpec)this.specs.get(var2);
      if (var3 != null) {
         throw new IllegalArgumentException("SecuritySpec with id=" + var2 + " already exists in the DD");
      } else {
         this.specs.put(var2, var1);
      }
   }

   public SecuritySpec removeSecuritySpec(String var1) {
      return (SecuritySpec)this.specs.remove(var1);
   }

   public SecuritySpec getSecuritySpec(String var1) {
      if (var1 == null) {
         var1 = "default-spec";
      }

      return (SecuritySpec)this.specs.get(var1);
   }

   public Iterator getSecuritySpecs() {
      return this.specs.values().iterator();
   }

   /** @deprecated */
   public SecuritySpec getSpec() {
      return this.getSecuritySpec("default-spec");
   }

   /** @deprecated */
   public void setSpec(SecuritySpec var1) {
      this.addSecuritySpec(var1);
   }

   /** @deprecated */
   public SecuritySpec getSecuritySpec() {
      return this.getSecuritySpec("default-spec");
   }

   public SignatureKey getSigningKey() {
      return this.signingKey;
   }

   public void setSigningKey(SignatureKey var1) {
      this.signingKey = var1;
   }

   public EncryptionKey getEncryptionKey() {
      return this.encryptionKey;
   }

   public void setEncryptionKey(EncryptionKey var1) {
      this.encryptionKey = var1;
   }

   public TimestampConfig getTimestampConfig() {
      return this.timestampConfig;
   }

   public void setTimestampConfig(TimestampConfig var1) {
      this.timestampConfig = var1;
   }

   private static SecurityDD createDefaultSecurityDD() {
      SecuritySpec var0 = new SecuritySpec();
      var0.setBinarySecurityTokenSpec(BinarySecurityTokenSpec.getDefaultSpec());
      var0.setEncryptionSpec(EncryptionSpec.getDefaultSpec());
      var0.setSignatureSpec(SignatureSpec.getDefaultSpec());
      SecurityDD var1 = new SecurityDD(var0);
      return var1;
   }

   public static final SecurityDD getDefaultSecurityDD() {
      return DEFAULT_DD;
   }

   private void fromXMLInternal(XMLInputStream var1) throws XMLStreamException {
      XMLEvent var2 = StreamUtils.skipWS(var1, false);
      if (!var2.isStartElement()) {
         throw new XMLStreamException("Did not receive expected start Element security");
      } else {
         StartElement var3 = (StartElement)var2;
         XMLName var4 = var3.getName();
         if (!"security".equals(var4.getLocalName())) {
            throw new XMLStreamException("Did not receive expected start Element security");
         } else {
            for(var2 = StreamUtils.skipWS(var1, true); !var2.isEndElement(); var2 = StreamUtils.skipWS(var1, true)) {
               if (!var2.isStartElement()) {
                  var1.next();
               } else {
                  var3 = (StartElement)var2;
                  var4 = var3.getName();
                  if (var4.getLocalName().equals("user")) {
                     if (this.user != null) {
                        throw new XMLStreamException("Received multiple users in dd, only one is allowed");
                     }

                     this.user = new User(var1);
                  } else if (var4.getLocalName().equals("encryptionKey")) {
                     if (this.encryptionKey != null) {
                        throw new XMLStreamException("Received multiple encryption keys in dd, only one is allowed");
                     }

                     this.encryptionKey = new EncryptionKey(var1);
                  } else if (var4.getLocalName().equals("timestamp")) {
                     if (this.timestampConfig != null) {
                        throw new XMLStreamException("Received multiple timestamp configurations in dd, only one is allowed");
                     }

                     this.timestampConfig = new TimestampConfig(var1);
                  } else if (var4.getLocalName().equals("signatureKey")) {
                     if (this.signingKey != null) {
                        throw new XMLStreamException("Received multiple signature keys in dd, only one is allowed");
                     }

                     this.signingKey = new SignatureKey(var1);
                  } else {
                     if (!var4.getLocalName().equals("SecuritySpec")) {
                        throw new XMLStreamException("Unexpected data in dd -- " + var3);
                     }

                     SecuritySpec var5 = new SecuritySpec(var1, "http://www.openuri.org/2002/11/wsse/spec");
                     this.addSecuritySpec(var5);
                  }
               }
            }

            StreamUtils.closeScope(var1, "security");
         }
      }
   }

   public void toXML(XMLOutputStream var1) throws XMLStreamException {
      ArrayList var2 = new ArrayList(2);
      Attribute[] var3 = new Attribute[var2.size()];
      var2.toArray(var3);
      StreamUtils.addStart(var1, "security", var3);
      this.childrenToXML(var1);
      StreamUtils.addEnd(var1, "security");
   }

   private void childrenToXML(XMLOutputStream var1) throws XMLStreamException {
      if (this.user != null) {
         this.user.toXML(var1);
      }

      if (this.signingKey != null) {
         this.signingKey.toXML(var1);
      }

      if (this.encryptionKey != null) {
         this.encryptionKey.toXML(var1);
      }

      if (this.timestampConfig != null) {
         this.timestampConfig.toXML(var1);
      }

      NamespaceAwareXOS var2 = new NamespaceAwareXOS(var1);
      var2.addPrefix("http://www.openuri.org/2002/11/wsse/spec", "spec");
      Set var3 = this.specs.entrySet();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Map.Entry var5 = (Map.Entry)var4.next();
         SecuritySpec var6 = (SecuritySpec)var5.getValue();
         var6.toXML(var2);
      }

   }

   public static void main(String[] var0) throws Exception {
      SecurityDD var1 = getDefaultSecurityDD();
      System.out.println(var1);
      System.out.println("\nDefault:");
      XMLOutputStream var2 = TestUtils.createXMLOutputStream(System.out);
      var1.toXML(var2);
      var2.flush();
      System.out.println("\nDefault with timestamp:");
      var1.addSecuritySpec(new SecuritySpec("myID", "http://foo.bar.com/", "me:I"));
      var1.setTimestampConfig(new TimestampConfig(true, 100L, false, 1000L, true, 10000L, true));
      var2 = TestUtils.createXMLOutputStream(System.out);
      var1.toXML(var2);
      var2.flush();
   }

   public String toString() {
      return "weblogic.xml.security.specs.SecurityDD{signingKey=" + this.signingKey + ", encryptionKey=" + this.encryptionKey + ", user=" + this.user + ", specs=" + (this.specs == null ? null : "size:" + this.specs.size() + this.specs) + ", timestampConfig=" + this.timestampConfig + "}";
   }
}
