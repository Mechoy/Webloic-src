package weblogic.wsee.security.wst.internal;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.spec.SecretKeySpec;
import weblogic.wsee.security.wst.binding.BinarySecret;
import weblogic.wsee.security.wst.binding.ComputedKey;
import weblogic.wsee.security.wst.binding.Entropy;
import weblogic.wsee.security.wst.binding.RequestedProofToken;
import weblogic.wsee.security.wst.faults.BadRequestException;
import weblogic.wsee.security.wst.faults.InvalidRequestException;
import weblogic.wsee.security.wst.faults.RequestFailedException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.helpers.BindingHelper;
import weblogic.xml.crypto.utils.KeyUtils;

public class EntropyHandler {
   private WSTContext wstContext;
   private Entropy reqEntropy;
   private Entropy resEntropy;
   private RequestedProofToken rpt;
   private Key key;

   public EntropyHandler(WSTContext var1, Entropy var2) throws WSTFaultException {
      this.wstContext = var1;
      this.reqEntropy = var2;

      try {
         this.init();
      } catch (NoSuchAlgorithmException var4) {
         throw new RequestFailedException("Unable to generate key for " + var1.getSymmetricKeyAlgorithm() + " in size " + var1.getKeySize());
      }
   }

   public EntropyHandler(WSTContext var1, Key var2) throws WSTFaultException {
      this.wstContext = var1;
      this.reqEntropy = null;
      this.key = var2;

      try {
         this.handleNoReqEntropy(var2);
      } catch (NoSuchAlgorithmException var4) {
         throw new RequestFailedException("Unable to generate key for " + var1.getSymmetricKeyAlgorithm() + " in size " + var1.getKeySize());
      }
   }

   public EntropyHandler(WSTContext var1, BinarySecret var2) throws WSTFaultException {
      this.wstContext = var1;
      this.reqEntropy = null;

      try {
         this.handleRequestorBinarySecret(var2);
      } catch (NoSuchAlgorithmException var4) {
         throw new RequestFailedException("Unable to generate key for " + var1.getSymmetricKeyAlgorithm() + " in size " + var1.getKeySize());
      }
   }

   private void init() throws NoSuchAlgorithmException, WSTFaultException {
      if (this.reqEntropy == null) {
         this.handleNoReqEntropy();
      } else if (this.reqEntropy.getBinarySecret() != null) {
         this.handleRequestorBinarySecret(this.reqEntropy.getBinarySecret());
      }

   }

   private void handleRequestorBinarySecret(BinarySecret var1) throws NoSuchAlgorithmException, WSTFaultException {
      String var2 = var1.getType();
      if (var2 != null && !var2.endsWith("/SymmetricKey")) {
         if (var2.endsWith("/AsymmetricKey")) {
            throw new BadRequestException("Asymmetric key is not net supported");
         }

         if (!var2.endsWith("/Nonce")) {
            throw new InvalidRequestException("Unknown BinarySecret type: " + var2);
         }

         String var3 = this.wstContext.getWstNamespaceURI();
         this.resEntropy = BindingHelper.createNewEntropy(var3, var3 + "/Nonce");
         ComputedKey var4 = new ComputedKey(var3);
         var4.setUri(var3 + "/CK/PSHA1");
         this.rpt = new RequestedProofToken(var3);
         this.rpt.setComputedKey(var4);

         try {
            this.key = KeyUtils.generateKey(var1.getValue(), this.resEntropy.getBinarySecret().getValue(), this.wstContext.getSymmetricKeyAlgorithm(), this.wstContext.getKeySize());
         } catch (NoSuchAlgorithmException var6) {
            throw new RequestFailedException("Unable to compute key from entropies");
         } catch (InvalidKeyException var7) {
            throw new RequestFailedException("Unable to compute key from entropies");
         }
      } else {
         this.key = new SecretKeySpec(var1.getValue(), this.wstContext.getSymmetricKeyAlgorithm());
      }

   }

   private void handleNoReqEntropy() throws NoSuchAlgorithmException {
      this.key = KeyUtils.newSecretKey(this.wstContext.getSymmetricKeyAlgorithm(), this.wstContext.getKeySize());
      this.handleNoReqEntropy(this.key);
   }

   private void handleNoReqEntropy(Key var1) throws NoSuchAlgorithmException {
      this.rpt = new RequestedProofToken(this.wstContext.getWstNamespaceURI());
      String var2 = this.rpt.getNamespaceURI();
      BinarySecret var3 = BindingHelper.createBinarySecret(var2, var1, var2 + "/SymmetricKey");
      this.rpt.setBinarySecret(var3);
   }

   public Entropy getResponseEntropy(Key var1) {
      String var2 = this.rpt.getNamespaceURI();
      if (null == var2) {
         var2 = this.wstContext.getWstNamespaceURI();
      }

      return BindingHelper.createNewEntropy(var2, var1, var2 + "/SymmetricKey");
   }

   public Entropy getResponseEntropy() {
      return this.resEntropy;
   }

   public Key getKey() {
      return this.key;
   }

   public RequestedProofToken getRequestedProofToken() {
      return this.rpt;
   }
}
