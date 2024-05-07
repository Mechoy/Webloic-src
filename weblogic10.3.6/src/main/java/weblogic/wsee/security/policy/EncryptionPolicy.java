package weblogic.wsee.security.policy;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.assertions.ConfidentialityAssertion;
import weblogic.wsee.security.policy.assertions.xbeans.ConfidentialityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.ConfidentialityTargetType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.encrypt.api.dom.DOMTBEXML;
import weblogic.xml.crypto.encrypt.api.spec.EncryptionMethodParameterSpec;
import weblogic.xml.crypto.wss.WSSecurityException;

public class EncryptionPolicy {
   public static final boolean verbose = Verbose.isVerbose(EncryptionPolicy.class);
   private EncryptionMethod keyWrapMethod;
   private CanonicalizationMethod c14nMethod;
   private List validEncryptionTokens;
   private List encryptionTargets;

   public EncryptionPolicy(XMLEncryptionFactory var1, SOAPMessageContext var2, ConfidentialityAssertion var3) throws PolicyException, WSSecurityException {
      this(var1, var2, var3, true);
   }

   public EncryptionPolicy(XMLEncryptionFactory var1, SOAPMessageContext var2, ConfidentialityAssertion var3, boolean var4) throws PolicyException, WSSecurityException {
      this.keyWrapMethod = null;
      this.validEncryptionTokens = new ArrayList();
      this.encryptionTargets = new ArrayList();
      Map var5 = var3.getNamespaceMap();
      ConfidentialityDocument.Confidentiality var6 = var3.getXbean().getConfidentiality();
      String var7 = "http://www.w3.org/2001/10/xml-exc-c14n#";

      try {
         if (var6.getKeyWrappingAlgorithm() != null) {
            this.keyWrapMethod = var1.newEncryptionMethod(var6.getKeyWrappingAlgorithm().getURI(), (Integer)null, (EncryptionMethodParameterSpec)null);
         }

         this.c14nMethod = var1.newCanonicalizationMethod(var7, (C14NMethodParameterSpec)null);
      } catch (InvalidAlgorithmParameterException var19) {
         throw new WSSecurityException(var19.getMessage(), var19);
      } catch (NoSuchAlgorithmException var20) {
         throw new WSSecurityException(var20.getMessage(), var20);
      }

      ConfidentialityTargetType[] var8 = var6.getTargetArray();

      for(int var9 = 0; var9 < var8.length; ++var9) {
         ConfidentialityTargetType var10 = var8[var9];
         EncryptionMethod var11 = null;

         String var12;
         try {
            var12 = var10.getEncryptionAlgorithm().getURI();
            var11 = var1.newEncryptionMethod(var12, (Integer)null, (EncryptionMethodParameterSpec)null);
            if (var11 == null) {
               throw new WSSecurityException(var12 + " is not a supported encryption algorithm.");
            }
         } catch (InvalidAlgorithmParameterException var18) {
            throw new WSSecurityException(var18.getMessage(), var18);
         }

         if (var4) {
            var12 = null;
            MessagePartsEvaluator var13 = new MessagePartsEvaluator(var10.getMessageParts(), var2, var5);
            List var24;
            if (var10.getEncryptContentOnly()) {
               var24 = var13.getNodesContent();
            } else {
               var24 = var13.getNodes();
            }

            if (var24 != null && var24.size() != 0) {
               ArrayList var14 = new ArrayList();
               Iterator var15;
               DOMTBEXML var17;
               if (var10.getEncryptContentOnly()) {
                  var15 = var24.iterator();

                  while(var15.hasNext()) {
                     NodeList var26 = (NodeList)var15.next();
                     var17 = new DOMTBEXML(var26, this.c14nMethod);
                     var14.add(var17);
                  }
               } else {
                  var15 = var24.iterator();

                  while(var15.hasNext()) {
                     Element var16 = (Element)var15.next();
                     var17 = new DOMTBEXML(var16, this.c14nMethod);
                     var14.add(var17);
                  }
               }

               this.encryptionTargets.add(new EncryptionTarget(var11, var14));
            } else if (verbose) {
               Verbose.log((Object)("MessageParts expression '" + var10.getMessageParts() + "' did not evaluate to any nodes in the message; target will be skipped"));
            }
         }
      }

      SecurityTokenType[] var21 = var6.getKeyInfo().getSecurityTokenArray();

      for(int var22 = 0; var22 < var21.length; ++var22) {
         SecurityTokenType var23 = var21[var22];
         SecurityToken var25 = new SecurityToken(XBeanUtils.getElement(var23), (String)null, var23.getTokenType(), var23.getIncludeInMessage());
         var25.setDerivedFromTokenType(var23.getDerivedFromTokenType());
         this.validEncryptionTokens.add(var25);
      }

   }

   public EncryptionMethod getKeyWrapMethod() {
      return this.keyWrapMethod;
   }

   public CanonicalizationMethod getC14nMethod() {
      return this.c14nMethod;
   }

   public List getValidEncryptionTokens() {
      return this.validEncryptionTokens;
   }

   public List getEncryptionTargets() {
      return this.encryptionTargets;
   }
}
