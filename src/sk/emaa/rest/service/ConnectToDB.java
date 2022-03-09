package sk.emaa.rest.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ConnectToDB {

	public static void main( String args[] ) {
		
		// start MONGO with
		// c:\Program Files\MongoDB\Server\3.6\bin>mongod --port 27017 --dbpath "g:\Angular\mongodb\data"
		
		// Creating a Mongo client 
		MongoClient mongo = new MongoClient("localhost", 27017);
	   
		// Creating Credentials 
		MongoCredential credential; 
		credential = MongoCredential.createCredential("emaa", "emaa", "wingtsun".toCharArray()); 
		System.out.println("Connected to the database successfully");
      
		// Accessing the database 
		MongoDatabase database = mongo.getDatabase("emaa");
		System.out.println("Credentials: " + credential);
      
		ConnectToDB connectToDB = new ConnectToDB();
		
		MongoCollection<Document> studentCollection = database.getCollection("student");
		// clear collection
		studentCollection.drop();
		for (String studentRow : connectToDB.getStudentRows()) {
			String[] studentData = studentRow.split(",");
			if (studentData.length == 4) {
				Document student = new Document();
				student.put("_id", studentData[0]);
				student.put("firstname", studentData[1]);
				student.put("lastname", studentData[2]);
				student.put("gender", studentData[3]);
				studentCollection.insertOne(student);
			} else {
				throw new RuntimeException("Corrupt students.csv file!");
			}
		}
		
		MongoCollection<Document> schoolCollection = database.getCollection("school");
		schoolCollection.drop();
		for (String schoolRow : connectToDB.getSchoolRows()) {
			String[] schoolData = schoolRow.split(",");
			if (schoolData.length == 2) {
				Document school = new Document();
				school.put("_id", schoolData[0]);
				school.put("town", schoolData[1]);
				schoolCollection.insertOne(school);
			} else {
				throw new RuntimeException("Corrupt schools.csv file!");
			}
		}
		
		mongo.close();
	}
	
	private List<String> getStudentRows() {
		List<String> rows = new ArrayList<>();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("students.csv").getFile());
		
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				rows.add(line);
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rows;
	}
	
	private List<String> getSchoolRows() {
		List<String> rows = new ArrayList<>();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("schools.csv").getFile());
		
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				rows.add(line);
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rows;
	}
	
}
