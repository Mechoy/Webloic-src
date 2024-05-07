package weblogic.wsee.buffer2.utils;

public class BufferingConstants {
   public static String TARGET_URI = "buffer2_URI";
   public static String DIRECTION = "buffer2_DIR";

   public static enum MsgDirection {
      REQUEST("buffer2_REQ") {
      },
      RESPONSE("buffer2_RESP") {
      };

      private String name;

      private MsgDirection(String var3) {
         this.name = var3;
      }

      public String toString() {
         return this.name;
      }

      // $FF: synthetic method
      MsgDirection(String var3, Object var4) {
         this(var3);
      }
   }
}
