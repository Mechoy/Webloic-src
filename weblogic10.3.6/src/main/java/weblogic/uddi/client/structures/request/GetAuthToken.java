package weblogic.uddi.client.structures.request;

public class GetAuthToken extends Request {
   private String cred = null;
   private String userID = null;

   public void setCred(String var1) {
      this.cred = var1;
   }

   public String getCred() {
      return this.cred;
   }

   public void setUserID(String var1) {
      this.userID = var1;
   }

   public String getUserID() {
      return this.userID;
   }
}
