package weblogic.wsee.security.policy;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import org.w3c.dom.Element;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.assertions.xbeans.MessagePartsType;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;
import weblogic.xml.crypto.wss.SecurityBuilder;
import weblogic.xml.crypto.wss.SecurityValidator;
import weblogic.xml.crypto.wss.WSSecurityException;

public class SigningReferencesFactory {
   private SecurityBuilder sbuilder;
   private SecurityValidator svalidator;

   public SigningReferencesFactory(SecurityBuilder var1) {
      this.sbuilder = var1;
   }

   public SigningReferencesFactory(SecurityValidator var1) {
      this.svalidator = var1;
   }

   public Reference newSigningTokenReference(SecurityToken var1, ContextHandler var2, String var3) throws WSSecurityException {
      Reference var4 = null;
      XMLSignatureFactory var5;
      if (this.sbuilder != null) {
         var5 = this.sbuilder.getXMLSignatureFactory();

         try {
            var4 = this.sbuilder.createReference(var1.getTokenTypeUri(), var1.getIssuerName(), var5.newDigestMethod(var3, (DigestMethodParameterSpec)null), new ArrayList(), var1.isIncludeInMessage(), var2);
         } catch (NoSuchAlgorithmException var9) {
            throw new WSSecurityException(var9);
         } catch (InvalidAlgorithmParameterException var10) {
            throw new WSSecurityException(var10);
         }
      }

      if (this.svalidator != null) {
         var5 = this.svalidator.getXMLSignatureFactory();

         try {
            var4 = this.svalidator.getReference(var1.getTokenTypeUri(), var1.getIssuerName(), var1.getClaims(), var5.newDigestMethod(var3, (DigestMethodParameterSpec)null), new ArrayList(), var1.isIncludeInMessage());
         } catch (NoSuchAlgorithmException var7) {
            throw new WSSecurityException(var7);
         } catch (InvalidAlgorithmParameterException var8) {
            throw new WSSecurityException(var8);
         }
      }

      return var4;
   }

   public List getSigningReferences(XMLSignatureFactory var1, MessagePartsType var2, DigestMethod var3, List var4, SOAPMessageContext var5, Map var6) throws WSSecurityException, PolicyException {
      List var7 = (new MessagePartsEvaluator(var2, var5, var6)).getNodes();
      return this.getSigningReferences(var1, var7, var3, var4);
   }

   public List getSigningReferences(XMLSignatureFactory var1, List var2, DigestMethod var3, List var4) throws WSSecurityException, PolicyException {
      ArrayList var5 = new ArrayList();

      String var8;
      for(Iterator var6 = var2.iterator(); var6.hasNext(); var5.add(var1.newReference(var8, var3, var4, (String)null, (String)null))) {
         Element var7 = (Element)var6.next();
         var8 = null;
         if (this.sbuilder != null) {
            var8 = this.sbuilder.assignUri(var7);
         } else {
            var8 = this.svalidator.getUri(var7);
         }
      }

      return var5;
   }
}
