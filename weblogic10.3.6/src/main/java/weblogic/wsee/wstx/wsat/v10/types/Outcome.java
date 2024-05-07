package weblogic.wsee.wstx.wsat.v10.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(
   name = "Outcome"
)
@XmlEnum
public enum Outcome {
   @XmlEnumValue("Commit")
   COMMIT("Commit"),
   @XmlEnumValue("Rollback")
   ROLLBACK("Rollback");

   private final String value;

   private Outcome(String var3) {
      this.value = var3;
   }

   public String value() {
      return this.value;
   }

   public static Outcome fromValue(String var0) {
      Outcome[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Outcome var4 = var1[var3];
         if (var4.value.equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException(var0);
   }
}
