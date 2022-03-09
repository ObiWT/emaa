package sk.emaa.rest.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.time.DateUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.internal.MorphiaCursor;
import sk.emaa.rest.service.model.ExistingTraining;
import sk.emaa.rest.service.model.SchoolFee;
import sk.emaa.rest.service.model.Student;
import sk.emaa.rest.service.model.Training;
import sk.emaa.rest.service.model.TrainingStudent;

@Path("/trainingService")
public class TrainingService extends AbstractService {
	
	private String pattern = "dd.MM.";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	
	private MongoDatabase database;
	private Datastore datastore;
	
	public TrainingService() {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase("emaa");
		Morphia morphia = new Morphia();
		morphia.mapPackage("sk.emaa.rest.service.model");
		datastore = morphia.createDatastore(mongoClient, "emaa");	
	}
	
	@POST
	@Path("/training")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTraining(Training training, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoCollection<Document> studentCollection = database.getCollection("training");
		
		// check if today's training already exists, if yes, delete it first
		List<Training> trainings = datastore.find(Training.class).filter("schoolId", training.getSchoolId()).find().toList();
	    for (Training existingTraining : trainings) {
	    	if (DateUtils.isSameDay(training.getDate(), existingTraining.getDate()))
	    		datastore.delete(existingTraining);
		}
		
		ObjectId generatedId = ObjectId.get();
		Document dbTraining = new Document("_id", generatedId.toHexString())
								  .append("schoolId", training.getSchoolId())
								  .append("date", training.getDate())
								  .append("presentStudentIds", training.getPresentStudentIds())
		 						  .append("notPresentStudentIds", training.getNotPresentStudentIds())
		 						  .append("notes", training.getNotes());
		studentCollection.insertOne(dbTraining);
		
		return Response.status(200).build();
	}
	
	@GET
	@Path("/training/{schoolId}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadExistingTraining(@PathParam("schoolId") String schoolId, @PathParam("date") Date date, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		// check if today's training already exists, if yes, delete it first
		Training existingTraining = null;
		List<Training> trainings = datastore.find(Training.class).filter("schoolId", schoolId).find().toList();
	    for (Training training : trainings) {
	    	if (DateUtils.isSameDay(date, training.getDate())) {
	    		existingTraining = training;
	    		break;
	    	}
		}
	    
	    List<TrainingStudent> trainingStudents = new ArrayList<>();
	    List<Student> students = datastore.find(Student.class).filter("schoolId", schoolId).filter("deactivated", false).find().toList();
	    
	    for (Student student : students) {
	    	TrainingStudent studentTraining = new TrainingStudent();
	    	studentTraining.setStudentId(student.getId());
	    	studentTraining.setFirstname(student.getFirstname());
	    	studentTraining.setLastname(student.getLastname());
	    	studentTraining.setPresent(existingTraining != null && existingTraining.getPresentStudentIds().contains(student.getId()));
	    	trainingStudents.add(studentTraining);
		}
	    
	    ExistingTraining training = new ExistingTraining();
	    training.setTrainingId(existingTraining != null ? existingTraining.getId() : null);
	    training.setStudents(trainingStudents);
	    training.setNotes(existingTraining != null ? existingTraining.getNotes() : null);
	    
	    return Response.status(200).entity(training).build();
	}
	
	@GET
	@Path("/overview/notes/{schoolId}/{date}")
	public Response getTrainingNotes(@PathParam("schoolId") String schoolId, @PathParam("date") String date, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		String[] splittedDate = date.split("\\.");
		if (splittedDate.length != 3) {
			return Response.status(417).build();
		}
		
		int year;
		int month;
		int day;
		
		try {
			year = Integer.parseInt(splittedDate[2]);
			month = Integer.parseInt(splittedDate[1]);
			day = Integer.parseInt(splittedDate[0]);
		} catch (NumberFormatException e) {			
			e.printStackTrace();
			return Response.status(417).build();
		}
		
		LocalDate trainingDay = LocalDate.of(year, month, day);
		
		Date dayStart = java.util.Date.from(trainingDay.atStartOfDay()
	      	      .atZone(ZoneId.systemDefault())
	      	      .toInstant());
		Date dayEnd = java.util.Date.from(trainingDay.plusDays(1).atStartOfDay()
	    	      .atZone(ZoneId.systemDefault())
	    	      .toInstant());
		
		String notes = null;
		
		MorphiaCursor<Training> cursor = datastore.find(Training.class)
	    		.filter("schoolId", schoolId)
	    		.filter("date >=", dayStart)
	    		.filter("date <=", dayEnd)
	    		.find();
		if (cursor.hasNext()) {
			notes = cursor.next().getNotes();
		}
		
	    return Response.status(200).entity(notes).build();
	}
	
	private Boolean isPresent(String studentId, List<String> presentStudentIds, List<String> notPresentStudentIds) {
		if (presentStudentIds.contains(studentId)) {
			return true;
		} else if (notPresentStudentIds.contains(studentId)) {
			return false;
		} else {
			return null;
		}
	}
	
	@GET
	@Path("/overview/{schoolId}/{month}/{year}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMonthOverview(
			@PathParam("schoolId") String schoolId, 
			@PathParam("month") int month, 
			@PathParam("year") int year, 
			@Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		YearMonth yearMonth = YearMonth.of(year, month + 1);
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
		
		Date startDate = java.util.Date.from(firstDayOfMonth.atStartOfDay()
	      	      .atZone(ZoneId.systemDefault())
	      	      .toInstant());
		Date endDate = java.util.Date.from(lastDayOfMonth.atStartOfDay()
	    	      .atZone(ZoneId.systemDefault())
	    	      .toInstant());
		
		// load data
		List<Training> trainings = datastore.find(Training.class)
	    		.filter("schoolId", schoolId)
	    		.filter("date >=", startDate)
	    		.filter("date <=", endDate)
	    		.find().toList();
		if (trainings.isEmpty()) {
			return null;
		}
	    List<Student> students = datastore.find(Student.class).filter("schoolId", schoolId).filter("deactivated", false).find().toList();
	    MorphiaCursor<SchoolFee> cursor = datastore.find(SchoolFee.class).filter("schoolId", schoolId).filter("month", month).filter("year", year).find();
	    SchoolFee fee = null;
	    if (cursor.hasNext()) {
	    	fee = cursor.next();
	    }
	    
	    Collections.sort(trainings); // order by date
	    Collections.sort(students); // order by lastname
	    
	    // if there is only one training in month overview, then this is the first training of the month, so we create also List<SchoolFee> for this month
	    if (fee == null) {
	    	ObjectId generatedId = ObjectId.get();
    		fee = new SchoolFee();
    		fee.setId(generatedId.toHexString());
    		fee.setSchoolId(schoolId);
    		fee.setMonth(month);
    		fee.setYear(year);
    		//fee.setPaid(new ArrayList<>());
    		datastore.save(fee);
	    }
        
	    List<JSONObject> listOfJsonTrainings = new ArrayList<>();
	    for (Student student : students) {
	    	try {
		    	JSONObject json = new JSONObject();
		    	json.append("studentId", student.getId());
		    	json.append("name", student.getFirstname() + " " + student.getLastname());
		    	
		    	for (Training training : trainings) {
					json.append(simpleDateFormat.format(training.getDate()), isPresent(student.getId(), training.getPresentStudentIds(), training.getNotPresentStudentIds()));   
		    	}
		    	
		    	json.append("paid", fee.isPaid() == null ? false : fee.isPaid().contains(student.getId()));
		    	listOfJsonTrainings.add(json);
	    	} catch (JSONException e) {
				e.printStackTrace();
			}
	    }
	    
		return Response.status(200).entity(listOfJsonTrainings.isEmpty() ? null : listOfJsonTrainings.toString()).build();
	}
	
	@POST
	@Path("/paySchoolFee/{schoolId}/{month}/{year}/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response paySchoolFee(
			@PathParam("schoolId") String schoolId, 
			@PathParam("month") int month, 
			@PathParam("year") int year, 
			@PathParam("studentId") String studentId, 
			@Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MorphiaCursor<SchoolFee> cursor = datastore.find(SchoolFee.class).filter("schoolId", schoolId).filter("month", month).filter("year", year).find();
	    SchoolFee fee = null;
	    if (cursor.hasNext()) {
	    	fee = cursor.next();
	    	if (fee.isPaid() == null) {
	    		List<String> paidList = new ArrayList<>();
	    		fee.setPaid(paidList);
	    	} 
	    	fee.isPaid().add(studentId);
	    	datastore.save(fee);
	    }
	    
	    Response response = getMonthOverview(schoolId, month, year, headers);
	    
		return response;
	}
	
	@POST
	@Path("/cancelPayment/{schoolId}/{month}/{year}/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelPayment(
			@PathParam("schoolId") String schoolId, 
			@PathParam("month") int month, 
			@PathParam("year") int year, 
			@PathParam("studentId") String studentId, 
			@Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoCollection<Document> schoolFeeCollection = database.getCollection("schoolFee");
		
		Bson filter = and(eq("schoolId", schoolId), eq("month", month), eq("year", year));
		
		Document update = new Document("$pull", new Document("paid", studentId));
		
		schoolFeeCollection.updateOne(filter, update);
	    
	    Response response = getMonthOverview(schoolId, month, year, headers);
	    
		return response;
	}
	
}
