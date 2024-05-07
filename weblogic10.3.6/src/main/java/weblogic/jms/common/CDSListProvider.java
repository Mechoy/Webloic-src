package weblogic.jms.common;

public interface CDSListProvider {
   DDMemberInformation[] registerListener(CDSListListener var1) throws javax.jms.JMSException;

   void unregisterListener(CDSListListener var1);
}
