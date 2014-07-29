package org.aksw.autosparql.tbsl.algorithm.learning;

import org.aksw.autosparql.commons.util.URLlib;
import org.aksw.autosparql.tbsl.algorithm.knowledgebase.Knowledgebase;
import org.aksw.autosparql.tbsl.algorithm.sparql.Slot;
import org.aksw.autosparql.tbsl.algorithm.sparql.SlotType;
import org.aksw.autosparql.tbsl.algorithm.sparql.Template;
import java.net.URLEncoder;
import org.apache.log4j.Logger;
import org.dllearner.common.index.Index;
import org.dllearner.common.index.IndexResultItem;
import org.dllearner.common.index.IndexResultSet;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.util.SimpleIRIShortFormProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.MutablePair;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class AGISTISEntityDisambiguation {

	private static final Logger logger = Logger.getLogger(AGISTISEntityDisambiguation.class.getName());

	private Knowledgebase knowledgebase;
	private SimpleIRIShortFormProvider iriSfp = new SimpleIRIShortFormProvider();
    private static final URLlib urllib = new URLlib();
    private static final String url = "http://139.18.2.164:8080/AGDISTIS";

	public AGISTISEntityDisambiguation(Knowledgebase knowledgebase) {
		this.knowledgebase = knowledgebase;
	}

	public Map<Template, Map<Slot, Collection<Entity>>> performEntityDisambiguation(Collection<Template> templates){
		Map<Template, Map<Slot, Collection<Entity>>> template2Allocations = new HashMap<Template, Map<Slot,Collection<Entity>>>();

		for(Template template : templates){
			Map<Slot, Collection<Entity>> slot2Entities = performEntityDisambiguation(template);
			template2Allocations.put(template, slot2Entities);
		}		
		return template2Allocations;
	}

	public Map<Slot, Collection<Entity>> performEntityDisambiguation(Template template){
		Map<Slot, Collection<Entity>> slot2Entities = new HashMap<Slot, Collection<Entity>>();
		List<Slot> slots = template.getSlots();
		for(Slot slot : slots){
			Collection<Entity> candidateEntities = getCandidateEntities(slot);
			slot2Entities.put(slot, candidateEntities);
		}
		return slot2Entities;
	}

    public static ArrayList<Pair<String, String>> getCandidateEntities(String text) throws Exception{
        ArrayList<Pair<String, String>> links = new ArrayList<Pair<String, String>>();

        String params = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("agdistis", "UTF-8");
        params += "&" + URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8");

        StringBuffer content = urllib.HttpPostRequest(url, params);
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(content.toString());
            JSONArray array = (JSONArray) obj;

            for(Object jObject: array){
                JSONObject jobj = (JSONObject) jObject;
                links.add(new MutablePair(jobj.get("disambiguatedURL"), jobj.get("namedEntity")));
            }
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return links;
    }

	/** get sorted list of entities
	 */
	private Collection<Entity> getCandidateEntities(Slot slot){
		logger.trace("Generating entity candidates for slot " + slot + "...");
		Set<Entity> candidateEntities = new HashSet<Entity>();
		if(slot.getSlotType() == SlotType.RESOURCE){
			List<String> words = slot.getWords();
            String text = "";
            for(String word: words){
                text = text + "<entity>"+ word + "</entity> ";
            }

            try{
                ArrayList<Pair<String, String>> links = getCandidateEntities(text);
                for(Pair<String, String> pair: links){
                    candidateEntities.add(new Entity(pair.getLeft(), pair.getRight()));
                }
            }catch (Exception e){
                logger.debug("AGISTIS Disambiguation exception:" + text);
                e.printStackTrace();
            }

		} else {
			Index index = getIndexForSlot(slot);
			List<String> words = slot.getWords();
			for(String word : words){
				
				// disable system.out
				PrintStream out = System.out;
				System.setOut(new PrintStream(new OutputStream() {@Override public void write(int arg0) throws IOException {}}));
				IndexResultSet rs = index.getResourcesWithScores(word, 10);
				// enable again
				System.setOut(out);
				for(IndexResultItem item : rs.getItems()){
					String uri = item.getUri();
					String label = item.getLabel();
					if(label == null){
						label = iriSfp.getShortForm(IRI.create(uri));
					}
					candidateEntities.add(new Entity(uri, label));
                    System.out.println("uri,label -> " + uri + "," + label);
				}
			}
		}
		logger.debug("Found " + candidateEntities.size() + " entities for slot "+slot+": "+candidateEntities);
		return candidateEntities;
	}

	private Index getIndexForSlot(Slot slot){
		Index index = null;
		SlotType type = slot.getSlotType();
		if(type == SlotType.CLASS){
			index = knowledgebase.getIndices().getClassIndex();
		} else if(type == SlotType.PROPERTY || type == SlotType.SYMPROPERTY){
			index = knowledgebase.getIndices().getPropertyIndex();
		} else if(type == SlotType.DATATYPEPROPERTY){
			index = knowledgebase.getIndices().getDataPropertyIndex();			
		} else if(type == SlotType.OBJECTPROPERTY){
			index = knowledgebase.getIndices().getObjectPropertyIndex();		
		} else if(type == SlotType.RESOURCE || type == SlotType.UNSPEC){
			index = knowledgebase.getIndices().getResourceIndex();
		}
		return index;
	}

}