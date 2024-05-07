<?xml version="1.0"?>
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:java="java"
  xmlns:weblogic="weblogic"
  version="1.0"
>
  <xsl:output method="xml"/>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    This style sheet includes the templates that upgrades the
    domain version

    Note:

    It is often helpful when debugging XSLT problems to just run
    this script against an 8.1 config.xml to generate and view the
    temporary config.xml vs. doing a full domain upgrade which never
    writes the temporary config.xml to disk.

    To do this, use:
    
      java weblogic.management.provider.internal.ConfigTransformer config.xml config.temp.xml

    Copyright(c) 2005 BEA Systems, Inc. All Rights Reserved.
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

      <xsl:choose>

        <!-- if the domain did not have a domain version, write out the one specified by the user -->
        <xsl:when test="not(string(./@ConfigurationVersion))">
          <xsl:attribute name="ConfigurationVersion">
            <xsl:value-of select="weblogic:upgrade.domain.directoryselection.SelectWebLogicVersionPlugIn.getUserSpecifiedDomainVersion()"/>
          </xsl:attribute>
        </xsl:when>

        <!-- if the domain did have a domain version, verify that it matches the one specified by the user -->
        <xsl:otherwise>
            <xsl:value-of select="weblogic:upgrade.domain.directoryselection.SelectWebLogicVersionPlugIn.validateDomainVersion(./@ConfigurationVersion)"/>
        </xsl:otherwise>

      </xsl:choose>

      <!-- copy the children -->
      <xsl:apply-templates select="*|text()|comment()"/>

    </xsl:element>
  </xsl:template>

</xsl:stylesheet>
