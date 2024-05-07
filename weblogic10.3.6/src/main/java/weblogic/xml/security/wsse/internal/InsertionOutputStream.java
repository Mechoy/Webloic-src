package weblogic.xml.security.wsse.internal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.Iterator;
import java.util.Map;
import weblogic.utils.collections.Stack;
import weblogic.xml.schema.binding.util.StdNamespace;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.encryption.EncryptedKey;
import weblogic.xml.security.encryption.ReferenceList;
import weblogic.xml.security.keyinfo.KeyInfo;
import weblogic.xml.security.keyinfo.KeyInfoValidationException;
import weblogic.xml.security.signature.Signature;
import weblogic.xml.security.signature.SoapNamespaceHelper;
import weblogic.xml.security.signature.XMLSignatureException;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.XMLEventBuffer;
import weblogic.xml.security.utils.XMLOutputStreamBase;
import weblogic.xml.security.wsse.BinarySecurityToken;
import weblogic.xml.security.wsse.SecurityTokenReference;
import weblogic.xml.security.wsse.UsernameToken;
import weblogic.xml.security.wsse.v200207.UsernameTokenImpl;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.security.wsu.Timestamp;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class InsertionOutputStream extends XMLOutputStreamBase {
   private static final int PRE_INSERT = -1;
   private static final int BUFFERING = 0;
   private static final int POST_INSERT = 1;
   private static final String SOAP_HEADER = "Header";
   private static final String SOAP_BODY = "Body";
   private static final String XSD_TRUE = "1";
   private final Object[] children;
   private final String soapNS;
   private String role;
   private Stack stack;
   private int state;
   private XMLEventBuffer buffer;
   private boolean debug = false;

   public InsertionOutputStream(String var1, String var2, Object[] var3, XMLOutputStream var4) {
      super(var4);
      if (var2 == null) {
         throw new IllegalArgumentException("Cannot set soapNS to null");
      } else {
         this.state = -1;
         this.soapNS = var2;
         this.role = var1;
         this.children = var3;
         this.stack = new Stack();
         this.buffer = null;
      }
   }

   public void close(boolean var1) throws XMLStreamException {
      switch (this.state) {
         case -1:
            throw new AssertionError("never found place to insert security");
         case 0:
            this.insertXML();

            while(this.buffer.hasNext()) {
               this.dest.add(this.buffer.next());
            }

            this.buffer = null;
            this.state = 1;
         case 1:
         default:
            super.close(var1);
      }
   }

   public void flush() {
   }

   protected void addXMLEvent(XMLEvent var1) throws XMLStreamException {
      if (this.debug) {
         System.out.println("ios - got " + var1);
      }

      switch (this.state) {
         case -1:
            switch (var1.getType()) {
               case 2:
                  StartElement var2 = (StartElement)var1;
                  if (this.inHeader(this.stack)) {
                     if (this.matchingSecurity(var2, this.role)) {
                        this.write(var2);
                        this.insertChildren();
                     } else {
                        this.stack.push(var2);
                        this.write(var2);
                     }

                     return;
                  } else {
                     if (this.startBody(var2)) {
                        this.insertHeader();
                        this.write(var2);
                     } else {
                        this.stack.push(var2);
                        this.write(var2);
                     }

                     return;
                  }
               case 4:
                  EndElement var3 = (EndElement)var1;
                  if (this.inHeader(this.stack) && this.endHeader(var3)) {
                     this.insertElement(this.getSoapPrefix(this.stack));
                     this.write(var3);
                  } else {
                     this.stack.pop();
                     this.write(var3);
                  }

                  return;
               default:
                  this.write(var1);
                  return;
            }
         case 0:
         case 1:
            this.write(var1);
      }

   }

   private String getSoapPrefix(Stack var1) {
      Iterator var2 = var1.iterator();

      XMLName var3;
      String var4;
      do {
         if (!var2.hasNext()) {
            throw new AssertionError("Unable to discover SOAP Prefix from message");
         }

         var3 = ((StartElement)var2.next()).getName();
         var4 = var3.getNamespaceUri();
      } while(!this.soapNS.equals(var4));

      return var3.getPrefix();
   }

   private void write(XMLEvent var1) throws XMLStreamException {
      if (this.state == 0) {
         this.buffer.add(var1);
      } else {
         this.dest.add(var1);
      }

   }

   private void insertHeader() throws XMLStreamException {
      String var1 = this.getSoapPrefix(this.stack);
      XMLName var2 = ElementFactory.createXMLName(this.soapNS, "Header", var1);
      this.dest.add(ElementFactory.createStartElement(var2));
      this.insertElement(var1);
      this.buffer.add(ElementFactory.createEndElement(var2));
   }

   private void insertElement(String var1) throws XMLStreamException {
      this.dest.add(ElementFactory.createStartElement(ElementFactory.createXMLName(WSSEConstants.WSSE_URI, "Security")));
      XMLName var2 = ElementFactory.createXMLName((String)null, "mustUnderstand", var1);
      this.dest.add(ElementFactory.createAttribute(var2, "1"));
      if (this.role != null) {
         XMLName var3 = ElementFactory.createXMLName((String)null, "role", var1);
         this.dest.add(ElementFactory.createAttribute(var3, this.role));
      }

      this.insertChildren();
      this.buffer.add(ElementFactory.createEndElement(WSSEConstants.WSSE_URI, "Security"));
   }

   private void insertChildren() {
      this.buffer = new XMLEventBuffer();
      this.state = 0;
   }

   private void insertXML() throws XMLStreamException {
      for(int var1 = 0; var1 < this.children.length; ++var1) {
         Object var2 = this.children[var1];
         if (var2 instanceof BinarySecurityToken) {
            ((BinarySecurityToken)var2).toXML(this.dest);
         } else if (var2 instanceof UsernameToken) {
            ((UsernameToken)var2).toXML(this.dest);
         } else if (var2 instanceof Signature) {
            Signature var3 = (Signature)var2;
            if (!this.emptySignature(var3)) {
               this.finish(var3, this.dest.getNamespaces());
               ((Signature)var2).toXML(this.dest, "http://www.w3.org/2000/09/xmldsig#", 0);
            }
         } else if (var2 instanceof EncryptedKey) {
            EncryptedKey var4 = (EncryptedKey)var2;
            if (!this.unusedKey(var4)) {
               this.finish(var4);
               var4.toXML(this.dest, "http://www.w3.org/2001/04/xmlenc#", 0);
            }
         } else if (var2 instanceof ReferenceList) {
            ((ReferenceList)var2).toXML(this.dest, "http://www.w3.org/2001/04/xmlenc#", 0);
         } else {
            if (!(var2 instanceof Timestamp)) {
               throw new AssertionError("unsupported child type for Security: " + var2);
            }

            Timestamp var5 = (Timestamp)var2;
            var5.toXML((XMLOutputStream)this.dest);
         }
      }

      this.state = 1;
   }

   private boolean startBody(StartElement var1) {
      XMLName var2 = var1.getName();
      String var3 = var2.getLocalName();
      if (!"Body".equals(var3)) {
         return false;
      } else {
         String var4 = var2.getNamespaceUri();
         return var4 == null || var4.equals(this.soapNS);
      }
   }

   private boolean matchingSecurity(StartElement var1, String var2) {
      XMLName var3 = var1.getName();
      String var4 = var3.getNamespaceUri();
      String var5 = var3.getLocalName();
      if (!WSSEConstants.WSSE_URI.equals(var4)) {
         return false;
      } else if (!"Security".equals(var5)) {
         return false;
      } else {
         String var6 = StreamUtils.getAttribute(var1, "role");
         if (var6 == null) {
            if (var2 != null) {
               return false;
            }
         } else if (!var6.equals(var2)) {
            return false;
         }

         return true;
      }
   }

   private boolean endHeader(EndElement var1) {
      XMLName var2 = var1.getName();
      return this.isSOAPHeader(var2);
   }

   private boolean inHeader(Stack var1) {
      if (var1.isEmpty()) {
         return false;
      } else {
         StartElement var2 = (StartElement)var1.peek();
         XMLName var3 = var2.getName();
         return this.isSOAPHeader(var3);
      }
   }

   private boolean isSOAPHeader(XMLName var1) {
      String var2 = var1.getNamespaceUri();
      if (var2 != null && var2.equals(this.soapNS)) {
         String var3 = var1.getLocalName();
         return "Header".equals(var3);
      } else {
         return false;
      }
   }

   private boolean unusedKey(EncryptedKey var1) {
      ReferenceList var2 = var1.getReferenceList();
      return var2 != null && !var2.getReferences().hasNext();
   }

   private boolean emptySignature(Signature var1) {
      return !var1.getReferences().hasNext();
   }

   private void finish(Signature var1, Map var2) throws SecurityProcessingException {
      SoapNamespaceHelper.setNamespace(var1, var2);

      KeyInfo var3;
      try {
         var3 = var1.getKeyInfo();
      } catch (KeyInfoValidationException var8) {
         throw new SecurityProcessingException("Validation error retrieving key info for signature", var8);
      }

      Object var4 = null;
      Iterator var5 = var3.getSecurityTokenReferences();

      while(var5.hasNext()) {
         SecurityTokenReference var6 = (SecurityTokenReference)var5.next();
         var4 = var6.getPrivateKey();
         if (var4 != null) {
            break;
         }

         var4 = var6.getSecretKey();
         if (var4 != null) {
            break;
         }
      }

      if (var4 == null) {
         throw new SecurityProcessingException("unable to retrieve private key for signing");
      } else {
         try {
            var1.sign((Key)var4);
         } catch (XMLSignatureException var7) {
            throw new SecurityProcessingException("Unable to complete signature " + var1, var7);
         }
      }
   }

   private void finish(EncryptedKey var1) {
   }

   public static void main(String[] var0) throws FileNotFoundException, XMLStreamException {
      XMLInputStream var1 = null;
      XMLOutputStream var2 = null;
      if (var0.length < 1) {
         System.out.println("InsertionOutputStream <input> [<output>]");
      } else {
         FileInputStream var3 = new FileInputStream(var0[0]);
         var1 = XMLInputStreamFactory.newInstance().newInputStream(var3);
         if (var0.length == 3) {
            var2 = XMLOutputStreamFactory.newInstance().newOutputStream(new FileOutputStream(var0[1]));
         } else {
            var2 = XMLOutputStreamFactory.newInstance().newOutputStream(System.out);
         }

         Object[] var5 = new Object[]{new UsernameTokenImpl("username", "password")};
         String var4 = StdNamespace.instance().soapEnvelope();
         InsertionOutputStream var6 = new InsertionOutputStream((String)null, var4, var5, var2);
         var6 = new InsertionOutputStream((String)null, var4, var5, var6);
         var6.add(var1);
         var6.close(true);
      }
   }
}
