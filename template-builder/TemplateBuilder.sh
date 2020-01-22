#!/usr/bin/java --source 11

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TemplateBuilder {

    public static void main(String[] args) throws Exception {

        String title = args.length > 0 ? args[0] : "";
        String description = args.length > 1 ? args[1] : "";
        String tags = args.length > 2 ? args[2] : "";
        String id = title.toLowerCase().replace(" ", "-");

        String template = "= __title__ \n" + "Albert Lacambra Basil \n" + ":jbake-title: __title__ \n"
                + ":description: __description__ \n" + ":jbake-date: __date__ \n" + ":jbake-type: post \n"
                + ":jbake-status: __status__ \n" + ":jbake-tags: __tags__ \n" + ":doc-id: __id__ \n";

        template = template.replaceAll("__title__", title).replaceAll("__description__", description)
                .replaceAll("__date__", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .replaceAll("__status__", "published").replaceAll("__tags__", tags).replaceAll("__id__", id);

        Path p = Files.createFile(Paths.get("../jbake-blog/content/blog/2020/" + id + ".adoc"));
        Files.write(p, template.getBytes());
    }
}