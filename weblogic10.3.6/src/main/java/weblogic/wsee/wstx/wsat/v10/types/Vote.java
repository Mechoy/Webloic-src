package weblogic.wsee.wstx.wsat.v10.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(
   name = "Vote"
)
@XmlEnum
public enum Vote {
   @XmlEnumValue("VoteCommit")
   VOTE_COMMIT("VoteCommit"),
   @XmlEnumValue("VoteRollback")
   VOTE_ROLLBACK("VoteRollback"),
   @XmlEnumValue("VoteReadOnly")
   VOTE_READ_ONLY("VoteReadOnly");

   private final String value;

   private Vote(String var3) {
      this.value = var3;
   }

   public String value() {
      return this.value;
   }

   public static Vote fromValue(String var0) {
      Vote[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Vote var4 = var1[var3];
         if (var4.value.equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException(var0);
   }
}
