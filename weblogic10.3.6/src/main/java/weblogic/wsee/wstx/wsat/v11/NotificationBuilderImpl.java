package weblogic.wsee.wstx.wsat.v11;

import weblogic.wsee.wstx.wsat.common.NotificationBuilder;
import weblogic.wsee.wstx.wsat.v11.types.Notification;

public class NotificationBuilderImpl extends NotificationBuilder<Notification> {
   public Notification build() {
      return new Notification();
   }
}
