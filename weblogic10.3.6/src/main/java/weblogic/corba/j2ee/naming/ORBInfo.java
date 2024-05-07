package weblogic.corba.j2ee.naming;

import javax.security.auth.Subject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.omg.CORBA.ORB;

public class ORBInfo {
   private ORB orb;
   private String clusterURL;
   private String key;

   public ORBInfo(ORB var1, String var2) {
      this.orb = var1;
      this.key = var2;
   }

   public String getKey() {
      return this.key;
   }

   public void setTransaction(UserTransaction var1) throws SystemException {
   }

   public void setSubject(Subject var1) {
   }

   public ORB getORB() {
      return this.orb;
   }

   public String getClusterURL() {
      return this.clusterURL;
   }

   public void setClusterURL(String var1) {
      this.clusterURL = var1;
   }
}
