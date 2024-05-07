package weblogic.servlet.cluster.wan;

public interface PersistenceServiceControl {
   void start();

   void stop();

   void halt();
}
