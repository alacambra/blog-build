package tech.lacambra.blog.solr_indexing;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Indexer {

  public static void main(String[] args) throws IOException {

    if (args.length < 1) {
      throw new RuntimeException("No content path given");
    }

    Indexer indexer = new Indexer();

    Path contentPath = Paths.get(args[0]);

    Files.walk(contentPath, 1)
        .filter(p -> !p.equals(contentPath) && Files.isDirectory(p))
        .flatMap(p -> {
          try {
            return Files.walk(p, 1);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .filter(p -> Files.isRegularFile(p))
        .map(p -> {

          try {
            return indexer.parseAdocText(p);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }

        })
        .filter(ParsedDocument::isPosted)
        .forEach(indexer::indexDoc);
  }

  private void indexDoc(ParsedDocument parsedDocument) {

    final SolrInputDocument doc = new SolrInputDocument();

    doc.addField("id", parsedDocument.getHeaderValue("doc-id").orElseThrow(() -> new RuntimeException("Id must be given for:" + parsedDocument.getUrl())));
    doc.addField("url", parsedDocument.getUrl());
    doc.addField("title", parsedDocument.getHeaderValue("jbake-title").orElse(""));
    doc.addField("description", parsedDocument.getHeaderValue("description").orElse(""));
    doc.addField("reducedText", parsedDocument.getTextResume());
    doc.addField("text", parsedDocument.getBodyText());
    doc.addField("date", parsedDocument.getDisplayDate());

    String collection = "blog-solr";

    try (HttpSolrClient client = getClient()) {

      client.add(collection, doc);
      client.commit(collection);

    } catch (Exception e) {
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

  private ParsedDocument parseAdocText(Path path) throws IOException {

    ParsedDocument parsedDocument = new ParsedDocument(path.toString());
    Files.readAllLines(path).forEach(parsedDocument::parseLine);

    return parsedDocument;
  }
}
