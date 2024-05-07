package weblogic.cluster.singleton;

public interface MemberDeathDetector {
   void start();

   void stop();

   String removeMember(String var1);
}
