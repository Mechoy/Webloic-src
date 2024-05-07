package weblogic.wsee.wsa.wsaddressing;

public enum WSAVersion {
   MemberSubmission,
   WSA10;

   public static WSAVersion latest() {
      return WSA10;
   }
}
