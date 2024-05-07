package weblogic.xml.security.wsse.v200207;

import java.security.PrivateKey;
import java.util.HashSet;
import java.util.Set;
import weblogic.xml.security.encryption.DecryptXMLInputStream;
import weblogic.xml.security.encryption.EncryptedKey;
import weblogic.xml.security.encryption.EncryptedKeyProvider;
import weblogic.xml.security.encryption.EncryptionException;
import weblogic.xml.security.encryption.ReferenceList;
import weblogic.xml.security.encryption.XMLEncReader;
import weblogic.xml.security.keyinfo.KeyProviderFactory;
import weblogic.xml.security.keyinfo.KeyResolver;
import weblogic.xml.security.signature.DSIGReader;
import weblogic.xml.security.signature.Signature;
import weblogic.xml.security.signature.SignatureValidationException;
import weblogic.xml.security.signature.SoapVerifyXMLInputStream;
import weblogic.xml.security.utils.EventBufferInputStream;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.XMLEventBuffer;
import weblogic.xml.security.utils.XMLInputStreamBase;
import weblogic.xml.security.wsse.BinarySecurityToken;
import weblogic.xml.security.wsse.SecureInputStream;
import weblogic.xml.security.wsse.Security;
import weblogic.xml.security.wsse.internal.SoapStreamState;
import weblogic.xml.security.wsse.internal.Utils;
import weblogic.xml.security.wsu.v200207.TimestampImpl;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartDocument;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputOutputStream;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class SecureInputStreamInternal extends XMLInputStreamBase implements WSSEConstants, SecureInputStream {
   private static final XMLName REFERENCE_LIST = ElementFactory.createXMLName("http://www.w3.org/2001/04/xmlenc#", "ReferenceList");
   private static final XMLName SIGNATURE = ElementFactory.createXMLName("http://www.w3.org/2000/09/xmldsig#", "Signature");
   private static final XMLName USERNAME_TOKEN;
   private static final XMLName BINARY_SECURITY_TOKEN;
   private static final XMLName TIMESTAMP;
   private static final XMLName ENCRYPTED_KEY;
   private static final boolean RETAIN_TIMESTAMPS = true;
   private final String role;
   private final XMLInputStream origin;
   private final Set processed;
   private String encoding;
   private KeyResolver keyResolver;
   private SecurityImpl security;

   public SecureInputStreamInternal(XMLInputStream var1, String var2, KeyResolver var3) throws XMLStreamException {
      this.processed = new HashSet();
      this.origin = var1;
      this.role = var2;
      this.keyResolver = var3;
      this.security = null;
      this.source = null;
   }

   public KeyResolver getKeyResolver() {
      return this.keyResolver;
   }

   public void setKeyResolver(KeyResolver var1) {
      if (this.source != null) {
         throw new IllegalStateException("Cannot change key resolver after stream has been read");
      } else {
         this.keyResolver = var1;
      }
   }

   public SecureInputStreamInternal(XMLInputStream var1, String var2, PrivateKey var3) throws XMLStreamException {
      this(var1, var2, new KeyResolver());
      if (var3 != null) {
         this.keyResolver.addKeyProvider(KeyProviderFactory.create(var3));
      }

   }

   private static String getEncoding(XMLInputStream var0) throws XMLStreamException {
      if (var0 != null) {
         XMLEvent var1 = var0.peek();
         if (var1.isStartDocument()) {
            return ((StartDocument)var1).getCharacterEncodingScheme();
         }
      }

      return null;
   }

   private static boolean findHeader(XMLInputStream var0, XMLEventBuffer var1, String var2) throws XMLStreamException {
      SoapStreamState var3 = new SoapStreamState();

      while(var0.hasNext()) {
         XMLEvent var5 = var0.peek();
         boolean var4;
         switch (var5.getType()) {
            case 2:
               StartElement var6 = (StartElement)var5;
               var4 = var3.update(var6);
               if (var3.inHeader() && var3.atTypeLevel()) {
                  XMLName var7 = var6.getName();
                  String var8 = var7.getNamespaceUri();
                  if ("Security".equals(var7.getLocalName()) && validWSSEuri(var8)) {
                     String var9 = StreamUtils.getAttribute(var6, "role");
                     if (var2 == null) {
                        if (var9 == null) {
                           return true;
                        }
                     } else if (var2.equals(var9)) {
                        return true;
                     }
                  }
               } else if (var3.after(1)) {
                  return false;
               }

               bufferNext(var0, var1);
               break;
            case 4:
               var4 = var3.update(bufferNext(var0, var1));
               if (var4 && var3.after(1)) {
                  return false;
               }
               break;
            default:
               var4 = var3.update(bufferNext(var0, var1));
         }

         if (var4) {
         }
      }

      return false;
   }

   private XMLInputStream buildStream() throws XMLStreamException {
      this.encoding = getEncoding(this.origin);
      this.security = new SecurityImpl(this.role);
      this.source = this.buildStream(this.origin, this.security);
      return this.source;
   }

   private XMLInputStream buildStream(XMLInputStream var1, SecurityImpl var2) throws XMLStreamException {
      XMLEventBuffer var3 = new XMLEventBuffer();
      boolean var4 = findHeader(var1, var3, this.role);
      if (!var4) {
         return EventBufferInputStream.reconstitute(var3, var1);
      } else {
         StartElement var5 = (StartElement)var1.next();
         if (!StreamUtils.matches(var5, (String)"Security") && !validWSSEuri(var5.getName().getNamespaceUri())) {
            throw new AssertionError("findHeader failed");
         } else {
            XMLEvent var6 = var1.peek();

            while(!var6.isEndElement()) {
               if (!var6.isStartElement()) {
                  var1.next();
                  var6 = var1.peek();
               } else {
                  if (StreamUtils.matches(var6, BINARY_SECURITY_TOKEN)) {
                     this.handleBST(var1, var2, var3);
                  } else if (StreamUtils.matches(var6, USERNAME_TOKEN)) {
                     this.handleUsernameToken(var1, var2);
                  } else if (StreamUtils.matches(var6, TIMESTAMP)) {
                     this.handleTimestamp(var1, var2, var3);
                  } else {
                     if (StreamUtils.matches(var6, SIGNATURE)) {
                        return this.handleSignature(var1, var2, var5, var3);
                     }

                     if (StreamUtils.matches(var6, REFERENCE_LIST)) {
                        return this.handleReferenceList(var1, var2, var5, var3);
                     }

                     if (StreamUtils.matches(var6, ENCRYPTED_KEY)) {
                        return this.handleEncryptedKey(var1, var2, var5, var3);
                     }

                     if (StreamUtils.matches(var5, (String)"Security")) {
                        throw new XMLStreamException("Unbalanced ELEMENT: Security");
                     }

                     Utils.handleException(WSSEConstants.QNAME_FAULT_UNSUPPORTEDSECURITYTOKEN, var5.getName() + " not currently supported", this.role);
                  }

                  var6 = var1.peek();
               }
            }

            var6 = var1.next();
            if (!matchingEndElement(var5, (EndElement)var6)) {
               throw new AssertionError("expected </Security>, got " + var6);
            } else {
               return EventBufferInputStream.reconstitute(var3, var1);
            }
         }
      }
   }

   private XMLInputStream handleEncryptedKey(XMLInputStream var1, SecurityImpl var2, StartElement var3, XMLEventBuffer var4) throws XMLStreamException {
      EncryptedKey var5 = (EncryptedKey)XMLEncReader.read(var1, 2);
      var2.append(var5);
      return this.buildStream(decryptStream(var5, this.keyResolver, this.encoding, reconstitute(var3, var4, var1)), var2);
   }

   private XMLInputStream handleReferenceList(XMLInputStream var1, SecurityImpl var2, StartElement var3, XMLEventBuffer var4) throws XMLStreamException {
      ReferenceList var5 = (ReferenceList)XMLEncReader.read(var1, 4);
      var2.append(var5);
      return this.buildStream(decryptStream(var5, this.keyResolver, this.encoding, reconstitute(var3, var4, var1)), var2);
   }

   private XMLInputStream handleSignature(XMLInputStream var1, SecurityImpl var2, StartElement var3, XMLEventBuffer var4) throws XMLStreamException {
      Signature var5 = (Signature)DSIGReader.read(var1, 8);
      var2.append(var5);
      return this.buildStream(verifyStream(var5, this.keyResolver, this.role, reconstitute(var3, var4, var1)), var2);
   }

   private void handleTimestamp(XMLInputStream var1, SecurityImpl var2, XMLEventBuffer var3) throws XMLStreamException {
      StartElement var4 = (StartElement)var1.peek();
      String var5 = StreamUtils.getAttributeByName("Id", WSUConstants.WSU_URI, var4);
      if (var5 == null) {
         var2.append(new TimestampImpl(var1));
      } else if (this.processed.contains(var5)) {
         this.skipElement(var1, var3);
      } else {
         XMLInputOutputStream var6 = XMLOutputStreamFactory.newInstance().newInputOutputStream();
         this.copyElement(var1, var3, var6);
         TimestampImpl var7 = new TimestampImpl(var6);
         var2.append(var7);
         this.processed.add(var5);
      }

   }

   private void skipElement(XMLInputStream var1, XMLEventBuffer var2) throws XMLStreamException {
      int var3 = 0;

      do {
         XMLEvent var4 = var1.next();
         var2.add(var4);
         switch (var4.getType()) {
            case 2:
               ++var3;
               break;
            case 4:
               --var3;
         }
      } while(var3 > 0);

   }

   private void copyElement(XMLInputStream var1, XMLEventBuffer var2, XMLOutputStream var3) throws XMLStreamException {
      int var4 = 0;

      do {
         XMLEvent var5 = var1.next();
         var2.add(var5);
         var3.add(var5);
         switch (var5.getType()) {
            case 2:
               ++var4;
               break;
            case 4:
               --var4;
         }
      } while(var4 > 0);

   }

   private void handleUsernameToken(XMLInputStream var1, SecurityImpl var2) throws XMLStreamException {
      var2.append(new UsernameTokenImpl(var1, WSSE_URI));
   }

   private void handleBST(XMLInputStream var1, SecurityImpl var2, XMLEventBuffer var3) throws XMLStreamException {
      if (!WSSEConstants.SIGN_BST) {
         BinarySecurityTokenImpl var4 = new BinarySecurityTokenImpl(var1, WSSE_URI);
         this.keyResolver.addKeyProvider(KeyProviderFactory.create((BinarySecurityToken)var4));
         var2.append(var4);
      } else {
         StartElement var8 = (StartElement)var1.peek();
         String var5 = StreamUtils.getAttributeByName("Id", WSUConstants.WSU_URI, var8);
         if (this.processed.contains(var5)) {
            this.skipElement(var1, var3);
         } else {
            XMLInputOutputStream var6 = XMLOutputStreamFactory.newInstance().newInputOutputStream();
            this.copyElement(var1, var3, var6);
            BinarySecurityTokenImpl var7 = new BinarySecurityTokenImpl(var6, WSSE_URI);
            this.keyResolver.addKeyProvider(KeyProviderFactory.create((BinarySecurityToken)var7));
            var2.append(var7);
            this.processed.add(var5);
         }
      }

   }

   private static final XMLInputStream verifyStream(Signature var0, KeyResolver var1, String var2, XMLInputStream var3) throws XMLStreamException {
      try {
         SoapVerifyXMLInputStream var4 = new SoapVerifyXMLInputStream(var0, var3, var1.copy());
         return var4;
      } catch (SignatureValidationException var6) {
         Utils.handleException(weblogic.xml.security.utils.Utils.getQName(WSSEConstants.QNAME_FAULT_INVALIDSECURITYTOKEN), var6.getMessage(), var2);
         throw new AssertionError("failed to validate signature");
      }
   }

   private static final XMLInputStream decryptStream(EncryptedKey var0, KeyResolver var1, String var2, XMLInputStream var3) throws XMLStreamException {
      ReferenceList var4 = var0.getReferenceList();
      var0.setKeyResolver(var1);

      EncryptedKeyProvider var5;
      try {
         var5 = new EncryptedKeyProvider(var0);
      } catch (EncryptionException var7) {
         if (var7.getMessage().indexOf("Invalid input length") > -1) {
            throw new XMLStreamException("Unable to decrypt EncryptedKey: key size of encryption/decryption mismatched", var7);
         }

         throw new XMLStreamException("Unable to decrypt EncryptedKey", var7);
      }

      var1.addKeyProvider(var5);
      KeyResolver var6 = new KeyResolver();
      var6.addKeyProvider(var5);
      return decryptStream(var4, var6, var2, var3);
   }

   private static XMLInputStream decryptStream(ReferenceList var0, KeyResolver var1, String var2, XMLInputStream var3) {
      DecryptXMLInputStream var4 = new DecryptXMLInputStream(var3, var0, var2);
      var4.setKeyResolver(var1);
      return var4;
   }

   private static final XMLInputStream reconstitute(StartElement var0, XMLEventBuffer var1, XMLInputStream var2) throws XMLStreamException {
      while(var2.hasNext()) {
         XMLEvent var3 = var2.peek();
         switch (var3.getType()) {
            case 2:
               var1.add(var0);
               return EventBufferInputStream.reconstitute(var1, var2);
            case 4:
               if (!matchingEndElement(var0, (EndElement)var3)) {
                  throw new AssertionError("expected </Security>, got " + var3);
               }

               var2.next();
               return EventBufferInputStream.reconstitute(var1, var2);
            default:
               var2.next();
         }
      }

      throw new XMLStreamException("stream ended unexpectedly with security header open");
   }

   private static final boolean matchingEndElement(StartElement var0, EndElement var1) {
      return StreamUtils.matches(var1, "Security", var0.getName().getNamespaceUri());
   }

   private static final boolean validWSSEuri(String var0) {
      return WSSEConstants.WSSE_URI.equals(var0);
   }

   private static final XMLEvent bufferNext(XMLInputStream var0, XMLEventBuffer var1) throws XMLStreamException {
      XMLEvent var2 = var0.next();
      var1.add(var2);
      return var2;
   }

   public Security getSecurityElement() {
      try {
         if (this.source == null) {
            this.buildStream();
         }
      } catch (XMLStreamException var2) {
         throw new AssertionError(var2);
      }

      return this.security;
   }

   public boolean hasNext() throws XMLStreamException {
      if (this.source == null) {
         this.buildStream();
      }

      return super.hasNext();
   }

   public XMLEvent next() throws XMLStreamException {
      if (this.source == null) {
         this.buildStream();
      }

      return this.source.next();
   }

   public XMLEvent peek() throws XMLStreamException {
      if (this.source == null) {
         this.buildStream();
      }

      return super.peek();
   }

   static {
      USERNAME_TOKEN = ElementFactory.createXMLName(WSSE_URI, "UsernameToken");
      BINARY_SECURITY_TOKEN = ElementFactory.createXMLName(WSSE_URI, "BinarySecurityToken");
      TIMESTAMP = ElementFactory.createXMLName(WSUConstants.WSU_URI, "Timestamp");
      ENCRYPTED_KEY = ElementFactory.createXMLName("http://www.w3.org/2001/04/xmlenc#", "EncryptedKey");
   }
}
