package weblogic.auddi.uddi;

public class PublisherCancelledException extends UDDIException {
   public PublisherCancelledException() {
      this((String)null);
   }

   public PublisherCancelledException(String var1) {
      super(30220, var1 == null ? "" : var1);
   }
}
