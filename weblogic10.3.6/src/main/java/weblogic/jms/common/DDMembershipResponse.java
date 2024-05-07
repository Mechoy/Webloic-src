package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.dispatcher.Response;

public class DDMembershipResponse extends Response implements Externalizable {
   private static final int EXTVERSIONDIABLO = 1;
   private static final int VERSION_MASK = 255;
   private static int _HAS_DD_MEMBER_INFORMATION = 65280;
   private DDMemberInformation[] ddMemberInformation;

   public DDMembershipResponse(DDMemberInformation[] var1) {
      this.ddMemberInformation = var1;
   }

   public DDMemberInformation[] getDDMemberInformation() {
      return this.ddMemberInformation;
   }

   public DDMembershipResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      int var3 = 0;
      if (this.ddMemberInformation != null && (var3 = this.ddMemberInformation.length) != 0) {
         var2 |= _HAS_DD_MEMBER_INFORMATION;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      if (var3 != 0) {
         var1.writeInt(var3);

         for(int var4 = 0; var4 < var3; ++var4) {
            this.ddMemberInformation[var4].writeExternal(var1);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         if ((var2 & _HAS_DD_MEMBER_INFORMATION) != 0) {
            int var4 = var1.readInt();
            this.ddMemberInformation = new DDMemberInformation[var4];

            for(int var5 = 0; var5 < var4; ++var5) {
               DDMemberInformation var6 = new DDMemberInformation();
               var6.readExternal(var1);
               this.ddMemberInformation[var5] = var6;
            }
         }

      }
   }
}
