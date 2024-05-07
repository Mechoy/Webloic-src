package weblogic.wsee.wstx.wsat.v10;

import weblogic.wsee.wstx.wsat.common.NotificationBuilder;
import weblogic.wsee.wstx.wsat.v10.types.Notification;

public class NotificationBuilderImpl extends NotificationBuilder<Notification> {
   public Notification build() {
      return new Notification();
   }
}
