package tech.lacambra.blog.solr_indexing;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Indexer {
  private static final Logger LOGGER = Logger.getLogger(Indexer.class.getName());
  private HttpSolrClient client;

  public static void main(String[] args) throws IOException {

    if (args.length < 1) {
      throw new RuntimeException("No content path given");
    }

    try (HttpSolrClient client = SolrClientProvider.getClient()) {
      Indexer indexer = new Indexer(client);
      indexer.indexAll(args[0]);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Indexer(HttpSolrClient client) {
    this.client = client;
  }

  public void indexAll(String path) {
    Path contentPath = Paths.get(path);
    try {
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
              return parseAdocText(p);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }

          })
          .filter(ParsedDocument::isPosted)
          .forEach(this::indexDoc);
    } catch (IOException e) {
      LOGGER.info("[indexAll] Error: " + e.getMessage());
    }
  }

  private void indexDoc(ParsedDocument parsedDocument) {

    final SolrInputDocument doc = new SolrInputDocument();

    String id = parsedDocument.getHeaderValue("doc-id").orElseThrow(() -> new RuntimeException("Id must be given for:" + parsedDocument.getUrl()));
    doc.addField("id", id);
    doc.addField("url", parsedDocument.getUrl());
    doc.addField("title", parsedDocument.getHeaderValue("jbake-title").orElse(""));
    doc.addField("description", parsedDocument.getHeaderValue("description").orElse(""));
    doc.addField("reducedText", parsedDocument.getTextResume());
    doc.addField("text", parsedDocument.getBodyText());
    doc.addField("date", parsedDocument.getDisplayDate());

    String collection = "blog-solr";

    try {
      client.add(collection, doc);
      client.commit(collection);
      LOGGER.info("[indexDoc] Indexed document " + id);

    } catch (SolrServerException | IOException e) {
      e.printStackTrace();
    }


  }

  private ParsedDocument parseAdocText(Path path) throws IOException {

    ParsedDocument parsedDocument = new ParsedDocument(path.toString());
    Files.readAllLines(path).forEach(parsedDocument::parseLine);

    return parsedDocument;
  }
}
