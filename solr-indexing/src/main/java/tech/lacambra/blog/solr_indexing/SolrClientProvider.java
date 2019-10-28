package tech.lacambra.blog.solr_indexing;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class SolrClientProvider {

  public static HttpSolrClient getClient() {
    final String solrUrl = "http://solr-blog-blog.apps.oc.lacambra/solr";
    return new HttpSolrClient.Builder(solrUrl)
        .withConnectionTimeout(10000)
        .withSocketTimeout(60000)
        .build();
  }

}
