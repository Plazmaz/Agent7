package me.dylan.Agent7.dictionary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DictionaryFileUtil {
	BufferedReader input;
	PrintWriter output;
	ArrayList<String> lines = new ArrayList<String>();
	String path;
	public DictionaryFileUtil(String s) {
		path=s;
		try {
			input = new BufferedReader(new FileReader(s));
			try {
				output = new PrintWriter(new FileWriter(s, true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}
	public void write(String text) {
		try {
			output = new PrintWriter(new FileWriter(path, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.println(text);
		output.close();
	}
	public void loadData() throws IOException {
		String s;
		input = new BufferedReader(new FileReader(path));
		while((s=input.readLine())!=null) {
			lines.add(s);
//			System.out.println(s);
		}
		input.close();
	}
	public boolean search(String query) {
		for(String s : lines) {
			if(s.equals(query)) {
				return true;
			}
		}
		return false;
	}
	public void copyData(String path) throws IOException {
		String s;
		input = new BufferedReader(new FileReader(path));
		while((s=input.readLine())!=null) {
			String[] split = s.split(" ");
			for(int i=0;i<split.length;i++) {
				write(split[i]);
			}
		}
		input.close();
	}
	public void Cleanup() throws IOException {
		String s;
		lines.clear();
		input = new BufferedReader(new FileReader(path));
		while((s=input.readLine())!=null) {
			if(!search(s)) {
				lines.add(s);
			}
		}
		writeCleanup();
		input.close();
	}
	public void writeCleanup() throws FileNotFoundException {
		output = new PrintWriter(path);
		for(int i=0;i<lines.size();i++) {
			String s = lines.get(i);
			output.println(s);
		}
	}

}
