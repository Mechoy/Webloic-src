package weblogic.xml.security.wsse.v200207;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import weblogic.xml.security.NamedKey;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.UserInfo;
import weblogic.xml.security.encryption.EncryptedKey;
import weblogic.xml.security.encryption.EncryptionException;
import weblogic.xml.security.encryption.ReferenceList;
import weblogic.xml.security.encryption.XMLEncReader;
import weblogic.xml.security.keyinfo.KeyInfo;
import weblogic.xml.security.keyinfo.KeyInfoValidationException;
import weblogic.xml.security.signature.DSIGReader;
import weblogic.xml.security.signature.InternalReference;
import weblogic.xml.security.signature.Reference;
import weblogic.xml.security.signature.Signature;
import weblogic.xml.security.signature.XMLSignatureException;
import weblogic.xml.security.specs.EncryptionSpec;
import weblogic.xml.security.specs.SignatureSpec;
import weblogic.xml.security.transforms.ExcC14NTransform;
import weblogic.xml.security.transforms.IncompatibleTransformException;
import weblogic.xml.security.transforms.Transform;
import weblogic.xml.security.transforms.TransformException;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.MutableStart;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.wsse.BinarySecurityToken;
import weblogic.xml.security.wsse.Security;
import weblogic.xml.security.wsse.SecurityTokenReference;
import weblogic.xml.security.wsse.Token;
import weblogic.xml.security.wsse.UsernameToken;
import weblogic.xml.security.wsse.internal.Operation;
import weblogic.xml.security.wsu.Expires;
import weblogic.xml.security.wsu.Timestamp;
import weblogic.xml.security.wsu.WSUFactory;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class SecurityImpl implements WSSEConstants, Security {
   private static final WSUFactory timestampFac;
   private String role = null;
   private List elements = new ArrayList(3);
   private List toDoList = new ArrayList();
   private Map tokenMap = null;

   public SecurityImpl(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public SecurityImpl(String var1) {
      this.role = var1;
   }

   public Signature addSignature(Token var1) throws SecurityProcessingException {
      return this.addSignature(var1, SignatureSpec.getDefaultSpec());
   }

   public Signature addSignature(Token var1, SignatureSpec var2) throws SecurityProcessingException {
      Signature var3 = new Signature();
      KeyInfo var4 = new KeyInfo(var1);
      var3.setKeyInfo(var4);
      if (WSSEConstants.SIGN_BST) {
         Iterator var5 = var4.getSecurityTokenReferences();

         while(var5.hasNext()) {
            SecurityTokenReference var6 = (SecurityTokenReference)var5.next();
            var3.addReference(this.createReference("#" + var1.getId(), var1, var2));
         }
      }

      try {
         var3.setSignatureMethod(var2.getSignatureMethod());
         var3.setCanonicalizationMethod(var2.getCanonicalizationMethod());
      } catch (XMLSignatureException var7) {
         throw new SecurityProcessingException("Unable to set Method", var7);
      }

      this.prepend(var3);
      this.addToDo(var3, var2);
      return var3;
   }

   private Reference createReference(String var1, Token var2, SignatureSpec var3) {
      InternalReference var4 = new InternalReference(var1);

      try {
         ExcC14NTransform var5 = (ExcC14NTransform)Transform.getTransform(var3.getCanonicalizationMethod());
         var4.addTransform(var5);
         this.addBSTToReference((InternalReference)var4, (BinarySecurityTokenImpl)var2);
      } catch (IncompatibleTransformException var6) {
         var6.printStackTrace();
      } catch (TransformException var7) {
         var7.printStackTrace();
      } catch (XMLStreamException var8) {
         var8.printStackTrace();
      }

      return var4;
   }

   private void addBSTToReference(InternalReference var1, BinarySecurityTokenImpl var2) throws XMLStreamException {
      ArrayList var3 = new ArrayList();
      var3.add(ElementFactory.createAttribute("ValueType", var2.getValueType()));
      var3.add(ElementFactory.createAttribute("EncodingType", var2.getEncodingType()));
      var3.add(ElementFactory.createAttribute(WSUConstants.WSU_URI, "Id", "wsu", var2.getId()));
      Attribute[] var4 = new Attribute[var3.size()];
      var3.toArray(var4);
      MutableStart var5 = (MutableStart)ElementFactory.createStartElement(WSSEConstants.WSSE_URI, "BinarySecurityToken", "wsse");

      for(int var6 = 0; var6 < var4.length; ++var6) {
         if (var4[var6] != null) {
            var5.addAttribute(var4[var6]);
         }
      }

      var1.observe(var5);
      var1.observe(ElementFactory.createCharacterData(var2.getEncodedValue()));
      var1.observe(ElementFactory.createEndElement(WSSEConstants.WSSE_URI, "BinarySecurityToken", "wsse"));
   }

   public EncryptedKey addEncryption(Token var1, EncryptionSpec var2) throws SecurityProcessingException {
      if (var1 == null && var2 != null) {
         var1 = var2.getToken();
      }

      X509Certificate var3;
      if (var1 != null) {
         var3 = var1.getCertificate();
      } else {
         var3 = null;
      }

      if (var3 == null) {
         throw new SecurityProcessingException("provided token " + var1 + " does not support key " + "encryption");
      } else {
         KeyInfo var4 = new KeyInfo(var1);
         return this.addEncryption(var3, var4, var2);
      }
   }

   public EncryptedKey addEncryption(Token var1, NamedKey var2, EncryptionSpec var3) throws SecurityProcessingException {
      EncryptedKey var4 = this.addEncryption(var1, var3);
      var4.setWrappedKey(var2.getKey());
      var4.setCarriedKeyName(var2.getName());
      return var4;
   }

   public ReferenceList addEncryption(NamedKey var1, EncryptionSpec var2) throws SecurityProcessingException {
      ReferenceList var3 = new ReferenceList();
      this.addToDo(var1.copy(var3), var2);
      this.prepend(var3);
      return var3;
   }

   public EncryptedKey addEncryption(X509Certificate var1, EncryptionSpec var2) throws SecurityProcessingException {
      if (var1 == null) {
         throw new AssertionError("No certificate available for key wrapping");
      } else {
         return this.addEncryption(var1, new KeyInfo(var1.getSubjectDN().toString()), var2);
      }
   }

   private EncryptedKey addEncryption(X509Certificate var1, KeyInfo var2, EncryptionSpec var3) throws SecurityProcessingException {
      EncryptedKey var4 = null;

      try {
         PublicKey var5 = var1.getPublicKey();
         String var6 = this.getWrappingAlgorithm(var5, var3.getKeyWrappingMethod());
         var4 = new EncryptedKey(var5, var3.getEncryptionMethod(), var6);
      } catch (EncryptionException var7) {
         throw new SecurityProcessingException("Problem adding encrypted key", var7);
      }

      var4.setKeyInfo(var2);
      return this.addEncryption(var4, var3);
   }

   private EncryptedKey addEncryption(EncryptedKey var1, EncryptionSpec var2) {
      this.prepend(var1);
      this.addToDo(var1, var2);
      return var1;
   }

   private String getWrappingAlgorithm(Key var1, String var2) throws SecurityProcessingException {
      String var3 = var1.getAlgorithm();
      if ("RSA".equals(var3)) {
         return var2;
      } else {
         throw new SecurityProcessingException("No keywrapping algorithm available for " + var3 + " keys");
      }
   }

   public EncryptedKey addEncryption(Token var1) throws SecurityProcessingException {
      return this.addEncryption(var1, EncryptionSpec.getDefaultSpec());
   }

   /** @deprecated */
   public void addBinarySecurityToken(BinarySecurityToken var1) {
      this.addToken((Token)var1);
   }

   /** @deprecated */
   public void addUsernameToken(UsernameToken var1) {
      this.addToken((Token)var1);
   }

   public Token addToken(Token var1) {
      this.tokenMap = null;

      try {
         this.updateReferenes(var1);
      } catch (SecurityProcessingException var3) {
         throw new IllegalStateException(var3.getMessage());
      }

      this.prepend(var1);
      this.addToDo(var1);
      return var1;
   }

   public Token addToken(X509Certificate var1, PrivateKey var2) {
      BinarySecurityTokenImpl var3 = new BinarySecurityTokenImpl(var1, var2);
      return this.addToken((Token)var3);
   }

   public Token addToken(UserInfo var1) {
      UsernameTokenImpl var2 = new UsernameTokenImpl(var1.getUsername(), var1.getPassword());
      return this.addToken((Token)var2);
   }

   void append(Object var1) {
      if (var1 instanceof Token) {
         this.tokenMap = null;
      }

      this.elements.add(var1);
   }

   void prepend(Object var1) {
      this.elements.add(0, var1);
   }

   public Iterator getChildren() {
      return this.elements.iterator();
   }

   public Iterator getTimestamps() {
      return new TypeIterator(Timestamp.class, this.elements.iterator());
   }

   public Timestamp addTimestamp() {
      return this.addTimestamp(timestampFac.createTimestamp());
   }

   public Timestamp addTimestamp(long var1) {
      return this.addTimestamp(timestampFac.createTimestamp(var1));
   }

   public Timestamp addTimestamp(Calendar var1) {
      return this.addTimestamp(timestampFac.createTimestamp(var1));
   }

   public Timestamp addTimestamp(Calendar var1, Calendar var2) {
      return this.addTimestamp(timestampFac.createTimestamp(var1, var2));
   }

   private Timestamp addTimestamp(Timestamp var1) {
      this.addToDo(var1);
      this.prepend(var1);
      return var1;
   }

   private void updateReferenes(Token var1) throws SecurityProcessingException {
      Iterator var2 = this.elements.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         KeyInfo var5;
         if (var3 instanceof Signature) {
            Signature var4 = (Signature)var3;

            try {
               var5 = var4.getKeyInfo();
            } catch (KeyInfoValidationException var7) {
               throw new SecurityProcessingException("Unable to update references: " + var7.getMessage(), var7);
            }

            this.updateReferences(var5, var1);
         } else if (var3 instanceof EncryptedKey) {
            EncryptedKey var8 = (EncryptedKey)var3;
            var5 = var8.getKeyInfo();
            this.updateReferences(var5, var1);
         }
      }

   }

   private void updateReferences(KeyInfo var1, Token var2) {
      Iterator var3 = var1.getSecurityTokenReferences();

      while(var3.hasNext()) {
         SecurityTokenReference var4 = (SecurityTokenReference)var3.next();
         if (var4.references(var2)) {
            var4.setReference("#" + var2.getId());
         }
      }

   }

   private Map getTokenMap() {
      if (this.tokenMap != null) {
         return this.tokenMap;
      } else {
         this.tokenMap = new HashMap();
         Iterator var1 = this.getTokens();

         while(var1.hasNext()) {
            Token var2 = (Token)var1.next();
            String var3 = var2.getId();
            if (var3 != null) {
               this.tokenMap.put(var3, var2);
            }
         }

         return this.tokenMap;
      }
   }

   private Iterator getTokens() {
      return new TypeIterator(Token.class, this.elements.iterator());
   }

   public Iterator getUsernameTokens() {
      return new TypeIterator(UsernameToken.class, this.elements.iterator());
   }

   public Iterator getBinarySecurityTokens() {
      return new TypeIterator(BinarySecurityToken.class, this.elements.iterator());
   }

   public Token getTokenById(String var1) {
      return (Token)this.getTokenMap().get(var1);
   }

   public Iterator getSignatures() {
      return new TypeIterator(Signature.class, this.elements.iterator());
   }

   public Iterator getEncryptedKeys() {
      return new TypeIterator(EncryptedKey.class, this.elements.iterator());
   }

   public String getRole() {
      return this.role;
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "Security");
      this.role = StreamUtils.getAttribute(var3, "role");
      XMLEvent var4 = var1.peek();

      while(!var4.isEndElement()) {
         if (!var4.isStartElement()) {
            var1.next();
            var4 = var1.peek();
         } else {
            if (StreamUtils.matches(var4, "BinarySecurityToken", var2)) {
               this.append(new BinarySecurityTokenImpl(var1, var2));
            } else if (StreamUtils.matches(var4, "UsernameToken", var2)) {
               this.append(new UsernameTokenImpl(var1, var2));
            } else if (StreamUtils.matches(var4, "Signature", "http://www.w3.org/2000/09/xmldsig#")) {
               this.append(DSIGReader.read(var1, 8));
            } else if (StreamUtils.matches(var4, "ReferenceList", "http://www.w3.org/2001/04/xmlenc#")) {
               this.append(XMLEncReader.read(var1, 4));
            } else if (StreamUtils.matches(var4, "EncryptedData", "http://www.w3.org/2001/04/xmlenc#")) {
               this.append(XMLEncReader.read(var1, 1));
            } else if (StreamUtils.matches(var4, "EncryptedKey", "http://www.w3.org/2001/04/xmlenc#")) {
               this.append(XMLEncReader.read(var1, 2));
            } else {
               StreamUtils.discard(var1);
            }

            var4 = var1.peek();
         }
      }

      StreamUtils.closeScope(var1, var2, "Security");
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[" + WSSE_URI + "]Security {\n");
      Iterator var2 = this.elements.iterator();

      while(var2.hasNext()) {
         var1.append(var2.next());
      }

      var1.append("\n}");
      return var1.toString();
   }

   List getToDoList() {
      return this.toDoList;
   }

   private void addToDo(Signature var1, SignatureSpec var2) {
      this.toDoList.add(new Operation(var1, var2));
   }

   private void addToDo(EncryptedKey var1, EncryptionSpec var2) {
      this.toDoList.add(new Operation(var1, var2));
   }

   private void addToDo(NamedKey var1, EncryptionSpec var2) {
      this.toDoList.add(new Operation(var1, var2));
   }

   private void addToDo(Token var1) {
      this.toDoList.add(var1);
   }

   private void addToDo(Timestamp var1) {
      this.toDoList.add(var1);
   }

   public boolean expired() {
      return this.expired(-1L);
   }

   public boolean expired(long var1) {
      Iterator var3 = this.getTimestamps();
      long var4 = System.currentTimeMillis();
      long var6 = var4 - var1;
      boolean var8 = true;

      while(var3.hasNext()) {
         Timestamp var9 = (Timestamp)var3.next();
         if (var1 >= 0L) {
            long var10 = var9.getCreated().getTime().getTimeInMillis();
            if (var10 < var6) {
               var8 = false;
               break;
            }
         }

         Expires var13 = var9.getExpires();
         if (var13 != null) {
            long var11 = var13.getTime().getTimeInMillis();
            if (var11 >= var4) {
               var8 = false;
               break;
            }
         }
      }

      return var8;
   }

   static {
      timestampFac = WSUFactory.getInstance(WSUConstants.WSU_URI);
   }

   private static class TypeIterator implements Iterator {
      private final Class clazz;
      private final Iterator source;
      private Object next = null;
      private boolean used = false;

      public TypeIterator(Class var1, Iterator var2) {
         this.clazz = var1;
         this.source = var2;
      }

      public boolean hasNext() {
         if (this.next != null) {
            return true;
         } else {
            this.next = this.findNext();
            return this.next != null;
         }
      }

      private Object findNext() {
         this.used = true;

         Object var1;
         do {
            if (!this.source.hasNext()) {
               return null;
            }

            var1 = this.source.next();
         } while(!this.clazz.isInstance(var1));

         return var1;
      }

      public Object next() {
         Object var1;
         if (this.next != null) {
            var1 = this.next;
            this.next = null;
            return var1;
         } else {
            var1 = this.findNext();
            if (var1 == null) {
               throw new NoSuchElementException();
            } else {
               return var1;
            }
         }
      }

      public void remove() {
         if (!this.used) {
            throw new IllegalStateException("Iterator has not been used");
         } else if (this.next != null) {
            throw new IllegalStateException("This iterator does not support remove() after hasNext()");
         } else {
            this.source.remove();
         }
      }
   }
}
