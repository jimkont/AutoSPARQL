package org.aksw.autosparql.commons.nlp.ner;

import com.aliasi.tag.Tagging;
import org.aksw.autosparql.commons.nlp.pos.PartOfSpeechTagger;
import org.aksw.autosparql.commons.util.URLlib;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LTPNamedEntityTagger{

    private static final String NAMED_ENTITY_TAG_DELIMITER = "_";

	/** if you only use it single threadedly just use the singleton to save initialization time */
	public static final LTPNamedEntityTagger INSTANCE = new LTPNamedEntityTagger();
    private String url_get_base = "http://api.ltp-cloud.com/analysis/?";
    private String api_key = "h144t7i7MFixIcmauzQh3ETM7iVIpn1BJeOoWN5K";
    private static final URLlib urllib = new URLlib();

	protected LTPNamedEntityTagger(){

	}

	public String getName() {
		return "LTP Named Entity Tagger";
	}

	public String getAnnotatedSentence(String text) {
		String out = "";
        StringBuffer content = urllib.urlopen(url_get_base+"api_key="+api_key+"&text="+text+"&format=xml&pattern=ner");

        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(content.toString()));

            Document doc = db.parse(is);

            NodeList nodes = doc.getElementsByTagName("sent");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);

                NodeList words = element.getElementsByTagName("word");

                for(int j=0;j<words.getLength();j++){
                    Element word = (Element) words.item(j);
                    String cont = word.getAttribute("cont");
                    String ner = word.getAttribute("ne");
                    out += " " + cont + NAMED_ENTITY_TAG_DELIMITER + ner;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

		return out.trim();
	}
}
