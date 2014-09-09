package org.aksw.autosparql.tbsl.algorithm;

import org.aksw.autosparql.tbsl.algorithm.sparql.Template;
import org.aksw.autosparql.tbsl.algorithm.templator.Templator;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.net.URL;
import java.util.Collections;

import org.aksw.autosparql.tbsl.algorithm.learning.TemplateInstantiation;
import org.aksw.autosparql.tbsl.algorithm.learning.TbslDbpedia;
import org.aksw.autosparql.tbsl.algorithm.knowledgebase.DBpediaKnowledgebase;
import com.hp.hpl.jena.query.ResultSet;
import org.aksw.autosparql.tbsl.algorithm.learning.NoTemplateFoundException;
import org.dllearner.kb.sparql.SparqlEndpoint;

import org.aksw.autosparql.commons.qald.EvaluationUtils;

public class Qald4Test {

    private static List<String> readQuestions(File file){
        List<String> questions = new ArrayList<String>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList questionNodes = doc.getElementsByTagName("question");

            String question;
            for(int i = 0; i < questionNodes.getLength(); i++){
                Element questionNode = (Element) questionNodes.item(i);
                for(int j=0;j<questionNode.getElementsByTagName("string").getLength();j++){
                    Element questionStringNode = (Element) questionNode.getElementsByTagName("string").item(j);

                    if(questionStringNode.getAttribute("lang").equals("zh")){
                        //Read question
                        //question = ((Element)questionStringNode.getElementsByTagName("string").item(0)).getChildNodes().item(0).getNodeValue().trim();
                        question = ((Element)questionStringNode).getChildNodes().item(0).getNodeValue().trim();
                        questions.add(question);
                    }
                }

            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }

    private static List<String> readQueries(File file){
        List<String> queries = new ArrayList<String>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList questionNodes = doc.getElementsByTagName("question");

            String query;
            for(int i = 0; i < questionNodes.getLength(); i++){
                Element questionNode = (Element) questionNodes.item(i);
                query = ((Element)questionNode.getElementsByTagName("query").item(0)).getChildNodes().item(0).getNodeValue().trim();

                queries.add(query);
            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queries;
    }

//    boolean Compare(ResultSet model, ResultSet ref){
//        QuerySolution s = model.nextSolution();
//
//
//        return false;
//    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SparqlEndpoint dbpediaEndpoint;

        //File file = new File("../DBpediaQA/benchmark/qald4/qald-4_multilingual_train_withanswers_linklabel.xml");
        File file = new File("../DBpediaQA/benchmark/qald4/qald-4_multilingual_test_withanswers_linklabel.xml");
        List<String> questions = readQuestions(file);
        List<String> queries = readQueries(file);

        //only a few questions can be answered
//        int[] ans = new int[] {3,};
        int[] ans = new int[200];

        for(int i=0;i<200;i++){
            ans[i] = i;
        }

        ArrayList<Integer> corrects = new  ArrayList<Integer>();
        ArrayList<Integer> incorrects = new  ArrayList<Integer>();
        ArrayList<Integer> notemps = new  ArrayList<Integer>();

        try{
            dbpediaEndpoint = new SparqlEndpoint(new URL("http://dbpedia.org/sparql"),Collections.<String>singletonList(""), Collections.<String>emptyList());
        } catch (MalformedURLException e){
            throw new RuntimeException(e);
        }

        int cnt = 0;
        int incnt = 0;
        for(int i: ans ){
            if(i >= questions.size()) continue;
            String question = questions.get(i);
            String query = queries.get(i);

            System.out.println("Question"+i+": " + question);
            try {
                TemplateInstantiation ti = TbslDbpedia.INSTANCE.answerQuestion(question);
                String sparqlQueryString = ti.getQuery();

                double accuracy = EvaluationUtils.accuracy(sparqlQueryString,query, dbpediaEndpoint);
                if(accuracy == 1.0){
                    System.out.println("Correct");
                    cnt++;
                    corrects.add(i+1);
                }else{
                    ResultSet crs = DBpediaKnowledgebase.INSTANCE.querySelect(query);
                    ResultSet rs = DBpediaKnowledgebase.INSTANCE.querySelect(sparqlQueryString);
                    incorrects.add(i+1);
                    System.out.println("Incorrect answer:");
                    if(crs!= null && crs.hasNext())
                        System.out.println(crs.nextSolution().toString());
                    if(rs!= null && rs.hasNext())
                        System.out.println(rs.nextSolution().toString());
                    System.out.println(query);
                    if(ti != null)
                        System.out.println(ti.getQuery());
                }
            }catch (NoTemplateFoundException e){
                e.printStackTrace();
                notemps.add(i+1);
            }
            catch (Exception e) {
                e.printStackTrace();
                incnt++;
            }
        }

        System.out.println("total: " + questions.size());
        System.out.println("correct: " + cnt);
        System.out.println("incorrect: " + (questions.size() - cnt));

        System.out.println("correct:");
        for(int i: corrects){
            System.out.print(i+", ");
        }
        System.out.println();

        System.out.println("incorrect:" + incorrects.size());
        for(int i: incorrects){
            System.out.print(i+", ");
        }
        System.out.println();

        System.out.println("No template:" + notemps.size());
        for(int i: notemps){
            System.out.print(i+", ");
        }
        System.out.println();
    }
}