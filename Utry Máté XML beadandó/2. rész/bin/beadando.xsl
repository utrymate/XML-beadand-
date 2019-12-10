<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:e="beadando">
    <xsl:template match="e:alkalmazasfejlesztes">
        <html>
            <head></head>
            <body>
                <h1>3. feladat: 3 XSL lekérdezés</h1>
                <h2>1. CSharp-ban és Java-ban írt alkalmazások adatai</h2>
                <table border="1">
                    <tr>
                        <th>Nyelv</th>
                        <th>Megrendelő neve</th>
                        <th>Telefon</th>
                        <th>Felhasználó neve</th>
                    </tr>
                    <xsl:for-each select="e:alkalmazasok/e:alkalmazas[e:nyelv = 'CSharp' or e:nyelv = 'Java']">
                        <tr>
                            <xsl:variable name="mkod" select="@mkod" />
                            <xsl:variable name="megrendelo" select="../../e:megrendelok/e:megrendelo[$mkod = @mkod]" />
                            <td>
                                <xsl:value-of select="e:nyelv" />
                            </td>
                            <td>
                                <xsl:value-of select="$megrendelo/e:nev" />
                            </td>
                            <td>
                                <xsl:value-of select="$megrendelo/e:telefon" />
                            </td>
                            <td>
                                <xsl:value-of select="../../e:felhasznalok/e:felhasznalo[$megrendelo/@fkod = @fkod]/e:nev" />
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
                <br/>
                <h2>2. Az elmúlt 30 év kerek éveiben kivitelezett alkalmazások fejlesztőinek adatai</h2>
                <table border="1">
                    <tr>
                        <th>Név</th>
                        <th>Lakcím</th>
                        <th>Kivitelezés éve</th>
                    </tr>
                    <xsl:for-each select="e:kivitelezesek/e:kivitelezes[e:datum = '2010' or e:datum = '2000' or e:datum = '1990']">
                        <tr>
                            <xsl:variable name="szkod" select="@szkod" />
                            <xsl:for-each select="../../e:szoftverfejlesztok/e:szoftverfejleszto">
                                <xsl:if test="$szkod = @szkod">
                                    <td>
                                        <xsl:value-of select="e:nev" />
                                    </td>
                                    <td>
                                        <xsl:value-of select="e:lakcim" />
                                    </td>
                                </xsl:if>
                            </xsl:for-each>
                                    <td>
                                        <xsl:value-of select="e:datum" />
                                    </td>   
                        </tr>
                    </xsl:for-each>
                </table>    
                <br/>
                <h2>3. Információk a Windows XP alkalmazásról</h2>
                <table border="1">
                    <tr>
                        <th>Megrendelő neve</th>
                        <th>Telefonszáma</th>
                        <th>Felhasználó felhasználóneve</th>
                        <th>Nyelv</th>
                    </tr>
                    <xsl:for-each select="e:alkalmazasok/e:alkalmazas[e:nev = 'Windows XP']">
                        <tr>
                            <xsl:variable name="mkod" select="@mkod" />
                            <xsl:variable name="megrendelo" select="../../e:megrendelok/e:megrendelo[$mkod = @mkod]" />
                            <td>
                                <xsl:value-of select="$megrendelo/e:nev" />
                            </td>
                            <td>
                                <xsl:value-of select="$megrendelo/e:telefon" />
                            </td>
                            <td>
                                <xsl:value-of select="../../e:felhasznalok/e:felhasznalo[$megrendelo/@fkod = @fkod]/e:felhasznalonev" />
                            </td>
                            <td>
                                <xsl:value-of select="e:nyelv" />
                            </td>
                        </tr>
                    </xsl:for-each>
                </table> 
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>