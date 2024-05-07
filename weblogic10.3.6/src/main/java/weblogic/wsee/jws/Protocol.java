package weblogic.wsee.jws;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class Protocol implements Serializable {
   public static final int ID_JAVA = 0;
   public static final int ID_HTTP_GET = 1;
   public static final int ID_HTTP_POST = 2;
   public static final int ID_HTTP_SOAP = 3;
   public static final int ID_HTTP_SOAP12 = 4;
   public static final int ID_HTTP_XML = 5;
   public static final int ID_JMS_SOAP = 6;
   public static final int ID_JMS_SOAP12 = 7;
   public static final int ID_JMS_XML = 8;
   public static final Protocol JAVA = new Protocol(0, "java-call");
   public static final Protocol HTTP_GET = new Protocol(1, "form-get");
   public static final Protocol HTTP_POST = new Protocol(2, "form-post");
   public static final Protocol HTTP_SOAP = new Protocol(3, "http-soap");
   public static final Protocol HTTP_SOAP12 = new Protocol(4, "http-soap12");
   public static final Protocol HTTP_XML = new Protocol(5, "http-xml");
   public static final Protocol JMS_SOAP = new Protocol(6, "jms-soap");
   public static final Protocol JMS_SOAP12 = new Protocol(7, "jms-soap12");
   public static final Protocol JMS_XML = new Protocol(8, "jms-xml");
   private static final Protocol[] _BINDINGS;
   public static final int SCHEME_JAVA = 0;
   public static final int SCHEME_HTTP = 1;
   public static final int SCHEME_JMS = 2;
   private static final int[] _SCHEMES;
   private int _id;
   private transient String _name;
   static final long serialVersionUID = 1L;

   private Protocol(int var1, String var2) {
      this._id = var1;
      this._name = var2;
   }

   public int getID() {
      return this._id;
   }

   public String getName() {
      return this._name;
   }

   public static Protocol getProtocolByID(int var0) {
      if (var0 >= 0 && var0 < _BINDINGS.length) {
         return _BINDINGS[var0];
      } else {
         throw new IllegalArgumentException("Invalid protocol id");
      }
   }

   public static Protocol getProtocolByName(String var0) {
      for(int var1 = 0; var1 < _BINDINGS.length; ++var1) {
         if (_BINDINGS[var1].getName().equals(var0)) {
            return _BINDINGS[var1];
         }
      }

      throw new IllegalArgumentException("Invalid protocol name: " + var0);
   }

   public static boolean isSoap(Protocol var0) {
      return HTTP_SOAP == var0 || HTTP_SOAP12 == var0 || JMS_SOAP == var0 || JMS_SOAP12 == var0;
   }

   public static boolean isSoap11(Protocol var0) {
      return HTTP_SOAP == var0 || JMS_SOAP == var0;
   }

   public static boolean isSoap12(Protocol var0) {
      return HTTP_SOAP12 == var0 || JMS_SOAP12 == var0;
   }

   public String toString() {
      return this._name;
   }

   public static int getSchemeForProtocol(Protocol var0) {
      int var1 = var0.getID();
      return _SCHEMES[var1];
   }

   public static Protocol getProtocol(boolean var0, String var1) {
      // $FF: Couldn't be decompiled
   }

   private Object readResolve() throws ObjectStreamException {
      return _BINDINGS[this._id];
   }

   static {
      _BINDINGS = new Protocol[]{JAVA, HTTP_GET, HTTP_POST, HTTP_SOAP, HTTP_SOAP12, HTTP_XML, JMS_SOAP, JMS_SOAP12, JMS_XML};
      _SCHEMES = new int[]{0, 1, 1, 1, 1, 1, 2, 2, 2};
   }
}
