package weblogic.wsee.security.saml;

import com.bea.security.saml2.providers.SAML2AttributeInfo;
import com.bea.security.saml2.providers.SAML2AttributeStatementInfo;
import java.util.Collection;
import java.util.Iterator;
import weblogic.security.providers.saml.SAMLAttributeInfo;
import weblogic.security.providers.saml.SAMLAttributeStatementInfo;
import weblogic.wsee.util.Verbose;

public class SAMLAttributeStatementDataFactory {
   private static boolean verbose = Verbose.isVerbose(SAMLAttributeStatementDataFactory.class);

   public static SAMLAttributeStatementData makeSAMLAttributeStatementData(Object var0) {
      if (null == var0) {
         return null;
      } else {
         if (var0 instanceof Collection) {
            Collection var1 = (Collection)var0;
            if (var1.isEmpty()) {
               if (verbose) {
                  Verbose.log((Object)"Empty SAML Attributes collection found");
               }

               return null;
            }

            Object var2 = var1.toArray()[0];
            if (var2 instanceof SAML2AttributeStatementInfo) {
               return makeSAMLAttributeStatementDataFromSAML2AttributeStatementInfo(var1);
            }

            if (var2 instanceof SAMLAttributeStatementInfo) {
               return makeSAMLAttributeStatementDataFromSAMLAttributeStatementInfo(var1);
            }

            if (verbose) {
               Verbose.log((Object)("Unknow SAML Attributes collection object found" + var2.toString()));
            }
         }

         return null;
      }
   }

   public static SAMLAttributeStatementData makeSAMLAttributeStatementDataFromSAML2AttributeStatementInfo(Collection<SAML2AttributeStatementInfo> var0) {
      Iterator var1 = var0.iterator();
      if (!var1.hasNext()) {
         if (verbose) {
            Verbose.log((Object)"Empty SAML2AttributeStatementInfo collection found for SAML Attributes 2.0");
         }

         return null;
      } else {
         SAML2AttributeStatementInfo var2 = (SAML2AttributeStatementInfo)var1.next();
         if (var2.getAttributeInfo() != null && !var2.getAttributeInfo().isEmpty()) {
            SAMLAttributeStatementDataImpl var3 = new SAMLAttributeStatementDataImpl();
            Iterator var4 = var2.getAttributeInfo().iterator();

            while(var4.hasNext()) {
               var3.addAttributeInfo((SAMLAttributeData)(new SAMLAttributeDataImpl((SAML2AttributeInfo)var4.next())));
            }

            if (verbose) {
               Verbose.log((Object)("Building SAMLAttributeStatementData for SAML Attributes 2.0, size =" + var3.size()));
            }

            return var3;
         } else {
            if (verbose) {
               Verbose.log((Object)"No AttributeInfo or empty AttributeInfo on SAML2AttributeStatementInfo found on SAML Attributes 2.0");
            }

            return null;
         }
      }
   }

   public static SAMLAttributeStatementData makeSAMLAttributeStatementDataFromSAMLAttributeStatementInfo(Collection<SAMLAttributeStatementInfo> var0) {
      Iterator var1 = var0.iterator();
      if (!var1.hasNext()) {
         if (verbose) {
            Verbose.log((Object)"Empty SAMLAttributeStatementInfo collection found for SAML Attributes 1.1");
         }

         return null;
      } else {
         SAMLAttributeStatementInfo var2 = (SAMLAttributeStatementInfo)var1.next();
         if (var2.getAttributeInfo() != null && !var2.getAttributeInfo().isEmpty()) {
            SAMLAttributeStatementDataImpl var3 = new SAMLAttributeStatementDataImpl();
            Iterator var4 = var2.getAttributeInfo().iterator();

            while(var4.hasNext()) {
               var3.addAttributeInfo((SAMLAttributeData)(new SAMLAttributeDataImpl((SAMLAttributeInfo)var4.next())));
            }

            if (verbose) {
               Verbose.log((Object)("Building SAMLAttributeStatementData for SAML Attributes 1.1, size =" + var3.size()));
            }

            return var3;
         } else {
            if (verbose) {
               Verbose.log((Object)"No AttributeInfo or empty AttributeInfo on SAMLAttributeStatementInfo found on SAML Attributes 1.1");
            }

            return null;
         }
      }
   }
}
