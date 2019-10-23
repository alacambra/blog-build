package tech.lacambra.blog.solr_indexing;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.Document;
import org.asciidoctor.jruby.ast.impl.BlockImpl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.asciidoctor.jruby.AsciidoctorJRuby.Factory.create;

public class Indexer {

  public static void main(String[] args) {

    Indexer indexer = new Indexer();
    Stream.of("test.adoc", "test1.adoc").forEach(n -> {

      ParsedDocument parsedDocument = null;

      try {
        parsedDocument = indexer.parseAdocText(n);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      indexer.indexDoc(parsedDocument);
    });

  }

  private void indexDoc(ParsedDocument parsedDocument) {

    final SolrInputDocument doc = new SolrInputDocument();

    doc.addField("id", parsedDocument.getHeaderValue("doc-id").orElseThrow(() -> new RuntimeException("Id must be given")));
    doc.addField("url", parsedDocument.getUrl());
    doc.addField("title", parsedDocument.getHeaderValue("jbake-title").orElse(""));
    doc.addField("description", parsedDocument.getHeaderValue("description").orElse(""));
    doc.addField("reducedText", parsedDocument.getTextResume());
    doc.addField("text", parsedDocument.getBodyText());

    String collection = "blog-solr";

    try (HttpSolrClient client = getClient()) {

      client.add(collection, doc);
      client.commit(collection);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void indexAndGet() {
    String collection = "blog-solr";

    final SolrInputDocument doc = new SolrInputDocument();
    String id = "id1";
    doc.addField("id", id);
    doc.addField("title", "Amazon Kindle Paperwhite");
    doc.addField("text", "lalala");
    doc.addField("reducedText", "poc");

    try (HttpSolrClient client = getClient()) {
      UpdateResponse updateResponse = client.add(collection, doc);
// Indexed documents must be committed
      client.commit(collection);

      final SolrQuery query = new SolrQuery("poc");
      query.addField("id");
//    query.addField("_text_");
      query.addField("text");
      query.addField("reducedText");
      query.setSort("id", SolrQuery.ORDER.asc);

      final QueryResponse response = client.query(collection, query);
      System.out.println(response);
    } catch (IOException | SolrServerException e) {
      e.printStackTrace();
    }
  }

  private HttpSolrClient getClient() {
    final String solrUrl = "http://solr-blog-blog.apps.oc.lacambra/solr";
    return new HttpSolrClient.Builder(solrUrl)
        .withConnectionTimeout(10000)
        .withSocketTimeout(60000)
        .build();
  }

  private ParsedDocument parseAdocText(String name) throws URISyntaxException, IOException {
    ParsedDocument parsedDocument = new ParsedDocument(name);

    Files.readAllLines(Paths.get(getClass().getClassLoader().getResource(name).toURI())).forEach(parsedDocument::parseLine);

    return parsedDocument;
  }

  private void parseAdocNative() {

    Asciidoctor asciidoctor = create();
    Document document = asciidoctor.loadFile(
        new File("/Users/albertlacambra/git/lacambra.tech/blog-build/solr-indexing/src/main/resources/test.adoc"),
        new HashMap<>());
    System.out.println(document.getDoctitle());

    document.getBlocks().stream().map(s -> ((BlockImpl) s))
        .forEach(s -> {
          System.out.println("------------------------------------------");
          System.out.println(s.getLines());
          System.out.println(s.getContent());
        });

  }
}
