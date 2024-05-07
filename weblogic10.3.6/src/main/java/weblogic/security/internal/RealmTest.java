package weblogic.security.internal;

import java.security.acl.Group;
import java.util.Enumeration;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.ListableRealm;
import weblogic.security.acl.User;
import weblogic.utils.enumerations.EnumerationUtils;

final class RealmTest {
   public static void main(String[] var0) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      if (var0.length < 4) {
         System.out.println("usage: main class user passwd group");
         System.exit(1);
      }

      String var1 = var0[0];
      String var2 = var0[1];
      String var3 = var0[2];
      String var4 = var0[3];
      System.out.println("creating realm " + var1 + " ...");
      ListableRealm var5 = (ListableRealm)Class.forName(var1).newInstance();
      System.out.println("... done");
      System.out.println("checking for user " + var2 + " ...");
      User var6 = var5.getUser(var2);
      System.out.println("... " + (var6 != null ? "exists" : "unperson"));
      System.out.println("authenticating user " + var2 + " ...");
      User var7 = var5.getUser(new DefaultUserInfoImpl(var2, var3));
      System.out.println("... auth " + (var7 != null ? "succeeded" : "failed"));
      System.out.println("checking group " + var4 + " ...");
      Group var8 = var5.getGroup(var4);
      System.out.println("... group " + (var8 != null ? EnumerationUtils.toString(var8.members()) : "ungroup"));
      System.out.println("Getting list of users ...");
      Enumeration var9 = var5.getUsers();
      System.out.println("... " + (var9 != null ? EnumerationUtils.toString(var9) : "null!?"));
      System.out.println("Getting list of groups ...");
      Enumeration var10 = var5.getGroups();
      System.out.println("... " + (var10 != null ? EnumerationUtils.toString(var10) : "null!?"));
   }
}
