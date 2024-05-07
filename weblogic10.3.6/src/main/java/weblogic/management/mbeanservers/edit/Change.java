package weblogic.management.mbeanservers.edit;

public interface Change {
   String MODIFY = "modify";
   String CREATE = "create";
   String DESTROY = "destroy";
   String ADD = "add";
   String REMOVE = "remove";
   String UNSET = "unset";

   Object getBean();

   String getAttributeName();

   String getOperation();

   Object getOldValue();

   Object getNewValue();

   boolean isRestartRequired();
}
