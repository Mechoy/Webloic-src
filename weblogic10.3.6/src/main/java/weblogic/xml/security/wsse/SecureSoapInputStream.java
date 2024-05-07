package weblogic.xml.security.wsse;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import weblogic.xml.security.SecurityAssertion;
import weblogic.xml.security.UserInfo;
import weblogic.xml.security.assertion.ElementConfidentialityAssertion;
import weblogic.xml.security.assertion.ElementIntegrityAssertion;
import weblogic.xml.security.assertion.IdentityAssertion;
import weblogic.xml.security.assertion.IntegrityAssertion;
import weblogic.xml.security.keyinfo.KeyResolver;
import weblogic.xml.security.keyinfo.KeyResult;
import weblogic.xml.security.keyinfo.KeypairProvider;
import weblogic.xml.security.keyinfo.X509KeyResult;
import weblogic.xml.security.signature.Reference;
import weblogic.xml.security.signature.Signature;
import weblogic.xml.security.signature.XMLSignatureException;
import weblogic.xml.security.utils.ObservedXMLInputStream;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.security.utils.XMLInputStreamBase;
import weblogic.xml.security.wsse.internal.MappingObserver;
import weblogic.xml.security.wsse.v200207.SecureInputStreamInternal;
import weblogic.xml.stream.BufferedXMLInputStream;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class SecureSoapInputStream extends XMLInputStreamBase {
   private final MappingObserver beforeMapper;
   private final MappingObserver afterMapper;
   private final SecureInputStream delegate;
   private final String role;
   private SecurityAssertion[] assertions;
   private boolean processingComplete;

   /** @deprecated */
   public SecureSoapInputStream(XMLInputStream var1, String var2, PrivateKey var3) throws XMLStreamException {
      this(var1, var2, new KeyResolver());
      if (var3 != null) {
         this.delegate.getKeyResolver().addKeyProvider(new KeypairProvider((PublicKey)null, var3, (String)null, (byte[])null, (String)null));
      }

   }

   public SecureSoapInputStream(XMLInputStream var1, String var2, KeyResolver var3) throws XMLStreamException {
      this.processingComplete = false;
      this.role = var2;
      this.beforeMapper = new MappingObserver();
      ObservedXMLInputStream var4 = new ObservedXMLInputStream(var1, this.beforeMapper);
      SecureInputStream var5 = this.delegate = new SecureInputStreamInternal(var4, var2, var3);
      this.afterMapper = new MappingObserver();
      var4 = new ObservedXMLInputStream(var5, this.afterMapper);
      this.source = var4;
   }

   public SecurityAssertion[] getSecurityAssertions() throws XMLStreamException {
      if (this.assertions != null) {
         return this.assertions;
      } else {
         if (!this.processingComplete) {
            this.bufferRemaining();
         }

         Security var1 = this.delegate.getSecurityElement();
         if (var1 == null) {
            return new SecurityAssertion[0];
         } else if (this.afterMapper.duplicateIds()) {
            throw new SOAPFaultException(Utils.getQName(weblogic.xml.security.wsse.v200207.WSSEConstants.QNAME_FAULT_FAILEDCHECK), "Invalid message: duplicate IDs found", this.role, (Detail)null);
         } else {
            ArrayList var2 = new ArrayList();
            this.addUsernameAssertions(var2);
            this.addSignatureAssertions(var2);
            this.addEncryptionAssertions(var2);
            this.assertions = new SecurityAssertion[var2.size()];
            var2.toArray(this.assertions);
            return this.assertions;
         }
      }
   }

   public KeyResolver getKeyResolver() {
      return this.delegate.getKeyResolver();
   }

   public void setKeyResolver(KeyResolver var1) {
      this.delegate.setKeyResolver(var1);
   }

   private void addUsernameAssertions(List var1) {
      Security var2 = this.delegate.getSecurityElement();
      Iterator var3 = var2.getUsernameTokens();

      while(var3.hasNext()) {
         UsernameToken var4 = (UsernameToken)var3.next();
         UserInfo var5 = var4.getUserInfo();
         var1.add(new IdentityAssertion(var5));
      }

   }

   private void addSignatureAssertions(List var1) {
      Security var2 = this.delegate.getSecurityElement();
      Iterator var3 = var2.getSignatures();

      while(var3.hasNext()) {
         Signature var4 = (Signature)var3.next();

         try {
            var4.validateReferences();
            String var5 = var4.getSignatureMethod();
            KeyResult var6 = var4.getValidatingKey();
            X509Certificate var7;
            if (var6 instanceof X509KeyResult) {
               var7 = ((X509KeyResult)var6).getCertificate();
            } else {
               var7 = null;
            }

            HashMap var8 = new HashMap();
            HashSet var9 = new HashSet();
            Iterator var10 = var4.getReferences();

            while(var10.hasNext()) {
               Reference var11 = (Reference)var10.next();
               if (weblogic.xml.security.wsse.internal.Utils.validReference(var11)) {
                  String var12 = var11.getURI();
                  String var13 = var12.substring(var12.indexOf(35) + 1);
                  var1.add(new IntegrityAssertion(var5, var13, var7));
                  XMLName var14 = this.afterMapper.getElementById(var13);
                  if (var14 == null) {
                     var14 = this.beforeMapper.getElementById(var13);
                  }

                  if (var14 != null) {
                     weblogic.xml.security.wsse.internal.Utils.addElement(var8, var14, var13);
                     var9.add(var14);
                  }
               } else {
                  System.out.println("Ignoring " + var11 + " because it contains " + "an unacceptable transform");
               }
            }

            var10 = var9.iterator();

            while(var10.hasNext()) {
               XMLName var16 = (XMLName)var10.next();
               SortedSet var17 = (SortedSet)var8.get(var16);
               if (weblogic.xml.security.wsse.internal.Utils.equivalent(var17, this.afterMapper.getElementIds(var16))) {
                  var1.add(new ElementIntegrityAssertion(var5, var7, var16, (String)null));
                  SortedSet var18 = this.afterMapper.getElementBodyIds(var16);
                  if (var18 != null && !var18.isEmpty()) {
                     var1.add(new ElementIntegrityAssertion(var5, var7, var16, "body"));
                  }

                  var18 = this.afterMapper.getElementHeaderIds(var16);
                  if (var18 != null && !var18.isEmpty()) {
                     var1.add(new ElementIntegrityAssertion(var5, var7, var16, "header"));
                  }
               } else {
                  if (weblogic.xml.security.wsse.internal.Utils.isSuperset(var17, this.afterMapper.getElementHeaderIds(var16))) {
                     var1.add(new ElementIntegrityAssertion(var5, var7, var16, "header"));
                  }

                  if (weblogic.xml.security.wsse.internal.Utils.isSuperset(var17, this.afterMapper.getElementBodyIds(var16))) {
                     var1.add(new ElementIntegrityAssertion(var5, var7, var16, "body"));
                  }
               }
            }
         } catch (XMLSignatureException var15) {
            weblogic.xml.security.wsse.internal.Utils.handleException(var15, this.role);
         }
      }

   }

   private void addEncryptionAssertions(List var1) {
      Set var2 = this.beforeMapper.getHeaderElements().keySet();
      Set var3 = this.afterMapper.getHeaderElements().keySet();
      this.createEncryptionAssertions(weblogic.xml.security.wsse.internal.Utils.diffTypes(var2, var3), "header", var1);
      var2 = this.beforeMapper.getBodyElements().keySet();
      var3 = this.afterMapper.getBodyElements().keySet();
      this.createEncryptionAssertions(weblogic.xml.security.wsse.internal.Utils.diffTypes(var2, var3), "body", var1);
      var2 = this.beforeMapper.getElements().keySet();
      var3 = this.afterMapper.getElements().keySet();
      this.createEncryptionAssertions(weblogic.xml.security.wsse.internal.Utils.diffTypes(var2, var3), (String)null, var1);
   }

   private void createEncryptionAssertions(Set var1, String var2, List var3) {
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         XMLName var5 = (XMLName)var4.next();
         var3.add(new ElementConfidentialityAssertion(var5.getLocalName(), var5.getNamespaceUri(), var2));
      }

   }

   private void bufferRemaining() throws XMLStreamException {
      if (this.hasNext() && !this.processingComplete) {
         BufferedXMLInputStream var1;
         if (!(this.source instanceof BufferedXMLInputStream)) {
            this.source = var1 = factory.newBufferedInputStream(this.source);
         } else {
            var1 = (BufferedXMLInputStream)this.source;
         }

         var1.mark();

         while(this.hasNext()) {
            this.next();
         }

         var1.reset();
         this.processingComplete = true;
      }

   }

   public Set getBodyElementNames() throws XMLStreamException {
      if (!this.processingComplete) {
         this.bufferRemaining();
      }

      return this.afterMapper.getBodyElements().keySet();
   }

   public Set getHeaderElementNames() throws XMLStreamException {
      if (!this.processingComplete) {
         this.bufferRemaining();
      }

      return this.afterMapper.getHeaderElements().keySet();
   }

   public Set getAllElementNames() throws XMLStreamException {
      if (!this.processingComplete) {
         this.bufferRemaining();
      }

      return this.afterMapper.getElements().keySet();
   }

   public Security getSecurityElement() {
      Security var1 = this.delegate.getSecurityElement();
      return var1;
   }

   /** @deprecated */
   public Throwable getCreationException() {
      return null;
   }

   public XMLEvent next() throws XMLStreamException {
      return this.source.next();
   }
}
