package weblogic.jms.frontend;

import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import javax.jms.JMSException;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import weblogic.jms.client.JMSConnection;
import weblogic.jms.dispatcher.DispatcherWrapper;

public class _FEConnectionFactoryRemote_Stub extends Stub implements FEConnectionFactoryRemote {
   private static final String[] TYPE_IDS = new String[]{"RMI:weblogic.jms.frontend.FEConnectionFactoryRemote:0000000000000000"};

   public String[] _ids() {
      return TYPE_IDS;
   }

   public JMSConnection connectionCreate(DispatcherWrapper var1) throws JMSException, RemoteException {
      try {
         InputStream var2 = null;

         JMSConnection var4;
         try {
            OutputStream var3 = (OutputStream)this._request("connectionCreate__weblogic_jms_dispatcher_DispatcherWrapper", true);
            var3.write_value(var1, DispatcherWrapper.class);
            var2 = (InputStream)this._invoke(var3);
            var4 = (JMSConnection)var2.read_value(JMSConnection.class);
            return var4;
         } catch (ApplicationException var10) {
            var2 = (InputStream)var10.getInputStream();
            String var14 = var2.read_string();
            if (var14.equals("IDL:javax/jms/JMSEx:1.0")) {
               throw (JMSException)var2.read_value(JMSException.class);
            }

            throw new UnexpectedException(var14);
         } catch (RemarshalException var11) {
            var4 = this.connectionCreate(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var4;
      } catch (SystemException var13) {
         throw Util.mapSystemException(var13);
      }
   }

   public JMSConnection connectionCreate(DispatcherWrapper var1, String var2, String var3) throws JMSException, RemoteException {
      try {
         InputStream var4 = null;

         JMSConnection var6;
         try {
            OutputStream var5 = (OutputStream)this._request("connectionCreate__weblogic_jms_dispatcher_DispatcherWrapper__CORBA_WStringValue__CORBA_WStringValue", true);
            var5.write_value(var1, DispatcherWrapper.class);
            var5.write_value(var2, String.class);
            var5.write_value(var3, String.class);
            var4 = (InputStream)this._invoke(var5);
            var6 = (JMSConnection)var4.read_value(JMSConnection.class);
            return var6;
         } catch (ApplicationException var12) {
            var4 = (InputStream)var12.getInputStream();
            String var16 = var4.read_string();
            if (var16.equals("IDL:javax/jms/JMSEx:1.0")) {
               throw (JMSException)var4.read_value(JMSException.class);
            }

            throw new UnexpectedException(var16);
         } catch (RemarshalException var13) {
            var6 = this.connectionCreate(var1, var2, var3);
         } finally {
            this._releaseReply(var4);
         }

         return var6;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public JMSConnection connectionCreateRequest(FEConnectionCreateRequest var1) throws JMSException, RemoteException {
      try {
         InputStream var2 = null;

         JMSConnection var4;
         try {
            OutputStream var3 = (OutputStream)this._request("connectionCreateRequest", true);
            var3.write_value(var1, FEConnectionCreateRequest.class);
            var2 = (InputStream)this._invoke(var3);
            var4 = (JMSConnection)var2.read_value(JMSConnection.class);
            return var4;
         } catch (ApplicationException var10) {
            var2 = (InputStream)var10.getInputStream();
            String var14 = var2.read_string();
            if (var14.equals("IDL:javax/jms/JMSEx:1.0")) {
               throw (JMSException)var2.read_value(JMSException.class);
            }

            throw new UnexpectedException(var14);
         } catch (RemarshalException var11) {
            var4 = this.connectionCreateRequest(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var4;
      } catch (SystemException var13) {
         throw Util.mapSystemException(var13);
      }
   }
}
