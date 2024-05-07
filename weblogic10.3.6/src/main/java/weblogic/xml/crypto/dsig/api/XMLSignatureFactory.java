package weblogic.xml.crypto.dsig.api;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.util.List;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfoFactory;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.SignatureMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;

public abstract class XMLSignatureFactory {
   private String mechanismType;
   private Provider provider;
   private static final String defaultFactory = "weblogic.xml.crypto.dsig.XMLSignatureFactoryImpl";

   protected XMLSignatureFactory(String var1, Provider var2) {
      this.mechanismType = var1;
      this.provider = var2;
   }

   public static XMLSignatureFactory getInstance() throws XMLSignatureException {
      return getSignatureFactory("weblogic.xml.crypto.dsig.XMLSignatureFactoryImpl");
   }

   public static XMLSignatureFactory getInstance(String var0) throws XMLSignatureException {
      return getSignatureFactory("weblogic.xml.crypto.dsig.XMLSignatureFactoryImpl");
   }

   public static XMLSignatureFactory getInstance(String var0, Provider var1) throws XMLSignatureException, NoSuchProviderException {
      return getSignatureFactory("weblogic.xml.crypto.dsig.XMLSignatureFactoryImpl");
   }

   public static XMLSignatureFactory getInstance(String var0, String var1) throws XMLSignatureException {
      return getSignatureFactory("weblogic.xml.crypto.dsig.XMLSignatureFactoryImpl");
   }

   private static XMLSignatureFactory getSignatureFactory(String var0) throws XMLSignatureException {
      try {
         return (XMLSignatureFactory)Class.forName(var0).newInstance();
      } catch (Exception var2) {
         throw new XMLSignatureException("can't create signature factory " + var0, var2);
      }
   }

   public String getMechanismType() {
      return this.mechanismType;
   }

   public Provider getProvider() {
      return this.provider;
   }

   public abstract KeyInfoFactory getKeyInfoFactory();

   public abstract URIDereferencer getURIDereferencer();

   public abstract boolean isFeatureSupported(String var1);

   public abstract CanonicalizationMethod newCanonicalizationMethod(String var1, C14NMethodParameterSpec var2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

   public abstract DigestMethod newDigestMethod(String var1, DigestMethodParameterSpec var2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

   public abstract Manifest newManifest(List var1);

   public abstract Manifest newManifest(List var1, String var2);

   public abstract Reference newReference(String var1, DigestMethod var2);

   public abstract Reference newReference(String var1, DigestMethod var2, List var3, String var4, String var5);

   public abstract SignatureMethod newSignatureMethod(String var1, SignatureMethodParameterSpec var2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

   public abstract SignatureProperties newSignatureProperties(List var1, String var2);

   public abstract SignatureProperty newSignatureProperty(List var1, String var2, String var3);

   public abstract SignedInfo newSignedInfo(CanonicalizationMethod var1, SignatureMethod var2, List var3);

   public abstract SignedInfo newSignedInfo(CanonicalizationMethod var1, SignatureMethod var2, List var3, String var4);

   public abstract Transform newTransform(String var1, TransformParameterSpec var2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

   public abstract XMLObject newXMLObject(List var1, String var2, String var3, String var4);

   public abstract XMLSignature newXMLSignature(SignedInfo var1, KeyInfo var2);

   public abstract XMLSignature newXMLSignature(SignedInfo var1, KeyInfo var2, List var3, String var4, String var5);

   public abstract XMLSignature unmarshalXMLSignature(XMLValidateContext var1) throws MarshalException;
}
