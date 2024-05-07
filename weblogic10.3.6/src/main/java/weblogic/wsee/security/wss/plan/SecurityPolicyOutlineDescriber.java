package weblogic.wsee.security.wss.plan;

import java.util.List;
import java.util.Map;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.wsee.security.saml.SAML2TokenHandler;
import weblogic.wsee.security.saml.SAMLTokenHandler;
import weblogic.wsee.security.wss.plan.helper.SOAPSecurityHeaderHelper;
import weblogic.wsee.security.wss.policy.SecurityPolicyInspectionException;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.SecurityValidator;
import weblogic.xml.crypto.wss11.internal.WSS11Context;

public class SecurityPolicyOutlineDescriber {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyOutlineDescriber.class);
   private static final boolean debug = false;
   private SecurityPolicyOutlineSketcher outlineSketcher;

   public SecurityPolicyOutlineDescriber(SecurityValidator var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Null security builder found");
      } else {
         this.outlineSketcher = new SecurityPolicyOutlineSketcher();
      }
   }

   public SecurityPolicyPlan getPolicyOutline() {
      return this.outlineSketcher.getOutline();
   }

   public void sketchPolicyOutline(SOAPMessage var1, Map<String, Object> var2, boolean var3, WSS11Context var4) throws WSSecurityException, SecurityPolicyInspectionException, MarshalException {
      if (null == var1) {
         throw new IllegalArgumentException("Null SOAP MessageContext found");
      } else {
         this.outlineSketcher.setServiceProvider(var3);
         SOAPSecurityHeaderHelper var5 = new SOAPSecurityHeaderHelper(var1);
         Element var6 = var5.getSecurityHeaderElement();
         if (null == var6) {
            this.outlineSketcher.sketchSecurity(false);
         } else {
            this.outlineSketcher.sketchSecurity(true);
            if (null != var4) {
               var4.setTokenHandler(new SAMLTokenHandler());
               var4.setTokenHandler(new SAML2TokenHandler());
            }

            this.outlineSketcher.sketchEncryptedKeyList(var5.getEncryptionElements(), var5.getRefListElements());
            List var7 = var5.getSignatrueElements();
            switch (var7.size()) {
               case 0:
                  break;
               case 1:
                  this.outlineSketcher.sketchSignatureElelment((Element)var7.get(0));
                  break;
               case 2:
                  byte var8 = 1;
                  if (SecurityPolicyOutlineSketcher.isSignatureElement((Element)var7.get(0))) {
                     var8 = 0;
                  }

                  this.outlineSketcher.sketchEndorseElement((Element)var7.get(1 - var8));
                  this.outlineSketcher.sketchSignatureElelment((Element)var7.get(var8));
                  break;
               default:
                  throw new SecurityPolicyInspectionException(3909);
            }

            List var11 = var5.getOtherElements();

            for(int var9 = 0; var9 < var11.size(); ++var9) {
               this.outlineSketcher.sketchOuline((Element)var11.get(var9));
            }

            try {
               SOAPBody var12 = var1.getSOAPBody();
               if (var12 == null) {
                  Verbose.log((Object)"Error when inspecting SOAP Message: SOAPBody not found");
                  throw new WSSecurityException("SOAP Message format error: SOAPBody not found");
               } else {
                  this.outlineSketcher.sketchSoapBody(var12);
               }
            } catch (SOAPException var10) {
               Verbose.log("Error when inspecting SOAP Body", var10);
               throw new WSSecurityException(var10);
            }
         }
      }
   }
}
