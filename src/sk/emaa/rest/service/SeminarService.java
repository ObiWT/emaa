package sk.emaa.rest.service;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import dev.morphia.query.internal.MorphiaCursor;
import sk.emaa.rest.service.model.ExamStudent;
import sk.emaa.rest.service.model.School;
import sk.emaa.rest.service.model.Seminar;
import sk.emaa.rest.service.model.SeminarDay;
import sk.emaa.rest.service.model.SeminarDayStudent;
import sk.emaa.rest.service.model.SeminarPerson;
import sk.emaa.rest.service.model.SeminarStudent;
import sk.emaa.rest.service.model.Student;

@Path("/seminarService")
public class SeminarService extends AbstractService {
	
	private MongoClient mongoClient;
	private Datastore datastore;
	private MongoDatabase database;
	
	public SeminarService() {
		mongoClient = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();
		morphia.mapPackage("sk.emaa.rest.service.model");
		datastore = morphia.createDatastore(mongoClient, "emaa");
		database = mongoClient.getDatabase("emaa");
	}

	@GET
	@Path("/prepare/{startDate}/{endDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response prepareSeminar(@PathParam("startDate") Date startDate, @PathParam("endDate") Date endDate, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		List<SeminarDay> seminarDays  = new ArrayList<SeminarDay>();
		
		List<Date> dates = getDatesBetween(startDate, DateUtils.addDays(endDate, 1));
		for (Date date : dates) {
			SeminarDay seminarDay = new SeminarDay();
			seminarDay.setDate(date);
			seminarDays.add(seminarDay);
		}
		
		Seminar seminar = new Seminar();
		seminar.setStartDate(startDate);
		seminar.setEndDate(endDate);
		seminar.setLocation(null);
		seminar.setDays(seminarDays);
		seminar.setDaysAccomodation(seminarDays != null ? seminarDays.size()-1 : 0);
		// set default values
		seminar.setPriceTraining(30);
		seminar.setPriceAccomodation(12);
		seminar.setPriceBreakfast(4);
		seminar.setPriceLunch(4);
		seminar.setPriceDinner(4);
		
		return Response.status(200).entity(seminar).build();
	}
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createSeminar(Seminar seminar, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		CodecRegistry pojoCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
		
		MongoCollection<Document> seminarCollection = database.getCollection("seminar");
		seminarCollection.withCodecRegistry(pojoCodecRegistry);
		
		List<Document> seminarDays = new ArrayList<>();
		for (SeminarDay day : seminar.getDays()) {
			Document dbSeminarDay = new Document("date", day.getDate())
										 .append("breakfastDisabled", day.getBreakfastDisabled())
										 .append("lunchDisabled", day.getLunchDisabled())
										 .append("dinnerDisabled", day.getDinnerDisabled());
			seminarDays.add(dbSeminarDay);
		}
		
		ObjectId generatedId = ObjectId.get();
		Document dbSeminar = new Document("_id", generatedId.toHexString())
								  .append("startDate", seminar.getStartDate())
								  .append("endDate", seminar.getEndDate())
								  .append("location", seminar.getLocation())
								  .append("days", seminarDays)
								  .append("daysAccomodation", seminar.getDaysAccomodation())
								  .append("priceTraining", seminar.getPriceTraining())
								  .append("priceAccomodation", seminar.getPriceAccomodation())
								  .append("priceBreakfast", seminar.getPriceBreakfast())
								  .append("priceLunch", seminar.getPriceLunch())
								  .append("priceDinner", seminar.getPriceDinner());
		seminarCollection.insertOne(dbSeminar);
		mongoClient.close();
		
		System.out.println("createSeminar(): " + dbSeminar.toJson().toString());
		
		return Response.status(200).build();
	}
	
	@SuppressWarnings("deprecation")
	@GET
	@Path("/seminars")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSeminars(@Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
	    List<Seminar> seminars = datastore.find(Seminar.class).order("-startDate").find().toList();
	    
	    System.out.println("getSeminars(): " + seminars);

        return Response.status(200).entity(seminars).build();
	}
	
	@GET
	@Path("/seminar/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSeminar(@PathParam("id") String id, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		Seminar seminar = datastore.find(Seminar.class).filter("id", id).first();
		
		return Response.status(200).entity(seminar).build();
	}
	
	@GET
	@Path("/seminarStudent/{studentId}/{seminarId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSeminarStudent(@PathParam("studentId") String studentId, @PathParam("seminarId") String seminarId, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		Student student = null;
		School school = null;
		SeminarStudent seminarStudent = new SeminarStudent();
		
		Seminar seminar = datastore.find(Seminar.class).filter("id", seminarId).first();
		if (seminar == null) {
			System.out.println("Nenasiel sa seminar s id=" + seminarId);
			return Response.status(417).build(); 
		}
		// if already exists, load it
		MorphiaCursor<SeminarStudent> seminarStudentCursor = datastore.find(SeminarStudent.class).filter("seminarId", seminar.getId()).filter("studentId", studentId).find();
		if (seminarStudentCursor.hasNext()) {
			seminarStudent = seminarStudentCursor.next();
			return Response.status(200).entity(seminarStudent).build(); 
		}		
		
		MorphiaCursor<Student> studentCursor = datastore.find(Student.class).filter("id", studentId).find();
		if (studentCursor.hasNext()) {
			student = studentCursor.next();
			MorphiaCursor<School> schoolCursor = datastore.find(School.class).filter("id", student.getSchoolId()).find();
			if (schoolCursor.hasNext()) {
				school = schoolCursor.next();
			} else {
				return Response.status(417).build(); // school NOT FOUND
			}
		} else {
			return Response.status(417).build(); // student NOT FOUND
		}
		
		List<SeminarDayStudent> seminarDaysStudent = new ArrayList<>();
		
		seminarStudent.setStudentId(student.getId());
		seminarStudent.setFirstname(student.getFirstname());
		seminarStudent.setLastname(student.getLastname());
		seminarStudent.setSchoolId(student.getSchoolId());
		seminarStudent.setSchoolCity(school.getCity());
		seminarStudent.setActualGrade(student.getActualGrade());
		seminarStudent.setSeminarId(seminar.getId());
		for (SeminarDay seminarDay : seminar.getDays()) {
			SeminarDayStudent seminarDayStudent = new SeminarDayStudent();
			seminarDayStudent.setDate(seminarDay.getDate());
			seminarDaysStudent.add(seminarDayStudent);
		}
		seminarStudent.setDays(seminarDaysStudent);
		seminarStudent.setInstructor(student.isInstructor());
		
		return Response.status(200).entity(seminarStudent).build();
	}
	
	@GET
	@Path("/seminarPerson/{id}/{seminarId}/{firstname}/{lastname}/{schooldId}/{schoolCity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSeminarPerson(
			@PathParam("id") String id, 
			@PathParam("seminarId") String seminarId, 
			@PathParam("firstname") String firstname,
			@PathParam("lastname") String lastname,
			@PathParam("schooldId") String schooldId,
			@PathParam("schoolCity") String schoolCity,
			@Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		SeminarPerson seminarPerson = new SeminarPerson();
		
		Seminar seminar = datastore.find(Seminar.class).filter("id", seminarId).first();
		if (seminar == null) {
			System.out.println("Nenasiel sa seminar s id=" + seminarId);
			return Response.status(417).build(); 
		}
		// if already exists, load it
		if (!"null".equals(id)) { // check due to new ObjectId(id)
			SeminarPerson foundSeminarPerson = datastore.find(SeminarPerson.class).filter("seminarId", seminar.getId()).filter("_id", new ObjectId(id)).first();
			if (foundSeminarPerson != null) {
				return Response.status(200).entity(foundSeminarPerson).build(); 
			}
		}
		
		List<SeminarDayStudent> seminarDaysStudent = new ArrayList<>();
		
		// seminarPerson.setStudentId(student.getId());
		seminarPerson.setFirstname("");
		seminarPerson.setLastname("");
		seminarPerson.setSchoolId(schooldId);
		seminarPerson.setSchoolCity(schoolCity);
		seminarPerson.setSeminarId(seminarId);
		for (SeminarDay seminarDay : seminar.getDays()) {
			SeminarDayStudent seminarDayStudent = new SeminarDayStudent();
			seminarDayStudent.setDate(seminarDay.getDate());
			seminarDaysStudent.add(seminarDayStudent);
		}
		seminarPerson.setDays(seminarDaysStudent);
		
		return Response.status(200).entity(seminarPerson).build();
	}
	
	@DELETE
	@Path("/seminarStudent/{seminarStudentId}")
	public Response removeSeminarStudent(@PathParam("seminarStudentId") String seminarStudentId, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoCollection<Document> seminarStudentCollection = database.getCollection("seminarStudent");
		
		Bson filter = eq("_id", new ObjectId(seminarStudentId));
		DeleteResult result = seminarStudentCollection.deleteOne(filter);
		if (result.getDeletedCount() != 1) {
			return Response.status(417).build();
		}
		
		return Response.status(200).build();
	}
	
	@DELETE
	@Path("/seminarPerson/{seminarPersonId}")
	public Response removeSeminarPerson(@PathParam("seminarPersonId") String seminarPersonId, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoCollection<Document> seminarPersonCollection = database.getCollection("seminarPerson");
		
		Bson filter = eq("_id", new ObjectId(seminarPersonId));
		DeleteResult result = seminarPersonCollection.deleteOne(filter);
		if (result.getDeletedCount() != 1) {
			return Response.status(417).build();
		}
		
		return Response.status(200).build();
	}
	
	@GET
	@Path("/seminarStudents/{seminarId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSeminarStudents(@PathParam("seminarId") String seminarId, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		List<SeminarStudent> seminarStudents = datastore.find(SeminarStudent.class).filter("seminarId", seminarId).find().toList();
		
		return Response.status(200).entity(seminarStudents).build();
	}
	
	@GET
	@Path("/seminarPersons/{seminarId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSeminarPersons(@PathParam("seminarId") String seminarId, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		List<SeminarPerson> seminarPersons = datastore.find(SeminarPerson.class).filter("seminarId", seminarId).find().toList();
		
		return Response.status(200).entity(seminarPersons).build();
	}
	
	@POST
	@Path("/seminarStudent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveSeminarStudent(SeminarStudent seminarStudent, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		CodecRegistry pojoCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
		MongoCollection<Document> seminarStudentCollection = database.getCollection("seminarStudent");
		seminarStudentCollection.withCodecRegistry(pojoCodecRegistry);
		
		List<Document> seminarDays = new ArrayList<>();
		for (SeminarDayStudent day : seminarStudent.getDays()) {
			Document dbSeminarDay = new Document("accomodation", day.getAccomodation())
										 .append("date", day.getDate())
										 .append("breakfast", day.isBreakfast())
										 .append("lunch", day.isLunch())
										 .append("dinner", day.isDinner());
			seminarDays.add(dbSeminarDay);
		}
		
		Bson filter1 = eq("seminarId", seminarStudent.getSeminarId());
		Bson filter2 = eq("studentId", seminarStudent.getStudentId());
		
		Bson update1 = set("firstname", seminarStudent.getFirstname());
		Bson update2 = set("lastname", seminarStudent.getLastname());
		Bson update3 = set("schoolId", seminarStudent.getSchoolId());
		Bson update4 = set("schoolCity", seminarStudent.getSchoolCity());
		Bson update5 = set("actualGrade", seminarStudent.getActualGrade());
		Bson update6 = set("goingToExam", seminarStudent.isGoingToExam());
		Bson update7 = set("vegetarian", seminarStudent.isVegetarian());
		Bson update8 = set("haveToPay", seminarStudent.getHaveToPay());
		Bson update9 = set("days", seminarDays);
		Bson update10 = set("paidForTrainings", seminarStudent.isPaidForTrainings());
		Bson updates = combine(update1, update2, update3, update4, update5, update6, update7, update8, update9, update10);
		UpdateOptions options = new UpdateOptions().upsert(true);
		UpdateResult updateResult = seminarStudentCollection.updateOne(and(filter1, filter2), updates, options);
		
        System.out.println(updateResult);
		
		return Response.status(200).build();
	}
	
	@POST
	@Path("/seminarPerson")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveSeminarPerson(SeminarPerson seminarPerson, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		CodecRegistry pojoCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
		MongoCollection<Document> seminarPersonCollection = database.getCollection("seminarPerson");
		seminarPersonCollection.withCodecRegistry(pojoCodecRegistry);
		
		List<Document> seminarDays = new ArrayList<>();
		for (SeminarDayStudent day : seminarPerson.getDays()) {
			Document dbSeminarDay = new Document("accomodation", day.getAccomodation())
										 .append("date", day.getDate())
										 .append("breakfast", day.isBreakfast())
										 .append("lunch", day.isLunch())
										 .append("dinner", day.isDinner());
			seminarDays.add(dbSeminarDay);
		}
		
		Bson filter = eq("_id", seminarPerson.getId() != null ? new ObjectId(seminarPerson.getId()) : ObjectId.get());
		
		Bson update0 = set("seminarId", seminarPerson.getSeminarId());
		Bson update1 = set("firstname", seminarPerson.getFirstname());
		Bson update2 = set("lastname", seminarPerson.getLastname());
		Bson update3 = set("schoolId", seminarPerson.getSchoolId());
		Bson update4 = set("schoolCity", seminarPerson.getSchoolCity());
		Bson update5 = set("vegetarian", seminarPerson.isVegetarian());
		Bson update6 = set("haveToPay", seminarPerson.getHaveToPay());
		Bson update7 = set("days", seminarDays);
		Bson updates = combine(update0, update1, update2, update3, update4, update5, update6, update7);
		UpdateOptions options = new UpdateOptions().upsert(true);
		UpdateResult updateResult = seminarPersonCollection.updateOne(and(filter), updates, options);
		
        System.out.println(updateResult);
		
		return Response.status(200).build();
	}
	
	@GET
	@Path("/seminar/next")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNextSeminar(@Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		Seminar nextSeminar = null;
		
		Query<Seminar> query = datastore.createQuery(Seminar.class);
		query.field("startDate").greaterThanOrEq(new Date());		
		MorphiaCursor<Seminar> cursor = query.find();
		if (cursor.hasNext()) {
			nextSeminar = cursor.next();
		}
		
		return Response.status(200).entity(nextSeminar).build();
	}
	
	private List<Date> getDatesBetween(Date startDate, Date endDate) {
		
	    List<Date> datesInRange = new ArrayList<>();
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(startDate);
	    
	    Calendar endCalendar = new GregorianCalendar();
	    endCalendar.setTime(endDate);
	    
	    while (calendar.before(endCalendar)) {
	        Date result = calendar.getTime();
	        datesInRange.add(result);
	        calendar.add(Calendar.DATE, 1);
	    }
	    
	    return datesInRange;
	}
	
	@GET
	@Path("/examStudent/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExamStudent(@PathParam("studentId") String studentId, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		ExamStudent examStudent = null;
		
		MorphiaCursor<ExamStudent> cursor = datastore.find(ExamStudent.class).filter("studentId", studentId).find();
		if (cursor.hasNext()) {
			examStudent = cursor.next();
		}
		
		return Response.status(200).entity(examStudent).build();
	}
	
	@POST
	@Path("/examStudent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveExam(ExamStudent examStudent, @Context HttpHeaders headers) {
		
		System.out.println("saveExam(): " + examStudent);
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("emaa");
		MongoCollection<Document> examStudentCollection = database.getCollection("examStudent");
		
		Bson filter = eq("studentId", examStudent.getStudentId());
		DeleteResult result = examStudentCollection.deleteOne(filter);
		if (result.getDeletedCount() != 1) {
			System.out.println("Chcel som vymazat stary examStudent a vytvorit novy, ale stary som nenasiel (id=" + examStudent.getStudentId() + ")");;
		}
		
		Key<ExamStudent> savedExamStudent = datastore.save(examStudent);   
	    System.out.println(savedExamStudent.getId());
        
        mongoClient.close();
        
		return Response.status(200).build();
	}

}
