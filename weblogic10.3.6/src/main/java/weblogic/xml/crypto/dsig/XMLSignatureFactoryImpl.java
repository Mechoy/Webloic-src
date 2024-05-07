package weblogic.xml.crypto.dsig;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import weblogic.xml.crypto.URIDereferencerBase;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Manifest;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignatureMethod;
import weblogic.xml.crypto.dsig.api.SignatureProperties;
import weblogic.xml.crypto.dsig.api.SignatureProperty;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLObject;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.dsig.api.XMLValidateContext;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfoFactory;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.SignatureMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;
import weblogic.xml.crypto.dsig.keyinfo.KeyInfoFactoryImpl;

public class XMLSignatureFactoryImpl extends XMLSignatureFactory {
   private KeyInfoFactory keyInfoFactory = new KeyInfoFactoryImpl();
   private URIDereferencer defaultURIDereferencer = new URIDereferencerBase();

   public XMLSignatureFactoryImpl() {
      super((String)null, (Provider)null);
   }

   public KeyInfoFactory getKeyInfoFactory() {
      return this.keyInfoFactory;
   }

   public URIDereferencer getURIDereferencer() {
      return this.defaultURIDereferencer;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public CanonicalizationMethod newCanonicalizationMethod(String var1, C14NMethodParameterSpec var2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
      return CanonicalizationMethodImpl.newCanonicalizationMethod(var1, var2);
   }

   public DigestMethod newDigestMethod(String var1, DigestMethodParameterSpec var2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
      return DigestMethodImpl.newDigestMethod(var1, var2);
   }

   public Manifest newManifest(List var1) {
      return null;
   }

   public Manifest newManifest(List var1, String var2) {
      return null;
   }

   public Reference newReference(String var1, DigestMethod var2) {
      return new ReferenceImpl(var1, var2);
   }

   public Reference newReference(String var1, DigestMethod var2, List var3, String var4, String var5) {
      return new ReferenceImpl(var1, var2, this.copyList(var3), var4, var5);
   }

   public SignatureMethod newSignatureMethod(String var1, SignatureMethodParameterSpec var2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
      return SignatureMethodImpl.newSignatureMethod(var1, var2);
   }

   public SignatureProperties newSignatureProperties(List var1, String var2) {
      return null;
   }

   public SignatureProperty newSignatureProperty(List var1, String var2, String var3) {
      return null;
   }

   public SignedInfo newSignedInfo(CanonicalizationMethod var1, SignatureMethod var2, List var3) {
      return new SignedInfoImpl(var1, var2, this.copyList(var3));
   }

   public SignedInfo newSignedInfo(CanonicalizationMethod var1, SignatureMethod var2, List var3, String var4) {
      return new SignedInfoImpl(var1, var2, this.copyList(var3), var4);
   }

   public Transform newTransform(String var1, TransformParameterSpec var2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
      return TransformImpl.newTransform(var1, var2);
   }

   public XMLObject newXMLObject(List var1, String var2, String var3, String var4) {
      return new XMLObjectImpl(this.copyList(var1), var2, var3, var4);
   }

   public XMLSignature newXMLSignature(SignedInfo var1, KeyInfo var2) {
      return new XMLSignatureImpl(var1, var2);
   }

   public XMLSignature newXMLSignature(SignedInfo var1, KeyInfo var2, List var3, String var4, String var5) {
      return new XMLSignatureImpl(var1, var2, this.copyList(var3), var4, var5);
   }

   public XMLSignature unmarshalXMLSignature(XMLValidateContext var1) throws MarshalException {
      return new XMLSignatureImpl(var1);
   }

   private List copyList(List var1) {
      Iterator var2 = var1.iterator();

      Object var3;
      do {
         if (!var2.hasNext()) {
            return Collections.unmodifiableList(var1);
         }

         var3 = var2.next();
      } while(var3 != null);

      throw new NullPointerException();
   }
}
