package weblogic.xml.security.wsse.v200207;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.UserInfo;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.security.wsse.UsernameToken;
import weblogic.xml.security.wsse.internal.BaseToken;
import weblogic.xml.security.wsu.v200207.CreatedImpl;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class UsernameTokenImpl extends BaseToken implements WSSEConstants, UsernameToken {
   private byte[] password;
   private String passwordType;
   private String passwordDigest;
   private byte[] username;
   private String id;
   private String b64nonce;
   private byte[] nonce;
   private CreatedImpl created;
   private boolean generateNonce;

   public UsernameTokenImpl(String var1) {
      this.password = null;
      this.passwordType = null;
      this.passwordDigest = null;
      this.username = null;
      this.id = null;
      this.b64nonce = null;
      this.nonce = null;
      this.created = null;
      this.generateNonce = false;
      this.username = var1.getBytes();
   }

   public UsernameTokenImpl(String var1, String var2, String var3) {
      this(var1);
      this.password = var2.getBytes();
      if (PASSWORDTYPE_PASSWORDDIGEST.equals(var3)) {
         var3 = PASSWORDTYPE_PASSWORDDIGEST;
      } else {
         if (!PASSWORDTYPE_PASSWORDTEXT.equals(var3)) {
            throw new AssertionError("Bad password type: " + var3);
         }

         this.passwordType = PASSWORDTYPE_PASSWORDTEXT;
      }

   }

   public UsernameTokenImpl(String var1, String var2) {
      this(var1, var2, PASSWORDTYPE_PASSWORDTEXT);
   }

   public UsernameTokenImpl(XMLInputStream var1, String var2) throws XMLStreamException {
      this.password = null;
      this.passwordType = null;
      this.passwordDigest = null;
      this.username = null;
      this.id = null;
      this.b64nonce = null;
      this.nonce = null;
      this.created = null;
      this.generateNonce = false;
      this.fromXMLInternal(var1, var2);
   }

   public final String getUsername() {
      return new String(this.username);
   }

   public final String getPassword() {
      return new String(this.password);
   }

   public final String getPasswordDigest() {
      return this.passwordDigest;
   }

   public final String getPasswordType() {
      return this.passwordType;
   }

   public void setId(String var1) throws SecurityProcessingException {
      if (var1 != null) {
         throw new SecurityProcessingException("Id for UsernameToken already set");
      } else {
         this.id = var1;
      }
   }

   public boolean verifyDigest(String var1) throws NoSuchAlgorithmException {
      if (this.nonce != null && var1 != null && this.created != null) {
         byte[] var2 = Utils.passwordDigest(this.nonce, this.created.getTimeString(), var1);
         return Arrays.equals(var2, Utils.base64(this.passwordDigest));
      } else {
         return false;
      }
   }

   public String getId() {
      if (this.id == null) {
         this.id = Utils.generateId("UsernameToken");
      }

      return this.id;
   }

   public Key getSecretKey() {
      throw new UnsupportedOperationException("Secret keys are unsupported on this token type");
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("UsernameToken:").append("\n Username:       ").append(this.username).append("\n Id:             ").append(this.id).append("\n PasswordType:   ").append(this.passwordType);
      if (this.password != null) {
         var1.append("\n Password:       ").append("****");
      }

      if (this.passwordDigest != null) {
         var1.append("\n PasswordDigest: ").append(this.passwordDigest);
      }

      if (this.b64nonce != null) {
         var1.append("\n Nonce:          ").append(this.b64nonce);
      }

      if (this.created != null) {
         var1.append("\n ").append(this.created);
      }

      return var1.toString();
   }

   public final void toXML(XMLOutputStream var1) throws XMLStreamException {
      this.toXML(var1, WSSE_URI, 0);
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException, SecurityProcessingException {
      Attribute[] var4 = new Attribute[]{ElementFactory.createAttribute(WSUConstants.WSU_URI, "Id", this.getId())};
      StreamUtils.addStart(var1, var2, "UsernameToken", var4, var3);
      int var5 = var3 + 2;
      StreamUtils.addElement(var1, var2, "Username", this.getUsername(), var5);
      if (this.password != null) {
         if (this.generateNonce || PASSWORDTYPE_PASSWORDDIGEST.equals(this.passwordType)) {
            if (this.nonce == null) {
               this.generateNonce();
            }

            if (this.created == null) {
               this.generateCreated();
            }
         }

         if (PASSWORDTYPE_PASSWORDTEXT.equals(this.passwordType)) {
            this.textToXML(var1, var2, var5);
         } else if (PASSWORDTYPE_PASSWORDDIGEST.equals(this.passwordType)) {
            this.digestToXML(var1, var2, var5);
         }

         if (this.b64nonce != null) {
            StreamUtils.addElement(var1, var2, "Nonce", this.b64nonce, var3);
         }

         if (this.created != null) {
            this.created.toXML(var1);
         }
      }

      StreamUtils.addEnd(var1, var2, "UsernameToken", var3);
   }

   private void textToXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      Attribute[] var4 = new Attribute[]{StreamUtils.createAttribute("Type", PASSWORDTYPE_PASSWORDTEXT)};
      StreamUtils.addElement(var1, var2, "Password", this.getPassword(), var4, var3, 0);
   }

   private void digestToXML(XMLOutputStream var1, String var2, int var3) throws SecurityProcessingException, XMLStreamException {
      if (this.passwordDigest == null) {
         this.generateDigest();
      }

      Attribute[] var4 = new Attribute[]{StreamUtils.createAttribute("Type", PASSWORDTYPE_PASSWORDDIGEST)};
      StreamUtils.addElement(var1, var2, "Password", this.passwordDigest, var4, var3, 0);
   }

   private void generateDigest() throws SecurityProcessingException {
      Object var1 = null;

      byte[] var4;
      try {
         var4 = Utils.passwordDigest(this.nonce, this.created.getTimeString(), this.getPassword());
      } catch (NoSuchAlgorithmException var3) {
         throw new SecurityProcessingException("Digest algorithm for UsernameToken PasswordDigest unavailable", var3);
      }

      this.passwordDigest = Utils.base64(var4);
   }

   private void generateCreated() {
      this.created = new CreatedImpl();
   }

   private void generateNonce() {
      this.nonce = Utils.generateNonce(16);
      this.b64nonce = Utils.base64(this.nonce);
   }

   public void resetDigest() {
      this.passwordDigest = null;
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "UsernameToken");
      this.id = StreamUtils.getAttribute(var3, "Id");

      for(XMLEvent var4 = var1.peek(); !var4.isEndElement(); var4 = var1.peek()) {
         if (var4.isStartElement()) {
            if (StreamUtils.matches(var4, "Username", var2)) {
               this.usernameFromXML(var1, var2);
            } else if (StreamUtils.matches(var4, "Password", var2)) {
               this.passwordFromXML(var1, var2);
            } else if (StreamUtils.matches(var4, "Nonce", var2)) {
               this.nonceFromXML(var1, var2);
            } else if (StreamUtils.matches(var4, "Created", WSUConstants.WSU_URI)) {
               this.created = new CreatedImpl(var1);
            }
         } else {
            var1.next();
         }
      }

      StreamUtils.closeScope(var1, var2, "UsernameToken");
   }

   private void usernameFromXML(XMLInputStream var1, String var2) throws XMLStreamException {
      this.username = StreamUtils.getValue(var1, var2, "Username").getBytes();
      if (this.username == null) {
         throw new XMLStreamException("UsernameToken did not contain username");
      }
   }

   private void passwordFromXML(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "Password");
      if (var3 != null) {
         String var4 = StreamUtils.getAttribute(var3, "Type");
         if (var4 != null) {
            this.passwordType = var4;
         } else {
            this.passwordType = PASSWORDTYPE_PASSWORDTEXT;
         }

         if (PASSWORDTYPE_PASSWORDTEXT.equals(this.passwordType)) {
            this.password = StreamUtils.getData(var1, "Password").getBytes();
            this.passwordDigest = null;
         } else if (PASSWORDTYPE_PASSWORDDIGEST.equals(this.passwordType)) {
            this.passwordDigest = StreamUtils.getData(var1, "Password");
         }

         StreamUtils.closeScope(var1, var2, "Password");
      }
   }

   private void nonceFromXML(XMLInputStream var1, String var2) throws XMLStreamException {
      this.b64nonce = StreamUtils.getValue(var1, var2, "Nonce");
      this.nonce = Utils.base64(this.b64nonce);
   }

   public UserInfo getUserInfo() {
      return this.passwordType != null && !this.passwordType.equals(PASSWORDTYPE_PASSWORDDIGEST) ? new UserInfo(this.getUsername(), this.getPassword()) : new UserInfo(this.getUsername(), this.passwordDigest != null ? this.passwordDigest.getBytes() : null, this.b64nonce, this.created != null ? this.created.getTimeString() : null);
   }

   public void setGenerateNonce(boolean var1) {
      this.generateNonce = var1;
   }

   public boolean isGenerateNonce() {
      return this.generateNonce;
   }

   public String getNonce() {
      if (this.b64nonce != null) {
         return this.b64nonce;
      } else {
         return this.nonce != null ? Utils.base64(this.nonce) : null;
      }
   }

   public long getCreatedTimeInMillis() {
      return this.created == null ? 0L : this.created.getTime().getTimeInMillis();
   }
}
