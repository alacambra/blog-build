package tech.lacambra.blog.solr_indexing;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.tomitribe.crest.Main;
import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Default;
import org.tomitribe.crest.api.Option;
import org.tomitribe.crest.environments.SystemEnvironment;

import java.util.logging.Logger;


public class Cli {

  private static final Logger LOGGER = Logger.getLogger(Cli.class.getName());
  private String collection = "blog-solr";

  @Command("check")
  public void check() {
    LOGGER.info("[check] OK!");
  }


  @Command("delete")
  public void delete(@Option("id") @Default("") String id) {

    if (id.isEmpty()) {
      deleteAll();
    } else {
      deleteId(id);
    }
  }

  @Command("reindex")
  public void reindex(@Option("path") @Default("") String path) {

    if (path.isEmpty()) {
      throw new RuntimeException("invalid path=" + path);
    }

    deleteAll();

    try (HttpSolrClient client = SolrClientProvider.getClient()) {
      Indexer indexer = new Indexer(client);
      indexer.indexAll(path);
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  public static void main(String[] args) throws Exception {
    Main main = new Main(Cli.class);
    main.main(new SystemEnvironment(), args);
  }

  private void deleteAll() {
    try (HttpSolrClient client = SolrClientProvider.getClient()) {

      if ("".isEmpty()) {
        SolrQuery q = new SolrQuery("*:*");
        q.addField("id");

        QueryResponse r = client.query(collection, q);

        while (!r.getResults().isEmpty()) {
          r.getResults().stream()
              .map(d -> d.get("id"))
              .forEach(id -> {
                try {
                  UpdateResponse ur = client.deleteById(collection, (String) id);
                  client.commit(collection);
                  LOGGER.info("[main] Deleted document " + id);

                } catch (Exception e) {
                  LOGGER.info("[main] Error deleting " + id + " : " + e.getMessage());
                }
              });

          r = client.query(collection, q);
        }
      }


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void deleteId(String id) {
    try (HttpSolrClient client = SolrClientProvider.getClient()) {

      UpdateResponse ur = client.deleteById(collection, (String) id);
      client.commit(collection);
      LOGGER.info("[main] Deleted document " + id);

    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }
}