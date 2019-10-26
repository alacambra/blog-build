package tech.lacambra.blog.solr_indexing;

import java.util.*;
import java.util.stream.Collectors;

public class ParsedDocument {

  private Map<String, String> headers;
  private List<String> body;
  private String fileName;

  public ParsedDocument(String fileName) {
    this.fileName = fileName.replace(".adoc", ".html");
    headers = new HashMap<>();
    body = new ArrayList<>();
  }

  public ParsedDocument parseLine(String line) {

    line = line.trim();

    if (line.startsWith(":")) {

      String headerName = parseHeaderName(line);
      String headerValue = parseHeaderValue(line);
      headers.put(headerName, headerValue);

    } else {

      body.add(line);

    }

    return this;
  }

  String parseHeaderName(String header) {

    header = header.trim();

    int index = header.indexOf(":", 1);
    String headerName = header.substring(1, index);

    return headerName.trim();
  }

  String parseHeaderValue(String header) {

    header = header.trim();

    int index = header.indexOf(":", 1);
    String headerValue = header.substring(index + 1);

    return headerValue.trim();
  }

  public List<String> getBodyLines() {
    return new ArrayList<>(body);
  }

  public String getBodyText() {
    return String.join("\n", body);
  }

  public Map<String, String> getHeaders() {
    return new HashMap<>(headers);
  }

  public String getTextResume() {
    return body.stream().limit(5).collect(Collectors.joining("\n"));
  }

  public Optional<String> getHeaderValue(String headerName) {
    return Optional.ofNullable(headers.get(headerName));
  }

  public String getUrl() {
    return fileName.substring(fileName.indexOf("content") + "content".length());
  }

  public boolean isPosted() {
    return headers.getOrDefault("jbake-type", "").equals("post");
  }

  public String getDisplayDate() {
    return headers.getOrDefault("jbake-date", "");
  }

  public String getFileName() {
    return fileName;
  }
}
