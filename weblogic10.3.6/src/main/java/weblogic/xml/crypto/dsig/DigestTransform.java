package weblogic.xml.crypto.dsig;

import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.XMLCryptoContext;

public class DigestTransform extends TransformImpl {
   private OutputStream os;
   private final MessageDigest md;

   public DigestTransform(OutputStream var1, MessageDigest var2) {
      this.os = var1;
      this.md = var2;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public String getAlgorithm() {
      return null;
   }

   public AlgorithmParameterSpec getParameterSpec() {
      return null;
   }

   public Data transform(Data var1, XMLCryptoContext var2) {
      return var1;
   }

   public OutputStream getOutputStream() {
      return new DigestOutputStream(this.os, this.md);
   }

   public String getURI() {
      return "Digest";
   }
}
