package weblogic.deployment;

public interface EjbInfo {
   String getHomeName();

   String getLocalHomeName();

   String[] getLocalBusinessInterfaceNames();

   String[] getRemoteBusinessInterfaceNames();

   String[] getImplementedInterfaceNames();

   boolean implementsInterface(String var1);
}
