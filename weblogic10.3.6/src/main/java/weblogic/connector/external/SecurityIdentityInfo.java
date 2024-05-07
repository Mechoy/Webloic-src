package weblogic.connector.external;

public interface SecurityIdentityInfo {
   boolean useAnonForManageAs();

   String getManageAsPrincipalName();

   boolean useAnonForRunAs();

   boolean useCallerForRunAs();

   String getRunAsPrincipalName();

   boolean useAnonForRunWorkAs();

   boolean useCallerForRunWorkAs();

   String getRunWorkAsPrincipalName();

   String getDefaultPrincipalName();
}
