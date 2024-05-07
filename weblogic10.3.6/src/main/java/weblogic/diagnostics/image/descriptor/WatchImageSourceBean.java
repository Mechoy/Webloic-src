package weblogic.diagnostics.image.descriptor;

public interface WatchImageSourceBean {
   WatchAlarmStateBean[] getWatchAlarmStates();

   WatchAlarmStateBean createWatchAlarmState();
}
