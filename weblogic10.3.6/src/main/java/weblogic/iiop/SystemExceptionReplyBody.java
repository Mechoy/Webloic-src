package weblogic.iiop;

public final class SystemExceptionReplyBody {
   byte[] exception_id;
   int minor_code_value;
   int completion_status;

   public void read(IIOPInputStream var1) {
      this.exception_id = var1.read_octet_sequence();
      this.minor_code_value = var1.read_long();
   }
}
