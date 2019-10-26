package tech.lacambra.blog.solr_indexing;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParsedDocumentTest {

  String filePath = "/../jbake-blog/content/blog/2019/create-solr-project-oc.html";

  ParsedDocument cut = new ParsedDocument(filePath);

  @Test
  public void parseHeaders() {

    String header = ":my.header.name: With some string value";
    String headerName = cut.parseHeaderName(header);
    String headerValue = cut.parseHeaderValue(header);

    assertEquals("my.header.name", headerName);
    assertEquals("With some string value", headerValue);

  }

  @Test
  public void parseTrimHeaders() {

    String header = " : my.header.name : With some string value ";
    String headerName = cut.parseHeaderName(header);
    String headerValue = cut.parseHeaderValue(header);

    assertEquals("my.header.name", headerName);
    assertEquals("With some string value", headerValue);

  }

  @Test
  public void parseUrl() {
    assertEquals(filePath, cut.getFileName());
    assertEquals("/blog/2019/create-solr-project-oc.html", cut.getUrl());
  }

}
