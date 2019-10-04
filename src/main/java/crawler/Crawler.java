package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	public static void main(String[] args) {
		
		String brand = "Thundergroup";
		String url = "http://www.thundergroup.com/app/stx.search.asp";
		
		File csvFile = new File("C:\\Users\\Michael\\ivm-workspace\\crowling\\src\\main\\Sheet.csv");
		
		System.out.println(new File("").getPath());
		try {
			System.out.println(new File("").getCanonicalPath().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> modelNames = new ArrayList<>();
		if(csvFile.isFile()) {
			BufferedReader csvReader;
			try {
				csvReader = new BufferedReader(new FileReader(csvFile));
				String row;
				String[] data; 
				while ((row = csvReader.readLine()) != null) {
					data = row.split(",");
					if(data[1].equals(brand)) {
						modelNames.add(data[0]);
					}
				}
				csvReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
		List<String> result = new ArrayList<>();
		
		for(String modelName : modelNames ) {
			Document doc; 
			
			try {
				doc = Jsoup.connect(url+"?Keyword="+modelName).userAgent("Mozilla").get();
				//System.out.println(doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				doc = null;
			}
			
			
			if(doc!= null) {
				Elements item_code = doc.select(".products_title");
				
				System.out.println(modelName +": "+item_code.size());
				for(Element item : item_code) {
					String[] itemP = item.text().split(":");
					
					String itemCode = (itemP.length>1)? itemP[itemP.length-1].trim(): null; 
					String itemDesc = "\"" + item.select("a").text() +"\"";
					result.add(itemCode +", " +itemDesc);
				}
			}
			
		}
		
		FileWriter output = null;
		try {
			
			output = new FileWriter(new File("C:\\Users\\Michael\\ivm-workspace\\crowling\\src\\main\\output"+UUID.randomUUID().toString()+".csv"));
			
			for(String r : result) {
				output.write(r+"\n");
				System.out.println(r);
			}
			output.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
