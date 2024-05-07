<?xml version="1.0"?>
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:java="java"
  xmlns:weblogic="weblogic"
  version="1.0"
>
  <xsl:output method="xml"/>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    This style sheet includes the templates that upgrade pre9.0 mbeans
    to 9.0 mbeans.

    Note:

    It is often helpful when debugging XSLT problems to just run
    this script against an 8.1 config.xml to generate and view the
    temporary config.xml vs. doing a full domain upgrade which never
    writes the temporary config.xml to disk.

    To do this, use:
    
      java weblogic.management.provider.internal.ConfigTransformer config.xml config.temp.xml

    Copyright(c) 2004 BEA Systems, Inc. All Rights Reserved.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Drive the upgrade by applying templates starting at the root of the
   document.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="/">
    <xsl:apply-templates select="*|text()|comment()"/>
  </xsl:template>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   By default, elements make a copy of themselves and their
   attributes.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="*">
    <xsl:element name="{name()}">
      <xsl:apply-templates select="@*|*|text()|comment()"/>
    </xsl:element>
  </xsl:template>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    By default, commments make a copy of themselves.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="comment()">
   <xsl:comment><xsl:value-of select="."/></xsl:comment>
  </xsl:template>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    By default, text nodes make a copy of themselves.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="text()">
   <xsl:value-of select="."/>
  </xsl:template>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    By default, attributes make a copy of themselves.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="@*">
    <xsl:attribute name="{name()}">
      <xsl:value-of select="."/>
    </xsl:attribute>
  </xsl:template>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Transform the Domain mbean.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="Domain">
    <xsl:element name="{name()}">

      <!-- copy the attributes -->
      <xsl:apply-templates select="@*"/>

      <!-- copy the children -->
      <xsl:apply-templates select="*|text()|comment()"/>

      <!-- create a SecurityConfiguration mbean if there was not one -->
      <xsl:if test="count(/Domain/SecurityConfiguration) &lt; 1">
        <xsl:element name="SecurityConfiguration">
        
        <!-- Turn off strict default URL, which is a new 9.0 feature. -->
          <xsl:attribute name="EnforceStrictURLPattern">
            <xsl:text>false</xsl:text>
          </xsl:attribute>

          <!-- Turn on compatiblity for connection filters. -->
          <xsl:attribute name="CompatibilityConnectionFiltersEnabled">
            <xsl:text>true</xsl:text>
          </xsl:attribute>

          <!-- give it the same name as the domain -->
          <xsl:attribute name="Name">
            <xsl:value-of select="./@Name"/>
          </xsl:attribute>

          <!-- copy in attrs from other mbeans, realms, providers, ulocks -->
          <xsl:call-template name="completeSecurityConfig"/>

        </xsl:element>
      </xsl:if>

    </xsl:element>
  </xsl:template>


  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Transform the SSL mbean.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="SSL">

    <xsl:element name="{name()}">

      <!--
       If IdentityAndTrustLocations is not set and the version is earlier than 8.0.
       set it to FilesOrKeyStoreProviders.
      -->
      <xsl:if test="not(string(./@IdentityAndTrustLocations))">

        <xsl:variable name="beforeVersion8">
          <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.beforeVersion(/Domain/@ConfigurationVersion,8)"/>
        </xsl:variable>

        <xsl:if test="$beforeVersion8='true'">
          <xsl:attribute name="IdentityAndTrustLocations">
            <xsl:text>FilesOrKeyStoreProviders</xsl:text>
          </xsl:attribute>
        </xsl:if>

      </xsl:if>

      <!-- copy the attributes -->
      <xsl:for-each select="@*">
        <xsl:choose>
          <!-- Remove obsolete attributes -->
          <xsl:when test="name()='MDAcceleration' or
                          name()='RC4Acceleration' or
                          name()='RSAAcceleration'"/>
          <!-- Copy the rest of the attributes -->
          <xsl:otherwise>
            <xsl:apply-templates select="."/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>

      <!-- copy the children -->
      <xsl:apply-templates select="*|text()|comment()"/>

    </xsl:element>
  </xsl:template>


  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Transform the Security mbean.

     Validate the commo security mbeans
     (terminates with a fatal error if there are any problems)

     copy its attributes except the ones moving to the
     SecurityConfiguration mbean (ie. connection filter attrs)

     do not copy its children (ie. realms, ulocks, providers) since
     they are moving to the SecurityConfiguration mbean.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="Security">

    <!-- Verify that the commo security mbeans are OK, terminate if not. -->
    <xsl:call-template name="validateCommoSecurityMBeans"/>

    <xsl:element name="{name()}">

      <!--
        Compute and set CompatibilityMode.
        Only write it out if it is 'true'.
      -->
      <xsl:variable name="compatibilityMode">
        <xsl:value-of
          select=
            "weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getCompatibilityMode(
               /Domain/@ConfigurationVersion,
               /Domain/Security/@RealmSetup,
               /Domain/Security/@CompatibilityMode,
               /Domain/Security/@Realm,
               count(/Domain/Security/weblogic.management.security.Realm)
             )"
        />
      </xsl:variable>

      <xsl:if test="$compatibilityMode='true'">
        <xsl:attribute name="CompatibilityMode">
          <xsl:text>true</xsl:text>
        </xsl:attribute>
      </xsl:if>

      <!-- handle attributes -->
      <xsl:for-each select="@*">
        <xsl:choose>

          <!-- Ignore the connection filter attributes -->
          <xsl:when test="name()='ConnectionFilter'      or
                          name()='ConnectionFilterRules' or
                          name()='ConnectionLoggerEnabled'"/>

          <!-- Ignore CompatibilityMode since we handled it above -->
          <xsl:when test="name()='CompatibilityMode'"/>

          <!-- Remove obsolete attributes -->
          <xsl:when test="name()='InteropEnabled'  or
                          name()='InteropUserName' or
                          name()='InteropPassword' or
                          name()='RealmSetup'"/>

          <!-- Copy the rest of the attributes -->
          <xsl:otherwise>
            <xsl:apply-templates select="."/>
          </xsl:otherwise>

        </xsl:choose>
      </xsl:for-each>

      <!-- copy the children (other than elements since they are handled elsewhere) -->
      <xsl:apply-templates select="text()|comment()"/>

    </xsl:element>
  </xsl:template>


  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Transform the SecurityConfiguration mbean.

   - copy its attributes and children

   - migrate data from the Security mbean to the
     SecurityConfiguration mbean.

   - add a reference from the SecurityConfiguration mbean to the
     default realm.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="SecurityConfiguration">

    <xsl:element name="{name()}">

      <!-- Turn off strict default URL, which is a new 9.0 feature. -->
      <xsl:attribute name="EnforceStrictURLPattern">
        <xsl:text>false</xsl:text>
      </xsl:attribute>
   
      <!-- Turn on compatiblity for connection filters. -->
      <xsl:attribute name="CompatibilityConnectionFiltersEnabled">
        <xsl:text>true</xsl:text>
      </xsl:attribute>

      <!-- copy SecurityConfig attributes -->
      <xsl:for-each select="@*">
        <xsl:choose>

          <!-- Remove the RealmBootStrapVersion (thus setting it to the default) -->
          <xsl:when test="name()='RealmBootStrapVersion'"/>

          <!-- Copy the rest of the attributes -->
          <xsl:otherwise>
            <xsl:apply-templates select="."/>
          </xsl:otherwise>

        </xsl:choose>
      </xsl:for-each>
      
      <!-- copy in attrs from other mbeans, realms, providers, ulocks -->
      <xsl:call-template name="completeSecurityConfig"/>

      <!-- copy the children -->
      <xsl:apply-templates select="*|text()|comment()"/>

    </xsl:element>
  </xsl:template>


  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Used during the validation phase to pass realm info to
    the java code that validates all the commo security mbeans.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="weblogic.management.security.Realm" mode="validate">
    <xsl:variable name="ignore">
      <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.validateRealm(
        ./@Name,
        ./@DisplayName,
        name(),
        ./@DefaultRealm,
        ./@Auditors,
        ./@AuthenticationProviders,
        ./@RoleMappers,
        ./@Authorizers,
        ./@Adjudicator,
        ./@CredentialMappers,
        ./@CertPathProviders,
        ./@CertPathBuilder,
        ./@KeyStores,
        ./@UserLockoutManager,
        ./@UseDeprecatedWebResource
      )"/>
    </xsl:variable>
  </xsl:template>


  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Used after the validation phase to copy a realm from the Security
    mbean to the SecurityConfiguration mbean, and copy the providers
    and user lockout manager into the realm.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="weblogic.management.security.Realm" mode="copy">

    <!-- if this realm does not have any cert path providers, add the default one -->
    <!--
      FIXME moreaut 11/9/2004 - this should always be true since pre9.0 configs
      do not support cert path providers.  only leaving this in now because of
      QA tests using 8.1 configs containing cert path providers because the
      9.0 config format is not stable.
    -->
    <xsl:variable name="addCertPath">
      <xsl:choose>
        <xsl:when test="string(@CertPathProviders)">
          <xsl:text>false</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>true</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="realmDisplayName">
      <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getDisplayName(./@DisplayName,name())"/>
    </xsl:variable>

    <xsl:variable name="realmObjectName">
      <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getObjectName(./@Name)"/>
    </xsl:variable>

    <xsl:variable name="wlcppType">
      <xsl:text>weblogic.security.providers.pk.WebLogicCertPathProvider</xsl:text>
    </xsl:variable>

    <xsl:variable name="wlcppDisplayName">
      <xsl:text>WebLogicCertPathProvider</xsl:text>
    </xsl:variable>

    <xsl:variable name="wlcppObjectName">
      <xsl:text>Security:Name=</xsl:text>
      <xsl:value-of select="$realmDisplayName"/>
      <xsl:value-of select="$wlcppDisplayName"/>
    </xsl:variable>

    <xsl:element name="{name()}">

      <!-- Note: XSLT requires that attributes are handled before children -->

      <!-- Add the WebLogicCertPathProvider if necessary -->
      <xsl:if test="$addCertPath='true'">
        <xsl:attribute name="CertPathBuilder">
          <xsl:value-of select="$wlcppDisplayName"/>
        </xsl:attribute>
      </xsl:if>

      <!-- Set the SecurityDDModel to Advanced for all pre-9.0 realms -->
      <xsl:attribute name="SecurityDDModel">
        <xsl:text>Advanced</xsl:text>
      </xsl:attribute>

      <!-- Disable combined role mappings for all pre-9.0 realms -->
      <xsl:attribute name="CombinedRoleMappingEnabled">
        <xsl:text>false</xsl:text>
      </xsl:attribute>

      <!-- handle the names -->
      <xsl:call-template name="transform-commo-names">
        <xsl:with-param name="displayName" select="./@DisplayName" />
        <xsl:with-param name="type"        select="name()"         />
      </xsl:call-template>

      <!-- handle attributes -->
      <xsl:for-each select="@*">

        <xsl:choose>

          <xsl:when test="name()='Name' or name()='DisplayName'">
            <!-- already handled the names so ignore these -->
          </xsl:when>

          <xsl:when test="name()='DefaultRealm'">
            <!--
              Ignore DefaultRealm since it is replaced with a reference
              from the SecurityConfiguration mbean
             -->
          </xsl:when>

          <xsl:when test="name()='UseDeprecatedWebResource'">
            <!--
              Ignore this attribute since it was deprecated in 7.0 and removed in 9.0
             -->
          </xsl:when>

          <xsl:when test="(
             name()='Auditors'                or
             name()='AuthenticationProviders' or
             name()='RoleMappers'             or
             name()='Authorizers'             or
             name()='Adjudicator'             or
             name()='CredentialMappers'       or
             name()='CertPathProviders'       or
             name()='KeyStores'               or
             name()='UserLockoutManager')">
            <!--
              the referenced providers and ulocks will be copied to the realm later
            -->
          </xsl:when>

          <!--
            FIXME moreaut 11/2/2004 - migrate cert path providers and builders as part of
            the scaffolding since we have QA tests with 8.1 style config.xmls containing
            9.0 cert path providers and builders.  We can remove this migration code
            once the tests are fixed.
          -->
          <xsl:when test="name()='CertPathBuilder'">

            <!-- add a CertPathBuilder attribute whose value is the display name of the builder -->
            <xsl:attribute name="{name()}">

              <xsl:variable name="objectNameWant">
                <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getObjectName(.)"/>
              </xsl:variable>

              <!-- loop over all the providers until we find one whose object name matches -->
              <xsl:for-each select="/Domain/Security/*">
                <xsl:variable name="objectNameHave">
                  <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getObjectName(@Name)"/>
                </xsl:variable>
                <xsl:if test="$objectNameWant=$objectNameHave">
                  <!-- we found the cert path builder.  set the attribute value to its computed display name -->
                  <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getDisplayName(@DisplayName,name())"/>
                </xsl:if>
              </xsl:for-each>

            </xsl:attribute>
          </xsl:when>

          <xsl:otherwise>
            <!-- Copy the rest of the attributes -->
            <xsl:apply-templates select="."/>
          </xsl:otherwise>

        </xsl:choose>
      </xsl:for-each>


      <!-- handle children -->

      <!-- Add the WebLogicCertPathProvider if necessary -->
      <xsl:if test="$addCertPath='true'">
        <xsl:element name="{$wlcppType}"/>
      </xsl:if>

      <!--
        Make copies of all providers and ulocks this realm refers to under this realm.
        Use the references from the realm to its children so that the providers are
        added in the correct invocation order (vs. using the back references from
        the providers to their realms).
      -->
      <xsl:call-template name="copy-realm-children"><xsl:with-param name="objectNames" select="./@Auditors"                /></xsl:call-template>
      <xsl:call-template name="copy-realm-children"><xsl:with-param name="objectNames" select="./@AuthenticationProviders" /></xsl:call-template>
      <xsl:call-template name="copy-realm-children"><xsl:with-param name="objectNames" select="./@RoleMappers"             /></xsl:call-template>
      <xsl:call-template name="copy-realm-children"><xsl:with-param name="objectNames" select="./@Authorizers"             /></xsl:call-template>
      <xsl:call-template name="copy-realm-children"><xsl:with-param name="objectNames" select="./@Adjudicator"             /></xsl:call-template>
      <xsl:call-template name="copy-realm-children"><xsl:with-param name="objectNames" select="./@CredentialMappers"       /></xsl:call-template>
      <xsl:call-template name="copy-realm-children"><xsl:with-param name="objectNames" select="./@CertPathProviders"       /></xsl:call-template>
      <xsl:call-template name="copy-realm-children"><xsl:with-param name="objectNames" select="./@KeyStores"               /></xsl:call-template>
      <xsl:call-template name="copy-realm-children"><xsl:with-param name="objectNames" select="./@UserLockoutManager"      /></xsl:call-template>

      <!-- copy the children -->
      <xsl:apply-templates select="*|text()|comment()"/>

    </xsl:element>

  </xsl:template>


  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Used during the validation phase to pass provider and user lockout
    manager info to the java code that validates all the commo
    security mbeans.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template match="*" mode="validate">
    <xsl:variable name="ignore">
      <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.validateProviderOrULock(
       ./@Name,
       ./@DisplayName,
       name(),
       ./@Realm
      )"/>
    </xsl:variable>
  </xsl:template>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Copy an ordered list of realm children into the realm
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template name="copy-realm-children">
    <xsl:param name="objectNames" />

    <!-- Get the first object name in the list -->
    <xsl:variable name="firstObjectName">
      <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getFirstObjectName($objectNames)"/>
    </xsl:variable>

    <xsl:if test="string($firstObjectName)">

      <!-- We have at least one object name -->

      <!--  Find the first child and copy it to the realm -->
      <xsl:for-each select="/Domain/Security/*">

        <xsl:variable name="objectNameHave">
          <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getObjectName(@Name)"/>
        </xsl:variable>

        <xsl:if test="$firstObjectName=$objectNameHave">
          <xsl:apply-templates select="." mode="copy"/>
        </xsl:if>

      </xsl:for-each>

      <!-- Recurse to copy the rest of the children -->

      <xsl:variable name="restOfObjectNames">
        <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getRestOfObjectNames($objectNames)"/>
      </xsl:variable>

      <xsl:call-template name="copy-realm-children">
        <xsl:with-param name="objectNames" select="$restOfObjectNames"/>
      </xsl:call-template>

    </xsl:if>

  </xsl:template>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Used after the validation phase to copy a provider or user lockout
    manager to its realm.
  -->
  <xsl:template match="*" mode="copy">
    <xsl:element name="{name()}">

      <!-- handle the names -->
      <xsl:call-template name="transform-commo-names">
        <xsl:with-param name="displayName" select="./@DisplayName" />
        <xsl:with-param name="type"        select="name()"         />
      </xsl:call-template>

      <!-- handle the attributes -->
      <xsl:for-each select="@*">
        <xsl:choose>

          <xsl:when test="name()='Name' or name()='DisplayName'">
            <!-- already handled the names so ignore these -->
          </xsl:when>

          <xsl:when test="name()='Realm'">
            <!-- drop the back reference to the realm -->
          </xsl:when>

          <xsl:otherwise>
            <!-- copy any other attributes as-is -->
            <xsl:apply-templates select="."/>
          </xsl:otherwise>

        </xsl:choose>
      </xsl:for-each>

      <!-- copy the children -->
      <xsl:apply-templates select="*|text()|comment()"/>

    </xsl:element>
  </xsl:template>


  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Utility to transform commo object names

   Set Name to the displayName if present, otherwise use the
   leaf name of the type.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template name="transform-commo-names">
    <xsl:param name="displayName" />
    <xsl:param name="type"        />
    <xsl:if test="string($displayName)">
      <xsl:attribute name="Name">
        <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getDisplayName($displayName,$type)"/>
      </xsl:attribute>
    </xsl:if>
  </xsl:template>


  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Validate the commo security mbeans, eg. bidirectional references,
    only one default realm, unique names.

    If there are problems, throws a fatal error containing the
    first one.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template name="validateCommoSecurityMBeans">

    <!-- start validation -->
    <xsl:variable name="ignore1">
      <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.startValidation()"/>
    </xsl:variable>

    <!-- have the commo security mbeans send their data to the validation code -->
    <xsl:apply-templates select="/Domain/Security/*" mode="validate"/>

    <!--
      end the validation.
      it throws an exception (which terminates the XSLT transform) if there is any problem.
    -->
    <xsl:variable name="ignore2">
      <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.endValidation()"/>
    </xsl:variable>
  </xsl:template>


  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Migrates data that lives in other mbeans to the
    SecurityConfiguration mbean.  The current output context must
    be a SecurityConfiguration mbean.

   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template name="completeSecurityConfig">

    <!-- migrate attributes from the Security mbean -->
    <xsl:apply-templates select="/Domain/Security/@ConnectionFilter"/>
    <xsl:apply-templates select="/Domain/Security/@ConnectionFilterRules"/>
    <xsl:apply-templates select="/Domain/Security/@ConnectionLoggerEnabled"/>

    <!--
      add a reference from the SecurityConfiguration mbean to the default Realm

      Cannot just select based on a realm DefaultRealm attribute being 'true'
      because of case matching problems (e.g 'True' vs. 'true').
      So, need to loop over all of them.
    -->
    <xsl:for-each select="/Domain/Security/weblogic.management.security.Realm">
      <xsl:if test="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getIsDefaultRealm(./@DefaultRealm)='true'">
        <xsl:attribute name="DefaultRealm">
          <xsl:value-of select="weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getDisplayName(./@DisplayName,name())"/>
        </xsl:attribute>
      </xsl:if>
    </xsl:for-each>

    <!-- handle children -->

    <!--
      Migrate realms from the Security mbean to the SecurityConfiguration mbean
    -->
    <xsl:for-each select="/Domain/Security/*">

      <!-- always copy realms -->
      <xsl:if test="name()='weblogic.management.security.Realm'">
        <xsl:apply-templates select="." mode="copy"/>
      </xsl:if>

      <!-- don't have to worry about orphaned providers and user lockout managers since the validation phase already rejected them -->
    </xsl:for-each>

    <xsl:if test="count(/Domain/Security/weblogic.management.security.Realm) &lt; 1">
      <xsl:call-template name="createDefaultSecurityConfig"/>
    </xsl:if>

  </xsl:template>


  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Create the CompatibilityRealm (for 6.x security).

    This must be called when the current output element is the
    SecurityConfigurationMBean.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template name="createCompatibilityRealm">

    <xsl:element name="weblogic.management.security.Realm">

      <xsl:attribute name="Name">            <xsl:text>CompatibilityRealm</xsl:text>       </xsl:attribute>
      <xsl:attribute name="SecurityDDModel"> <xsl:text>Advanced</xsl:text>                 </xsl:attribute>
      <xsl:attribute name="CombinedRoleMappingEnabled"> <xsl:text>false</xsl:text>         </xsl:attribute>
      <xsl:attribute name="CertPathBuilder"> <xsl:text>WebLogicCertPathProvider</xsl:text> </xsl:attribute>

      <xsl:element name="weblogic.security.providers.realmadapter.RealmAdapterAuthenticator">
        <xsl:attribute name="ActiveTypes"><xsl:text>AuthenticatedUser</xsl:text></xsl:attribute>
      </xsl:element>

      <xsl:element name="weblogic.security.providers.authorization.DefaultRoleMapper"/>

      <xsl:element name="weblogic.security.providers.realmadapter.RealmAdapterAuthorizer"/>

      <xsl:element name="weblogic.security.providers.authorization.DefaultAuthorizer"/>

      <xsl:element name="weblogic.security.providers.realmadapter.RealmAdapterAdjudicator"/>

      <xsl:element name="weblogic.security.providers.credentials.DefaultCredentialMapper"/>

      <xsl:element name="weblogic.security.providers.pk.WebLogicCertPathProvider"/>

      <xsl:element name="weblogic.security.providers.pk.DefaultKeyStore"/>

      <xsl:element name="weblogic.management.security.authentication.UserLockoutManager"/>

    </xsl:element>

  </xsl:template>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Create the default realm for 7.0+ security.

    Need to pass in whether or not the domain is from before 8x
    since before 8x, myrealm included a DefaultKeyStore.

    This must be called when the current output element is the
    SecurityConfigurationMBean.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template name="createDefaultRealm">

    <xsl:param name="before8x"/>

    <xsl:element name="weblogic.management.security.Realm">

      <xsl:attribute name="Name">            <xsl:text>myrealm</xsl:text>                  </xsl:attribute>
      <xsl:attribute name="SecurityDDModel"> <xsl:text>Advanced</xsl:text>                 </xsl:attribute>
      <xsl:attribute name="CombinedRoleMappingEnabled"> <xsl:text>false</xsl:text>         </xsl:attribute>
      <xsl:attribute name="CertPathBuilder"> <xsl:text>WebLogicCertPathProvider</xsl:text> </xsl:attribute>

      <xsl:element name="weblogic.security.providers.authentication.DefaultAuthenticator"/>

      <xsl:element name="weblogic.security.providers.authentication.DefaultIdentityAsserter">
        <xsl:attribute name="ActiveTypes"><xsl:text>AuthenticatedUser</xsl:text></xsl:attribute>
      </xsl:element>

      <xsl:element name="weblogic.security.providers.authorization.DefaultRoleMapper"/>

      <xsl:element name="weblogic.security.providers.authorization.DefaultAuthorizer"/>

      <xsl:element name="weblogic.security.providers.authorization.DefaultAdjudicator"/>

      <xsl:element name="weblogic.security.providers.credentials.DefaultCredentialMapper"/>

      <xsl:element name="weblogic.security.providers.pk.WebLogicCertPathProvider"/>

      <xsl:if test="$before8x='true'">
        <xsl:element name="weblogic.security.providers.pk.DefaultKeyStore"/>
      </xsl:if>

      <xsl:element name="weblogic.management.security.authentication.UserLockoutManager"/>

    </xsl:element>

  </xsl:template>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Create the default set of security realms, providers and ulocks
    based upon the version of the config.xml that is being upgraded.

    This must be called when the current output element is the
    SecurityConfigurationMBean.
   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
  <xsl:template name="createDefaultSecurityConfig">

    <!-- returns 6/7/8 only, do not bother checking this -->
    <xsl:variable name="configVersion">
      <xsl:value-of
        select=
          "weblogic:security.internal.SecurityPre90UpgradeXSLTUtils.getDefaultSecurityConfigVersion(
             /Domain/@ConfigurationVersion,
             /Domain/Security/@RealmSetup,
             /Domain/Security/@CompatibilityMode,
             /Domain/Security/@Realm
           )"
      />
    </xsl:variable>

    <!-- Find the name of the default realm - CompatibilityRealm for 6x, myrealm for 7x/8x -->
    <xsl:variable name="defaultRealmDisplayName">
      <xsl:choose>
        <xsl:when test="$configVersion=6">
          <xsl:text>CompatibilityRealm</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>myrealm</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!-- Determine if the config is before 8x -->
    <xsl:variable name="before8x">
      <xsl:choose>
        <xsl:when test="$configVersion=8">
          <xsl:text>false</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>true</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!-- Make the SecurityConfiguration mbean refer to the default realm -->
    <xsl:attribute name="DefaultRealm">
      <xsl:value-of select="$defaultRealmDisplayName"/>
    </xsl:attribute>

    <!-- Create the CompatibilityRealm for 6x only -->
    <xsl:if test="$configVersion=6">
      <xsl:call-template name="createCompatibilityRealm"/>
    </xsl:if>

    <!-- Create myrealm always, if it is before 8.x then it needs a DefaultKeyStore too -->
    <xsl:call-template name="createDefaultRealm">
      <xsl:with-param name="before8x" select="$before8x"/>
    </xsl:call-template>

  </xsl:template>

</xsl:stylesheet>
