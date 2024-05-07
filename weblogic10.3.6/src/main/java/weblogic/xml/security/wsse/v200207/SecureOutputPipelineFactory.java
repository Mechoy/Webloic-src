package weblogic.xml.security.wsse.v200207;

import java.util.ArrayList;
import java.util.List;
import weblogic.xml.security.NamedKey;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.encryption.EncryptXMLOutputStream;
import weblogic.xml.security.encryption.EncryptedKey;
import weblogic.xml.security.encryption.EncryptionException;
import weblogic.xml.security.signature.Signature;
import weblogic.xml.security.signature.SoapSignXMLOutputStream;
import weblogic.xml.security.specs.EncryptionSpec;
import weblogic.xml.security.specs.SignatureSpec;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.wsse.Token;
import weblogic.xml.security.wsse.internal.EncryptingPreprocessor;
import weblogic.xml.security.wsse.internal.InsertionOutputStream;
import weblogic.xml.security.wsse.internal.Operation;
import weblogic.xml.security.wsse.internal.SigningPreprocessor;
import weblogic.xml.security.wsse.internal.SpecPreprocessorOutputStream;
import weblogic.xml.security.wsu.Timestamp;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class SecureOutputPipelineFactory {
   public static XMLOutputStream createStream(SecurityImpl var0, XMLOutputStream var1, String var2, String var3) throws SecurityProcessingException {
      List var4 = var0.getToDoList();
      String var5 = var0.getRole();
      XMLOutputStream var6 = var1;
      ArrayList var7 = new ArrayList();

      for(int var8 = var4.size() - 1; var8 >= 0; --var8) {
         Object var9 = var4.get(var8);
         if (var9 instanceof Operation) {
            Operation var10 = (Operation)var9;
            Object var11 = var10.getOperation();
            var7.add(var11);
            if (var11 instanceof Signature) {
               Signature var12 = (Signature)var11;
               SignatureSpec var13 = (SignatureSpec)var10.getSpec();
               var6 = addInsertionStream(var6, var5, var3, var7);
               if (var13 == null) {
                  var6 = addSignatureStream(var6, var12, SignatureSpec.getDefaultSpec());
               } else {
                  var6 = addSignatureStream(var6, var12, var13);
               }
            } else {
               EncryptionSpec var16;
               if (var11 instanceof EncryptedKey) {
                  EncryptedKey var14 = (EncryptedKey)var11;
                  var16 = (EncryptionSpec)var10.getSpec();
                  var6 = addInsertionStream(var6, var5, var3, var7);
                  if (var16 == null) {
                     var6 = addEncryptionStream(var6, var2, var14, EncryptionSpec.getDefaultSpec());
                  } else {
                     var6 = addEncryptionStream(var6, var2, var14, var16);
                  }
               } else if (var11 instanceof NamedKey) {
                  NamedKey var15 = (NamedKey)var11;
                  var16 = (EncryptionSpec)var10.getSpec();
                  var6 = addInsertionStream(var6, var5, var3, var7);
                  if (var16 == null) {
                     var6 = addEncryptionStream(var6, var2, var15, EncryptionSpec.getDefaultSpec());
                  } else {
                     var6 = addEncryptionStream(var6, var2, var15, var16);
                  }
               }
            }
         } else if (var9 instanceof Token) {
            var7.add(var9);
         } else {
            if (!(var9 instanceof Timestamp)) {
               throw new SecurityProcessingException("Unknown type in Security element: " + var9);
            }

            var7.add(var9);
         }

         var4.remove(var9);
      }

      if (!var7.isEmpty()) {
         var6 = addInsertionStream(var6, var5, var3, var7);
      }

      return var6;
   }

   private static XMLOutputStream addInsertionStream(XMLOutputStream var0, String var1, String var2, List var3) {
      Object[] var4 = var3.toArray();
      var3.clear();
      NamespaceAwareXOS var6;
      if (var0 instanceof NamespaceAwareXOS) {
         var6 = (NamespaceAwareXOS)var0;
      } else {
         var6 = new NamespaceAwareXOS(var0);
      }

      InsertionOutputStream var5 = new InsertionOutputStream(var1, var2, var4, var6);
      return var5;
   }

   private static XMLOutputStream addEncryptionStream(XMLOutputStream var0, String var1, EncryptedKey var2, EncryptionSpec var3) throws SecurityProcessingException {
      EncryptXMLOutputStream var4 = new EncryptXMLOutputStream(var0, var1);

      EncryptingPreprocessor var5;
      try {
         var5 = new EncryptingPreprocessor(var2, var3.getEncryptionMethod(), var4);
      } catch (EncryptionException var7) {
         throw new SecurityProcessingException("Unable to encrypt data using EncryptionMethod " + var3.getEncryptionMethod(), var7);
      }

      SpecPreprocessorOutputStream var6 = new SpecPreprocessorOutputStream(var4, var3, var5);
      return var6;
   }

   private static XMLOutputStream addEncryptionStream(XMLOutputStream var0, String var1, NamedKey var2, EncryptionSpec var3) throws SecurityProcessingException {
      EncryptXMLOutputStream var4 = new EncryptXMLOutputStream(var0, var1);

      EncryptingPreprocessor var5;
      try {
         var5 = new EncryptingPreprocessor(var2, var3.getEncryptionMethod(), var4);
      } catch (EncryptionException var7) {
         throw new SecurityProcessingException("Unable to encrypt data using EncryptionMethod " + var3.getEncryptionMethod(), var7);
      }

      SpecPreprocessorOutputStream var6 = new SpecPreprocessorOutputStream(var4, var3, var5);
      return var6;
   }

   private static XMLOutputStream addSignatureStream(XMLOutputStream var0, Signature var1, SignatureSpec var2) throws SecurityProcessingException {
      SoapSignXMLOutputStream var3 = new SoapSignXMLOutputStream(var0);

      try {
         var3.addSignature(var1);
      } catch (XMLStreamException var6) {
         throw new SecurityProcessingException(var6);
      }

      SigningPreprocessor var4 = new SigningPreprocessor(var1, var2.getCanonicalizationMethod(), var3);
      SpecPreprocessorOutputStream var5 = new SpecPreprocessorOutputStream(var3, var2, var4);
      return var5;
   }
}
