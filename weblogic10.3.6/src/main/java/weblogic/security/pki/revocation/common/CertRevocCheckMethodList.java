package weblogic.security.pki.revocation.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class CertRevocCheckMethodList {
   private final List<SelectableMethod> list;

   public CertRevocCheckMethodList(String var1) throws IllegalArgumentException {
      this(parseSelectableMethodList(var1));
   }

   public CertRevocCheckMethodList(SelectableMethodList var1) {
      checkNonNull(var1, "SelectableMethodList");
      ArrayList var2 = new ArrayList();
      switch (var1) {
         case OCSP:
            var2.add(CertRevocCheckMethodList.SelectableMethod.OCSP);
            break;
         case CRL:
            var2.add(CertRevocCheckMethodList.SelectableMethod.CRL);
            break;
         case OCSP_THEN_CRL:
            var2.add(CertRevocCheckMethodList.SelectableMethod.OCSP);
            var2.add(CertRevocCheckMethodList.SelectableMethod.CRL);
            break;
         case CRL_THEN_OCSP:
            var2.add(CertRevocCheckMethodList.SelectableMethod.CRL);
            var2.add(CertRevocCheckMethodList.SelectableMethod.OCSP);
            break;
         default:
            throw new IllegalStateException("Unexpected value: " + var1);
      }

      this.list = Collections.unmodifiableList(var2);
   }

   public int size() {
      return this.list.size();
   }

   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   public boolean contains(SelectableMethod var1) {
      checkNonNull(var1, "SelectableMethod");
      return this.list.contains(var1);
   }

   public Iterator<SelectableMethod> iterator() {
      return this.list.iterator();
   }

   public SelectableMethod get(int var1) {
      return (SelectableMethod)this.list.get(var1);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof CertRevocCheckMethodList)) {
         return false;
      } else {
         CertRevocCheckMethodList var2 = (CertRevocCheckMethodList)var1;
         return this.list.equals(var2.list);
      }
   }

   public int hashCode() {
      return this.list.hashCode();
   }

   private static boolean add(List<SelectableMethod> var0, SelectableMethod var1) {
      checkNonNull(var0, "List<SelectableMethod>");
      checkNonNull(var1, "SelectableMethod");
      return !var0.contains(var1) ? var0.add(var1) : false;
   }

   private static void checkNonNull(Object var0, String var1) {
      if (null == var1) {
         var1 = "";
      }

      if (null == var0) {
         throw new IllegalArgumentException("Non-null " + var1 + " expected.");
      }
   }

   private static SelectableMethodList parseSelectableMethodList(String var0) {
      checkNonNull(var0, "String");
      var0 = var0.toUpperCase(Locale.US);

      try {
         SelectableMethodList var1 = CertRevocCheckMethodList.SelectableMethodList.valueOf(var0);
         return var1;
      } catch (IllegalArgumentException var3) {
         throw new IllegalArgumentException("Unrecognized CertRevocCheckMethodList: '" + var0 + "'.", var3);
      }
   }

   public static enum SelectableMethodList {
      OCSP,
      CRL,
      OCSP_THEN_CRL,
      CRL_THEN_OCSP;
   }

   public static enum SelectableMethod {
      OCSP,
      CRL;
   }
}
