package weblogic.wsee.security.wss.plan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.security.policy.SigningReferencesFactory;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wss.plan.helper.SOAPSecurityHeaderHelper;
import weblogic.wsee.security.wss.plan.helper.XpathNodesHelper;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.policy.wssp.EncryptionPolicyBlueprintImpl;
import weblogic.wsee.security.wss.policy.wssp.SigningPolicyBlueprintImpl;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.SecurityBuilder;
import weblogic.xml.crypto.wss11.internal.SecurityValidator;

public class SecurityPolicyBlueprint extends SecurityPolicyPlanImpl {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyBlueprint.class);
   private static final boolean debug = false;
   private SecurityBuilder sbuilder = null;
   private SecurityValidator sValidator = null;
   private XMLEncryptionFactory xmlEncryptionFactory;
   private XMLSignatureFactory xmlSignatureFactory;
   private SigningReferencesFactory signingReferencesFactory;
   protected List requiredElements;
   protected List<QNameExpr> requiredParts = null;

   public SecurityPolicyBlueprint(SecurityBuilder var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Null security builder found");
      } else {
         this.sbuilder = var1;
         this.setBuildingPlan(27);
         this.xmlEncryptionFactory = var1.getXMLEncryptionFactory();
         this.xmlSignatureFactory = var1.getXMLSignatureFactory();
         this.signingReferencesFactory = new SigningReferencesFactory(var1);
         this.signingPolicy = new SigningPolicyBlueprintImpl(this.xmlSignatureFactory, this.signingReferencesFactory);
         this.encryptionPolicy = new EncryptionPolicyBlueprintImpl(this.xmlEncryptionFactory);
         this.endorsingPolicy = new SigningPolicyBlueprintImpl(this.xmlSignatureFactory, this.signingReferencesFactory);
      }
   }

   public SecurityPolicyBlueprint(SecurityValidator var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Null security validator found");
      } else {
         this.sValidator = var1;
         this.xmlEncryptionFactory = this.sValidator.getXMLEncryptionFactory();
         this.xmlSignatureFactory = this.sValidator.getXMLSignatureFactory();
         this.signingReferencesFactory = new SigningReferencesFactory(this.sValidator);
         this.signingPolicy = new SigningPolicyBlueprintImpl(this.xmlSignatureFactory, this.signingReferencesFactory);
         this.encryptionPolicy = new EncryptionPolicyBlueprintImpl(this.xmlEncryptionFactory);
         this.endorsingPolicy = new SigningPolicyBlueprintImpl(this.xmlSignatureFactory, this.signingReferencesFactory);
      }
   }

   public SecurityBuilder getSecurityBuilder() {
      return this.sbuilder;
   }

   public SecurityValidator getSecurityValidator() {
      return this.sValidator;
   }

   public boolean isForValidator() {
      return this.sValidator != null;
   }

   public XMLEncryptionFactory getXmlEncryptionFactory() {
      return this.xmlEncryptionFactory;
   }

   public XMLSignatureFactory getXmlSignatureFactory() {
      return this.xmlSignatureFactory;
   }

   public SigningReferencesFactory getSigningReferencesFactory() {
      return this.signingReferencesFactory;
   }

   public void addRequiredElement(XPath var1) {
      if (this.requiredElements == null) {
         this.requiredElements = new ArrayList();
      }

      this.requiredElements.add(var1);
   }

   public void addRequiredPart(QNameExpr var1) {
      if (this.requiredParts == null) {
         this.requiredParts = new ArrayList();
      }

      this.requiredParts.add(var1);
   }

   public List<QNameExpr> getRequiredParts() {
      return this.requiredParts;
   }

   public List<XPath> getReqElementList() {
      return this.requiredElements;
   }

   public void verifyPolicy(SOAPMessageContext var1) throws SecurityPolicyArchitectureException {
      if (this.getRequiredParts() != null && this.getRequiredParts().size() > 0) {
         if (verbose) {
            Verbose.log((Object)("Checking required parts for " + this.getRequiredParts().size()));
         }

         try {
            label47: {
               Iterator var2 = this.getRequiredParts().iterator();
               SOAPMessage var3 = var1.getMessage();

               QName var5;
               List var6;
               do {
                  if (!var2.hasNext()) {
                     break label47;
                  }

                  QNameExpr var4 = (QNameExpr)var2.next();
                  var5 = new QName(var4.getNamespaceUri(), var4.getLocalName());
                  if (verbose) {
                     Verbose.log((Object)("Checking required parts for QName =" + var5.toString()));
                  }

                  var6 = SOAPSecurityHeaderHelper.getNonSecurityElements(var3, var4);
               } while(var6 != null && var6.size() != 0);

               throw new SecurityPolicyArchitectureException("Missing Exception on getting Header Element QName =" + var5.toString());
            }
         } catch (WSSecurityException var7) {
            throw new SecurityPolicyArchitectureException("Exception on getting Header Elements", var7);
         }
      }

      List var8 = this.getReqElementList();
      if (var8 != null) {
         XpathNodesHelper.findNode(var8, var1, true);
      }

   }
}
