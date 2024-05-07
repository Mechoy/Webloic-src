package weblogic.entitlement.rules;

import javax.security.auth.Subject;
import weblogic.security.SecurityLogger;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.PredicateArgument;
import weblogic.security.service.ContextHandler;
import weblogic.security.shared.LoggerWrapper;
import weblogic.security.spi.Resource;
import weblogic.security.utils.ESubjectImpl;

public class SignaturePredicate extends BasePredicate {
   public static final String GROUP_TYPE = "group";
   public static final String USERNAME_TYPE = "user";
   private static final String VERSION = "1.0";
   private static final PredicateArgument[] arguments = new PredicateArgument[]{new StringPredicateArgument("SignaturePredicateSignerTypeArgumentName", "SignaturePredicateSignerTypeArgumentDescription", (String)null), new StringPredicateArgument("SignaturePredicateSignedElementArgumentName", "SignaturePredicateSignedElementArgumentDescription", (String)null), new StringPredicateArgument("SignaturePredicateSignerNameArgumentName", "SignaturePredicateSignerNameArgumentDescription", (String)null)};
   private static LoggerWrapper log = LoggerWrapper.getInstance("SecurityPredicate");
   private String signerType = "group";
   private String signerName = null;
   private String signerElement = null;

   public SignaturePredicate() {
      super("SignaturePredicateName", "SignaturePredicateDescription");
   }

   public void init(String[] var1) throws IllegalPredicateArgumentException {
      if (var1 != null && var1.length == 3) {
         String var2 = var1[0];
         if ("user".equalsIgnoreCase(var2)) {
            this.signerType = "user";
         } else {
            if (!"group".equalsIgnoreCase(var2)) {
               throw new IllegalPredicateArgumentException(SecurityLogger.getTypeMustValueIs("group", "user", var2));
            }

            this.signerType = "group";
         }

         if (var1[1] == null) {
            throw new IllegalPredicateArgumentException(SecurityLogger.getSignatureTypeCanNotBeNull());
         } else {
            this.signerElement = "Integrity{" + var1[1] + "}";
            if (var1[2] == null) {
               throw new IllegalPredicateArgumentException(SecurityLogger.getSignedByCanNotBeNull());
            } else {
               this.signerName = var1[2];
               if (log.isDebugEnabled()) {
                  log.debug("SignaturePredicate.init: signerType=" + this.signerType + ", signerName=" + this.signerName + ", signerElement=" + this.signerElement);
               }

            }
         }
      } else {
         throw new IllegalPredicateArgumentException(SecurityLogger.getThreeArgumentsRequired());
      }
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      if (log.isDebugEnabled()) {
         log.debug("SignaturePredicate.evaluate: matching " + this.signerType + " " + this.signerName);
      }

      if (var3 == null) {
         if (log.isDebugEnabled()) {
            log.debug("SignaturePredicate.evaluate: context is null, returning false");
         }

         return false;
      } else {
         Subject var4 = (Subject)var3.getValue(this.signerElement);
         if (var4 == null) {
            if (log.isDebugEnabled()) {
               log.debug("SignaturePredicate.evaluate: no signer, returning false");
            }

            return false;
         } else {
            ESubjectImpl var5 = new ESubjectImpl(var4);
            boolean var6 = this.signerType == "user" ? var5.isUser(this.signerName) : var5.isMemberOf(this.signerName);
            if (log.isDebugEnabled()) {
               log.debug("SignaturePredicate.evaluate: returning " + var6);
            }

            return var6;
         }
      }
   }

   public boolean isSupportedResource(String var1) {
      return var1.startsWith("type=<webservices>");
   }

   public String getVersion() {
      return "1.0";
   }

   public int getArgumentCount() {
      return arguments.length;
   }

   public PredicateArgument getArgument(int var1) {
      return arguments[var1];
   }
}
