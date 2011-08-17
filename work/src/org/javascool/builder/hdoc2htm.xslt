<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sx="http://icl.com/saxon" extension-element-prefixes="sx">

  <!-- This XSLT translates the proglet's HML documentation to HTML3.2 http://www.w3.org/TR/REC-html32-19970114) -->

  <xsl:import href="./hml2htm.xslt"/>

  <sx:function name="sx:jvs2htm" xmlns:jvs2htm="java:org.javascool.builder.Jvs2Html">
    <xsl:param name="string"/>
    <sx:return select="jvs2htm:run($string)"/>
  </sx:function>

<!-- These tags produce javasccool's doc specific constructs -->

<xsl:template match="div|p">
  <xsl:choose>
    <xsl:when test="@class = 'sujet'">
      <div align="right">
        <xsl:if test ="count(intros) > 0">[<a href="#intros">introduction</a>]</xsl:if>
        <xsl:if test ="count(works) > 0">[<a href="#works">travail proposé</a>]</xsl:if>
        <xsl:if test ="count(notes) > 0">[<a href="#notes">remarques</a>]</xsl:if>
      </div>
      <xsl:call-template name="div"/>
    </xsl:when>
    <xsl:when test="@class = 'objectif'"><h2>Objectif.</h2><xsl:call-template name="div"/></xsl:when>
    <xsl:when test="@class = 'intros' or @class = 'works' or @class = 'notes'">
      <xsl:choose>
        <xsl:when test="@class = 'intros'"><h2>Introduction.</h2></xsl:when>
        <xsl:when test="@class = 'works'"><h2>Travail proposé.</h2></xsl:when>
        <xsl:when test="@class = 'notes'"><h2>Remarques.</h2></xsl:when>
      </xsl:choose>
      <div id="{@class}"><ol><xsl:for-each select="*"><li><xsl:call-template name="div"/></li></xsl:for-each></ol></div>
    </xsl:when>
    <xsl:when test="@class = 'code'"><p><table witdh="90%" border="1" align="center"><tr><td><xsl:call-template name="div"/></td></tr></table></p></xsl:when>
    <xsl:otherwise><xsl:call-template name="div"/></xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="l">
  <xsl:choose>
    <xsl:when test="@class = 'javascool'"><tt><a href="http://javascool.gforge.inria.fr" style="padding:0;margin:0;text-decoration:none">Java'sCool</a></tt></xsl:when>
    <xsl:when test="@class = 'note'"><sup><a href="{concat('#', @link)}"><xsl:value-of select="@link"/></a></sup></xsl:when>
    <xsl:otherwise><xsl:call-template name="l"/></xsl:otherwise>
  </xsl:choose>
</xsl:template>


<!-- These tags performs the Jvs2Htm conversion -->

<xsl:template match="code">
  <div class="code"><table width="90%" border="2"><tr><td>
    <xsl:value-of disable-output-escaping="yes" select="sx:jvs2htm(.)"/>
  </td></tr></table></div>
</xsl:template>

<!-- These tags allows to show pieces of code -->

<xsl:template match="r">
  <font color="#990000"><b><xsl:apply-templates/></b></font>
</xsl:template>

<xsl:template match="n">
  <font color="#505000"><xsl:apply-templates/></font>
</xsl:template>

<xsl:template match="v">
  <font color="#008000">&quot;<xsl:apply-templates/>&quot;</font>
</xsl:template>

<xsl:template match="m">
  <p><tt><font color="#202080"><big>//</big>&#160;<xsl:apply-templates/></font></tt></p>
</xsl:template>

<xsl:template match="T">
  &#160;&#160;&#160;
</xsl:template>

</xsl:stylesheet>
