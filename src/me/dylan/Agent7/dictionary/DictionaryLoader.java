package me.dylan.Agent7.dictionary;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import me.dylan.Agent7.Agent7;

public class DictionaryLoader {
	ArrayList<File> txtfiles = new ArrayList<File>();
	ArrayList<String> wordstoadd = new ArrayList<String>();
	private HashMap<File, ArrayList<String>> wordsOnFiles = new HashMap<File, ArrayList<String>>();
	private boolean loaded = false;
	int loadedwords = 0;
	String dictionStr = "Dictionary.txt";
	File dictionaryFile = new File(dictionStr);
	public void init() {

		loadAllTextFiles();
		for (File f : txtfiles) {
			lookForWords(f);
		}
//		lookForWords(dictionaryFile);
		// try {
		// // cleanUpOldWordlists();
		// writeWordsToList();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		this.setLoaded(true);

	}

	public void cleanUpOldDictionary() throws IOException {
		PrintWriter fout = new PrintWriter(new FileWriter(dictionStr,
				false));
		for (String s : wordsOnFiles.get(dictionaryFile)) {
			if (!searchForWord(s) && !s.isEmpty()) {
				if (!s.equals(s.toUpperCase()) && !s.equals(s.toLowerCase())) {
					fout.println(s);
					fout.println(s + "123");
					fout.println(s + "1234");
				}
				fout.println(s.toLowerCase());
				fout.println(s.toUpperCase());
				fout.println(s.toUpperCase() + "123");
				fout.println(s.toUpperCase() + "1234");
				fout.println(s.toLowerCase() + "123");
				fout.println(s.toLowerCase() + "1234");
			} else {
				wordstoadd.remove(s);
			}
		}
		fout.close();
	}

	public void cleanUpOldWordlists() throws IOException {
		for (File f : wordsOnFiles.keySet()) {
			PrintWriter fout = new PrintWriter(new FileWriter(f, false));
			for (String s : wordsOnFiles.get(f)) {
				System.out.println(s);
				if (!searchForWord(s) && !s.isEmpty()) {
					if (!s.equals(s.toUpperCase())
							&& !s.equals(s.toLowerCase())) {
						fout.println(s);
						fout.println(s + "123");
						fout.println(s + "1234");
					}
					fout.println(s.toLowerCase());
					fout.println(s.toUpperCase());
					fout.println(s.toUpperCase() + "123");
					fout.println(s.toUpperCase() + "1234");
					fout.println(s.toLowerCase() + "123");
					fout.println(s.toLowerCase() + "1234");
				} else {
					wordstoadd.remove(s);
				}
			}
			fout.close();
		}
	}

	public void loadAllTextFiles() {
		String path = new File(dictionaryFile.getAbsolutePath())
				.getParent();
		File parent = new File(path);
		File[] files = parent.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.getName().toLowerCase().endsWith(".txt")
					&& !f.getName().startsWith("Dictionary")) {
				txtfiles.add(f);
				System.out.println("Added file: " + f.getName());
			}
		}

	}

	public void lookForWords(File f) {
		try {
			BufferedReader fin = new BufferedReader(new FileReader(f));
			if (!wordsOnFiles.containsKey(f))
				wordsOnFiles.put(f, new ArrayList<String>());
			String s;
			String[] wordsonline;
			while ((s = fin.readLine()) != null) {
				s = s.trim();
				wordsonline = new String[s.length()];
				wordsonline = s.split("\\s+");
				for (int i = 0; i < wordsonline.length; i++) {
					if (!wordsonline[i].isEmpty()) {
						// wordsonline[i] = wordsonline[i].replace("\n", "\n");
						wordsonline[i] = wordsonline[i].replace("“", "");
						wordsonline[i] = wordsonline[i].replace("”", "");
						wordsonline[i] = wordsonline[i].replace("’", "");
						wordsonline[i] = wordsonline[i].replace("©", "");
						wordsonline[i] = wordsonline[i].replace("»", "");
						if (!wordsonline[i]
								.equals(wordsonline[i].toUpperCase())
								&& !wordsonline[i].equals(wordsonline[i]
										.toLowerCase())) {
							wordsOnFiles.get(f).add(wordsonline[i]);

						} else {
							wordsOnFiles.get(f).add(
									wordsonline[i].toLowerCase());
							wordsOnFiles.get(f).add(
									wordsonline[i].toUpperCase());
						}
						loadedwords++;
						if (loadedwords % 30000 == 0)
							Agent7.logLine("Loaded " + loadedwords + " Words.");
					}
				}

			}
			fin.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized void writeWordsToList() throws IOException {
		PrintWriter fout = new PrintWriter(new FileWriter(dictionStr,
				true));
		ArrayList<String> words = new ArrayList<String>();
		for (String s : (ArrayList<String>) wordstoadd.clone()) {
			if (!searchForWord(s)) {
				fout.println(s);
				words.add(s);
			}
		}
		wordsOnFiles.put(dictionaryFile, words);
		fout.close();
	}

	public boolean searchForWord(String word) throws FileNotFoundException {
		for (File f : txtfiles) {
			if (!getCurrentWords().containsKey(f)) {
				getCurrentWords().put(f, new ArrayList<String>());
			}
			if (getCurrentWords().get(f).contains(word)) {
				return true;
			}
		}
		// System.out.println(word);
		return false;
	}

	public HashMap<File, ArrayList<String>> getCurrentWords() {
		return wordsOnFiles;
	}

	public void setCurrentWords(HashMap<File, ArrayList<String>> curwords) {
		this.wordsOnFiles = curwords;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public ArrayList<String> getAllFileContents() {
		ArrayList<String> alldata = new ArrayList<String>();
		for (File f : wordsOnFiles.keySet()) {
			alldata.addAll(wordsOnFiles.get(f));
		}
		return alldata;
	}
}
