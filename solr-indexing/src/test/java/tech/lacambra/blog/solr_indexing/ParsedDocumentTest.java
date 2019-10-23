package tech.lacambra.blog.solr_indexing;


import org.junit.Assert;
import org.junit.Test;

public class ParsedDocumentTest {

  ParsedDocument cut = new ParsedDocument("test.adoc");

  @Test
  public void parseHeaders() {

    String header = ":my.header.name: With some string value";
    String headerName = cut.parseHeaderName(header);
    String headerValue = cut.parseHeaderValue(header);

    Assert.assertEquals("my.header.name", headerName);
    Assert.assertEquals("With some string value", headerValue);

  }

  @Test
  public void parseTrimHeaders() {

    String header = " : my.header.name : With some string value ";
    String headerName = cut.parseHeaderName(header);
    String headerValue = cut.parseHeaderValue(header);

    Assert.assertEquals("my.header.name", headerName);
    Assert.assertEquals("With some string value", headerValue);

  }

}
