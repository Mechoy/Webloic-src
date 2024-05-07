package weblogic.wsee.security.saml;

import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Node;
import weblogic.kernel.KernelStatus;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.wst.helpers.EncryptedKeyInfoBuilder;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.common.keyinfo.SecretKeyProvider;
import weblogic.xml.crypto.utils.CertUtils;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.SecurityTokenValidateResult;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.WSSecurityInfo;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.crypto.wss11.internal.WSS11Context;
import weblogic.xml.security.utils.Utils;

public abstract class AbstractSAMLTokenHandler implements SecurityTokenHandler {
   private static final boolean verbose = Verbose.isVerbose(AbstractSAMLTokenHandler.class);
   private static final boolean debug = false;
   private static final boolean DEBUG_SX_INTEROP_ISSUED_TOKEN = false;
   private static final boolean DEBUG_UNIT_TEST_ONLY = false;

   public abstract QName[] getQNames();

   public abstract String[] getValueTypes();

   public abstract boolean isSupportedTokenType(String var1);

   public abstract boolean isSupportedValueType(String var1);

   public abstract boolean isSaml2();

   public SecurityToken getSecurityToken(String var1, Object var2, ContextHandler var3) throws WSSecurityException {
      return new SAMLTokenImpl(var1, var2);
   }

   public SecurityToken getSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException {
      if (verbose) {
         Verbose.log((Object)("getSecurityToken for value type =" + var1));
      }

      WSSecurityInfo var5 = (WSSecurityInfo)var4.getValue("com.bea.contextelement.xml.SecurityInfo");
      if (null == var5) {
         if (verbose) {
            Verbose.log((Object)"com.bea.contextelement.xml.SecurityInfoNot found");
         }

         return null;
      } else {
         List var6 = var5.getSecurityTokens();
         if (var6 != null && var6.size() != 0) {
            Iterator var7 = var6.iterator();

            SecurityToken var8;
            while(var7.hasNext()) {
               var8 = (SecurityToken)var7.next();
               if (verbose) {
                  Verbose.log((Object)("Checking Token Id = " + var8.getId() + " valueType = " + var8.getValueType() + " of " + var8.toString()));
               }

               if (var8.getValueType().equals(var1)) {
                  return var8;
               }
            }

            if ("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID".equals(var1)) {
               if (verbose) {
                  Verbose.log((Object)"Checking again with Token Type  = http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1");
               }

               var7 = var6.iterator();

               while(var7.hasNext()) {
                  var8 = (SecurityToken)var7.next();
                  if ("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1".equals(var8.getValueType())) {
                     return var8;
                  }
               }
            }

            return null;
         } else {
            if (verbose) {
               Verbose.log((Object)"No SAML tokens from com.bea.contextelement.xml.SecurityInfo");
            }

            return null;
         }
      }
   }

   public SecurityTokenReference getSTR(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      if (this.isSaml2() && var1.equals(WSSConstants.REFERENCE_QNAME)) {
         return new SAMLSecurityTokenReference(var1, var2, var3);
      } else {
         return !this.isSaml2() && var1.equals(WSSConstants.KEY_IDENTIFIER_QNAME) ? new SAMLSecurityTokenReference(var1, var2, var3) : null;
      }
   }

   public SecurityToken newSecurityToken(Node var1) throws MarshalException {
      try {
         SAMLTokenImpl var2 = new SAMLTokenImpl(new SAMLCredentialImpl(var1));
         return var2;
      } catch (WSSecurityException var3) {
         throw new MarshalException("Invalid SAML token on wsee " + var3.getMessage(), var3);
      }
   }

   public SecurityTokenReference newSecurityTokenReference(Node var1) throws weblogic.xml.dom.marshal.MarshalException {
      SAMLSecurityTokenReference var2 = new SAMLSecurityTokenReference();
      var2.unmarshal(var1);
      return var2;
   }

   public KeyProvider getKeyProvider(SecurityToken var1, MessageContext var2) {
      if (null == var1) {
         return null;
      } else {
         SAMLToken var3 = (SAMLToken)var1;
         if (var3.isHolderOfKey() || null != var3.getPublicKey()) {
            PublicKey var4 = var3.getPublicKey();
            if (var4 != null) {
               if (verbose) {
                  Verbose.log((Object)"Returning public key SAMLKeyProvider for token");
               }

               return new SAMLKeyProvider(var4, var3.getPrivateKey(), var3.getAssertionID(), var3);
            }

            if (null != var3.getSecretKey()) {
               Key var5 = var3.getSecretKey();
               if (null != var5) {
                  if (verbose) {
                     Verbose.log((Object)"Returning Scret Key of SecretKeyProvider for token");
                  }

                  EncryptedKeyInfoBuilder.debugKey(var5, "Got Secret Key from SAML Token");
                  return new SecretKeyProvider(var5, (String)null, var3.getAssertionID().getBytes(), (String)null, var3);
               }
            } else if (null == var3.getCredential()) {
               if (verbose) {
                  Verbose.log((Object)"Null credentail on SAML token");
               }
            } else if (var3.getCredential() instanceof SAMLCredentialImpl) {
               SAMLCredentialImpl var19 = (SAMLCredentialImpl)var3.getCredential();
               if (null != var19.getEncryptedKeyProvider()) {
                  if (verbose) {
                     Verbose.log((Object)"Returning EncryptedKeyProvider from SAMLCredential for token");
                  }

                  return var19.getEncryptedKeyProvider();
               }

               if (null != var19.getSymmetircKey()) {
                  if (verbose) {
                     Verbose.log((Object)"Returning Symmetric key of SecretKeyProvider for token");
                  }

                  return new SecretKeyProvider(var19.getSymmetircKey(), (String)null, var3.getAssertionID().getBytes(), (String)null, var3);
               }

               if (null != var19.getSecurityTokenReference()) {
                  SecurityTokenReference var6 = var19.getSecurityTokenReference();
                  if (verbose) {
                     Verbose.log((Object)("Getting Keys from SecurityTokenReference in the SAML Token on HofK case for STR =" + var6));
                  }

                  String var7 = var6.getValueType();
                  String var8 = Utils.toBase64(var6.getKeyIdentifier().getIdentifier());
                  SecurityTokenHandler var9 = null;
                  WSS11Context var10 = null;

                  try {
                     var10 = (WSS11Context)var2.getProperty("weblogic.xml.crypto.wss.WSSecurityContext");
                     var9 = var10.getRequiredTokenHandler(var7);
                     if (null == var9) {
                        Verbose.log((Object)("Unable to find the SecurityTokenHandler for valueType = " + var7));
                        return null;
                     }
                  } catch (Exception var18) {
                     Verbose.logException(var18);
                     return null;
                  }

                  try {
                     SecurityToken var11 = var9.getSecurityToken(var6, var2);
                     if (null == var11) {
                        Verbose.log((Object)("Unable to find the secToken for valueType = " + var7));
                        return null;
                     }

                     return new SAMLKeyProvider(var11.getPublicKey(), var11.getPrivateKey(), var3.getAssertionID(), var3);
                  } catch (WSSecurityException var16) {
                     Verbose.log((Object)("Unable to find SKI=" + var8));
                     Verbose.logException(var16);
                     System.out.println("Wrong SKI from the SAML Token, Try to get with the client key for signature again");

                     try {
                        CredentialProvider var12 = var10.getRequiredCredentialProvider(var7);
                        Object var13 = var12.getCredential(var7, (String)null, new SecurityTokenContextHandler(var10), Purpose.SIGN);
                        if (var13 == null) {
                           Verbose.log((Object)("Again, without SKI still unable to find the credentail for valueType = " + var7));
                           return null;
                        }

                        SecurityToken var14 = var9.getSecurityToken(var7, var13, new SecurityTokenContextHandler(var10));
                        if (null == var14) {
                           Verbose.log((Object)("Again, without SKI still unable to find the secToken for valueType = " + var7));
                           return null;
                        }

                        Verbose.log((Object)"Try again, hacker code without SKI and got the secToken for  for SAMLKeyProvider");
                        return new SAMLKeyProvider(var14.getPublicKey(), var14.getPrivateKey(), var3.getAssertionID(), var3);
                     } catch (Exception var15) {
                        Verbose.logException(var15);
                        return null;
                     }
                  } catch (Exception var17) {
                     Verbose.logException(var17);
                     return null;
                  }
               }
            }
         }

         return null;
      }
   }

   public SecurityToken getSecurityToken(SecurityTokenReference var1, MessageContext var2) throws WSSecurityException {
      WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var2);
      List var4 = var3.getSecurityTokens();
      String var5 = var1.getReferenceURI();
      if (var5 != null && var5.startsWith("#")) {
         var5 = var5.substring(1);
      }

      KeyIdentifier var6 = var1.getKeyIdentifier();
      Iterator var7 = var4.iterator();

      while(var7.hasNext()) {
         SecurityToken var8 = (SecurityToken)var7.next();
         if (var5 != null) {
            if (var5.equals(var8.getId())) {
               return var8;
            }
         } else if (var6 != null && var8 instanceof SAMLToken) {
            SAMLToken var9 = (SAMLToken)var8;
            if (Arrays.equals(var9.getAssertionID().getBytes(), var6.getIdentifier())) {
               return var8;
            }
         }
      }

      throw new WSSecurityException("Failed to retrieve token for reference " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
   }

   public SecurityTokenValidateResult validateUnmarshalled(SecurityToken var1, MessageContext var2) {
      SAMLToken var3 = (SAMLToken)var1;
      SecurityTokenContextHandler var4 = new SecurityTokenContextHandler();
      String var5 = (String)var2.getProperty("weblogic.wsee.connection.end_point_address");
      var4.addContextElement("com.bea.contextelement.saml.TargetResource", CSSUtils.getEndpointPath(this.isSaml2(), var5));
      ArrayList var6;
      if (this.isSaml2()) {
         var6 = new ArrayList();
         var4.addContextElement("com.bea.contextelement.saml2.Attributes", var6);
      } else {
         var6 = new ArrayList();
         var4.addContextElement("com.bea.contextelement.saml.Attributes", var6);
      }

      String var17 = "weblogic.xml.crypto.wss.provider.Purpose";
      SAMLCredential var7 = (SAMLCredential)var3.getCredential();
      if (verbose) {
         ((SAMLCredentialImpl)var7).verbose();
         Verbose.log((Object)("Is HofK =" + var3.isHolderOfKey()));
         if (null == var2.getProperty(var17)) {
            Verbose.log((Object)(var17 + " is null"));
         } else {
            Verbose.log((Object)(var17 + "=" + var2.getProperty(var17) + " is Decryption =" + (Purpose.DECRYPT == var2.getProperty(var17))));
         }
      }

      Node var8 = (Node)var7.getCredential();
      if (var3.isHolderOfKey() && Purpose.DECRYPT == var2.getProperty(var17)) {
         if (verbose) {
            Verbose.log((Object)("Bypass CSS Validation due to it is HofK and purpose =" + var2.getProperty(var17)));
         }
      } else {
         try {
            var3.setSubject(CSSUtils.assertIdentity(var8, var4, this.isSaml2()).getSubject());
            if (verbose) {
               Verbose.log((Object)("asserted identity: subject is '" + var3.getSubject() + "'"));
            }
         } catch (Exception var16) {
            if (verbose) {
               Verbose.log("SAML Token is rejected by CSS", var16);
            }

            return new SecurityTokenValidateResult(false, "The SAML token is not valid, it is rejected by CSS ");
         }
      }

      Object var9 = null;
      if (this.isSaml2()) {
         var9 = var4.getValue("com.bea.contextelement.saml2.Attributes");
      } else {
         var9 = var4.getValue("com.bea.contextelement.saml.Attributes");
      }

      SAMLAttributeStatementData var10 = SAMLAttributeStatementDataFactory.makeSAMLAttributeStatementData(var9);
      if (null != var10) {
         if (verbose) {
            Verbose.log((Object)"Saving SAMLAttributeStatementData into SAML Credential and message context ");
         }

         var2.setProperty("weblogic.wsee.security.saml.attributies", var10);
         var7.setAttributes(var10);
      }

      if (var3.isHolderOfKey() && var7.getEncryptedKey() != null) {
         try {
            EncryptedKeyInfoBuilder.processEncryptedKey(var7, var2);
         } catch (Exception var15) {
            if (verbose) {
               Verbose.log("Error on processing EncryptedKey element ", var15);
            }

            return new SecurityTokenValidateResult(false, "Error in processing EncryptedKey element in the SAML Token, Exception =" + var15.toString());
         }
      }

      boolean var11 = true;
      X509Certificate var12 = var3.getHolderOfCert();
      if (var12 != null && var3.isHolderOfKey()) {
         boolean var13 = true;
         WssPolicyContext var14 = (WssPolicyContext)var2.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
         if (var14 != null && !var14.getWssConfiguration().validateHOKNeeded()) {
            var13 = false;
         } else if (!KernelStatus.isServer()) {
            var13 = false;
         }

         if (var13) {
            if (verbose) {
               Verbose.log((Object)("WssPolicyContext.WSS_POLICY_CTX_PROP is not set, validating Certificate  of " + var12.toString()));
            }

            var11 = CertUtils.validateCertificate(var12);
            if (!var11) {
               Verbose.log((Object)"Certificate is fail to validate");
            } else if (verbose) {
               Verbose.log((Object)"WssPolicyContext.WSS_POLICY_CTX_PROP is set, or it is off-server case, bypass the validating Certificate");
            }
         }
      }

      return new SecurityTokenValidateResult(var11);
   }

   public SecurityTokenValidateResult validateProcessed(SecurityToken var1, MessageContext var2) {
      return new SecurityTokenValidateResult(true);
   }

   public boolean matches(SecurityToken var1, String var2, String var3, ContextHandler var4, Purpose var5) {
      if (var1 == null) {
         return false;
      } else {
         Object var6 = var1.getCredential();
         if (var6 == null) {
            return false;
         } else {
            return var6 instanceof SAMLCredential && (this.isSupportedTokenType(var2) || this.isSupportedValueType(var2));
         }
      }
   }

   public Subject getSubject(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      SAMLToken var3 = (SAMLToken)var1;
      return var3.getSubject();
   }
}
