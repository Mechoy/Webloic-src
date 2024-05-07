package weblogic.xml.security.wsse.internal;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import weblogic.xml.security.signature.InvalidReferenceException;
import weblogic.xml.security.signature.Reference;
import weblogic.xml.security.signature.ReferenceValidationException;
import weblogic.xml.security.signature.SignatureValidationException;
import weblogic.xml.security.transforms.Transform;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.stream.XMLName;

public class Utils {
   public static final boolean validReference(Reference var0) {
      if (var0 == null) {
         return false;
      } else {
         Transform[] var1 = var0.getTransforms();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            Transform var3 = var1[var2];
            if (!TransformUtils.preservesSemantics(var3)) {
               return false;
            }
         }

         return true;
      }
   }

   public static final boolean addElement(Map var0, XMLName var1, String var2) {
      Object var3 = (SortedSet)var0.get(var1);
      if (var3 == null) {
         var3 = new TreeSet();
         var0.put(var1, var3);
      }

      return ((SortedSet)var3).add(var2);
   }

   public static final boolean equivalent(SortedSet var0, SortedSet var1) {
      if (var0 != null && var1 != null) {
         Iterator var2 = var0.iterator();
         Iterator var3 = var1.iterator();

         Object var4;
         Object var5;
         do {
            if (!var2.hasNext()) {
               if (var3.hasNext()) {
                  return false;
               }

               return true;
            }

            if (!var3.hasNext()) {
               return false;
            }

            var4 = var2.next();
            var5 = var3.next();
         } while(var4.equals(var5));

         return false;
      } else {
         return var0 == var1;
      }
   }

   public static final boolean isSuperset(SortedSet var0, SortedSet var1) {
      if (var0 != null) {
         return var1 != null ? var0.containsAll(var1) : true;
      } else {
         return var1 == null || var1.isEmpty();
      }
   }

   public static final Set diffTypes(Set var0, Set var1) {
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         XMLName var4 = (XMLName)var3.next();
         if (!var0.contains(var4)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public static final void handleException(XMLName var0, String var1, String var2) {
      handleException(getQName(var0), var1, var2);
   }

   public static final void handleException(QName var0, String var1, String var2) {
      SOAPFaultException var3 = new SOAPFaultException(var0, var1, var2, (Detail)null);
      var3.printStackTrace();
      throw var3;
   }

   public static final void handleException(Throwable var0, String var1) {
      QName var2 = getQName(WSSEConstants.QNAME_FAULT_INVALIDSECURITY);
      String var3;
      if (var0 instanceof ReferenceValidationException) {
         var2 = getQName(WSSEConstants.QNAME_FAULT_FAILEDCHECK);
         StringBuffer var4 = new StringBuffer();
         var4.append(var0.getMessage()).append(": ");
         ReferenceValidationException var5 = (ReferenceValidationException)var0;
         InvalidReferenceException[] var6 = var5.getInvalidReferenceExceptions();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            InvalidReferenceException var8 = var6[var7];
            var4.append("<Reference URI=\"" + var8.getReference().getURI() + "\" /> ");
         }

         var3 = var4.toString();
      } else if (!(var0 instanceof InvalidReferenceException) && !(var0 instanceof SignatureValidationException)) {
         var3 = var0.getMessage();
      } else {
         var2 = getQName(WSSEConstants.QNAME_FAULT_FAILEDCHECK);
         var3 = var0.getMessage();
      }

      handleException(var2, var3, var1);
   }

   private static QName getQName(XMLName var0) {
      return weblogic.xml.security.utils.Utils.getQName(var0);
   }
}
