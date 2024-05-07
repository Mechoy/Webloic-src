package weblogic.wsee.wstx.wsat.validation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.TransactionAttributeType;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.Transactional.TransactionFlowType;

public class TXAttributesValidator {
   public static final short TX_NOT_SET = -1;
   public static final short TX_NOT_SUPPORTED = 0;
   public static final short TX_REQUIRED = 1;
   public static final short TX_SUPPORTS = 2;
   public static final short TX_REQUIRES_NEW = 3;
   public static final short TX_MANDATORY = 4;
   public static final short TX_NEVER = 5;
   Set<InvalidCombination> inValidateCombinations = new HashSet();
   static Set<Combination> validateCombinations = new HashSet();

   public void visitOperation(String var1, short var2, Transactional.TransactionFlowType var3) {
      TransactionAttributeType var4 = fromIndex(var2);
      this.visitOperation(var1, var4, var3);
   }

   public void validate() throws WebServiceException {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.inValidateCombinations.iterator();

      while(var2.hasNext()) {
         InvalidCombination var3 = (InvalidCombination)var2.next();
         var1.append("The effective TransactionAttributeType " + var3.ejbTx).append(" and WS-AT Transaction flowType ").append(var3.wsat).append(" on WebService operation ").append(var3.operationName).append(" is not a valid combination! ");
      }

      if (var1.length() > 0) {
         throw new WebServiceException(var1.toString());
      }
   }

   public void visitOperation(String var1, TransactionAttributeType var2, Transactional.TransactionFlowType var3) {
      if (var3 == null) {
         var3 = TransactionFlowType.NEVER;
      }

      Combination var4 = new Combination(var2, var3);
      if (!validateCombinations.contains(var4)) {
         this.inValidateCombinations.add(new InvalidCombination(var2, var3, var1));
      }

   }

   public static boolean isValid(TransactionAttributeType var0, Transactional.TransactionFlowType var1) {
      return validateCombinations.contains(new Combination(var0, var1));
   }

   private static TransactionAttributeType fromIndex(Short var0) {
      switch (var0) {
         case 0:
            return TransactionAttributeType.NOT_SUPPORTED;
         case 1:
            return TransactionAttributeType.REQUIRED;
         case 2:
            return TransactionAttributeType.SUPPORTS;
         case 3:
            return TransactionAttributeType.REQUIRES_NEW;
         case 4:
            return TransactionAttributeType.MANDATORY;
         case 5:
            return TransactionAttributeType.NEVER;
         default:
            return TransactionAttributeType.SUPPORTS;
      }
   }

   static {
      validateCombinations.add(new Combination(TransactionAttributeType.REQUIRED, TransactionFlowType.MANDATORY));
      validateCombinations.add(new Combination(TransactionAttributeType.REQUIRED, TransactionFlowType.NEVER));
      validateCombinations.add(new Combination(TransactionAttributeType.MANDATORY, TransactionFlowType.MANDATORY));
      validateCombinations.add(new Combination(TransactionAttributeType.REQUIRED, TransactionFlowType.SUPPORTS));
      validateCombinations.add(new Combination(TransactionAttributeType.SUPPORTS, TransactionFlowType.SUPPORTS));
      validateCombinations.add(new Combination(TransactionAttributeType.REQUIRES_NEW, TransactionFlowType.NEVER));
      validateCombinations.add(new Combination(TransactionAttributeType.NEVER, TransactionFlowType.NEVER));
      validateCombinations.add(new Combination(TransactionAttributeType.NOT_SUPPORTED, TransactionFlowType.NEVER));
      validateCombinations.add(new Combination(TransactionAttributeType.SUPPORTS, TransactionFlowType.NEVER));
      validateCombinations.add(new Combination(TransactionAttributeType.SUPPORTS, TransactionFlowType.MANDATORY));
   }

   static class InvalidCombination {
      TransactionAttributeType ejbTx;
      Transactional.TransactionFlowType wsat;
      String operationName;

      InvalidCombination(TransactionAttributeType var1, Transactional.TransactionFlowType var2, String var3) {
         this.ejbTx = var1;
         this.wsat = var2;
         this.operationName = var3;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            InvalidCombination var2 = (InvalidCombination)var1;
            if (this.ejbTx != var2.ejbTx) {
               return false;
            } else if (!this.operationName.equals(var2.operationName)) {
               return false;
            } else {
               return this.wsat == var2.wsat;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1 = this.ejbTx.hashCode();
         var1 = 31 * var1 + this.wsat.hashCode();
         var1 = 31 * var1 + this.operationName.hashCode();
         return var1;
      }
   }

   static class Combination {
      TransactionAttributeType ejbTx;
      Transactional.TransactionFlowType wsat;

      Combination(TransactionAttributeType var1, Transactional.TransactionFlowType var2) {
         this.ejbTx = var1;
         this.wsat = var2;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            Combination var2 = (Combination)var1;
            if (this.ejbTx != var2.ejbTx) {
               return false;
            } else {
               return this.wsat == var2.wsat;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1 = this.ejbTx.hashCode();
         var1 = 31 * var1 + this.wsat.hashCode();
         return var1;
      }
   }
}
