package org.aksw.autosparql.commons.nlp.pos;

import com.aliasi.tag.Tagging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import org.aksw.autosparql.commons.util.URLlib;
import java.net.*;
import java.io.*;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class LTPPartOfSpeechTagger implements PartOfSpeechTagger{

	/** if you only use it single threadedly just use the singleton to save initialization time */
	public static final LTPPartOfSpeechTagger INSTANCE = new LTPPartOfSpeechTagger();
    private String url_get_base = "http://api.ltp-cloud.com/analysis/?";
    private String api_key = "h144t7i7MFixIcmauzQh3ETM7iVIpn1BJeOoWN5K";
    private static final URLlib urllib = new URLlib();
    private HashMap<String, String > pos_map = new HashMap<String, String>(); //map the chinese pos to English one

	protected LTPPartOfSpeechTagger(){
        pos_map.put("Ag", "JJ");
        pos_map.put("Dg", "RB");
        pos_map.put("Ng", "NN");
        pos_map.put("Tg", "NNP");
        pos_map.put("Vg", "VB");
        pos_map.put("a", "JJ");
        pos_map.put("ad", "JJ");
        pos_map.put("an", "JJ");
        pos_map.put("b", "DT");
        pos_map.put("c", "CC");
        pos_map.put("d", "RB");
        pos_map.put("e", "UH");
        pos_map.put("f", "IN");
        pos_map.put("g", "g");
        pos_map.put("h", "RB");
        pos_map.put("i", "NNP");
        pos_map.put("j", "j");
        pos_map.put("k", "NN");
        pos_map.put("l", "l");
        pos_map.put("m", "CD");
        pos_map.put("n", "NN");
        pos_map.put("nr", "NNP");
        pos_map.put("ns", "NNP");
        pos_map.put("nt", "NNP");
        pos_map.put("nz", "NNP");
        pos_map.put("nh", "NNP");
        pos_map.put("o", "o");
        pos_map.put("p", "IN");
        pos_map.put("q", "q");
        pos_map.put("r", "NN");
        pos_map.put("s", "RB");
        pos_map.put("t", "CD");
        pos_map.put("u", "IN");
        pos_map.put("v", "VB");
        pos_map.put("vd", "RB");
        pos_map.put("vn", "NN");
        pos_map.put("w", ".");
        pos_map.put("wp", ".");
        pos_map.put("ws", "NNP");
        pos_map.put("x", "x");
        pos_map.put("y", "RB");
        pos_map.put("z", "JJ");
	}
	
	@Override
	public String getName() {
		return "LTP POS Tagger";
	}

	@Override
	public String tag(String text) {
		String out = "";
        StringBuffer content = urllib.urlopen(url_get_base+"api_key="+api_key+"&text="+text+"&format=xml&pattern=pos");

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

                    String pos = word.getAttribute("pos");
                    if(pos_map.containsKey(pos))
                        pos = pos_map.get(pos);
                    out += " " + cont + "/" + pos;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

		return out.trim();
	}

	@Override
	public List<String> tagTopK(String sentence) {
		return Collections.singletonList(tag(sentence));
	}
	
	public List<String> getTags(String text){
		List<String> tags = new ArrayList<String>();

        String out = "";
        StringBuffer content = urllib.urlopen(url_get_base+"api_key="+api_key+"&text="+text+"&format=xml&pattern=pos");

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
                    String pos = word.getAttribute("pos");
                    if(pos_map.containsKey(pos))
                        pos = pos_map.get(pos);
                    tags.add(pos);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

		return tags;
	}
	
	@Override
	public Tagging<String> getTagging(String text){
		List<String> tokenList = new ArrayList<String>();
		List<String> tagList = new ArrayList<String>();

        StringBuffer content = urllib.urlopen(url_get_base+"api_key="+api_key+"&text="+text+"&format=xml&pattern=pos");

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
                    String pos = word.getAttribute("pos");
                    if(pos_map.containsKey(pos))
                        pos = pos_map.get(pos);
                    tokenList.add(cont);
                    tagList.add(pos);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

		return new Tagging<String>(tokenList, tagList);
	}
}
