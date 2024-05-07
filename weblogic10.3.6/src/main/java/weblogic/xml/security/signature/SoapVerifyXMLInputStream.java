package weblogic.xml.security.signature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.xml.security.keyinfo.KeyResolver;
import weblogic.xml.security.transforms.ExcC14NTransform;
import weblogic.xml.security.utils.XMLInputStreamBase;
import weblogic.xml.security.utils.XMLStreamObserver;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class SoapVerifyXMLInputStream extends XMLInputStreamBase implements DSIGConstants {
   private final List signatures;
   private KeyResolver resolver;
   private XMLStreamObserver unresolvedReferences;

   private SoapVerifyXMLInputStream(XMLInputStream var1, KeyResolver var2) {
      super(var1);
      this.signatures = new ArrayList(1);
      this.unresolvedReferences = new XMLStreamObserver(WSSEConstants.ID_NAMESPACES);
      this.resolver = var2 != null ? var2 : new KeyResolver();
   }

   /** @deprecated */
   public SoapVerifyXMLInputStream(Signature var1, XMLInputStream var2) throws XMLStreamException {
      this((Signature)var1, var2, (KeyResolver)null);
   }

   public SoapVerifyXMLInputStream(Signature var1, XMLInputStream var2, KeyResolver var3) throws XMLSignatureException {
      this(var2, var3);
      this.addSignature(var1);
   }

   public SoapVerifyXMLInputStream(Signature[] var1, XMLInputStream var2, KeyResolver var3) throws XMLStreamException {
      this(var2, var3);

      for(int var4 = 0; var4 < var1.length; ++var4) {
         Signature var5 = var1[var4];
         this.addSignature(var5);
      }

   }

   /** @deprecated */
   public SoapVerifyXMLInputStream(Signature[] var1, XMLInputStream var2) throws XMLStreamException {
      this((Signature[])var1, var2, (KeyResolver)null);
   }

   public void addSignature(Signature var1) throws XMLSignatureException {
      if (this.resolver == null) {
         throw new IllegalStateException("No KeyResolver: cannot resolve key to validate signature");
      } else {
         var1.validateSignature(this.resolver);
         this.setupSignature(var1);
      }
   }

   private void setupSignature(Signature var1) {
      this.signatures.add(var1);
      Iterator var2 = var1.getReferences();

      while(var2.hasNext()) {
         Reference var3 = (Reference)var2.next();
         if (var3 instanceof InternalReference) {
            InternalReference var4 = (InternalReference)var3;
            String var5 = var4.getURI();
            this.unresolvedReferences.add((String)var5, var4);
            if (WSSEConstants.C14N_INCLUSIVE_NAMESPACES) {
               ExcC14NTransform.setupVerify(this.unresolvedReferences, var5, var4);
            }
         }
      }

   }

   public XMLEvent next() throws XMLStreamException {
      XMLEvent var1 = this.source.next();
      this.unresolvedReferences.observe(var1);
      return var1;
   }
}
