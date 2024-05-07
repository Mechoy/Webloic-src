package weblogic.xml.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.xml.crypto.dom.WLDOMSignContextImpl;
import weblogic.xml.crypto.dom.WLDOMValidateContextImpl;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfoFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyValue;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.SignatureMethodParameterSpec;
import weblogic.xml.dom.Util;

public class Test {
   public static void main(String[] var0) {
      System.setProperty("weblogic.xml.crypto.dsig.api.XMLSignatureFactory", "weblogic.xml.crypto.dsig.XMLSignatureFactoryImpl");

      try {
         testCreateXMLDetachedSignature();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void testSignatureFactory() {
      try {
         XMLSignatureFactory var0 = XMLSignatureFactory.getInstance();
         System.out.println(var0);
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }

   public static void testCreateXMLDetachedSignature() throws Exception {
      XMLSignatureFactory var0 = XMLSignatureFactory.getInstance();
      Reference var1 = var0.newReference("http://www.w3.org/TR/xml-stylesheet", var0.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", (DigestMethodParameterSpec)null));
      SignedInfo var2 = var0.newSignedInfo(var0.newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments", (C14NMethodParameterSpec)null), var0.newSignatureMethod("http://www.w3.org/2000/09/xmldsig#dsa-sha1", (SignatureMethodParameterSpec)null), Collections.singletonList(var1));
      KeyPairGenerator var3 = KeyPairGenerator.getInstance("DSA");
      KeyPair var4 = var3.generateKeyPair();
      KeyInfoFactory var5 = var0.getKeyInfoFactory();
      KeyValue var6 = var5.newKeyValue(var4.getPublic());
      KeyInfo var7 = var5.newKeyInfo(Collections.singletonList(var6));
      XMLSignature var8 = var0.newXMLSignature(var2, var7);
      Document var9 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      Element var10 = var9.createElementNS("http://foo.com", "foo:root");
      var10.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "http://foo.com");
      var9.appendChild(var10);
      WLDOMSignContextImpl var11 = new WLDOMSignContextImpl(var4.getPrivate(), var10);
      NodeURIDereferencer var12 = new NodeURIDereferencer(var9);
      var11.setURIDereferencer(var12);
      System.out.println("signing...");
      var8.sign(var11);
      System.out.println("done");
      String var13 = Util.printNode(var9);
      System.out.println(var13);
      WLDOMValidateContextImpl var14 = new WLDOMValidateContextImpl(var4.getPublic(), var10.getFirstChild());
      var14.setURIDereferencer(var12);
      System.out.println("unmarshalling...");
      var8 = var0.unmarshalXMLSignature(var14);
      System.out.println("done");
      System.out.println("validating...");
      boolean var15 = var8.validate(var14);
      System.out.println("Signature core validation result: " + var15);
      Iterator var16 = var8.getSignedInfo().getReferences().iterator();

      for(int var17 = 0; var16.hasNext(); ++var17) {
         boolean var18 = ((Reference)var16.next()).validate(var14).status();
         System.out.println("ref[" + var17 + "] validity status: " + var18);
      }

      System.out.println("done");
   }
}
