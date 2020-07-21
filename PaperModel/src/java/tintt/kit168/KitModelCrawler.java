/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.kit168;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import tintt.crawler.BaseCrawler;
import tintt.dao.TagDAO;
import tintt.entities.Category;
import tintt.entities.Model;
import tintt.entities.Tag;
import tintt.utils.ElementChecker;
import tintt.utils.ParseUtils;
import tintt.utils.TextUtils;

/**
 *
 * @author TiTi
 */
public class KitModelCrawler extends BaseCrawler {

    private String pageUrl;
    private Category category;

    public KitModelCrawler(ServletContext context, String pageUrl, Category category) {
        super(context);
        this.pageUrl = pageUrl;
        this.category = category;
    }

    public Model getModel() {
        BufferedReader reader = null;
        Model model = null;
        try {
            reader = getBufferedReaderForUrl(pageUrl);
            System.out.println("page: " + pageUrl);
            String document = getModelDocument(reader);
            stAXParserForModel(document);
            return stAXParserForModel(document);
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(KitModelCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }

    private Model stAXParserForModel(String document)
            throws XMLStreamException, UnsupportedEncodingException {

        document = TextUtils.refineHtml(document);
        XMLEventReader eventReader = parseStringToXMLEventReader(document);

        String imageSrc = getModelImageSource(eventReader);
        Integer numOfSheets = getNumOfSheets(eventReader);
        String name = getModelName(eventReader);
        Integer numOfParts = null;
        Boolean hasInstruction = hasInstruction(eventReader);
        String link = pageUrl;
        System.out.println("link: " + link);
        List<Tag> tags = getTags(eventReader);
        Integer difficulty = 0;

        Model model = new Model(0, name, numOfSheets, numOfParts, difficulty,
                imageSrc, link, category, tags, hasInstruction);

        return model;
    }

    private String getModelDocument(BufferedReader reader) throws IOException {
        String document = "<modelDocument>";
        String line = "";
        boolean isStart = false;

        while ((line = reader.readLine()) != null) {
            if (!isStart && line.startsWith("<article")) {
                isStart = true;
            }

            if (isStart) {
                document += line + " ";
            }
            if (isStart && line.contains("</article>")) {
                break;
            }
        }
        document += "</modelDocument>";
        return document;
    }

    private String getModelName(XMLEventReader eventReader) throws XMLStreamException {
        String name = null;
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "h1", "class", "entry-title  post-content__title  h2")) {
                    event = (XMLEvent) eventReader.next();
                    Characters nameChars = event.asCharacters();
                    name = nameChars.getData();
                    return name;
                }
            }
        }

        return name;
    }

    private String getModelImageSource(XMLEventReader eventReader) {
        XMLEvent event;
        String src = null;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "img", "class",
                        "attachment-full size-full wp-post-image")) {
                    Attribute srcAttr = startElement.getAttributeByName(new QName("src"));
                    src = srcAttr.getValue();
                    return src;
                }
            }
        }
        return src;
    }

    private Integer getNumOfSheets(XMLEventReader eventReader) {
        int numOfSheet = 0;
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "span", "class", "glyphicon  glyphicon-comment")) {
                    eventReader.next();
                    event = (XMLEvent) eventReader.next();

                    Characters chars = event.asCharacters();
                    String text = chars.getData();

                    if (text.contains("Số tờ kit")) {
                        numOfSheet = ParseUtils.extractNumber(text);

                        return numOfSheet;
                    }
                }
            }
        }
        return numOfSheet;
    }

    private Boolean hasInstruction(XMLEventReader eventReader) {
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "p")) {
                    event = (XMLEvent) eventReader.next();
                    if (event.isStartElement() || event.isEndElement()) {
                        continue;
                    }
                    Characters chars = event.asCharacters();
                    String text = chars.getData();

                    if (text.contains("Hướng dẫn")) {
                        return !text.toLowerCase().contains("không");
                    }
                }
            }
        }
        return false;
    }

    private List<Tag> getTags(XMLEventReader eventReader) {
        List<Tag> tags = new ArrayList<>();
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "a", "rel", "tag")) {
                    event = (XMLEvent) eventReader.next();
                    Characters chars = event.asCharacters();

                    String tagName = chars.getData();
                    Tag tag = getTag(tagName);
                    tags.add(tag);
                }
            } else if (event.isEndElement()) {
                EndElement element = event.asEndElement();
                if (ElementChecker.isElementWith(element, "article")) {
                    break;
                }
            }
        }
        return tags;
    }

    private Tag getTag(String tagName) {
        TagDAO tagDAO = TagDAO.getInstance();
        return tagDAO.getAndInsertIfNewTag(tagName);
    }
}
