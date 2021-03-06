package org.aksw.autosparql.tbsl.algorithm.ltag.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import org.aksw.autosparql.commons.nlp.ner.DBpediaSpotlightNER;
import org.aksw.autosparql.commons.nlp.ner.NER;

public class Preprocessor {
	
	private static final Logger logger = Logger.getLogger(Preprocessor.class);

	static final String[] genericReplacements = { "[!?.,;]", "" };
    static final String[] genericReplacements_zh = { "[！？。，；]", "" };
    static final String[] chineseReplacements = { "[了]", "" };

    static final String[] chineseQuestionmark = { "[吗么]", "" };


	static final String[] englishReplacements = { "don't", "do not", "doesn't", "does not" };
        static final String[] hackReplacements = { " 1 "," one "," 2 "," two "," 3 "," three "," 4 "," four "," 5 "," five "," 6 "," six "," 7 "," seven ",
        " 8 "," eight "," 9 "," nine "," 10 "," ten "," 11 "," eleven "," 12 "," twelve "," 13 "," thirteen "," 14 "," fourteen "," 15 "," fifteen ",
        " 16 "," sixteen "," 17 "," seventeen "," 18 "," eighteen "," 19 "," nineteen "," 20 "," twenty "};
    static final String[] hackReplacements_zh = { " 1 "," 一 "," 2 "," 二 "," 3 "," 三 "," 4 "," 四 "," 5 "," 五 "," 6 "," 六 "," 7 "," 七 ",
            " 8 "," 八 "," 9 "," 九 "," 10 "," 十 "," 11 "," 十一 "," 12 "," 十二 "," 13 "," 十三 "," 14 "," 十四 "," 15 "," 十五 ",
            " 16 "," 十六 "," 17 "," 十七 "," 18 "," 十八 "," 19 "," 十九 "," 20 "," 二十 "};
	static boolean USE_NER;
	static boolean VERBOSE;
	static NER ner;
    private static Map<String, String> phraseTableMap;
    private static Map<String, String> propertyTableMap;
    private static final String phrase_table_path = "./algorithm-tbsl/src/main/resources/zh_en_phrase_table.txt";
    private static final String property_table_path = "./algorithm-tbsl/src/main/resources/property_names_zh.txt";

	public Preprocessor(boolean n) {
		USE_NER = n;
		VERBOSE = true;
		if (USE_NER) {
//			ner = new LingPipeNER(true); //not case sensitive best solution?
			ner = new DBpediaSpotlightNER();
		}

        phraseTableMap = readTable(phrase_table_path);
        propertyTableMap = readTable(property_table_path);
	}

    public Map<String, String> readTable(String fileName) {
        Map<String, String> result = new HashMap<String, String>();

        try{
            java.util.Scanner sc = new Scanner(new File(fileName));
            while ( sc.hasNext() ){
                String line = sc.nextLine();
                String tokens[] = line.split("\t");
                if(tokens.length == 2){
                    String label = tokens[0];
                    String url = tokens[1];
                    result.put(label, url);
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        return result;
    }

    public void setVERBOSE(boolean b) {
		VERBOSE = b;
	}
	
	public String normalize(String s) {
		return normalize(s, new String[0]);
	}

	public String normalize(String s, String... repl) {

		if (repl.length % 2 != 0 || genericReplacements.length % 2 != 0 || englishReplacements.length % 2 != 0) {
			throw new IllegalArgumentException();
		}

		List<String> replacements = new ArrayList<String>();
		replacements.addAll(Arrays.asList(repl));
		replacements.addAll(Arrays.asList(englishReplacements));
		replacements.addAll(Arrays.asList(genericReplacements));
        replacements.addAll(Arrays.asList(genericReplacements_zh));
        replacements.addAll(Arrays.asList(chineseQuestionmark));
        replacements.addAll(Arrays.asList(chineseReplacements));
        replacements.addAll(Arrays.asList(hackReplacements));

        s = s.replaceAll(",\\s"," and ").replaceAll(" and but "," but ");
		for (int i = 0; i < replacements.size(); i += 2) {
			s = s.replaceAll(replacements.get(i), replacements.get(i + 1));
		}

		return s;
	}

    // refer to
    // Zhang, Liyi, Yazi Li, and Jian Meng. "Design of chinese word segmentation system based on improved chinese converse dictionary and reverse maximum matching algorithm." Web Information Systems–WISE 2006 Workshops. Springer Berlin Heidelberg, 2006.
    public String condensebasedDitionary(String taggedstring){//TODO: dictionary based condens
        boolean Changed = true;

        while(Changed){
            Changed = false;
            //get pos & words
            ArrayList<String> input = new ArrayList<String>();
            ArrayList<String> pos = new ArrayList<String>();
            for (String s : taggedstring.split(" ")) {
                input.add(s.substring(0,s.indexOf("/")));
                pos.add(s.substring(s.indexOf("/"), s.length()));
            }

            //condense every bigram ~ four gram which is in the dictionary
            for(int j=4;j>1;j--){
                for(int i=input.size()-1;i>=0;i--){
                   //get the reversed ngram
                   String ngram = "";
                   for(int k=i-j+1; k>=0&&k<=i;k++){
                       ngram += input.get(k);
                   }
                   if(ngram == "") continue;

                   //System.out.println(ngram);
                   if(phraseTableMap.containsKey(ngram)){
                       if(j>1) Changed = true;

                       //condense the input
                       taggedstring = "";
                       for(int kk=0;kk<i-j+1;kk++){
                           taggedstring += input.get(kk);
                           taggedstring += pos.get(kk);
                           taggedstring += " ";
                       }
                       taggedstring += ngram + "/NNP" + " ";
                       for(int kk=i+1;kk<input.size();kk++){
                           taggedstring += input.get(kk);
                           taggedstring += pos.get(kk);
                           taggedstring += " ";
                       }

                       taggedstring = taggedstring.trim();

                       break;
                   }
                }

                if(Changed) break;
            }
        }

        return taggedstring;
    }

    public String condensePropertybasedDitionary(String taggedstring){//TODO: dictionary based condens
        boolean Changed = true;

        while(Changed){
            Changed = false;
            //get pos & words
            ArrayList<String> input = new ArrayList<String>();
            ArrayList<String> pos = new ArrayList<String>();
            for (String s : taggedstring.split(" ")) {
                input.add(s.substring(0,s.indexOf("/")));
                pos.add(s.substring(s.indexOf("/"), s.length()));
            }

            //condense every bigram ~ four gram which is in the dictionary
            for(int j=4;j>1;j--){
                for(int i=input.size()-1;i>=0;i--){
                    //get the reversed ngram
                    String ngram = "";
                    for(int k=i-j+1; k>=0&&k<=i;k++){
                        ngram += input.get(k);
                    }
                    if(ngram == "") continue;

                    //System.out.println(ngram);
                    if(propertyTableMap.containsKey(ngram)){
                        if(j>1) Changed = true;

                        //condense the input
                        taggedstring = "";
                        for(int kk=0;kk<i-j+1;kk++){
                            taggedstring += input.get(kk);
                            taggedstring += pos.get(kk);
                            taggedstring += " ";
                        }
                        taggedstring += ngram + "/NN" + " ";
                        for(int kk=i+1;kk<input.size();kk++){
                            taggedstring += input.get(kk);
                            taggedstring += pos.get(kk);
                            taggedstring += " ";
                        }

                        taggedstring = taggedstring.trim();

                        break;
                    }
                }

                if(Changed) break;
            }
        }

        return taggedstring;
    }

    public String postCondense(String taggedstring) {
        Pattern NNNNPPattern      = Pattern.compile("^((\\p{L}+/NN)\\s(\\p{L}+/NNP))");
        String condensedstring = taggedstring.replaceAll("``/``","").replaceAll("''/''","").replaceAll("  "," ");
        Matcher m;

        m = NNNNPPattern.matcher(condensedstring);
        while (m.find()) {
            if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+" 的/IN "+m.group(3));
            condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+" 的/IN "+m.group(3));
        }
        return condensedstring;
    }

	public String condense(String taggedstring) {
		
		/* condense: 
		 * x/RBR adj/JJ > adj/JJR, x/RBS adj/JJ > adj/JJS, x/WRB adj/JJ > x/JJH
		 * nn/RBR of/IN > nn/NPREP
		 * usw. 
		 * Note: Matching these patterns is order-dependent!
		 * */ 
		String condensedstring = taggedstring.replaceAll("``/``","").replaceAll("''/''","").replaceAll("  "," ");
		Matcher m;
		
		Pattern compAdjPattern    = Pattern.compile("(\\w+/RBR.(\\w+)/JJ)");
//		Pattern superAdjPattern   = Pattern.compile("(\\w+/RBS.(\\w+)/JJ)"); // TODO "(the most) official languages" vs "the (most official) languages"
		Pattern howManyPattern    = Pattern.compile("(how/WRB.many/JJ)"); 
		Pattern howAdjPattern     = Pattern.compile("(\\w+/WRB.(\\w+)(?<!many)/JJ)"); 
		Pattern thesameasPattern  = Pattern.compile("(the/DT.same/JJ.(\\w+)/NN.as/IN)");
        Pattern themostPattern  = Pattern.compile("(最/RB.(\\p{L}+)/JJ.的/IN)");
        Pattern pronounPattern  = Pattern.compile("(((\\p{L}+)/NN).的/IN)");
		Pattern nprepPattern      = Pattern.compile("\\s((\\w+)/NNS?.of/IN)");
		Pattern didPattern        = Pattern.compile("(?i)(\\s((did)|(do)|(does))/VB.?)\\s"); 
		Pattern prepfrontPattern  = Pattern.compile("(\\A\\w+/((TO)|(IN)).)\\w+/WDT"); // TODO (Nicht ganz sauber. Bei P-Stranding immer zwei Querys, hier nur eine.)
		Pattern passivePattern1a  = Pattern.compile("(((has)|(have)|(had))/VB[A-Z]?.been/VBN.(\\w+)/VBN.by/IN)");
		Pattern passivePattern1b  = Pattern.compile("(\\s((has)|(have)|(had))/VB[A-Z]?(.+\\s)been/VBN\\s(\\w+)/VB(N|D))");
		Pattern passivePattern2a  = Pattern.compile("(((is)|(are)|(was)|(were))/VB[A-Z]?.(\\w+)/VBN.by/IN)");
		Pattern pseudopassPattern = Pattern.compile("(((is)|(are)|(was)|(were))/VB[A-Z]?.(\\w+)/VBN.\\w+/((TO)|(IN)))");
		Pattern pseudopwhPattern  = Pattern.compile("(((is)|(are)|(was)|(were))/VB[A-Z]?.(.+)\\s(\\w+)/VB(N|D).\\w+/((TO)|(IN)))");
		Pattern saveIsThere       = Pattern.compile("((is)|(are))/(VB[A-Z]?).there/(RB)");
		Pattern passivePattern2b  = Pattern.compile("(((is)|(are)|(was)|(were))/VB[A-Z]?.((.+)\\s\\w+)/VB(N|D))");
		Pattern passpartPattern   = Pattern.compile("\\s((\\w+)/VBN.by/IN)");
        Pattern passpartPattern_zh   = Pattern.compile("\\s((\\p{L}+)/V.的/U)");
		Pattern vpassPattern      = Pattern.compile("\\s(\\w+/VBD.(\\w+)/VBN)");
		Pattern vpassinPattern    = Pattern.compile("\\s((\\w+)/VPASS.\\w+/IN)");
		Pattern gerundinPattern   = Pattern.compile("\\s((\\w+)/((VBG)|(VBN)).\\w+/IN)");
		Pattern vprepPattern      = Pattern.compile("\\s((\\w+)(?<!have)/V[A-Z]+\\s\\w+/(IN|TO))");
		Pattern whenPattern       = Pattern.compile("\\A(when/WRB\\s(.+\\s)(\\w+)/((V[A-Z]+)|(PASS[A-Z]+)))");
		Pattern wherePattern      = Pattern.compile("\\A(where/WRB\\s(.+\\s)(\\w+)/((V[A-Z]+)|(PASS[A-Z]+)))");
		Pattern adjsPattern       = Pattern.compile("((\\w+)/JJ.(\\w+)/JJ)");
//              Pattern adjnnpPattern     = Pattern.compile("((\\w+)(?<!many)/JJ.(\\w+)/NNP(S)?)");
		Pattern adjnounPattern    = Pattern.compile("((\\w+)(?<!many)/JJ.(\\w+)/NN(S)?(\\s|\\z))");
		Pattern adjnprepPattern   = Pattern.compile("((\\w+)(?<!many)/JJ.(\\w+)/NPREP)");

        //副。。
        //什么时候

        //Chinese patterns
        m = themostPattern.matcher(condensedstring);
        while (m.find()) {
            if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by 最" + m.group(2)+"的/JJS");
            condensedstring = condensedstring.replaceFirst(m.group(1),"最" + m.group(2)+"的/JJS");
        }

        m = pronounPattern.matcher(condensedstring);
        while (m.find()) {
            if (VERBOSE) logger.debug("Replacing " + m.group(2) + m.group(3) + "/NNP ");
            condensedstring = condensedstring.replaceFirst(m.group(2),m.group(3) + "/NNP");
        }

		m = compAdjPattern.matcher(condensedstring); 
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/JJR");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/JJR");
		}
//		m = superAdjPattern.matcher(condensedstring); 
//		while (m.find()) {
//			logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/JJS");
//			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/JJS");
//		}
		m = howManyPattern.matcher(condensedstring); 
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by how/WLEX many/WLEX");
			condensedstring = condensedstring.replaceFirst(m.group(1),"how/WLEX many/WLEX");
		}
		m = howAdjPattern.matcher(condensedstring); 
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/JJH");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/JJH");
		}
		m = thesameasPattern.matcher(condensedstring); 
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/NNSAME");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/NNSAME");
		}
		m = nprepPattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/NPREP");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/NPREP");
		}



		m = didPattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by \"\"");
			condensedstring = condensedstring.replaceFirst(m.group(1),"");
		}
		m = prepfrontPattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by \"\"");
			condensedstring = condensedstring.replaceFirst(m.group(1),"");
		}
		m = passivePattern1a.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(6)+"/PASSIVE");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(6)+"/PASSIVE");
		}
		m = passivePattern1b.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(6)+m.group(7)+"/PASSIVE");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(6) + m.group(7)+"/PASSIVE");
		}
		m = passivePattern2a.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(7)+"/PASSIVE");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(7)+"/PASSIVE");
		}
		m = pseudopassPattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(7)+"/VPREP");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(7)+"/VPREP");
		}
		m = pseudopwhPattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(7)+m.group(8)+"/VPREP");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(7)+" "+m.group(8)+"/VPREP");
		}
		m = saveIsThere.matcher(condensedstring);
		while (m.find()) {
			condensedstring = condensedstring.replaceFirst(m.group(4),"LEX").replaceFirst(m.group(5),"LEX"); // TODO what a dirty hack!
		}
		m = passivePattern2b.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(7)+"/PASSIVE");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(7)+"/PASSIVE");
		}
		m = passpartPattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/PASSPART");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/PASSPART");
		}
        m = passpartPattern_zh.matcher(condensedstring);
        while (m.find()) {
            if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/PASSPART");
            condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/PASSPART");
        }
		m = vpassPattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/VPASS");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/VPASS");
		}
		m = vpassinPattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/VPASSIN");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/VPASSIN");
		}
		m = gerundinPattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/GERUNDIN");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/GERUNDIN");
		}
		m = vprepPattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"/VPREP");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"/VPREP");
		}
		m = whenPattern.matcher(condensedstring);
		while (m.find()) {
                    if (m.group(4).equals("VPREP")) {
                        if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+m.group(3)+"/WHENPREP");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2) + m.group(3)+"/WHENPREP");
                    } else {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+m.group(3)+"/WHEN");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2) + m.group(3)+"/WHEN");
                    }
		}
		m = wherePattern.matcher(condensedstring);
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+m.group(3)+"/WHERE");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2) + m.group(3)+"/WHERE");
		}
		m = adjsPattern.matcher(condensedstring); 
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"_"+m.group(3)+"/JJ");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"_"+m.group(3)+"/JJ");
		}
		m = adjnounPattern.matcher(condensedstring); 
		while (m.find()) {
//                    if (!m.group(4).startsWith("NNP")) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"_"+m.group(3)+"/JJNN");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"_"+m.group(3)+"/JJNN ");
//                    }
		}
		m = adjnprepPattern.matcher(condensedstring); 
		while (m.find()) {
			if (VERBOSE) logger.debug("Replacing " + m.group(1) + " by " + m.group(2)+"_"+m.group(3)+"/NPREP");
			condensedstring = condensedstring.replaceFirst(m.group(1),m.group(2)+"_"+m.group(3)+"/NPREP");
		}

        //condensedstring = condenseNominals(condensedstring);

        condensedstring = condensebasedDitionary(condensedstring);
        condensedstring = condensePropertybasedDitionary(condensedstring);
        condensedstring = postCondense(condensedstring);
		return condensedstring;
	}

	public String condenseNominals(String s) {
		
		String flat = s;
		
		Matcher m;
		Pattern quotePattern1 = Pattern.compile("``/``(\\s)?(\\p{L}+(/\\p{L}+\\s)).*''/''");
		Pattern quotePattern2 = Pattern.compile("(``/``((.*)_)''/'')");
		Pattern nnpPattern    = Pattern.compile("\\s?((\\p{L}+)/NNP[S]?\\s(\\p{L}+))/NNP[S]?(\\W|$)");
		//Pattern nnPattern     = Pattern.compile("\\s?((\\p{L}+)/NN[S]?\\s(\\p{L}+))/NN[S]?(\\W|$)");
        //Pattern nnPattern     = Pattern.compile("\\s?((\\p{L}+)/NN[S]?\\s(\\p{L}+)/NN[S]?)(\\W|$)");
		Pattern nnnnpPattern  = Pattern.compile("\\s?((\\p{L}+)/NNP[S]?\\s(\\p{L}+)/NN[S]?)(\\W|$)");

		m = quotePattern1.matcher(flat);
		while (m.find()) {
			flat = flat.replaceFirst(m.group(3),"");
			m = quotePattern1.matcher(flat);
		}
		m = quotePattern2.matcher(flat);
		while (m.find()) {
			flat = flat.replaceFirst(m.group(2),m.group(3)+"/NNP");
		}
		
		m = nnpPattern.matcher(flat);
		while (m.find()) {
			flat = flat.replaceFirst(m.group(1),m.group(2) + "" + m.group(3));
			m = nnpPattern.matcher(flat);
		}
		m = nnpPattern.matcher(flat);
		while (m.find()) {
			flat = flat.replaceFirst(m.group(1),m.group(2) + "" + m.group(3));
			m = nnpPattern.matcher(flat);
		}
//		m = nnPattern.matcher(flat);
//		while (m.find()) {
//			flat = flat.replaceFirst(m.group(1),m.group(2) + "" + m.group(3) + "/NN");
//			m = nnPattern.matcher(flat);
//		}
		m = nnnnpPattern.matcher(flat);
		while (m.find()) {
			flat = flat.replaceFirst(m.group(1),m.group(2) + "" + m.group(3) + "/NNP" + m.group(4));
			m = nnnnpPattern.matcher(flat);
		}

        return flat;
	}
	
	public String findNEs(String tagged,String untagged) {
		
		String out = tagged;
		
		String[] postags = {"NN","NNS","NNP","NNPS","NPREP","JJ","JJR","JJS","JJH",
				"VB","VBD","VBG","VBN","VBP","VBZ","PASSIVE","PASSPART","VPASS","VPASSIN",
				"GERUNDIN","VPREP","WHEN","WHERE","IN","TO","DT"};
		
		List<String> namedentities = ner.getNamedEntitites(untagged);
		List<String> usefulnamedentities = new ArrayList<String>();
		
		if (VERBOSE) logger.debug("Proposed NEs: " + namedentities);
		
		// keep only longest matches (e.g. keep 'World of Warcraft' and forget about 'Warcraft') 
		// containing at least one upper case letter (in order to filter out errors like 'software')
		for (String s1 : namedentities) {
			if (s1.matches(".*[A-Z].*") && !Arrays.asList(postags).contains(s1)) { 
				boolean isLongestMatch = true;
				for (String s2 : namedentities) {
					if (!s2.equals(s1) && s2.contains(s1)) {
						isLongestMatch = false;
					}
				}
				if (isLongestMatch) {
					usefulnamedentities.add(s1);
				}
			}
		}
		
		if (VERBOSE) logger.debug("Accepted NEs: " + usefulnamedentities);
		
		// replace POS tags accordingly
		for (String ne : usefulnamedentities) {
			String[] neparts = ne.split(" ");
			Pattern p; Matcher m;
			for (String nep : neparts) {
				p = Pattern.compile("(\\s)?(" + nep + "/([A-Z]+))(\\s)?");
				m = p.matcher(out);
				while (m.find()) {
					out = out.replaceFirst(m.group(2),nep+"/NNP");
				}
			}
		}

		return out;
	}
	
}
