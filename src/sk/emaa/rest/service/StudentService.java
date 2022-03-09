package sk.emaa.rest.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.internal.MorphiaCursor;
import sk.emaa.rest.service.model.Student;
import sk.emaa.rest.service.model.StudentAutocomplete;

@Service
@Path("/studentService")
public class StudentService extends AbstractService {
	
	private MongoClient mongoClient;
	private Morphia morphia;
	private Datastore datastore;
	private MongoDatabase database;
	
	public StudentService() {
		mongoClient = new MongoClient("localhost", 27017);		
		morphia = new Morphia();
		morphia.mapPackage("sk.emaa.rest.service.model");
		datastore = morphia.createDatastore(mongoClient, "emaa");
		database = mongoClient.getDatabase("emaa");
	}
	
	@GET
	@Path("/students/{schoolId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudents(@PathParam("schoolId") String schoolId, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
	    List<Student> students;
	    if ("0".equals(schoolId)) {
	    	students = datastore.find(Student.class).find().toList();
	    } else {
	    	students = datastore.find(Student.class).filter("schoolId", schoolId).find().toList();
	    }
	    
	    /*
	    // works too
	    Query<Student> query = datastore.createQuery(Student.class);
	    query.filter("schoolId", schoolId);
	    List<Student> students = query.find().toList();
	    */

        return Response.status(200).entity(students).build();
	}
	
	@GET
	@Path("/students/seminar/{schoolId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsForSeminar(@PathParam("schoolId") String schoolId, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
	    List<Student> students;
	    if ("0".equals(schoolId)) {
	    	students = datastore.find(Student.class).filter("deactivated", false).find().toList();
	    } else {
	    	students = datastore.find(Student.class).filter("schoolId", schoolId).filter("deactivated", false).find().toList();
	    }
	    
	    /*
	    // works too
	    Query<Student> query = datastore.createQuery(Student.class);
	    query.filter("schoolId", schoolId);
	    List<Student> students = query.find().toList();
	    */

        return Response.status(200).entity(students).build();
	}
	
	@GET
	@Path("/student/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudent(@PathParam("id") String id, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		Student student = null;
		MorphiaCursor<Student> cursor = datastore.find(Student.class).filter("id", id).find();
		if (cursor.hasNext()) {
			student = cursor.next();
			// convert UTC to local date/time
		    student.setBirthdate(new Date(student.getBirthdate().getTime() - Calendar.getInstance().getTimeZone().getOffset(student.getBirthdate().getTime())));
		}

        return Response.status(200).entity(student).build();
	}
	
	@POST
	@Path("/student")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createStudent(Student student, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoCollection<Document> studentCollection = database.getCollection("student");
		//String generatedId = IDgenerator.createID();
		ObjectId generatedId = ObjectId.get();
		Document dbStudent = new Document("_id", generatedId.toHexString())
								  .append("gender", student.getGender())
								  .append("schoolId", student.getSchoolId())
								  .append("actualGrade", student.getActualGrade())
								  .append("firstname", student.getFirstname())
								  .append("lastname", student.getLastname())
								  .append("birthdate", student.getBirthdate())
								  .append("parentContact", student.getParentContact())
								  //.append("birthdate", new Date(student.getBirthdate().getTime() + Calendar.getInstance().getTimeZone().getOffset(student.getBirthdate().getTime())))
								  .append("streetAndNumber", student.getStreetAndNumber())
								  .append("city", student.getCity())
								  .append("zipCode", student.getZipCode())
								  .append("phone", student.getPhone())
								  .append("email", student.getEmail())
								  .append("notes", student.getNotes())
								  .append("deactivated", false)
								  .append("instructor", student.isInstructor());
		studentCollection.insertOne(dbStudent);
		mongoClient.close();
		
		System.out.println("createStudent(): " + dbStudent.toJson().toString());
		
		return Response.status(200).build();
	}
	
	@GET
	@Path("/students-autocomplete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsForAutocomplete(@Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		List<StudentAutocomplete> studentsAutocomplete = new ArrayList<StudentAutocomplete>();
		
		List<Student> students = datastore.find(Student.class).filter("deactivated", false).find().toList();
		for (Student student : students) {
			studentsAutocomplete.add(new StudentAutocomplete(student.getId(), student.getFirstname(), student.getLastname()));
		}		
		
		return Response.status(200).entity(studentsAutocomplete).build();
	}
	
	@PATCH
	@Path("/student/changeActivation/{id}/{deactivate}")
	public Response changeActivation(@PathParam("id") String id, @PathParam("deactivate") boolean deactivate, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoCollection<Document> studentCollection = database.getCollection("student");
		studentCollection.updateOne(Filters.eq("_id", id), new Document("$set", new Document("deactivated", deactivate)));
		
		Student student = null;
		MorphiaCursor<Student> cursor = datastore.find(Student.class).filter("id", id).find();
		if (cursor.hasNext()) {
			student = cursor.next();
			// convert UTC to local date/time
		    student.setBirthdate(new Date(student.getBirthdate().getTime() - Calendar.getInstance().getTimeZone().getOffset(student.getBirthdate().getTime())));
		}
		
        return Response.status(200).entity(student).build();
	}
	
	@POST
	@Path("/student/upgrade/{id}/{actualGrade}/{examNotes}")
	public Response upgrade(@PathParam("id") String id, @PathParam("actualGrade") int actualGrade, @PathParam("examNotes") String examNotes, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		System.out.println("Upgrade student " + id + " with new grade: " + actualGrade);
		
		MongoCollection<Document> studentCollection = database.getCollection("student");
		studentCollection.updateOne(Filters.eq("_id", id), new Document("$set", new Document("actualGrade", actualGrade).append("examNotes", examNotes)));
		
        return Response.status(200).build();
	}
	  
}
