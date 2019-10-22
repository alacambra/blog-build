package tech.lacambra.blog.solr_indexing;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;

public class Indexer {

  public static void main(String[] args) throws IOException, SolrServerException {

    Indexer indexer = new Indexer();
//    String collection = "configset-ex";
    String collection = "blog-solr";

    final SolrInputDocument doc = new SolrInputDocument();
    String id = "id1";
    doc.addField("id", id);
    doc.addField("title", "Amazon Kindle Paperwhite");
    doc.addField("text", "lalala");
    doc.addField("reducedText", "lalala2");

    try (HttpSolrClient client = indexer.getClient()) {
      UpdateResponse updateResponse = client.add(collection, doc);
// Indexed documents must be committed
      client.commit(collection);

      final SolrQuery query = new SolrQuery("lalala2");
      query.addField("id");
//    query.addField("_text_");
      query.addField("text");
      query.addField("reducedText");
      query.setSort("id", SolrQuery.ORDER.asc);

      final QueryResponse response = client.query(collection, query);
      System.out.println(response);
    }

  }

  private HttpSolrClient getClient() {
    final String solrUrl = "http://solr-blog-blog.apps.oc.lacambra/solr";
//    final String solrUrl = "http://solr-demo-blog.apps.oc.lacambra/solr";
    return new HttpSolrClient.Builder(solrUrl)
        .withConnectionTimeout(10000)
        .withSocketTimeout(60000)
        .build();
  }
}
